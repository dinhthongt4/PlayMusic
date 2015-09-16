package com.example.thong.playmusic.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.config.FieldFinal;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by thong on 9/15/15.
 */

@EActivity(R.layout.activity_video_view)
public class VideoViewActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    @ViewById(R.id.youtube_view)
    YouTubePlayerView mYouTubePlayerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @AfterViews
    void init() {

        mYouTubePlayerView.initialize(FieldFinal.KEY_YOUTUBE, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo("_oEA18Y8gM0");
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
