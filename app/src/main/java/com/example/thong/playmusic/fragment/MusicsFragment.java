package com.example.thong.playmusic.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerMusicsAdapter;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.MediaInfo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by thong on 03/09/2015.
 */

@EFragment(R.layout.fragment_musics)
public class MusicsFragment extends Fragment {

    @ViewById(R.id.recycler_musics)
    RecyclerView mRecyclerMusics;

    private RecyclerView.LayoutManager mLayoutManager;
    private ManagerPlay mManagerPlay;
    private ArrayList<MediaInfo> mMediaInfos;
    private RecyclerMusicsAdapter mMusicsAdapter;

    @AfterViews
    void init() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerMusics.setLayoutManager(mLayoutManager);
        mManagerPlay = ManagerPlay.getInstance();
        mMediaInfos = mManagerPlay.getListMusics();
        mMusicsAdapter = new RecyclerMusicsAdapter(mMediaInfos);
        mRecyclerMusics.setAdapter(mMusicsAdapter);
    }
}
