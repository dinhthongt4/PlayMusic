package com.example.thong.playmusic.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerAlbumsAdapter;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.Album;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by thong on 04/09/2015.
 */

@EFragment(R.layout.fragment_album)
public class AlbumFragment extends Fragment{

    private ManagerPlay mManagerPlay;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Album> mAlbum;
    private RecyclerAlbumsAdapter mRecyclerAlbumsAdapter;

    @ViewById(R.id.recycler_album)
    RecyclerView mRecyclerView;


    @AfterViews
    public void init() {
        mManagerPlay = ManagerPlay.getInstance();
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAlbum = new ArrayList<>();
        for (int i = 0; i < 5 ; i ++) {
            Album album = new Album();
            album.setAlbumName("thong "+i);
            mAlbum.add(album);
        }
        mRecyclerAlbumsAdapter = new RecyclerAlbumsAdapter(mAlbum);
        mRecyclerView.setAdapter(mRecyclerAlbumsAdapter);
    }
}
