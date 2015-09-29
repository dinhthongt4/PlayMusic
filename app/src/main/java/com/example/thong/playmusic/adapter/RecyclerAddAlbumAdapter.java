package com.example.thong.playmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.model.Album;

import java.util.ArrayList;

/**
 * Created by thongdt on 29/09/2015.
 */
public class RecyclerAddAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Album> mAlbums;
    private OnClickAlbumListener mOnClickAlbumListener;
    private OnClickNewAlbumListener mOnClickNewAlbumListener;

    public RecyclerAddAlbumAdapter(ArrayList<Album> albums) {
        this.mAlbums = albums;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_new_album_in_dialog,parent,false);
            return new NewAlbumViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_add_album,parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position != mAlbums.size()) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtNameAlbum.setText(mAlbums.get(position).getAlbumName());
        }
    }

    @Override
    public int getItemCount() {
        return mAlbums.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mAlbums.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setOnClickAlbumListener(OnClickAlbumListener onClickAlbumListener) {
        mOnClickAlbumListener = onClickAlbumListener;
    }

    public void setOnClickNewAlbumListener(OnClickNewAlbumListener onClickNewAlbumListener) {
        mOnClickNewAlbumListener = onClickNewAlbumListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatarAlbum;
        TextView txtNameAlbum;

        public ViewHolder(View itemView) {
            super(itemView);

            txtNameAlbum = (TextView) itemView.findViewById(R.id.txtNameAlbum);
            imgAvatarAlbum = (ImageView) itemView.findViewById(R.id.imgAvatarAlbum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnClickAlbumListener != null) {
                        mOnClickAlbumListener.OnClick(getPosition());
                    }
                }
            });
        }
    }

    class NewAlbumViewHolder extends RecyclerView.ViewHolder {

        public NewAlbumViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnClickNewAlbumListener != null) {
                        mOnClickNewAlbumListener.OnClick();
                    }
                }
            });
        }
    }

    public interface OnClickAlbumListener {
        void OnClick(int position);
    }

    public interface OnClickNewAlbumListener {
        void OnClick();
    }
}
