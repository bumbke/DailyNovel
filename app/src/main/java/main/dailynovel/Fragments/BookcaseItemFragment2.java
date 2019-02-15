package main.dailynovel.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.dailynovel.ItemClickListener;
import main.dailynovel.NovelActivity;
import main.dailynovel.Objects.*;
import main.dailynovel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookcaseItemFragment2 extends Fragment {
    RecyclerView rvBookcase;
    NovelAdapterVertical adapterVertical;
    List<Novel> listNovels = new ArrayList<>();
    List<User> users = new ArrayList<>();
    ArrayList<Novel> novels = new ArrayList<>();
    ArrayList<String> novelLike = new ArrayList<>();
    int position;

    public BookcaseItemFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookcase_item2, container, false);

        rvBookcase = (RecyclerView) view.findViewById(R.id.rvBookcase);
        LinearLayoutManager vertical = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false);
        rvBookcase.setLayoutManager(vertical);
        rvBookcase.setItemAnimator(new DefaultItemAnimator());
        rvBookcase.addItemDecoration(new DividerItemDecoration(
                rvBookcase.getContext(),
                vertical.getOrientation()
        ));

        adapterVertical = new NovelAdapterVertical(getActivity(), listNovels, new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), NovelActivity.class);
                startActivity(intent);
                intent.putExtra("useremail", getArguments().getString("useremail"));
                intent.putExtra("username", getArguments().getString("username"));
                intent.putExtra("Object", listNovels.get(position));
            }
        });

        prepareItems();

        return view;
    }

    private void prepareItems() {
        getNovel();
        getUser();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserMail().equalsIgnoreCase(getArguments().getString("useremail"))) {
                position = i;
            }
        }

        if (!isNovelEmpty()) {
            if (!isUserEmpty()) {
                if (users.get(position).getNovelSubscribe()!=null) {
                    novelLike = users.get(position).getNovelSubscribe();
                    listNovels.clear();
                    for (int i = 0; i < novels.size(); i++) {
                        if (novelLike.get(i).equalsIgnoreCase(novels.get(i).getNovelID())) {
                            listNovels.add(novels.get(i));
                        }
                    }
                    rvBookcase.setAdapter(adapterVertical);
                }
            }
        }
    }

    private void getNovel() {
        Cursor cursor;
        novels.clear();
        DBAdapter dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();

        cursor = dbAdapter.getNovel();
        while (cursor.moveToNext()) {
            Novel novel = new Novel(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6));
            novels.add(novel);
        }

        if(!(novels.size()<1)) {
            rvBookcase.setAdapter(adapterVertical);
        }
        dbAdapter.closeDB();
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

    private boolean isNovelEmpty() {
        DBAdapter dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();

        if(dbAdapter.isNovelEmpty()>0) {
            return false;
        }
        return true;
    }

    private boolean isUserEmpty() {
        DBAdapter dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();

        if(dbAdapter.isUserEmpty()>0) {
            return false;
        }
        return true;
    }
}
