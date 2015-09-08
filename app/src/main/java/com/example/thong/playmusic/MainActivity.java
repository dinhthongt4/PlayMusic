package com.example.thong.playmusic;

import android.content.Intent;
import android.media.MediaPlayer;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import android.widget.SeekBar;
import android.widget.TextView;

import com.example.thong.playmusic.adapter.ViewPagerMainAdapter;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.MediaInfo;
import com.example.thong.playmusic.service.MediaPlayerService_;
import com.example.thong.playmusic.widget.CircleImageView;
import com.example.thong.playmusic.widget.TabBar;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    private static final String TAG = "MAIN";
    private ManagerPlay mManagerPlay;
    private ViewPagerMainAdapter mViewPagerMainAdapter;

    @ViewById(R.id.seekBar)
    SeekBar mSeekBar;

    @ViewById(R.id.imgMediaPlayer)
    CircleImageView mImgMediaPlayer;

    @ViewById(R.id.txtNameMediaPlayer)
    TextView mTxtNameMediaPlayer;

    @ViewById(R.id.txtArtistMediaPlayer)
    TextView mTxtArtistMediaPlayer;

    @ViewById(R.id.imgPause)
    ImageView mImgPause;

    @ViewById(R.id.viewPager)
    ViewPager mViewPager;

    @ViewById(R.id.tabBar)
    TabBar mTabBar;

    @AfterViews
    public void init() {
        Intent intent  = new Intent(this, MediaPlayerService_.class);
        startService(intent);
        mManagerPlay = ManagerPlay.getInstance();
        setListener();
        checkMediaPlayer();
        setAnimationDisk();

        mViewPagerMainAdapter = new ViewPagerMainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerMainAdapter);
    }

    @Click(R.id.imgPause)
    void listenerPause() {
        Log.v(TAG,"1");
        if(mManagerPlay.getIsPause()) {
            mImgPause.setImageResource(R.drawable.ic_pause);
            mManagerPlay.onStart();
            mManagerPlay.setIsPause(false);
        } else {
            mImgPause.setImageResource(R.drawable.ic_play);
            mManagerPlay.onPause();
            mManagerPlay.setIsPause(true);
        }
    }

    @Click(R.id.imgNext)
    void listenerNext() {
        mManagerPlay.onNext(this);
    }

    @Click(R.id.imgBack)
    void listenerBack() {
        mManagerPlay.onBack(this);
    }

    @Click(R.id.imgStop)
    void listenerStop() {
        stopService(new Intent(this,MediaPlayerService_.class));
    }

    // listener for media player
    private void setListener() {

        mManagerPlay.setmOnSuccessPlayer(new ManagerPlay.OnSuccessPlayer() {
            @Override
            public void onSuccess(MediaInfo mediaInfo) {

                mTxtNameMediaPlayer.setText(checkLimitText(mediaInfo.getName(),15));
                mTxtArtistMediaPlayer.setText(checkLimitText(mediaInfo.getArtist(),20));
                mSeekBar.setMax(Integer.parseInt(mediaInfo.getDuration()));
                mSeekBar.setVisibility(View.VISIBLE);
                mManagerPlay.setIsPause(false);
                mImgPause.setImageResource(R.drawable.ic_pause);
                if(mediaInfo.getBmMediaPlayer() == null) {
                    mImgMediaPlayer.setImageResource(R.drawable.ic_disk1);

                } else {
                    mImgMediaPlayer.setImageBitmap(mediaInfo.getBmMediaPlayer());
                }
            }
        });

        mManagerPlay.setOnChangeDuration(new ManagerPlay.OnChangeDuration() {
            @Override
            public void onChanger(MediaPlayer mediaPlayer) {
                mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                mSeekBar.setMax(mediaPlayer.getDuration());
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            boolean touch;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress == mManagerPlay.getCurrentMediaPlayer().getDuration()) {
                    mManagerPlay.onNext(getApplicationContext());
                }

                if (touch) {
                    mSeekBar.setProgress(progress);
                    mManagerPlay.getCurrentMediaPlayer().seekTo(progress);
                    touch = false;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                touch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabBar.clickTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabBar.setOnClickTabBar(new TabBar.OnClickTabBar() {
            @Override
            public void onClick(int position) {
                mViewPager.setCurrentItem(position);
            }
        });
    }

    // check limit of textView
    private String checkLimitText(String s, int limit) {
        if(s.length() > limit) {
            s = s.substring(0,limit);
            s = s + "...";
        }

        return s;
    }

    private void setAnimationDisk() {
        mImgMediaPlayer.setStartAnimation();
    }

    private void checkMediaPlayer() {
        if (mManagerPlay.getCurrentMediaPlayer() != null) {

            mTxtNameMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getName());
            mTxtArtistMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getArtist());
            if(mManagerPlay.getIsPause()) {
                mImgPause.setImageResource(R.drawable.ic_play);
            } else {
                mImgPause.setImageResource((R.drawable.ic_pause));
            }
        }
    }
}
