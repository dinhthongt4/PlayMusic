package com.example.thong.playmusic.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.model.Tracks;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by thong on 03/09/2015.
 */
public class RecyclerMusicsAdapter extends RecyclerView.Adapter<RecyclerMusicsAdapter.ViewHolder> {

    private final ArrayList<Tracks> mTrackses;
    private OnItemClick mOnItemClick;
    private static final String TAG = "RecyclerMusicsAdapter";
    private OnClickDownloadListener mOnClickDownloadListener;
    private OnClickAddAlbumListener mOnClickAddAlbumListener;

    public RecyclerMusicsAdapter(ArrayList<Tracks> trackses) {
        this.mTrackses = trackses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_musics, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (mTrackses.get(i).getTitle() != null) {
            if (mTrackses.get(i).getTitle().contains("-")) {
                String[] title = mTrackses.get(i).getTitle().split("-");
                mTrackses.get(i).setTitle(title[0]);

                if (title.length > 1) {
                    mTrackses.get(i).setArtist(title[1]);
                    viewHolder.txtArtist.setText(mTrackses.get(i).getArtist());
                }
            }
            viewHolder.txtName.setText(mTrackses.get(i).getTitle());
        }
        if (mTrackses.get(i).getArtwork_url() != null) {
            ImageLoader.getInstance().displayImage(mTrackses.get(i).getArtwork_url(), viewHolder.imgMediaPlayer);
        }

        if(mTrackses.get(i).isDownloadable()) {
            viewHolder.imgDownload.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mTrackses.size();
    }

    public void setOnClickDownloadListener(OnClickDownloadListener onClickDownloadListener) {
        mOnClickDownloadListener = onClickDownloadListener;
    }

    public void setOnClickAddAlbumListener(OnClickAddAlbumListener onClickAddAlbumListener) {
        mOnClickAddAlbumListener = onClickAddAlbumListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imgMediaPlayer;
        final TextView txtName;
        final TextView txtArtist;
        ImageView imgAddAlbum;
        ImageView imgDownload;

        ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtNameMediaPlayer);
            txtArtist = (TextView) itemView.findViewById(R.id.txtArtistMediaPlayer);
            imgMediaPlayer = (ImageView) itemView.findViewById(R.id.imgMediaPlayer);
            imgAddAlbum = (ImageView) itemView.findViewById(R.id.imgAddAlbum);
            imgDownload = (ImageView) itemView.findViewById(R.id.imgDownload);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClick != null) {
                        mOnItemClick.onClick(getPosition());
                    }
                }
            });

            imgAddAlbum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnClickAddAlbumListener != null) {
                        mOnClickAddAlbumListener.onClick(getPosition());
                    }
                }
            });

            imgDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnClickDownloadListener != null) {
                        mOnClickDownloadListener.onClick(getPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }


    public interface OnItemClick {
        void onClick(int position);
    }

    public interface OnClickDownloadListener {
        void onClick(int position);
    }

    public interface OnClickAddAlbumListener {
        void onClick(int postion);
    }
}
