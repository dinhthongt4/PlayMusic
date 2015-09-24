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
import com.example.thong.playmusic.model.ChildMusicOnline;
import com.example.thong.playmusic.model.MediaInfo;

import java.util.ArrayList;

/**
 * Created by thong on 03/09/2015.
 */
public class RecyclerMusicsAdapter extends RecyclerView.Adapter<RecyclerMusicsAdapter.ViewHolder> {

    private final ArrayList<ChildMusicOnline> mChildMusicOnlines;
    private OnItemClick mOnItemClick;
    private static final String TAG = "RecyclerMusicsAdapter";
    public RecyclerMusicsAdapter(ArrayList<ChildMusicOnline> childMusicOnlines) {
        this.mChildMusicOnlines = childMusicOnlines;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_musics, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtName.setText(mChildMusicOnlines.get(i).getTitle());
        viewHolder.txtArtist.setText("by " +mChildMusicOnlines.get(i).getArtist());
        MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
        metaRetriver.setDataSource(mChildMusicOnlines.get(i).getUrlStream());
        Log.v(TAG,mChildMusicOnlines.get(i).getUrlStream());
        byte[] art = metaRetriver.getEmbeddedPicture();
        if(art != null) {
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(art, 0, art.length);
                viewHolder.imgMediaPlayer.setImageBitmap(songImage);
        }
    }

    @Override
    public int getItemCount() {
        return mChildMusicOnlines.size();
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
