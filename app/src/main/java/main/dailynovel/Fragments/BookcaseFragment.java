package main.dailynovel.Fragments;


import android.database.Cursor;
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
import java.util.Arrays;
import java.util.List;

import main.dailynovel.Objects.*;
import main.dailynovel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookcaseFragment extends Fragment {
    ViewPager vpBook;
    BookcaseViewPager vpBookcase;
    TabLayout tlBook;
    int page = 0;
    ArrayList<User> users = new ArrayList<>();
    String useremail;

    public BookcaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookcase, container, false);
        getUser();

        if(getArguments()!=null) {
            useremail = getArguments().getString("accountemail");
        }

        vpBook = (ViewPager)view.findViewById(R.id.vpBook);
        tlBook= (TabLayout) view.findViewById(R.id.tlBook);

        vpBookcase = new BookcaseViewPager(getActivity().getSupportFragmentManager());
        switch (page) {
            case 0:
                BookcaseItemFragment item1 = new BookcaseItemFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("useremail", useremail);
                bundle1.putString("username", getArguments().getString("accountname"));
                item1.setArguments(bundle1);
                vpBookcase.addFragment(item1);
            case 1:
                BookcaseItemFragment2 item2 = new BookcaseItemFragment2();
                Bundle bundle2 = new Bundle();
                bundle2.putString("useremail", useremail);
                bundle2.putString("username", getArguments().getString("accountname"));
                item2.setArguments(bundle2);
                vpBookcase.addFragment(item2);
            case 2:
                BookcaseItemFragment3 item3 = new BookcaseItemFragment3();
                Bundle bundle3 = new Bundle();
                bundle3.putString("useremail", useremail);
                bundle3.putString("username", getArguments().getString("accountname"));
                item3.setArguments(bundle3);
                vpBookcase.addFragment(item3);
        }

        vpBook.setAdapter(vpBookcase);

        tlBook.setTabGravity(TabLayout.GRAVITY_FILL);
        tlBook.setupWithViewPager(vpBook);
        tlBook.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private void getUser() {
        ArrayList<String> subs = null, like = null, view;
        users.clear();
        DBAdapter dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();

        Cursor cursor = dbAdapter.getUser();
        while (cursor.moveToNext()) {
            if(cursor.getString(2)!=null) {
                subs = new ArrayList<>(Arrays.asList(dbAdapter.convertStringToArray(cursor.getString(2))));
            }

            if(cursor.getString(3)!=null) {
                like = new ArrayList<>(Arrays.asList(dbAdapter.convertStringToArray(cursor.getString(3))));
            }
            User user = new User(cursor.getString(0), cursor.getString(1), subs, like);
            users.add(user);
        }
        dbAdapter.closeDB();
    }
}

class BookcaseViewPager extends FragmentStatePagerAdapter {
    private final List<Fragment> listFragment = new ArrayList<>();

    public BookcaseViewPager(FragmentManager fm) {
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

    public void addFragment(Fragment fragment){
        listFragment.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Đang theo dõi";
            case 1:
                return "Đã thích";
            case 2:
                return "Vừa xem";
        }
        return null;
    }
}
