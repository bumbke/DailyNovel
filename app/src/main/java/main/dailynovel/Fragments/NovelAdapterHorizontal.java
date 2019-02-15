package main.dailynovel.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;

import main.dailynovel.ItemClickListener;
import main.dailynovel.Objects.Novel;
import main.dailynovel.R;

/**
 * Created by l3umb on 11/25/2017.
 */

public class NovelAdapterHorizontal extends RecyclerView.Adapter<HolderHorizontal> {
    List<Novel> novels;
    Context context;
    ItemClickListener listener;

    public NovelAdapterHorizontal(Context context, List<Novel> novels, ItemClickListener listener) {
        this.context = context;
        this.novels = novels;
        this.listener = listener;
    }

    @Override
    public HolderHorizontal onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal,null);
        final HolderHorizontal holderHorizontal = new HolderHorizontal(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, holderHorizontal.getPosition());
            }
        });
        return holderHorizontal;
    }

    @Override
    public void onBindViewHolder(HolderHorizontal holder, int position) {
        Bitmap bm = null;
        String coverURL = novels.get(position).getNovelCover();
        if(!coverURL.substring(coverURL.indexOf("images/story/")+"images/story/".length()).matches("(\\w{2,}+\\.(?i)(jpg|png|gif|bmp))$")){
            holder.imgCover.setImageResource(R.drawable.cover);
        } else {
            try {
                InputStream in = new java.net.URL(novels.get(position).getNovelCover()).openStream();
                bm = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.imgCover.setImageBitmap(bm);
        }
        holder.txtTitle.setText(novels.get(position).getNovelName());
    }

    @Override
    public int getItemCount() {
        return novels.size();
    }
}

class HolderHorizontal extends RecyclerView.ViewHolder implements View.OnClickListener{
    ItemClickListener itemClickListener;
    ImageView imgCover;
    TextView txtTitle, txtRank;

    public HolderHorizontal(View itemView) {
        super(itemView);
        imgCover = (ImageView) itemView.findViewById(R.id.imgCover);
        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtRank = (TextView) itemView.findViewById(R.id.txtRank);
        txtRank.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
