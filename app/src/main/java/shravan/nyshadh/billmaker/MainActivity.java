package shravan.nyshadh.billmaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        fragments[1] = new HistoryFragment();
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
}
