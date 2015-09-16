package com.example.thong.playmusic.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.example.thong.playmusic.MainActivity_;
import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.ViewPagerIntroduceAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by thong on 9/15/15.
 */

@EActivity(R.layout.activity_introduce)
public class IntroduceActivity extends FragmentActivity {

    private int [] mCount = new int[5];
    private ViewPagerIntroduceAdapter mViewPagerIntroduceAdapter;

    @ViewById(R.id.viewPager)
    ViewPager mViewPager;

    @ViewById(R.id.txtNext)
    TextView mTxtNext;

    @AfterViews
    void init() {
        mCount[0] = R.drawable.abc;
        mCount[1] = R.drawable.bca;
        mCount[2] = R.drawable.cab;
        mCount[3] = R.drawable.abc;
        mCount[4] = R.drawable.bca;

        mViewPagerIntroduceAdapter = new ViewPagerIntroduceAdapter(getSupportFragmentManager(), mCount);
        mViewPager.setAdapter(mViewPagerIntroduceAdapter);
    }

    @Click(R.id.txtNext)
    void setClickNext() {
        if(mViewPager.getCurrentItem() == mCount.length - 2) {
            mTxtNext.setText("FINISH");
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        } else if(mViewPager.getCurrentItem() == mCount.length - 1) {
            MainActivity_.intent(this).start();
            finish();
        } else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    }

    @Click(R.id.txtBack)
    void setClickBack() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        mTxtNext.setText("NEXT");
    }
}
