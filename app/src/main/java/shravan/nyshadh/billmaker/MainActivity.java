package shravan.nyshadh.billmaker;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, InvoiceHistoryAdapter.InvoiceActionListener {
    TabLayout tabLayout;
    PagerAdapter pagerAdapter;
    ViewPager pager;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        navigationView = findViewById(R.id.nav);
        drawerLayout = findViewById(R.id.drawer);
        tabLayout.setupWithViewPager(pager);
        setSupportActionBar(toolbar);
        setUpNavigation();
        Fragment[] fragments = new Fragment[2];
        fragments[0] = new CustomerListFragment();
        fragments[1] = new HistoryFragment(this);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments, new String[]{"Customers", "History"});
        pager.setAdapter(pagerAdapter);

        //TODO create customer update and delete activity
        //TODO create history detail page
        //TODO create invoice pdf
    }

    private void setUpNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerSlideAnimationEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create:
                startActivity(new Intent(getApplicationContext(), NewEntryActivity.class));
                break;
            case R.id.newCustomer:
                startActivity(new Intent(getApplicationContext(), AddNewCustomerActivity.class));
                break;
        }
        return false;
    }

    @Override
    public void onActionSelected(String action, Invoice invoice) {
        if (!action.equals(Common.ACTION_INVOICE_DETAIL)) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.edit_number_dialog, null, false);
            EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
            ImageView imgClose = view.findViewById(R.id.imgClose);
            Button sendBtn = view.findViewById(R.id.sendBtn);
            etPhoneNumber.setText(invoice.getNumber());
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
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
                        Toast.makeText(MainActivity.this, "Whatsapp not installed on your device", Toast.LENGTH_SHORT).show();
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
}
