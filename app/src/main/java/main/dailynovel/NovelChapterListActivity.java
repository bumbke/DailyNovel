package main.dailynovel;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import main.dailynovel.Objects.Crawler;
import main.dailynovel.Search.SearchActivity;

public class NovelChapterListActivity extends AppCompatActivity {
    ImageButton imbSearch;
    String itemID;
    int itemChapter;
    ArrayList<String> title = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_chapter_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        itemID = intent.getExtras().getString("itemID");
        itemChapter = intent.getExtras().getInt("itemChapter");

        try {
            Preload novelTask = new Preload(NovelChapterListActivity.this);
            novelTask.execute();
        } catch (RejectedExecutionException e){
            //Handle when has exception thrown
        }


        imbSearch = (ImageButton)findViewById(R.id.imbSearch);
        imbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NovelChapterListActivity.this, SearchActivity.class);
                i.putExtra("itemName", title);
                startActivity(i);
            }
        });


    }

    class Preload extends AsyncTask<String, Integer, List<String>> {
        ListView lvChapters;
        ArrayAdapter<String> adapter;

        public Preload(Activity activity) {
            lvChapters = (ListView) activity.findViewById(R.id.lvChapters);
            adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_expandable_list_item_1, title);
            lvChapters.setAdapter(adapter);

            lvChapters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(NovelChapterListActivity.this, NovelReaderActivity.class);
                    intent.putExtra("itemID", itemID);
                    intent.putExtra("itemChapter", i);
                    intent.putExtra("itemName", title.get(i));
                    startActivity(intent);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int i = values[0];
            adapter.notifyDataSetChanged();
        }

        @Override
        protected List<String> doInBackground(String... strings) {
            for (int i = 0; i <= itemChapter; i++) {
                SystemClock.sleep(10);
                try {
                    title.add(Crawler.getChapterTitle(itemID, i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
}
