package com.example.thong.playmusic.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerPlayMusicAdapter;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.Tracks;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by thongdt on 29/09/2015.
 */

@EActivity(R.layout.activity_play_music)
public class UIPlayMusicActivity extends Activity {

    @ViewById(R.id.recyclerListMusics)
    RecyclerView mRecyclerViewListMusic;

    @ViewById(R.id.imgRepeat)
    ImageView mImgRepeat;

    @ViewById(R.id.imgPause)
    ImageView mImgPlayPause;

    @ViewById(R.id.seekBar)
    SeekBar mSeekBar;

    @ViewById(R.id.txtNameMediaPlayer)
    TextView mTxtNameMusic;

    @ViewById(R.id.txtArtistMediaPlayer)
    TextView mTxtArtist;

    @ViewById(R.id.imgMediaPlayer)
    ImageView mImgAvatar;

    @ViewById(R.id.txtCurrentDuration)
    TextView mTxtCurrentDuration;

    @ViewById(R.id.txtDuration)
    TextView mTxtDuration;

    private RecyclerPlayMusicAdapter mRecyclerPlayMusicAdapter;
    private ArrayList<Tracks> mTrackses;
    private ManagerPlay mManagerPlay;
    private int mProgress;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkMediaPlayer();
        }
    };
    private IntentFilter mIntentFilter;

    @AfterViews
    void init() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewListMusic.setLayoutManager(manager);
        mManagerPlay = ManagerPlay.getInstance();
        mTrackses = new ArrayList<>();
        if (mManagerPlay.getListMusics() != null) {
            mTrackses.addAll(mManagerPlay.getListMusics());
        }
        mRecyclerPlayMusicAdapter = new RecyclerPlayMusicAdapter(mTrackses, mManagerPlay.getNumberMedia());
        mRecyclerViewListMusic.setAdapter(mRecyclerPlayMusicAdapter);

        checkMediaPlayer();

        setListener();
    }

    @Click(R.id.imgRepeat)
    void setListenerRepeet() {
        if (mManagerPlay.getListMusics() != null) {
            if (mManagerPlay.isRepeat()) {
                mManagerPlay.setIsRepeat(false);
                mImgRepeat.setImageResource(R.drawable.ic_repeat);
            } else {
                mManagerPlay.setIsRepeat(true);
                mImgRepeat.setImageResource(R.drawable.icon_repeat_selected);
            }
        }
    }

    @Click(R.id.imgPause)
    void setListenerPlayPause() {
        if (mManagerPlay.getIsPause()) {
            mImgPlayPause.setImageResource(R.drawable.ic_pause);
            mManagerPlay.onStart(this);
            mManagerPlay.setIsPause(false);
        } else {
            mImgPlayPause.setImageResource(R.drawable.ic_play);
            mManagerPlay.onPause();
            mManagerPlay.setIsPause(true);
        }
    }

    @Click(R.id.imgNext)
    void setListenerNext() {
        mManagerPlay.onNext(this);
    }

    @Click(R.id.imgBack)
    void setListenerBack() {
        mManagerPlay.onBack(this);
    }

    private void setListener() {

        mRecyclerPlayMusicAdapter.setmOnItemClickListener(new RecyclerPlayMusicAdapter.OnItemClickListener() {
            @Override
            public void OnClick(int position, View v) {

                if(mManagerPlay.isOnline()) {
                    if(mManagerPlay.getCurrentMediaPlayer() != null) {
                        mManagerPlay.getCurrentMediaPlayer().stop();
                    }
                    mManagerPlay.playSoundOnline(position,getApplicationContext());
                } else {
                    if (mManagerPlay.getCurrentMediaPlayer() != null) {
                        mManagerPlay.getCurrentMediaPlayer().stop();
                    }
                    mManagerPlay.playSound(getApplicationContext(), position);
                }
            }
        });

        mManagerPlay.setmOnSuccessPlayer(new ManagerPlay.OnSuccessPlayer() {
            @Override
            public void onSuccess(Tracks childMusicOnline) {
                mTxtNameMusic.setText((childMusicOnline.getTitle()));

                if (childMusicOnline.getArtist() != null) {
                    mTxtArtist.setText(childMusicOnline.getArtist());
                }
                mManagerPlay.setIsPause(false);
                mImgPlayPause.setImageResource(R.drawable.ic_pause);

                if (mManagerPlay.isRepeat()) {
                    mImgRepeat.setImageResource(R.drawable.icon_repeat_selected);

                } else {
                    mImgRepeat.setImageResource(R.drawable.ic_repeat);
                }

                if (childMusicOnline.getArtwork_url() != null) {
                    ImageLoader.getInstance().displayImage(childMusicOnline.getArtwork_url(), mImgAvatar);
                }

                mTxtDuration.setText(getTimeString(childMusicOnline.getDuration()));
                mSeekBar.setMax((int) childMusicOnline.getDuration());

                if (mManagerPlay.getListMusics() != null) {
                    mRecyclerPlayMusicAdapter.notifyDataSetChanged();
                }
            }
        });

        mManagerPlay.setOnChangeDuration(new ManagerPlay.OnChangeDuration() {
            @Override
            public void getDuration(int duration) {
                mTxtCurrentDuration.setText(getTimeString(duration));
                mSeekBar.setProgress(duration);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                @Override
                                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                                    if(progress - mProgress > 1100) {
                                                        mManagerPlay.getCurrentMediaPlayer().seekTo(progress);
                                                    } else if (progress - mProgress < 0) {
                                                        mManagerPlay.getCurrentMediaPlayer().seekTo(progress);
                                                    }
                                                    mProgress = progress;

                                                    mTxtCurrentDuration.setText(getTimeString(progress));
                                                }

                                                @Override
                                                public void onStartTrackingTouch(SeekBar seekBar) {

                                                }

                                                @Override
                                                public void onStopTrackingTouch(SeekBar seekBar) {

                                                }
                                            }
        );
    }

    private void checkMediaPlayer() {
        if (mManagerPlay.getCurrentInfoMediaPlayer() != null) {

            mTxtNameMusic.setText(mManagerPlay.getCurrentInfoMediaPlayer().getTitle());
            mTxtArtist.setText(mManagerPlay.getCurrentInfoMediaPlayer().getArtist());
            if (mManagerPlay.getIsPause()) {
                mImgPlayPause.setImageResource(R.drawable.ic_play);
            } else {
                mImgPlayPause.setImageResource((R.drawable.ic_pause));
            }

            if (mManagerPlay.isRepeat()) {
                mImgRepeat.setImageResource(R.drawable.icon_repeat_selected);
            } else {
                mImgRepeat.setImageResource(R.drawable.ic_repeat);
            }

            if (mManagerPlay.getCurrentInfoMediaPlayer().getArtwork_url() != null) {
                ImageLoader.getInstance().displayImage(mManagerPlay.getCurrentInfoMediaPlayer().getArtwork_url(), mImgAvatar);
            }

            mTxtDuration.setText(getTimeString(mManagerPlay.getCurrentInfoMediaPlayer().getDuration()));
            mSeekBar.setMax((int) mManagerPlay.getCurrentInfoMediaPlayer().getDuration());

            if (mManagerPlay.getListMusics() != null) {
                mRecyclerPlayMusicAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf.append(String.format("%02d", hours)).append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(FieldFinal.ACTION_CHANGE_MEDIA);

        registerReceiver(broadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
