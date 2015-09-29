package com.example.thong.playmusic.fragment;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.activity.DetailAlbumActivity;
import com.example.thong.playmusic.activity.DetailAlbumActivity_;
import com.example.thong.playmusic.adapter.RecyclerAlbumsAdapter;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.Album;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thong on 04/09/2015.
 */

@EFragment(R.layout.fragment_album)
public class AlbumFragment extends Fragment{

    private ManagerPlay mManagerPlay;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Album> mAlbumArrayList;
    private RecyclerAlbumsAdapter mRecyclerAlbumsAdapter;

    @ViewById(R.id.recycler_album)
    RecyclerView mRecyclerView;


    @AfterViews
    public void init() {
        mManagerPlay = ManagerPlay.getInstance();
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAlbumArrayList = new ArrayList<>();
        getAlbumName();
        mRecyclerAlbumsAdapter = new RecyclerAlbumsAdapter(mAlbumArrayList);
        mRecyclerView.setAdapter(mRecyclerAlbumsAdapter);
        // set listener in album
        setListener();
    }

    private void getAlbumName() {
        Album album = new Album();
        album.setAlbumName("New Album");

        mAlbumArrayList.add(album);
        ManagerDatabase managerDatabase = new ManagerDatabase(getActivity());
        try {
            managerDatabase.open();
            mAlbumArrayList.addAll(0, managerDatabase.getNameAlbum());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            managerDatabase.close();
        }
    }

    private void setListener() {

        // click item album listener
        mRecyclerAlbumsAdapter.setOnItemAlbumClickListener(new RecyclerAlbumsAdapter.OnItemAlbumClickListener() {
            @Override
            public void onClick(int id, String albumName) {
                DetailAlbumActivity_.intent(getActivity()).extra("id", id).extra("albumName", albumName).start();
            }
        });

        // click new album
        mRecyclerAlbumsAdapter.setOnNewAlbumClickListener(new RecyclerAlbumsAdapter.OnNewAlbumClickListener() {
            @Override
            public void onClick() {
                NewAlbumDialogFragment newAlbumDialogFragment = new NewAlbumDialogFragment_();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                newAlbumDialogFragment.show(ft, "ABC");

                newAlbumDialogFragment.setOnClickSubmitListener(new NewAlbumDialogFragment.OnClickSubmitListener() {
                    @Override
                    public void onClick() {
                        mAlbumArrayList.clear();
                        getAlbumName();
                        mRecyclerAlbumsAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("123", "123");
    }

    public void reLoadAlbum() {
        mAlbumArrayList.clear();
        getAlbumName();
        mRecyclerAlbumsAdapter.notifyDataSetChanged();
    }
}
