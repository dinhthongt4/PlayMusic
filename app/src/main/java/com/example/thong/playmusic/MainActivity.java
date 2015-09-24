package com.example.thong.playmusic;

import android.content.Intent;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.thong.playmusic.adapter.ViewPagerMainAdapter;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.ChildMusicOnline;
import com.example.thong.playmusic.service.MediaPlayerService_;
import com.example.thong.playmusic.widget.CircleImageView;
import com.example.thong.playmusic.widget.TabBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {

    private static final String TAG = "MAIN";
    private ManagerPlay mManagerPlay;
    private ViewPagerMainAdapter mViewPagerMainAdapter;

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

    @AfterViews
    public void init() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);
        Intent intent = new Intent(this, MediaPlayerService_.class);
        startService(intent);
        mManagerPlay = ManagerPlay.getInstance();
        setListener();
        checkMediaPlayer();

        mViewPagerMainAdapter = new ViewPagerMainAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerMainAdapter);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Click(R.id.imgPause)
    void listenerPause() {

        if (mManagerPlay.getIsPause()) {
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
            public void onSuccess(ChildMusicOnline childMusicOnline) {

                mTxtNameMediaPlayer.setText(checkLimitText(childMusicOnline.getTitle(), 15));
                mTxtArtistMediaPlayer.setText(checkLimitText(childMusicOnline.getArtist(), 20));
                mManagerPlay.setIsPause(false);
                mImgPause.setImageResource(R.drawable.ic_pause);
                if (childMusicOnline.getUrlAvatar() != null) {
                    ImageLoader.getInstance().displayImage(childMusicOnline.getUrlAvatar(), mImgMediaPlayer);
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
        if (mManagerPlay.getCurrentMediaPlayer() != null) {

            mTxtNameMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getTitle());
            mTxtArtistMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getArtist());
            if (mManagerPlay.getIsPause()) {
                mImgPause.setImageResource(R.drawable.ic_play);
            } else {
                mImgPause.setImageResource((R.drawable.ic_pause));
            }
        }
    }
}
