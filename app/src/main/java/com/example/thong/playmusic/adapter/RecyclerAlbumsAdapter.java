package com.example.thong.playmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.model.Album;

import java.util.ArrayList;

/**
 * Created by thong on 04/09/2015.
 */
public class RecyclerAlbumsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Album> mAlbums;
    private OnNewAlbumClickListener mOnNewAlbumClickListener;
    private OnItemAlbumClickListener mOnItemAlbumClickListener;

    public RecyclerAlbumsAdapter(ArrayList<Album> albums) {
        this.mAlbums = albums;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mAlbums.size() - 1) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_new_album,parent,false);
            NewAlbumViewHolder newAlbumViewHolder = new NewAlbumViewHolder(view);
            return newAlbumViewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_album,parent,false);
            AlbumViewHolder viewHolder = new AlbumViewHolder(view);
            return viewHolder;
        }
        }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position != mAlbums.size() -1) {
            AlbumViewHolder albumViewHolder = (AlbumViewHolder) holder;
            albumViewHolder.txtNameAlbum.setText(mAlbums.get(position).getAlbumName());
        }
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public void setOnItemAlbumClickListener(OnItemAlbumClickListener onItemAlbumClickListener) {
        mOnItemAlbumClickListener = onItemAlbumClickListener;
    }

    public void setOnNewAlbumClickListener(OnNewAlbumClickListener onNewAlbumClickListener) {
        mOnNewAlbumClickListener = onNewAlbumClickListener;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{

        TextView txtNameAlbum;
        ImageView imgAvatarAlbum;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            txtNameAlbum = (TextView) itemView.findViewById(R.id.txtNameAlbum);
            imgAvatarAlbum = (ImageView) itemView.findViewById(R.id.imgAvatarAlbum);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemAlbumClickListener != null) {
                        mOnItemAlbumClickListener.onClick(mAlbums.get(getPosition()).getIdAlbum(),mAlbums.get(getPosition()).getAlbumName());
                    }
                }
            });
        }
    }

    public class NewAlbumViewHolder extends RecyclerView.ViewHolder{

        public NewAlbumViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnNewAlbumClickListener != null) {
                        mOnNewAlbumClickListener.onClick();
                    }
                }
            });
        }
    }

    public interface OnNewAlbumClickListener {
        void onClick();
    }

    public interface OnItemAlbumClickListener {
        void onClick(int id, String albumName);
    }
}
