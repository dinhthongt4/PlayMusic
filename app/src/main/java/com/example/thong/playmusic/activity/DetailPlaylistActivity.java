package com.example.thong.playmusic.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.MainActivity;
import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerMusicsAdapter;
import com.example.thong.playmusic.adapter.RecyclerSearchAdapter;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.fragment.AddAlubmDialogFragment;
import com.example.thong.playmusic.fragment.AddAlubmDialogFragment_;
import com.example.thong.playmusic.fragment.AllowDialogFragment;
import com.example.thong.playmusic.fragment.AllowDialogFragment_;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.Tracks;
import com.example.thong.playmusic.model.TypeMusicOnline;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;

/**
 * Created by thongdt on 28/09/2015.
 */

@EActivity(R.layout.activity_detail_album)
public class DetailPlaylistActivity extends FragmentActivity {

    private static final String TAG = "DetailPlaylistActivity";

    @Extra("tracks")
    TypeMusicOnline mTypeMusicOnline;

    private RecyclerView.LayoutManager mLayoutManager;
    private ManagerPlay mManagerPlay;
    private RecyclerSearchAdapter mRecyclerSearchAdapter;
    private boolean mIsFirstClick;

    @ViewById(R.id.recyclerViewMusics)
    RecyclerView mRecyclerViewMusic;

    @ViewById(R.id.txtNameAlbum)
    TextView mTxtNameAlbum;

    @ViewById(R.id.imgPause)
    ImageView mImgPause;

    @ViewById(R.id.txtNameMediaPlayer)
    TextView mTxtNameMediaPlayer;

    @ViewById(R.id.txtArtistMediaPlayer)
    TextView mTxtArtistMediaPlayer;

    @ViewById(R.id.imgRepeat)
    ImageView mImgRepeat;

    @ViewById(R.id.imgMediaPlayer)
    ImageView mImgMediaPlayer;

    @AfterViews
    void init() {
        mTxtNameAlbum.setText(mTypeMusicOnline.getPermalink());
        mManagerPlay = ManagerPlay.getInstance();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewMusic.setLayoutManager(mLayoutManager);
        mManagerPlay = ManagerPlay.getInstance();
        mRecyclerSearchAdapter = new RecyclerSearchAdapter(mTypeMusicOnline.getTracks());
        mRecyclerViewMusic.setAdapter(mRecyclerSearchAdapter);
        setClickListener();
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

    @Click(R.id.rlPlayMusic)
    void listenerPlayMusic() {
        UIPlayMusicActivity_.intent(this).start();
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

    private void setClickListener() {
       mRecyclerSearchAdapter.setOnClickItemListener(new RecyclerSearchAdapter.OnClickItemListener() {
           @Override
           public void onClick(int position) {

               if (mManagerPlay.getCurrentMediaPlayer() != null) {
                   mManagerPlay.getCurrentMediaPlayer().stop();
               }
               if(!mIsFirstClick) {
                   mManagerPlay.playSoundOnline(mTypeMusicOnline.getTracks(), position,getApplicationContext());
                   mIsFirstClick = true;
               } else {
                   mManagerPlay.playSoundOnline(position,getApplicationContext());
               }

           }
       });


        mManagerPlay.setmOnSuccessPlayer(new ManagerPlay.OnSuccessPlayer() {
            @Override
            public void onSuccess(Tracks childMusicOnline) {

                if (mManagerPlay.getCurrentInfoMediaPlayer().getArtist() != null) {
                    mTxtArtistMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getArtist());
                }

                if (mManagerPlay.getIsPause()) {
                    mImgPause.setImageResource(R.drawable.ic_play);
                } else {
                    mTxtNameMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getTitle());
                    mImgPause.setImageResource((R.drawable.ic_pause));
                }

                if (mManagerPlay.isRepeat()) {
                    mImgRepeat.setImageResource(R.drawable.icon_repeat_selected);
                } else {
                    mImgRepeat.setImageResource(R.drawable.ic_repeat);
                }

                if (childMusicOnline.getArtwork_url() != null) {
                    ImageLoader.getInstance().displayImage(childMusicOnline.getArtwork_url(), mImgMediaPlayer);
                }
            }
        });

        mRecyclerSearchAdapter.setOnClickDownloadListener(new RecyclerSearchAdapter.OnClickDownloadListener() {
            @Override
            public void onClick(final int position) {
                AllowDialogFragment allowDialogFragment = new AllowDialogFragment_();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                allowDialogFragment.show(ft, "ABC");
                allowDialogFragment.setOnClickSubmitListener(new AllowDialogFragment.OnClickSubmitListener() {
                    @Override
                    public void onClick() {

                        if (!mTypeMusicOnline.getTracks().get(position).getDownload_url().startsWith("http://") && !mTypeMusicOnline.getTracks().get(position).getDownload_url().startsWith("https://"))
                            mTypeMusicOnline.getTracks().get(position).setDownload_url("http://" + mTypeMusicOnline.getTracks().get(position).getDownload_url());

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTypeMusicOnline.getTracks().get(position).getDownload_url() + "?client_id=" + FieldFinal.CLIENT_ID));
                        startActivity(browserIntent);
                    }
                });
            }
        });
    }
}
