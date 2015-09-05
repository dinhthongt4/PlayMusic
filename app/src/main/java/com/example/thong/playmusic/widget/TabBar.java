package com.example.thong.playmusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thong.playmusic.R;

/**
 * Created by thong on 03/09/2015.
 */
public class TabBar extends RelativeLayout {

    private RelativeLayout rlTabBar;
    private TextView mTxtMusics;
    private TextView mTxtAlbum;
    private TextView mTxtVideo;
    private TextView mTxtOnline;
    private OnClickTabBar mOnClickTabBar;

    private static final int COLOR_DEFAULT = 0x00000000;
    private static final int COLOR_SELECTED = 0xca0489B1;

    public TabBar(Context context) {
        super(context);
        init(context);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rlTabBar = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.tab_bar,this,false);
        addView(rlTabBar);

        mTxtMusics = (TextView) rlTabBar.findViewById(R.id.txtMusics);
        mTxtAlbum = (TextView) rlTabBar.findViewById(R.id.txtAlBums);
        mTxtVideo = (TextView) rlTabBar.findViewById(R.id.txtVideo);
        mTxtOnline = (TextView) rlTabBar.findViewById(R.id.txtOnline);

        setListener();
        clickTab(0);
    }

    private void setListener() {
        mTxtMusics.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTab(0);
            }
        });

        mTxtAlbum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTab(1);
            }
        });

        mTxtVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTab(2);
            }
        });

        mTxtOnline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTab(3);
            }
        });
    }

    public void setOnClickTabBar(OnClickTabBar onClickTabBar) {
        mOnClickTabBar = onClickTabBar;
    }

    // click position tab bar
    public void clickTab(int position) {
        setColorDefault();
        if(position == 0) {
            mTxtMusics.setBackgroundColor(COLOR_SELECTED);

            if(mOnClickTabBar != null) {
                mOnClickTabBar.onClick(0);
            }
        } else if(position == 1) {
            mTxtAlbum.setBackgroundColor(COLOR_SELECTED);
            if(mOnClickTabBar != null) {
                mOnClickTabBar.onClick(1);
            }
        } else if(position == 2) {
            mTxtVideo.setBackgroundColor(COLOR_SELECTED);
            if(mOnClickTabBar != null) {
                mOnClickTabBar.onClick(2);
            }
        } else if(position == 3) {
            mTxtOnline.setBackgroundColor(COLOR_SELECTED);
            if(mOnClickTabBar != null) {
                mOnClickTabBar.onClick(3);
            }
        }
    }

    private void setColorDefault() {
        mTxtVideo.setBackgroundColor(COLOR_DEFAULT);
        mTxtAlbum.setBackgroundColor(COLOR_DEFAULT);
        mTxtMusics.setBackgroundColor(COLOR_DEFAULT);
        mTxtOnline.setBackgroundColor(COLOR_DEFAULT);
    }

    public interface OnClickTabBar {
        void onClick(int position);
    }
}
