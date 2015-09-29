package com.example.thong.playmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.VideoViewFullScreenActivity;
import com.example.thong.playmusic.VideoViewFullScreenActivity_;
import com.example.thong.playmusic.adapter.RecyclerVideoSameAdapter;
import com.example.thong.playmusic.api.Api;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.model.Item;
import com.example.thong.playmusic.model.ListVideos;
import com.example.thong.playmusic.widget.SpacesItemDecoration;
import com.example.thong.playmusic.widget.VideoControllerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit.RestAdapter;

/**
 * Created by thong on 9/15/15.
 */

@EActivity(R.layout.activity_video_view)
public class VideoViewActivity extends Activity implements VideoControllerView.MediaPlayerControl {

    public static final int REQUEST_CODE = 0;
    private static final String TAG = "VideoViewActivity";
    private final String BASE_URL = "http://keepvid.com/?url=https://www.youtube.com/watch?v=";
    private VideoControllerView mVideoControllerView;
    private int mVideoDuration;
    private ArrayList<Item> mItems;
    private RecyclerVideoSameAdapter mRecyclerVideoSameAdapter;
    private String mUrlVideo;

    @ViewById(R.id.video_detail)
    VideoView mVideoView;

    @ViewById(R.id.rlParentVideo)
    RelativeLayout mRlParentVideo;

    @ViewById(R.id.rlRecyclerVideoSame)
    RecyclerView mRecyclerViewVideo;

    @ViewById(R.id.progressBar)
    ProgressBar mProgressBar;

    @Extra("id")
    String mVideoId;

    @Extra("url")
    String mUrlImage;

    @Extra("name")
    String mNameVideo;

    @Extra("channel")
    String mChanelVideo;

    @ViewById(R.id.txtNameVideo)
    TextView mTxtNameVideo;

    @AfterViews
    void init() {
        mVideoControllerView = new VideoControllerView(this, false);
        RequestTask requestTask = new RequestTask();
        requestTask.execute(BASE_URL + mVideoId);
        initMedia();
        mRecyclerViewVideo.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewVideo.setLayoutManager(linearLayoutManager);
        mItems = new ArrayList<>();
        int spacingItem = getResources().getDimensionPixelSize(R.dimen.margin_5);
        mRecyclerViewVideo.addItemDecoration(new SpacesItemDecoration(spacingItem));
        mRecyclerVideoSameAdapter = new RecyclerVideoSameAdapter(mItems);
        mRecyclerViewVideo.setAdapter(mRecyclerVideoSameAdapter);
        getVideosSearch("snippet", mNameVideo, "video", 10, FieldFinal.KEY_YOUTUBE);
        mTxtNameVideo.setText(mNameVideo);
        setListener();
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
                mVideoControllerView.setUrlThumbnail(mUrlImage);
            }
        });
    }

    @UiThread
    void setUIVideo() {
        mRecyclerVideoSameAdapter.notifyDataSetChanged();
    }

    @Background
    void getVideosSearch(String path, String search, String type, int maxResults, String key) {

        int positionSearch = 0;

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.googleapis.com").build();
        Api getDataAPI = restAdapter.create(Api.class);
        ListVideos listVideos = getDataAPI.getVideoSearch(path, search, type, maxResults, key);
        mItems.addAll(positionSearch, listVideos.getItems());
        setUIVideo();
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
    public void fullScreenToggle() {
        Log.v("full",mUrlVideo);
        VideoViewFullScreenActivity_.intent(this).extra("position",mVideoView.getCurrentPosition())
                .extra("urlStreamVideo",mUrlVideo).startForResult(REQUEST_CODE);
    }

    @OnActivityResult(REQUEST_CODE)
    void onResult(int resultCode, Intent data) {

        Log.v(TAG, String.valueOf(data.getIntExtra("position",0)));
        mVideoView.seekTo(data.getIntExtra("position",0));
    }

    private void setListener() {
        mRecyclerVideoSameAdapter.setOnItemClickListener(new RecyclerVideoSameAdapter.OnItemClickListener() {
            @Override
            public void OnClick(int position) {
                RequestTask requestTask = new RequestTask();
                mNameVideo = mItems.get(position).getSnippet().getTitle();
                mUrlImage = mItems.get(position).getSnippet().getThumbnail().getHigh().getUrl();
                mVideoId = mItems.get(position).getId().getVideoId();
                requestTask.execute(BASE_URL + mVideoId);
                if (mVideoView.isPlaying()) {
                    mVideoView.stopPlayback();
                }
                mVideoControllerView.removeAllViews();
                mVideoControllerView = null;
                mVideoControllerView = new VideoControllerView(getApplicationContext(), false);
                mItems.clear();
                getVideosSearch("snippet", mNameVideo, "video", 10, FieldFinal.KEY_YOUTUBE);
                initMedia();
            }
        });
    }

    class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
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

            Log.v("result", responseString.toString());
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
                mUrlVideo = url;
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
