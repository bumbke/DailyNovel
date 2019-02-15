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
import java.util.List;

import main.dailynovel.ItemClickListener;
import main.dailynovel.NovelActivity;
import main.dailynovel.Objects.*;
import main.dailynovel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookcaseItemFragment3 extends Fragment {
    RecyclerView rvBookcase;
    NovelAdapterVertical adapterVertical;
    ArrayList<Novel> novels = new ArrayList<>();

    public BookcaseItemFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookcase_item3, container, false);

        rvBookcase = (RecyclerView) view.findViewById(R.id.rvBookcase);
        LinearLayoutManager vertical = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.VERTICAL,false);
        rvBookcase.setLayoutManager(vertical);
        rvBookcase.setItemAnimator(new DefaultItemAnimator());
        rvBookcase.addItemDecoration(new DividerItemDecoration(
                rvBookcase.getContext(),
                vertical.getOrientation()
        ));

        adapterVertical = new NovelAdapterVertical(getActivity(), novels, new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), NovelActivity.class);
                startActivity(intent);
                intent.putExtra("useremail", getArguments().getString("useremail"));
                intent.putExtra("username", getArguments().getString("username"));
                intent.putExtra("Object", novels.get(position));
            }
        });

        getNovel();

        return view;
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

    private boolean isEmpty() {
        DBAdapter dbAdapter = new DBAdapter(getActivity());
        dbAdapter.openDB();

        if(dbAdapter.isNovelEmpty()>0) {
            return false;
        }
        return true;
    }
}
