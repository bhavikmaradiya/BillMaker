package shravan.nyshadh.billmaker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;
    private String[] titles;

    public PagerAdapter(@NonNull FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > 0) {
            return titles[position];
        } else {
            return super.getPageTitle(position);
        }
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}