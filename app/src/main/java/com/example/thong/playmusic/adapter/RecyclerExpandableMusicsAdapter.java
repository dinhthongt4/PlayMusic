package com.example.thong.playmusic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.thong.playmusic.R;
import com.example.thong.playmusic.model.ChildMusicOnline;
import com.example.thong.playmusic.model.TypeMusicOnline;
import java.util.List;

/**
 * Created by thong on 9/11/15.
 */
public class RecyclerExpandableMusicsAdapter extends ExpandableRecyclerAdapter<RecyclerExpandableMusicsAdapter.ParentViewHolder,RecyclerExpandableMusicsAdapter.ChildViewHolder> {

    private OnChildItemClick mOnChildItemClick;

    public void setOnChildItemClick(OnChildItemClick onChildItemClick) {
        mOnChildItemClick = onChildItemClick;
    }

    public RecyclerExpandableMusicsAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_parent,viewGroup,false);
        ParentViewHolder parentViewHolder = new ParentViewHolder(view);
        return parentViewHolder;
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_child,viewGroup,false);
        ChildViewHolder childViewHolder = new ChildViewHolder(view);
        return childViewHolder;
    }

    @Override
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, final int i, Object o) {
        TypeMusicOnline typeMusicOnline = (TypeMusicOnline) o;
        parentViewHolder.txtTypeMusics.setText(typeMusicOnline.getTypeMusicOnline());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int i, Object o) {
        ChildMusicOnline childMusicOnline = (ChildMusicOnline) o;
        childViewHolder.txtNameMusic.setText(childMusicOnline.getTitle());
    }

    public class ParentViewHolder extends com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder {
        TextView txtTypeMusics;
        View iteView;

        public ParentViewHolder(View itemView) {
            super(itemView);
            iteView = itemView;
            txtTypeMusics = (TextView) itemView.findViewById(R.id.txtTypeMusics);
        }
    }

    public class ChildViewHolder extends com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder {
        ImageView imgAvatarMusic;
        TextView txtNameMusic;
        TextView txtArtistMusic;
        ImageView imgDownloadMusic;

        public ChildViewHolder(View itemView) {
            super(itemView);

            imgAvatarMusic = (ImageView) itemView.findViewById(R.id.imgAvatarMusic);
            txtNameMusic = (TextView) itemView.findViewById(R.id.txtNameMusic);
            txtArtistMusic = (TextView) itemView.findViewById(R.id.txtArtistMusic);
            imgDownloadMusic = (ImageView) itemView.findViewById(R.id.imgDownLoad);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("position1", String.valueOf(getPosition()));
                    if(mOnChildItemClick != null) {
                        mOnChildItemClick.onClick(getPosition());

                    }
                }
            });
        }
    }

    public interface OnChildItemClick {
        void onClick(int position);
    }
}
