package com.example.nguyenantin.bususingqrcode;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BottomNavigationBarActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static FragmentPagerAdapter adapter ;
    private ViewPagerAdapter mAdapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws RuntimeException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        createViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();
        tabLayout.getTabAt(1).select();
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));

    }
    private void createTabIcons() throws RuntimeException {

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.tabs);
        tabContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_outline_black_24dp, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabContent);

        LinearLayout tabLinearLayoutTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContentTwo = (TextView) tabLinearLayoutTwo.findViewById(R.id.tabs);
        tabContentTwo.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_home_black_24dp,0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabContentTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContentThree = (TextView) tabThree.findViewById(R.id.tabs);
        tabContentThree.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attach_money_black_24dp, 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabContentThree);

    }

    private void createViewPager(ViewPager viewPager) throws RuntimeException {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AccountFragment());
        adapter.addFrag(new HomeFragment());
        adapter.addFrag(new ProfileFragment());
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager mFragmentManager;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            mFragmentManager = manager;
        }

        @Override
        public Fragment getItem(int position) throws RuntimeException {
            switch (position) {
                case 0:
                    return ProfileFragment.newInstance();
                case 1:
                    return HomeFragment.newInstance();
                case 2:
                    return AccountFragment.newInstance();
                default:
                    return null;
            }
        }

        public Fragment getFragment(int position) {
            Fragment fragment = null;
            fragment = mFragmentManager.findFragmentByTag(null);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
            }
            return object;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) throws RuntimeException{
            mFragmentList.add(fragment);
        }
    }

}
