package com.example.thong.playmusic;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.thong.playmusic.widget.VideoControllerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by thongdt on 25/09/2015.
 */

@EActivity(R.layout.activity_video_full_screen)
public class VideoViewFullScreenActivity extends Activity implements VideoControllerView.MediaPlayerControl{

    private VideoControllerView mVideoControllerView;
    private int mVideoDuration;

    @ViewById(R.id.rlParentVideo)
    RelativeLayout mRlParentVideo;

    @ViewById(R.id.video_detail)
    VideoView mVideoView;

    @Extra("position")
    long mPosition;

    @Extra("urlStreamVideo")
    String mUrl;

    @AfterViews
    void init() {
        mVideoControllerView = new VideoControllerView(this, false);
        mVideoView.setVideoPath(mUrl);
        initMedia();
    }

    private void initMedia() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoDuration = 0;
                mVideoDuration = mVideoView.getDuration();
                mVideoControllerView.setMediaPlayer(VideoViewFullScreenActivity.this);
                mVideoControllerView.setAnchorView(mRlParentVideo);
                mVideoControllerView.show();
                mVideoControllerView.doPauseResume();
            }
        });
    }

    @Override
    public void start() {
        if (!mVideoView.isPlaying()) {
            mVideoView.start();
            mVideoView.seekTo((int) mPosition);
        }
    }

    @Override
    public void pause() {
        if (mVideoView.isPlaying())
            mVideoView.pause();
        else
            mVideoView.resume();
    }

    @Override
    public int getDuration() {
        return mVideoDuration;
    }

    @Override
    public int getCurrentPosition() {
        return mVideoView.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mVideoView.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mVideoView.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public void fullScreenToggle() {

    }

    @Override
    public void onBackPressed() {
        setResult(1, getIntent().putExtra("position", mVideoView.getCurrentPosition()));
        Log.v("position", String.valueOf(mVideoView.getCurrentPosition()));
        super.onBackPressed();
    }
}
