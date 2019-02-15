package main.dailynovel.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import main.dailynovel.ItemClickListener;
import main.dailynovel.MainActivity;
import main.dailynovel.Objects.*;
import main.dailynovel.R;

/**
 * Created by l3umb on 11/25/2017.
 */

public class NovelAdapterVertical extends RecyclerView.Adapter<HolderVertical>{
    List<Novel> novels;
    Context context;
    ItemClickListener listener;

    public NovelAdapterVertical(Context context, List<Novel> novels, ItemClickListener listener) {
        this.context = context;
        this.novels = novels;
        this.listener = listener;
    }

    @Override
    public HolderVertical onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical,null);
        final HolderVertical holderVertical = new HolderVertical(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(view, holderVertical.getPosition());
            }
        });
        return holderVertical;
    }

    @Override
    public void onBindViewHolder(HolderVertical holder, int position) {
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
        holder.txtAuthor.setText("Tác giả: " + novels.get(position).getNovelAuthors());
        holder.txtStatus.setText("Tình trạng: " + novels.get(position).getNovelStatus());
        holder.txtChapter.setText("Số chương: " + novels.get(position).getNovelChapter());
    }

    @Override
    public int getItemCount() {
        return novels.size();
    }
}

class HolderVertical extends RecyclerView.ViewHolder implements View.OnClickListener {
    ItemClickListener itemClickListener;
    ImageView imgCover;
    TextView txtTitle, txtAuthor, txtStatus, txtChapter;

    public HolderVertical(View itemView) {
        super(itemView);
        imgCover = (ImageView) itemView.findViewById(R.id.imgCover);
        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtAuthor = (TextView) itemView.findViewById(R.id.txtAuthor);
        txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
        txtChapter = (TextView) itemView.findViewById(R.id.txtChapter);
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(view, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
