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
public class RecyclerAlbumsAdapter extends RecyclerView.Adapter<RecyclerAlbumsAdapter.ViewHolder> {

    private ArrayList<Album> mAlbums;

    public RecyclerAlbumsAdapter(ArrayList<Album> albums) {
        this.mAlbums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_album,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(mAlbums.get(position).getImgPathAvatar() != null) {
            holder.imgAvatarAlbum.setImageBitmap(mAlbums.get(position).getImgPathAvatar());
        }
        holder.txtNameAlbum.setText(mAlbums.get(position).getAlbumName());
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtNameAlbum;
        ImageView imgAvatarAlbum;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNameAlbum = (TextView) itemView.findViewById(R.id.txtNameAlbum);
            imgAvatarAlbum = (ImageView) itemView.findViewById(R.id.imgAvatarAlbum);
        }
    }
}
