package com.example.thong.playmusic.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.activity.DetailAlbumActivity_;
import com.example.thong.playmusic.activity.DetailPlaylistActivity_;
import com.example.thong.playmusic.adapter.RecyclerPlaylistAdapter;
import com.example.thong.playmusic.adapter.RecyclerSearchAdapter;
import com.example.thong.playmusic.api.Api;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.SearchMusics;
import com.example.thong.playmusic.model.Tracks;
import com.example.thong.playmusic.model.TypeMusicOnline;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit.RestAdapter;


@EFragment(R.layout.fragment_musics_online)
public class MusicsOnlineFragment extends Fragment {

    private static final String TAG = "ONLINE";

    private ArrayList<Tracks> mTracks;
    private ArrayList<TypeMusicOnline> mTypeMusicOnlines;
    private RecyclerPlaylistAdapter mRecyclerPlaylistAdapter;
    private RecyclerView.LayoutManager mLayoutManagerLinear;
    private RecyclerView.LayoutManager mLayoutManagerGrid;
    private RecyclerSearchAdapter mRecyclerSearchAdapter;
    private ManagerPlay mManagerPlay;

    @ViewById(R.id.recyclerViewMusicOnline)
    RecyclerView mRecyclerViewMusicsOnline;

    @ViewById(R.id.imgSearch)
    ImageView mImgSearch;

    @ViewById(R.id.edtSearch)
    EditText mEdtSearch;

    @ViewById(R.id.progressBarSearch)
    ProgressBar mProgressBarSearch;

    @ViewById(R.id.rlSearch)
    RelativeLayout mRlSearch;

    @ViewById(R.id.txtPlaylist)
    TextView mTxtPlaylist;

    @ViewById(R.id.txtSearch)
    TextView mTxtSearch;


    @AfterViews
    void init() {
        mManagerPlay = ManagerPlay.getInstance();

        mTxtPlaylist.setBackgroundColor(0xff7878F5);
        mRlSearch.setVisibility(View.GONE);

        mRecyclerViewMusicsOnline.setHasFixedSize(true);
        mLayoutManagerLinear = new LinearLayoutManager(getActivity());
        mLayoutManagerGrid = new GridLayoutManager(getActivity(),2);
        mRecyclerViewMusicsOnline.setLayoutManager(mLayoutManagerGrid);

        mTracks = new ArrayList<>();
        mRecyclerSearchAdapter = new RecyclerSearchAdapter(mTracks);

        mTypeMusicOnlines = new ArrayList<>();
        mRecyclerPlaylistAdapter = new RecyclerPlaylistAdapter(mTypeMusicOnlines);
        mRecyclerViewMusicsOnline.setAdapter(mRecyclerPlaylistAdapter);

        getPlaylistMusics();
        setListener();
    }

    // get playlist music in soundclound
    @Background
    void getPlaylistMusics() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://api.soundcloud.com").build();
        Api api = restAdapter.create(Api.class);

        ArrayList<TypeMusicOnline> typeMusicOnlines = api.getMusicsOnline(FieldFinal.CLIENT_ID);

        if(mTypeMusicOnlines.size() >  0) {
            mTypeMusicOnlines.clear();
        }

        mTypeMusicOnlines.addAll(typeMusicOnlines);
        Log.v(TAG,"size" +   String.valueOf(mTypeMusicOnlines.size()));
        setUiMusicsOnline();
    }

    // set ui after load complex music in play list
    @UiThread
    void setUiMusicsOnline() {
        Log.v(TAG, "size" + String.valueOf(mTypeMusicOnlines.size()));
        mRecyclerPlaylistAdapter.notifyDataSetChanged();
    }

    // get list musics when search
    @Background
    void getPlaylistSearch(String strSearch) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://api.soundcloud.com").build();
        Api api = restAdapter.create(Api.class);
        SearchMusics searchMusics = api.getMusicsSearchOnline(strSearch, FieldFinal.CLIENT_ID, 20);
        ArrayList<Tracks> trackses = searchMusics.getCollection();
        if(mTracks.size() > 0) {
            mTracks.clear();
        }

        Log.v(TAG, String.valueOf(trackses.size()));
        mTracks.addAll(trackses);
        setUiMusicSearch();
    }

    @UiThread
    void setUiMusicSearch() {
        mImgSearch.setVisibility(View.VISIBLE);
        mProgressBarSearch.setVisibility(View.GONE);
        mRecyclerSearchAdapter.notifyDataSetChanged();
    }

    @Click(R.id.imgSearch)
    void setListenerSearch() {
        mImgSearch.setVisibility(View.GONE);
        mProgressBarSearch.setVisibility(View.VISIBLE);
        String strSearch = mEdtSearch.getText().toString();
        getPlaylistSearch(strSearch);
    }

    @Click(R.id.txtPlaylist)
    void setListenerPlaylistItem() {
        mTxtPlaylist.setBackgroundColor(0xff7878F5);
        mTxtSearch.setBackgroundColor(0xffA9A9F5);
        mRlSearch.setVisibility(View.GONE);
        mRecyclerViewMusicsOnline.setLayoutManager(mLayoutManagerGrid);
        mRecyclerViewMusicsOnline.setAdapter(mRecyclerPlaylistAdapter);
    }

    @Click(R.id.txtSearch)
    void setListenerSearchItem() {
        mTxtSearch.setBackgroundColor(0xff7878F5);
        mTxtPlaylist.setBackgroundColor(0xffA9A9F5);
        mRlSearch.setVisibility(View.VISIBLE);
        mRecyclerViewMusicsOnline.setLayoutManager(mLayoutManagerLinear);
        mRecyclerViewMusicsOnline.setAdapter(mRecyclerSearchAdapter);
    }

    public void setListener() {
        mRecyclerSearchAdapter.setOnClickItemListener(new RecyclerSearchAdapter.OnClickItemListener() {
            @Override
            public void onClick(int position) {
                mManagerPlay.setIsRepeat(true);
                if(mManagerPlay.getCurrentMediaPlayer() != null) {
                    mManagerPlay.getCurrentMediaPlayer().release();
                }
                mManagerPlay.playSoundOnline(mTracks.get(position));
            }
        });

        mRecyclerSearchAdapter.setOnClickAddAlbumListener(new RecyclerSearchAdapter.OnClickAddAlbumListener() {
            @Override
            public void onClick(int postion) {

            }
        });

        mRecyclerSearchAdapter.setOnClickDownloadListener(new RecyclerSearchAdapter.OnClickDownloadListener() {
            @Override
            public void onClick(final int position) {
                AllowDialogFragment allowDialogFragment = new AllowDialogFragment_();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                allowDialogFragment.show(ft, "ABC");
                allowDialogFragment.setOnClickSubmitListener(new AllowDialogFragment.OnClickSubmitListener() {
                    @Override
                    public void onClick() {

                        Log.v(TAG, String.valueOf(mTracks.size()));
                        if (!mTracks.get(position).getDownload_url().startsWith("http://") && !mTracks.get(position).getDownload_url().startsWith("https://"))
                            mTracks.get(position).setDownload_url("http://" + mTracks.get(position).getDownload_url());

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTracks.get(position).getDownload_url() + "?client_id=" + FieldFinal.CLIENT_ID));
                        startActivity(browserIntent);
                    }
                });
            }
        });

        mRecyclerPlaylistAdapter.setOnClickItemListener(new RecyclerPlaylistAdapter.OnClickItemListener() {
            @Override
            public void onClick(int position) {
                DetailPlaylistActivity_.intent(getActivity()).extra("tracks",mTypeMusicOnlines.get(position)).start();
            }
        });
    }
}
