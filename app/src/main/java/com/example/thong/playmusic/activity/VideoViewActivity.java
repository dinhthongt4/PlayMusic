package com.example.thong.playmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.config.FieldFinal;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by thong on 9/15/15.
 */

@EActivity(R.layout.activity_video_view)
public class VideoViewActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private static final String TAG = "VideoViewActivity";
    private YouTubePlayer mYouTubePlayer;

    @ViewById(R.id.youtube_view)
    YouTubePlayerView mYouTubePlayerView;

    @Extra("id")
    String mVideoId;

    @ViewById(R.id.imgPause)
    ImageView mImgPause;

    @ViewById(R.id.txtDuration)
    TextView mTxtDuration;

    @ViewById(R.id.txtTimeCurrent)
    TextView mTxtTimeCurrent;

    @ViewById(R.id.seekBarVideo)
    SeekBar mSeekBarVideo;

    @Click(R.id.imgBack)
    void backClickListener() {
        finish();
    }

    @Click(R.id.imgFullScreen)
    void fullScreenClickListener() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+mVideoId+""));
        intent.putExtra("force_fullscreen",true);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @AfterViews
    void init() {

        Log.v(TAG,mVideoId);
        mYouTubePlayerView.initialize(FieldFinal.KEY_YOUTUBE, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(mVideoId);
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mYouTubePlayer = youTubePlayer;
//        mTxtDuration.setText(youTubePlayer.getDurationMillis());
//        mSeekBarVideo.setProgress(youTubePlayer.getCurrentTimeMillis());

        Log.v(TAG, String.valueOf(youTubePlayer.getCurrentTimeMillis()));
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
