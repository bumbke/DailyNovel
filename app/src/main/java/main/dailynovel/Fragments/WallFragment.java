package main.dailynovel.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import main.dailynovel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WallFragment extends Fragment {
    ViewPager viewPager;
    WallViewPager vpWall;
    TabLayout tabLayout;
    int page = 0;

    public WallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wall, container, false);
        viewPager = (ViewPager)view.findViewById(R.id.vpWall);
        tabLayout= (TabLayout) view.findViewById(R.id.tlWall);

        vpWall = new WallViewPager(getActivity().getSupportFragmentManager());
        switch (page) {
            case 0:
                WallItemFragment item1 = new WallItemFragment();
                vpWall.addFragment(item1);
//            case 1:
//                WallItemFragment item2 = new WallItemFragment();
//                vpWall.addFragment(item2);
        }

        viewPager.setAdapter(vpWall);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}

class WallViewPager extends FragmentStatePagerAdapter {
    private final List<Fragment> listFragment = new ArrayList<>();

    public WallViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public void addFragment(Fragment fragment) {
        listFragment.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Facebook";
//            case 1:
//                return "Twitter";
        }
        return null;
    }
}
