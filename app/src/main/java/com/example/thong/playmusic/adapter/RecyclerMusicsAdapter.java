package com.example.thong.playmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.model.ChildMusicOnline;
import com.example.thong.playmusic.model.MediaInfo;

import java.util.ArrayList;

/**
 * Created by thong on 03/09/2015.
 */
public class RecyclerMusicsAdapter extends RecyclerView.Adapter<RecyclerMusicsAdapter.ViewHolder> {

    private final ArrayList<ChildMusicOnline> mMediaInfos;
    private OnItemClick mOnItemClick;

    public RecyclerMusicsAdapter(ArrayList<ChildMusicOnline> mediaInfos) {
        this.mMediaInfos = mediaInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_musics, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtName.setText(mMediaInfos.get(i).getTitle());
        viewHolder.txtArtist.setText(mMediaInfos.get(i).getArtist());
    }

    @Override
    public int getItemCount() {
        return mMediaInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imgMediaPlayer;
        final TextView txtName;
        final TextView txtArtist;

        ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtNameMediaPlayer);
            txtArtist = (TextView) itemView.findViewById(R.id.txtArtistMediaPlayer);
            imgMediaPlayer = (ImageView) itemView.findViewById(R.id.imgMediaPlayer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mOnItemClick !=  null) {
                        mOnItemClick.onClick(getPosition());
                    }
                }
            });
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }


    public interface OnItemClick {
        public void onClick(int position);
    }
}
