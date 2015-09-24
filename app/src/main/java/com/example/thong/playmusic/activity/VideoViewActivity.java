package com.example.thong.playmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.widget.VideoControllerView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by thong on 9/15/15.
 */

@EActivity(R.layout.activity_video_view)
public class VideoViewActivity extends Activity implements VideoControllerView.MediaPlayerControl{


    private static final String TAG = "VideoViewActivity";
    private final String BASE_URL = "http://keepvid.com/?url=https://www.youtube.com/watch?v=";
    private VideoControllerView mVideoControllerView;
    private int mVideoDuration;

    @ViewById(R.id.video_detail)
    VideoView mVideoView;

    @ViewById(R.id.rlParentVideo)
    RelativeLayout mRlParentVideo;

    @ViewById(R.id.rlRecyclerVideoSame)
    RecyclerView mRecyclerViewVideo;

    @Extra("id")
    String mVideoId;

    @AfterViews
    void init() {
        mVideoControllerView = new VideoControllerView(this,false);
        RequestTask requestTask = new RequestTask();
        requestTask.execute(BASE_URL+mVideoId);
        initMedia();
    }


    private void initMedia() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoDuration = 0;
                mVideoDuration = mVideoView.getDuration();
                mVideoControllerView.setMediaPlayer(VideoViewActivity.this);
                mVideoControllerView.setAnchorView(mRlParentVideo);
                mVideoControllerView.show();
            }
        });
    }

    @Override
    public void start() {
        if (!mVideoView.isPlaying()) {
            mVideoControllerView.setBackgroundDrawable(null);
            mVideoView.start();
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
    public boolean canPause() {
        return false;
    }

    @Override
    public void fullScreenToggle() {

    }

    @Override
    public void playNormalVideo() {

    }

    @Override
    public void playDoubleSpeedVideo() {

    }

    class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... uri) {
            HttpURLConnection httpURLConnection = null;
            StringBuilder responseString = null;
            BufferedReader reader = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(uri[0]).openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(15 * 1000);
                httpURLConnection.connect();
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                responseString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    responseString.append(line + "\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace(); //If you want further info on failure...
                }
            }

            Log.v("result",responseString.toString());
            return String.valueOf(responseString);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Parser HTML
            Document doc = Jsoup.parse(result);
            String url = "";
            Element div = doc.select("div[id=dl").first();
            if (div != null) {
                for (int i = 0; i < div.childNodeSize(); i++) {
                    Element link = div.getElementsByIndexEquals(i).select("a[class=l]").first();
                    if (link != null && link.text().equals("» Download MP4 «")) {
                        url = link.attr("href");
                        Log.v("urlStream", url);
                        break;
                    }
                }
                mVideoView.setVideoPath(url);
            }
        }
    }
}
