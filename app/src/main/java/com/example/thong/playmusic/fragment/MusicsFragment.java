package com.example.thong.playmusic.fragment;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerMusicsAdapter;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.Tracks;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by thong on 03/09/2015.
 */

@EFragment(R.layout.fragment_musics)
public class MusicsFragment extends Fragment {

    private static final String TAG = "MusicsFragment";

    @ViewById(R.id.recycler_musics)
    RecyclerView mRecyclerMusics;

    private RecyclerView.LayoutManager mLayoutManager;
    private ManagerPlay mManagerPlay;
    private ArrayList<Tracks> mTrackses;
    private RecyclerMusicsAdapter mMusicsAdapter;

    @AfterViews
    public void init() {
        mTrackses = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerMusics.setLayoutManager(mLayoutManager);
        mManagerPlay = ManagerPlay.getInstance();
        mTrackses = listOfSongs(getActivity());
        insertMusics();
        getListMusic();
        mMusicsAdapter = new RecyclerMusicsAdapter(mTrackses);
        mRecyclerMusics.setAdapter(mMusicsAdapter);
        setListener();
    }

    private void getListMusic() {
        ManagerDatabase managerDatabase = new ManagerDatabase(getActivity());
        try {
            managerDatabase.open();

            if(mTrackses != null) {
                mTrackses.clear();
            }

            mTrackses.addAll(managerDatabase.getDataMediaInfo());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            managerDatabase.close();
        }
    }

    void setListener() {
        mMusicsAdapter.setOnItemClick(new RecyclerMusicsAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {

                if (mManagerPlay.getCurrentMediaPlayer() != null) {
                    mManagerPlay.getCurrentMediaPlayer().stop();
                }

                if (mManagerPlay.getListMusics() == null) {
                    mManagerPlay.playSound(getActivity(), position, mTrackses);
                } else {
                    mManagerPlay.playSound(getActivity(), position);
                }
            }
        });

        mMusicsAdapter.setOnClickAddAlbumListener(new RecyclerMusicsAdapter.OnClickAddAlbumListener() {
            @Override
            public void onClick(final int postion) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddAlubmDialogFragment addAlubmDialogFragment = new AddAlubmDialogFragment_();
                addAlubmDialogFragment.show(fragmentTransaction, "abc");

                addAlubmDialogFragment.setOnSelectedAlbumListener(new AddAlubmDialogFragment.OnSelectedAlbumListener() {
                    @Override
                    public void OnSelected(int albumId) {

                        ManagerDatabase managerDatabase = new ManagerDatabase(getActivity());
                        try {
                            managerDatabase.open();

                            Log.v(TAG, String.valueOf(mTrackses.get(postion).getId()));

                            managerDatabase.insertMediaGroup(mTrackses.get(postion).getId(), albumId);
                            Toast.makeText(getActivity(), "Add success", Toast.LENGTH_LONG).show();
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

    //get all musics in device
    public static ArrayList<Tracks> listOfSongs(Context context){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor c = context.getContentResolver().query(uri, null, MediaStore.Audio.Media.IS_MUSIC + " != 0", null, null);
        ArrayList<Tracks> listOfSongs = new ArrayList<Tracks>();
        c.moveToFirst();
        while(c.moveToNext()){
            Tracks tracks = new Tracks();

            String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String data = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));

            tracks.setTitle(title);
            tracks.setArtist(artist);
            tracks.setDuration(duration);
            tracks.setStream_url(data);
            listOfSongs.add(tracks);
        }
        c.close();
        return listOfSongs;
    }

    // insert music to database
    public void insertMusics() {
        ManagerDatabase managerDatabase = new ManagerDatabase(getActivity());
        try {
            managerDatabase.open();
            for (int i = 0; i < mTrackses.size(); i ++) {
                managerDatabase.insertMediaInfo(mTrackses.get(i).getStream_url(),mTrackses.get(i).getTitle(),
                        0,mTrackses.get(i).getArtist() +"", "", String.valueOf(mTrackses.get(i).getDuration()),
                        mTrackses.get(i).getArtwork_url()+"","","","");
            }
        } catch (SQLException e) {
            Log.v(TAG,"loi");
        } finally {
            managerDatabase.close();
        }
    }
}
