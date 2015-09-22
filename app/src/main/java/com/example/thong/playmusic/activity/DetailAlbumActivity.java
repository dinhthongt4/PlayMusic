package com.example.thong.playmusic.activity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerMusicsAdapter;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.ChildMusicOnline;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thongdt on 18/09/2015.
 */
@EActivity(R.layout.activity_detail_album)
public class DetailAlbumActivity extends Activity {

    private static final String TAG = "DetailAlbumActivity";
    private RecyclerView.LayoutManager mLayoutManager;
    private ManagerPlay mManagerPlay;
    private ArrayList<ChildMusicOnline> mChildMusicOnlines;
    private RecyclerMusicsAdapter mMusicsAdapter;

    @ViewById(R.id.recyclerViewMusics)
    RecyclerView mRecyclerViewMusic;

    @Extra("id")
    int mId;

    @Extra("albumName")
    String mAlbumName;

    @ViewById(R.id.txtNameAlbum)
    TextView mTxtNameAlbum;

    @ViewById(R.id.imgPause)
    ImageView mImgPause;

    @ViewById(R.id.txtNameMediaPlayer)
    TextView mTxtNameMediaPlayer;

    @ViewById(R.id.txtArtistMediaPlayer)
    TextView mTxtArtistMediaPlayer;

    @AfterViews
    void init() {
        mTxtNameAlbum.setText(mAlbumName);
        mManagerPlay = ManagerPlay.getInstance();
        checkMediaPlayer();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewMusic.setLayoutManager(mLayoutManager);
        mManagerPlay = ManagerPlay.getInstance();
        getChilMusicOnline();
        mMusicsAdapter = new RecyclerMusicsAdapter(mChildMusicOnlines);
        mRecyclerViewMusic.setAdapter(mMusicsAdapter);
        setClickListener();
    }

    private void getChilMusicOnline() {
        ManagerDatabase managerDatabase = new ManagerDatabase(this);
        try {
            managerDatabase.open();
            ArrayList<Integer> idMusics = managerDatabase.getListIdMusics(mId);
            mChildMusicOnlines = new ArrayList<>();
            for (int i =0; i < idMusics.size(); i++) {
                mChildMusicOnlines.add(managerDatabase.getDataMediaInfo(idMusics.get(i)));
                Log.v(TAG,mChildMusicOnlines.get(i).getUrlStream());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            managerDatabase.close();
        }
    }

    @Click(R.id.imgPause)
    void listenerPause() {

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
        checkMediaPlayer();
    }

    @Click(R.id.imgBack)
    void listenerBack() {
        mManagerPlay.onBack(this);
        checkMediaPlayer();
    }

    private void checkMediaPlayer() {
        if (mManagerPlay.getCurrentMediaPlayer() != null) {

            mTxtNameMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getTitle());
            mTxtArtistMediaPlayer.setText(mManagerPlay.getCurrentInfoMediaPlayer().getArtist());
            if(mManagerPlay.getIsPause()) {
                mImgPause.setImageResource(R.drawable.ic_play);
            } else {
                mImgPause.setImageResource((R.drawable.ic_pause));
            }
        }
    }

    private void setClickListener() {
        mMusicsAdapter.setOnItemClick(new RecyclerMusicsAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                if( mManagerPlay.getCurrentMediaPlayer() != null) {
                    mManagerPlay.getCurrentMediaPlayer().release();
                }
                mManagerPlay.playSound(getApplicationContext(),position,mChildMusicOnlines);
                checkMediaPlayer();
            }
        });
    }
}
