package com.example.thong.playmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.model.Item;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by thongdt on 24/09/2015.
 */
public class RecyclerVideoSameAdapter extends RecyclerView.Adapter<RecyclerVideoSameAdapter.ViewHolder>{

    private ArrayList<Item> mItems;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerVideoSameAdapter(ArrayList<Item> items) {
        this.mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_same_video,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(mItems.get(position).getSnippet().getThumbnail().getHigh().getUrl(),holder.imgThumbnailVideo);
        holder.txtNameVideo.setText(mItems.get(position).getSnippet().getTitle());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgThumbnailVideo;
        TextView txtNameVideo;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnailVideo = (ImageView) itemView.findViewById(R.id.imgThumbnailVideo);
            txtNameVideo = (TextView) itemView.findViewById(R.id.txtNameVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.OnClick(getPosition());
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnClick(int position);
    }
}
