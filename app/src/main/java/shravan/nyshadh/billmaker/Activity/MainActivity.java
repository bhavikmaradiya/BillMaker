package shravan.nyshadh.billmaker.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import shravan.nyshadh.billmaker.Adapter.InvoiceHistoryAdapter;
import shravan.nyshadh.billmaker.Adapter.PagerAdapter;
import shravan.nyshadh.billmaker.Fragment.CustomerListFragment;
import shravan.nyshadh.billmaker.Fragment.HistoryFragment;
import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.Modal.Invoice;
import shravan.nyshadh.billmaker.R;

public class MainActivity extends AppCompatActivity implements InvoiceHistoryAdapter.InvoiceActionListener {
    TabLayout tabLayout;
    PagerAdapter pagerAdapter;
    ViewPager pager;
    Toolbar toolbar;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
        setSupportActionBar(toolbar);

        if (!getSharedPreferences(Common.KEY_LOGIN, MODE_PRIVATE).getBoolean(Common.IS_LOGGEDIN, false)) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

        Fragment[] fragments = new Fragment[2];
        fragments[0] = new CustomerListFragment();
        fragments[1] = new HistoryFragment();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments, new String[]{"Customers", "History"}, FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        pager.setAdapter(pagerAdapter);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Conform Logout?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        getSharedPreferences(Common.KEY_LOGIN, MODE_PRIVATE).edit().putBoolean(Common.IS_LOGGEDIN, false).clear().apply();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActionSelected(String action, Invoice invoice) {
        dialog = new Dialog(MainActivity.this);
        if (!action.equals(Common.ACTION_INVOICE_DETAIL)) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.edit_number_dialog, null, false);
            EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
            ImageView imgClose = view.findViewById(R.id.imgClose);
            Button sendBtn = view.findViewById(R.id.sendBtn);
            etPhoneNumber.setText(invoice.getCustomer() != null ? invoice.getCustomer().getCustomerPhone() : "");
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(view);
            dialog.show();

            imgClose.setOnClickListener(v -> dialog.dismiss());

            if (action.equals(Common.ACTION_INVOICE_WHATSAPP)) {
                sendBtn.setText("SEND");
            } else {
                sendBtn.setText("CALL");
            }

            sendBtn.setOnClickListener(v -> {
                if (action.equals(Common.ACTION_INVOICE_WHATSAPP)) {
                    boolean installed = appInstalledOrNot("com.whatsapp");
                    if (installed) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (etPhoneNumber.getText().toString().startsWith("+91")) {
                            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + etPhoneNumber.getText().toString() + "&text="));
                        } else if (etPhoneNumber.getText().toString().startsWith("91")) {
                            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+" + etPhoneNumber.getText().toString() + "&text="));
                        } else {
                            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+91" + etPhoneNumber.getText().toString() + "&text="));
                        }
                        startActivity(intent);
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Toasty.warning(MainActivity.this, "Whatsapp not installed on your device", Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + etPhoneNumber.getText().toString()));
                    if (ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            });
        } else {
            startActivity(new Intent(getApplicationContext(), InvoiceDetailActivity.class).putExtra(action, invoice));
        }


    }

    private boolean appInstalledOrNot(String url) {
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
