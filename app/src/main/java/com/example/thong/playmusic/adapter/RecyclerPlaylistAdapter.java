package com.example.thong.playmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.model.TypeMusicOnline;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by thongdt on 25/09/2015.
 */
public class RecyclerPlaylistAdapter extends RecyclerView.Adapter<RecyclerPlaylistAdapter.ViewHolder> {

    private OnClickItemListener mOnClickItemListener;
    private ArrayList<TypeMusicOnline> mTypeMusicOnlines;
    private int mNumberImage = 0;

    public RecyclerPlaylistAdapter(ArrayList<TypeMusicOnline> typeMusicOnlines) {
        this.mTypeMusicOnlines = typeMusicOnlines;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_playlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(mTypeMusicOnlines.get(position).getTracks().get(mNumberImage).getArtwork_url(),holder.imgAvatarPlaylist);
        holder.txtNumberMusic.setText("+ " + mTypeMusicOnlines.get(position).getTracks().size());
        holder.txtNamePlaylist.setText(mTypeMusicOnlines.get(position).getPermalink());
    }

    @Override
    public int getItemCount() {
        return mTypeMusicOnlines.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatarPlaylist;
        TextView txtNamePlaylist;
        TextView txtNumberMusic;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatarPlaylist = (ImageView) itemView.findViewById(R.id.imgAvatarPlaylist);
            txtNamePlaylist = (TextView) itemView.findViewById(R.id.txtNamePlaylist);
            txtNumberMusic = (TextView) itemView.findViewById(R.id.txtNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickItemListener != null) {
                        mOnClickItemListener.onClick(getPosition());
                    }
                }
            });
        }
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        mOnClickItemListener = onClickItemListener;
    }

    public interface OnClickItemListener {
        void onClick(int position);
    }
}
