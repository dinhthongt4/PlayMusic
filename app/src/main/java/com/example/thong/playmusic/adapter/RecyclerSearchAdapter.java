package com.example.thong.playmusic.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.fragment.AllowDialogFragment;
import com.example.thong.playmusic.fragment.NewAlbumDialogFragment;
import com.example.thong.playmusic.fragment.NewAlbumDialogFragment_;
import com.example.thong.playmusic.model.Tracks;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by thongdt on 25/09/2015.
 */
public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder> {

    ArrayList<Tracks> mTrackses;
    private OnClickItemListener mOnClickItemListener;
    private OnClickDownloadListener mOnClickDownloadListener;
    private OnClickAddAlbumListener mOnClickAddAlbumListener;

    public RecyclerSearchAdapter(ArrayList<Tracks> trackses) {
        this.mTrackses = trackses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_musics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(mTrackses.get(position).getArtwork_url() != null) {
            ImageLoader.getInstance().displayImage(mTrackses.get(position).getArtwork_url(), holder.imgAvatar);
        }

        if (mTrackses.get(position).getTitle() != null) {
            if (mTrackses.get(position).getTitle().contains("-")) {
                String[] title = mTrackses.get(position).getTitle().split("-");
                mTrackses.get(position).setTitle(title[0]);

                if (title.length > 1) {
                    mTrackses.get(position).setArtist(title[1]);
                    holder.txtArtist.setText(mTrackses.get(position).getArtist());
                }
            }

            holder.txtTitle.setText(mTrackses.get(position).getTitle());
        }

        if(mTrackses.get(position).isDownloadable()) {
            holder.imgDownLoad.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mTrackses.size();
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        mOnClickItemListener = onClickItemListener;
    }

    public void setOnClickDownloadListener(OnClickDownloadListener onClickDownloadListener) {
        mOnClickDownloadListener = onClickDownloadListener;
    }

    public void setOnClickAddAlbumListener(OnClickAddAlbumListener onClickAddAlbumListener) {
        mOnClickAddAlbumListener = onClickAddAlbumListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtTitle;
        TextView txtArtist;
        ImageView imgAddAlbum;
        ImageView imgDownLoad;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView) itemView.findViewById(R.id.imgMediaPlayer);
            txtTitle = (TextView) itemView.findViewById(R.id.txtNameMediaPlayer);
            txtArtist = (TextView) itemView.findViewById(R.id.txtArtistMediaPlayer);
            imgAddAlbum = (ImageView) itemView.findViewById(R.id.imgAddAlbum);
            imgDownLoad = (ImageView) itemView.findViewById(R.id.imgDownload);


            // set listener in item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickItemListener != null) {
                        mOnClickItemListener.onClick(getPosition());
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

            imgDownLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnClickDownloadListener != null) {
                        mOnClickDownloadListener.onClick(getPosition());
                    }
                }
            });
        }
    }

    public interface OnClickItemListener {
        void onClick(int position);
    }

    public interface OnClickDownloadListener {
        void onClick(int position);
    }

    public interface OnClickAddAlbumListener {
        void onClick(int postion);
    }
}
