package com.example.thong.playmusic;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.thong.playmusic.activity.UIPlayMusicActivity_;
import com.example.thong.playmusic.adapter.ViewPagerMainAdapter;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.Tracks;
import com.example.thong.playmusic.service.MediaPlayerService;
import com.example.thong.playmusic.service.MediaPlayerService_;
import com.example.thong.playmusic.widget.TabBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    private static final String TAG = "MAIN";
    private ManagerPlay mManagerPlay;
    private ViewPagerMainAdapter mViewPagerMainAdapter;
    private ServiceConnection mServiceConnection;
    private boolean mIsBound;
    private MediaPlayerService mMediaPlayerService;

    @ViewById(R.id.imgMediaPlayer)
    ImageView mImgMediaPlayer;

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

    @ViewById(R.id.imgRepeat)
    ImageView mImgRepeat;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkMediaPlayer();
        }
    };
    private IntentFilter mIntentFilter;

    @AfterViews
    public void init() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
             ;

        Intent intent  = new Intent(MainActivity.this, MediaPlayerService_.class);
        startService(intent);

        mViewPagerMainAdapter = new ViewPagerMainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerMainAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mManagerPlay = ManagerPlay.getInstance();
        setListener();
        checkMediaPlayer();


    }
    @Click(R.id.rlPlayMusic)
    void listenerPlayMusic() {
        UIPlayMusicActivity_.intent(this).start();
    }

    @Click(R.id.imgPause)
    void listenerPause() {

        if (mManagerPlay.getIsPause()) {
            mManagerPlay.setIsPause(false);
            mImgPause.setImageResource(R.drawable.ic_pause);
            mManagerPlay.onStart(this);

        } else {
            mManagerPlay.setIsPause(true);
            mImgPause.setImageResource(R.drawable.ic_play);
            mManagerPlay.onPause();
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

    @Click(R.id.imgRepeat)
    void listenerRepeat() {
        if(mManagerPlay.getListMusics() != null) {
            if(mManagerPlay.isRepeat()) {
                mManagerPlay.setIsRepeat(false);
                mImgRepeat.setImageResource(R.drawable.ic_repeat);
            } else {
                mManagerPlay.setIsRepeat(true);
                mImgRepeat.setImageResource(R.drawable.icon_repeat_selected);
            }
        }
     }


    // listener for media player
    private void setListener() {

        mManagerPlay.setmOnSuccessPlayer(new ManagerPlay.OnSuccessPlayer() {
            @Override
            public void onSuccess(Tracks tracks) {

                mTxtNameMediaPlayer.setText(checkLimitText(tracks.getTitle(), 15));
                mTxtArtistMediaPlayer.setText(checkLimitText(tracks.getArtist(), 20));
                mManagerPlay.setIsPause(false);
                mImgPause.setImageResource(R.drawable.ic_pause);

                if(mManagerPlay.isRepeat()) {
                    mImgRepeat.setImageResource(R.drawable.icon_repeat_selected);

                } else {
                    mImgRepeat.setImageResource(R.drawable.ic_repeat);
                }

                if (tracks.getArtwork_url() != null) {
                    ImageLoader.getInstance().displayImage(tracks.getArtwork_url(), mImgMediaPlayer);
                }
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

        if (s == null) {
            s = "";
        }

        if (s.length() > limit) {
            s = s.substring(0, limit);
            s = s + "...";
        }

        return s;
    }

    private void checkMediaPlayer() {
        if (mManagerPlay.getCurrentInfoMediaPlayer() != null) {
            mTxtNameMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getTitle());
            mTxtArtistMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getArtist());
            if (mManagerPlay.getIsPause()) {
                mImgPause.setImageResource(R.drawable.ic_play);
            } else {
                mImgPause.setImageResource((R.drawable.ic_pause));
            }
            if(mManagerPlay.isRepeat()) {
                mImgRepeat.setImageResource(R.drawable.icon_repeat_selected);

            } else {
                mImgRepeat.setImageResource(R.drawable.ic_repeat);
            }

            if (mManagerPlay.getCurrentInfoMediaPlayer().getArtwork_url() != null) {
                ImageLoader.getInstance().displayImage(mManagerPlay.getCurrentInfoMediaPlayer().getArtwork_url(), mImgMediaPlayer);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(FieldFinal.ACTION_CHANGE_MEDIA);

        registerReceiver(broadcastReceiver,mIntentFilter);

        if(mManagerPlay != null) {
            checkMediaPlayer();
            setListener();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // unbindService(mServiceConnection);
        Log.v(TAG,"Destrouy");
    }
}
