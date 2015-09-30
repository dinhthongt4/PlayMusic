package com.example.thong.playmusic.activity;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerMusicsAdapter;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.fragment.AddAlubmDialogFragment;
import com.example.thong.playmusic.fragment.AddAlubmDialogFragment_;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.Tracks;

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
public class DetailAlbumActivity extends FragmentActivity {

    private static final String TAG = "DetailAlbumActivity";
    private RecyclerView.LayoutManager mLayoutManager;
    private ManagerPlay mManagerPlay;
    private ArrayList<Tracks> mTrackses;
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

    @ViewById(R.id.imgRepeat)
    ImageView mImgRepeat;

    @AfterViews
    void init() {
        mTxtNameAlbum.setText(mAlbumName);
        mManagerPlay = ManagerPlay.getInstance();
        checkMediaPlayer();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewMusic.setLayoutManager(mLayoutManager);
        mManagerPlay = ManagerPlay.getInstance();
        getChilMusicOnline();
        mMusicsAdapter = new RecyclerMusicsAdapter(mTrackses);
        mRecyclerViewMusic.setAdapter(mMusicsAdapter);
        setClickListener();
    }

    @Click(R.id.rlPlayMusic)
    void listenerPlayMusic() {
        UIPlayMusicActivity_.intent(this).start();
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

    private void getChilMusicOnline() {
        ManagerDatabase managerDatabase = new ManagerDatabase(this);
        try {
            managerDatabase.open();
            ArrayList<Integer> idMusics = managerDatabase.getListIdMusics(mId);
            mTrackses = new ArrayList<>();
            for (int i =0; i < idMusics.size(); i++) {
                mTrackses.add(managerDatabase.getDataMediaInfo(idMusics.get(i)));;
                Log.v(TAG, String.valueOf(mTrackses.get(i).getId()));
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
                    mManagerPlay.getCurrentMediaPlayer().stop();
                }
                mManagerPlay.playSound(getApplicationContext(),position,mTrackses);
                checkMediaPlayer();
            }
        });

        mMusicsAdapter.setOnClickAddAlbumListener(new RecyclerMusicsAdapter.OnClickAddAlbumListener() {
            @Override
            public void onClick(final int postion) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddAlubmDialogFragment addAlubmDialogFragment = new AddAlubmDialogFragment_();
                addAlubmDialogFragment.show(fragmentTransaction, "abc");

                addAlubmDialogFragment.setOnSelectedAlbumListener(new AddAlubmDialogFragment.OnSelectedAlbumListener() {
                    @Override
                    public void OnSelected(int albumId) {

                        ManagerDatabase managerDatabase = new ManagerDatabase(getApplicationContext());
                        try {
                            managerDatabase.open();

                            Log.v(TAG, String.valueOf(mTrackses.get(postion).getId()));

                            managerDatabase.insertMediaGroup(mTrackses.get(postion).getId(), albumId);
                            Toast.makeText(getApplicationContext(), "Add success", Toast.LENGTH_SHORT);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            managerDatabase.close();
                        }
                    }
                });
            }
        });
    }

}
