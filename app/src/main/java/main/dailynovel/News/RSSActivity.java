package main.dailynovel.News;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import main.dailynovel.NewsDetailActivity;
import main.dailynovel.R;

public class RSSActivity extends AppCompatActivity {

    private ListView lvNews;
    private NewsAdapter adapter;
    private final String BongdaRSS="http://www.vietgiaitri.com/game/cosplay/feed/";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        lvNews = (ListView)findViewById(R.id.lvNews);
        lvNews.setOnItemClickListener(onItemClick);

        try {
            new LoadRSS().execute(BongdaRSS);
        } catch (RejectedExecutionException e){
            //Handle when has exception thrown
        }

        //Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //      SET TOOLBAR
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        //      ADD BACK BUTTON
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent detail = new Intent(RSSActivity.this,NewsDetailActivity.class);
            detail.putExtra("LINK",adapter.getItem(position).getLink());
            startActivity(detail);
        }
    };

    class LoadRSS extends AsyncTask<String, Void, ArrayList<NewsType>> {

        final ThreadLocal<ProgressDialog> dialog = new ThreadLocal<>();
        @Override
        protected void onPreExecute() {
            dialog.set(new ProgressDialog(RSSActivity.this));
            dialog.get().setMessage("Loading...");
            dialog.get().setCancelable(false);
            dialog.get().show();
        }

        @Override
        protected ArrayList<NewsType> doInBackground(String... params) {
            String url = params[0];
            ArrayList<NewsType> result = new ArrayList<>();
            // Load Data ve va parse = JSOUP
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("item");
                for (Element item:
                        elements
                     ) {
                    String title = item.select("title").text();
                    String link = item.select("link").text();
                    String des = item.select("description").text();
                    //HTML => dung Jsoup parse no tiep => img
                    Document docImage = Jsoup.parse(des);
                    String imageURl = docImage.select("img").get(0).attr("src");
                    result.add(new NewsType(title,link,imageURl));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsType> newsTypes) {
            dialog.get().dismiss();
            adapter = new NewsAdapter(RSSActivity.this,0,newsTypes,getLayoutInflater());
            lvNews.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
}
