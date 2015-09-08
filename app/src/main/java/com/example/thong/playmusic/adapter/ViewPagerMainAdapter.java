package com.example.thong.playmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.thong.playmusic.fragment.AlbumFragment;
import com.example.thong.playmusic.fragment.AlbumFragment_;
import com.example.thong.playmusic.fragment.MusicsFragment_;
import com.example.thong.playmusic.fragment.VideoFragment;
import com.example.thong.playmusic.fragment.VideoFragment_;

/**
 * Created by thong on 03/09/2015.
 */
public class ViewPagerMainAdapter extends FragmentStatePagerAdapter {

    public ViewPagerMainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new MusicsFragment_();
        } else if (position == 1) {
            return new AlbumFragment_();
        } else {
            return new VideoFragment_();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
