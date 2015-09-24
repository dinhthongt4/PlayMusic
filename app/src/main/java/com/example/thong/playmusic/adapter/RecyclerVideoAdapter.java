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
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;

/**
 * Created by thongdt on 24/09/2015.
 */
public class RecyclerVideoAdapter extends RecyclerView.Adapter<RecyclerVideoAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerVideoAdapter.ViewHolderHeader> {

    private ArrayList<Item> mItems;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerVideoAdapter(ArrayList<Item> items) {
        mItems = items;
    }


    @Override
    public long getHeaderId(int i) {
        return mItems.get(i).getHeaderID();
    }

    @Override
    public ViewHolderHeader onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_header_video,viewGroup,false);
        ViewHolderHeader viewHolderHeader = new ViewHolderHeader(view);
        return viewHolderHeader;
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolderHeader viewHolderHeader, int i) {
        viewHolderHeader.textView.setText(mItems.get(i).getType());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_video,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if(mItems.get(i).getSnippet().getThumbnail().getHigh().getUrl() != null) {
            ImageLoader.getInstance().displayImage(mItems.get(i).getSnippet().getThumbnail().getHigh().getUrl(),viewHolder.imgThumbnail);
        }
        viewHolder.txtNameVideo.setText(mItems.get(i).getSnippet().getTitle());
        viewHolder.txtNameChannel.setText("by" + mItems.get(i).getSnippet().getChannelTitle());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgThumbnail;
        TextView txtNameVideo;
        TextView txtNameChannel;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.imgThumbnailVideo);
            txtNameVideo = (TextView) itemView.findViewById(R.id.txtNameVideo);
            txtNameChannel = (TextView) itemView.findViewById(R.id.txtNameChannel);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(getPosition());
                    }
                }
            });
        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txtTypeVideo);
        }
    }

    public interface OnItemClickListener {
      void onClick(int position);
    }
}
