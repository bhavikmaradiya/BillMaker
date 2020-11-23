package shravan.nyshadh.billmaker;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import shravan.nyshadh.billmaker.Adapter.PagerAdapter;
import shravan.nyshadh.billmaker.Fragment.ManualProductFragment;
import shravan.nyshadh.billmaker.Fragment.ScanProductFragment;
import shravan.nyshadh.billmaker.Fragment.SelectCustomerFragment;
import shravan.nyshadh.billmaker.Modal.Common;

public class NewEntryActivity extends AppCompatActivity {
    SwipableViewPager viewPager;
    Button cancelBtn, nextBtn;
    private boolean onBackPressed = false;

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
        Fragment[] fragments = new Fragment[3];
        fragments[0] = new SelectCustomerFragment();
        fragments[1] = new ScanProductFragment();
        fragments[2] = new ManualProductFragment();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments, null));
        addListener();
        if (Common.selectedCustomer != null) {
            viewPager.setCurrentItem(1);
        }
    }

    private void addListener() {
        nextBtn.setOnClickListener(view -> {
            if (Common.selectedCustomer != null) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                Toast.makeText(this, "First select customer to continue!", Toast.LENGTH_SHORT).show();
            }
        });
        cancelBtn.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() > 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            } else {
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
        } else if (onBackPressed) {
            super.onBackPressed();
        } else if (!onBackPressed) {
            onBackPressed = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed = false;
                }
            }, 3000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }

}
