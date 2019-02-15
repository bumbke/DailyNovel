package main.dailynovel.Search;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import main.dailynovel.NovelActivity;
import main.dailynovel.Objects.Crawler;
import main.dailynovel.Objects.Novel;
import main.dailynovel.R;

public class SearchActivity extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    SearchView searchView;
    ListView lvResult;
    ItemAdapter adapter;
    List<ItemAdapter.Item> items = new ArrayList<>();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        //      SET HIDE STATUS BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //      SET TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        intent = getIntent();
        for (int i = 0; i < intent.getExtras().getStringArrayList("itemName").size(); i++) {
            items.add(new ItemAdapter.Item(intent.getExtras().getStringArrayList("itemName").get(i)));
        }

        lvResult = (ListView)findViewById(R.id.lvResult);
        adapter = new ItemAdapter(SearchActivity.this, items);
        lvResult.setAdapter(adapter);
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Novel novel = new Novel(intent.getExtras().getStringArrayList("itemID").get(i), intent.getExtras().getStringArrayList("itemName").get(i),
                        intent.getExtras().getStringArrayList("itemAuthor").get(i), intent.getExtras().getStringArrayList("itemType").get(i),
                        intent.getExtras().getStringArrayList("itemStatus").get(i), intent.getExtras().getStringArrayList("itemCover").get(i),
                        Integer.parseInt(intent.getExtras().getStringArrayList("itemChapter").get(i)));
                Intent i1 = new Intent(SearchActivity.this, NovelActivity.class);
                i1.putExtra("Object", novel);
                i1.putExtra("useremail", intent.getExtras().getString("useremail"));
                i1.putExtra("username", intent.getExtras().getString("username"));
                startActivity(i1);
            }
        });

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
}
