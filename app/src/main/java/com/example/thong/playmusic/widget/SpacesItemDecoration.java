package com.example.thong.playmusic.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by thongdt on 24/09/2015.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
        // Add top margin only for the first item to avoid double space between items
        if(parent.getChildLayoutPosition(view) == 0)
            outRect.top = space;
    }
}