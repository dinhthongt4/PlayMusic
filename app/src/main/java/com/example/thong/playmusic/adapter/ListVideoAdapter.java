package com.example.thong.playmusic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by thong on 9/7/15.
 */
public class ListVideoAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private ArrayList<Item> mItems;
    private final Context mContext;
    private OnLoadMoreListener mOnLoadMoreListener;

    public ListVideoAdapter(ArrayList<Item> items, Context context) {
        this.mItems = items;
        mContext = context;
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {

        HeaderViewHolder headerViewHolder;
        if (view == null) {
            headerViewHolder = new HeaderViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_header_videos, viewGroup, false);
            headerViewHolder.txtType = (TextView) view.findViewById(R.id.txtTypeVideo);
            view.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) view.getTag();
        }

        headerViewHolder.txtType.setText(mItems.get(i).getType());
        return view;
    }

    @Override
    public long getHeaderId(int i) {
        return mItems.get(i).getHeaderID();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        if (mItems.get(i).getTypeItem() == 1) {

            View v = LayoutInflater.from(mContext).inflate(R.layout.item_list_more_video, viewGroup, false);
            final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            final TextView txtMore = (TextView) v.findViewById(R.id.txtMoreVideo);
            final ImageView imgMore = (ImageView) v.findViewById(R.id.imgMoreVideo);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    txtMore.setVisibility(View.GONE);
                    imgMore.setVisibility(View.GONE);
                    mOnLoadMoreListener.onClickLoadMore(mItems.get(i).getType(), mItems.get(i).getTypeItem()
                            , mItems.get(i).getHeaderID(), mItems.get(i).getNumberList());
                }
            });

            return v;

        } else {

            View v = view;
            ViewHolder viewHolder;


            if (v == null || v.getTag() == null) {
                viewHolder = new ViewHolder();

                v = LayoutInflater.from(mContext).inflate(R.layout.item_list_videos, viewGroup, false);

                viewHolder.imgThumbnail = (ImageView) v.findViewById(R.id.imgThumbnailVideo);
                viewHolder.txtNameVideo = (TextView) v.findViewById(R.id.txtNameVideo);
                viewHolder.txtNameChannel = (TextView) v.findViewById(R.id.txtNameChannel);
                viewHolder.txtDatePublish = (TextView) v.findViewById(R.id.txtDatePublish);

                v.setTag(viewHolder);

            } else {

                viewHolder = (ViewHolder) v.getTag();
            }

            if (mItems.size() > 0) {
                Picasso.with(mContext).load(mItems.get(i).getSnippet().getThumbnail()
                        .getHigh().getUrl()).into(viewHolder.imgThumbnail);
            }
            viewHolder.txtNameVideo.setText(mItems.get(i).getSnippet().getTitle());
            viewHolder.txtNameChannel.setText(mItems.get(i).getSnippet().getChannelTitle());
            viewHolder.txtDatePublish.setText(mItems.get(i).getSnippet().getPublishAt());

            return v;
        }


    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    class HeaderViewHolder {
        TextView txtType;
    }

    class ViewHolder {
        ImageView imgThumbnail;
        TextView txtNameVideo;
        TextView txtNameChannel;
        TextView txtDatePublish;
    }

    public interface OnLoadMoreListener {
        void onClickLoadMore(String typeHeader, int typeItem, int headerID, int numberList);
    }
}
