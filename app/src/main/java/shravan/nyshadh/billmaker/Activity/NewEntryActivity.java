package shravan.nyshadh.billmaker.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import es.dmoral.toasty.Toasty;
import shravan.nyshadh.billmaker.Adapter.PagerAdapter;
import shravan.nyshadh.billmaker.Fragment.ManualProductFragment;
import shravan.nyshadh.billmaker.Fragment.ScanProductFragment;
import shravan.nyshadh.billmaker.Fragment.SelectCustomerFragment;
import shravan.nyshadh.billmaker.Modal.Common;
import shravan.nyshadh.billmaker.R;
import shravan.nyshadh.billmaker.SwipableViewPager;

public class NewEntryActivity extends AppCompatActivity implements ManualProductFragment.CreateInvoiceListener {
    SwipableViewPager viewPager;
    Button cancelBtn, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        setTitle("Invoice");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cancelBtn = findViewById(R.id.cancelBtn);
        nextBtn = findViewById(R.id.nextBtn);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setCanScroll(false);
        viewPager.setOffscreenPageLimit(3);

        if (!getSharedPreferences(Common.LOGIN, MODE_PRIVATE).getBoolean(Common.IS_LOGGEDIN, false)) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        Fragment[] fragments = new Fragment[3];
        fragments[0] = new SelectCustomerFragment();
        fragments[1] = new ScanProductFragment();
        fragments[2] = new ManualProductFragment();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments, null, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        addListener();
        if (Common.selectedCustomer != null) {
            viewPager.setCurrentItem(1);
        }
    }

    private void addListener() {
        nextBtn.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() == 0 && Common.selectedCustomer != null) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else if (viewPager.getCurrentItem() == 0 && Common.selectedCustomer == null) {
                Toasty.info(this, "First select customer to continue!", Toasty.LENGTH_SHORT).show();
            } else if (viewPager.getCurrentItem() == 1 && Common.SELECTED_PRODUCTS != null && Common.SELECTED_PRODUCTS.size() > 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else if (viewPager.getCurrentItem() == 0 && Common.SELECTED_PRODUCTS == null || (Common.SELECTED_PRODUCTS != null ? Common.SELECTED_PRODUCTS.size() : 0) <= 0) {
                Toasty.info(this, "Please add products to continue!", Toasty.LENGTH_LONG).show();
            }
        });
        cancelBtn.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() > 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            } else {
                Common.selectedCustomer = null;
                super.onBackPressed();
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    cancelBtn.setText("Previous");
                } else {
                    cancelBtn.setText("Cancel");
                }
                if (position == 2){
                    nextBtn.setVisibility(View.GONE);
                }else {
                    nextBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else {
            Common.selectedCustomer = null;
            Common.TAX_PERCENT = 0;
            Common.SELECTED_PRODUCTS = null;
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Common.selectedCustomer = null;
        Common.TAX_PERCENT = 0;
        Common.SELECTED_PRODUCTS = null;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onCreateInvoice() {
        Toast.makeText(this, "onCreateInvoice", Toast.LENGTH_SHORT).show();
    }
}
