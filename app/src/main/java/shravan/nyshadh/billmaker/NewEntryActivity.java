package shravan.nyshadh.billmaker;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class NewEntryActivity extends AppCompatActivity {
    SwipableViewPager viewPager;
    Button cancelBtn, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
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
    }

    private void addListener() {
        nextBtn.setOnClickListener(view -> {
//            if (Common.selectedCustomer != null) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//            } else {
//                Toast.makeText(this, "First select customer to continue!", Toast.LENGTH_SHORT).show();
//            }
        });
        cancelBtn.setOnClickListener(view -> {
            if (viewPager.getCurrentItem() > 0)
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
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
}
