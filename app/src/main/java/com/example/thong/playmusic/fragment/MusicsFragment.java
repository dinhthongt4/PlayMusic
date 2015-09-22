package com.example.thong.playmusic.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerMusicsAdapter;
import com.example.thong.playmusic.database.ManagerDatabase;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.ChildMusicOnline;
import com.example.thong.playmusic.model.MediaInfo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

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
    private ArrayList<ChildMusicOnline> mChildMusicOnlines;
    private RecyclerMusicsAdapter mMusicsAdapter;

    @AfterViews
    void init() {
        mChildMusicOnlines = new ArrayList<>();
        mChildMusicOnlines = listOfSongs(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerMusics.setLayoutManager(mLayoutManager);
        mManagerPlay = ManagerPlay.getInstance();
        mMusicsAdapter = new RecyclerMusicsAdapter(mChildMusicOnlines);
        mRecyclerMusics.setAdapter(mMusicsAdapter);
        insertMusics();
        setListener();
    }

    void setListener() {
        mMusicsAdapter.setOnItemClick(new RecyclerMusicsAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {

                if (mManagerPlay.getCurrentMediaPlayer() != null) {
                    mManagerPlay.getCurrentMediaPlayer().release();
                }

                if (mManagerPlay.getListMusics() == null || mManagerPlay.getListMusics().get(position).isOnline()) {
                    mManagerPlay.playSound(getActivity(), position, mChildMusicOnlines);
                } else {
                    mManagerPlay.playSound(getActivity(), position);
                }
            }
        });
    }

    //get all musics in device
    public static ArrayList<ChildMusicOnline> listOfSongs(Context context){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor c = context.getContentResolver().query(uri, null, MediaStore.Audio.Media.IS_MUSIC + " != 0", null, null);
        ArrayList<ChildMusicOnline> listOfSongs = new ArrayList<ChildMusicOnline>();
        c.moveToFirst();
        while(c.moveToNext()){
            ChildMusicOnline childMusicOnline = new ChildMusicOnline();

            String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            long duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String data = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));

            childMusicOnline.setTitle(title);
            childMusicOnline.setAlbum(album);
            childMusicOnline.setArtist(artist);
            childMusicOnline.setDuration(duration);
            childMusicOnline.setUrlStream(data);
            listOfSongs.add(childMusicOnline);
        }
        c.close();
        return listOfSongs;
    }

    // insert music to database
    public void insertMusics() {
        ManagerDatabase managerDatabase = new ManagerDatabase(getActivity());
        try {
            managerDatabase.open();
            for (int i = 0; i < mChildMusicOnlines.size(); i ++) {
                managerDatabase.insertMediaInfo(mChildMusicOnlines.get(i).getUrlStream(),mChildMusicOnlines.get(i).getTitle(),
                        0,mChildMusicOnlines.get(i).getArtist() +"", "", String.valueOf(mChildMusicOnlines.get(i).getDuration()),
                        mChildMusicOnlines.get(i).getUrlAvatar()+"","","","");
            }
        } catch (SQLException e) {
            Log.v(TAG,"loi");
        } finally {
            managerDatabase.close();
        }
    }
}
