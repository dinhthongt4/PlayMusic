package com.example.thong.playmusic.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.thong.playmusic.fragment.IntroduceFragment;

/**
 * Created by thong on 9/15/15.
 */
public class ViewPagerIntroduceAdapter extends FragmentStatePagerAdapter {

    private int[] mCount;

    public ViewPagerIntroduceAdapter(FragmentManager fm, int[] count) {
        super(fm);
        mCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        IntroduceFragment introduceFragment = new IntroduceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("count", mCount[position]);
        introduceFragment.setArguments(bundle);

        return introduceFragment;
    }

    @Override
    public int getCount() {
        return mCount.length;
    }
}
