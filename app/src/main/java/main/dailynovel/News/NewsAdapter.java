package main.dailynovel.News;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import main.dailynovel.R;

/**
 * Created by bumbk on 10/20/2017.
 */

public class NewsAdapter extends ArrayAdapter<NewsType> {
    private LayoutInflater inflater;
    public NewsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<NewsType> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.news_items,null);
        }
        ImageView imgAvatar = (ImageView)convertView.findViewById(R.id.imgAvatar);
        TextView txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
        NewsType NT = getItem(position);
        txtTitle.setText(NT.getTitle());
        //View anh
        Picasso.with(inflater.getContext()).load(NT.getImageURL()).into(imgAvatar);
        return convertView;
    }
}
