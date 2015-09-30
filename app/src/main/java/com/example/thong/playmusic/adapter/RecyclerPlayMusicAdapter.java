package com.example.thong.playmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.model.Tracks;

import java.util.ArrayList;

/**
 * Created by thongdt on 30/09/2015.
 */
public class RecyclerPlayMusicAdapter extends RecyclerView.Adapter<RecyclerPlayMusicAdapter.ViewHolder> {

    private ArrayList<Tracks> mTrackses;
    private OnItemClickListener mOnItemClickListener;
    private int mNumberMedia;

    public RecyclerPlayMusicAdapter(ArrayList<Tracks> trackses, int numberMedia) {
        this.mTrackses = trackses;
        this.mNumberMedia = numberMedia;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_play_music, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtNameMusic.setText(mTrackses.get(position).getTitle());
        if(mTrackses.get(position).getArtist() != null) {
            holder.txtNameArtist.setText(mTrackses.get(position).getArtist());
        }

        if (mNumberMedia == position) {
            holder.rootView.setBackgroundColor(0xffBDBDBD);
        }
    }

    @Override
    public int getItemCount() {
        return mTrackses.size();
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameMusic;
        TextView txtNameArtist;
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            txtNameMusic = (TextView) itemView.findViewById(R.id.txtNameMusic);
            txtNameArtist = (TextView) itemView.findViewById(R.id.txtNameArtist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.OnClick(getPosition(), v);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnClick(int position, View v);
    }
}
