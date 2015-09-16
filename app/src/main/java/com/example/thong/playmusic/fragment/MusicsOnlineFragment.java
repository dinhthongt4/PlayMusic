package com.example.thong.playmusic.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bignerdranch.expandablerecyclerview.ClickListeners.ExpandCollapseListener;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.RecyclerExpandableMusicsAdapter;
import com.example.thong.playmusic.api.Api;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.model.ChildMusicOnline;
import com.example.thong.playmusic.model.RootMusicOnline;
import com.example.thong.playmusic.model.TypeMusicOnline;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit.RestAdapter;

/**
 * Created by thong on 9/11/15.
 */

@EFragment(R.layout.fragment_musics_online)
public class MusicsOnlineFragment extends Fragment {
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String TAG = "ONLINE";
    private RecyclerExpandableMusicsAdapter mRecyclerExpandableMusicsAdapter;
    private ArrayList<ParentObject> mParentObjects;
    private int mPositionList = 1000;

    @ViewById(R.id.recyclerViewMusicOnline)
    RecyclerView mRecyclerViewMusicsOnline;

    @ViewById(R.id.imgSearch)
    ImageView mImgSearch;

    @ViewById(R.id.edtSearch)
    EditText mEdtSearch;

    @ViewById(R.id.progressBarSearch)
    ProgressBar mProgressBarSearch;

    @AfterViews
    void init() {
        mRecyclerViewMusicsOnline.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewMusicsOnline.setLayoutManager(mLayoutManager);
        mParentObjects = new ArrayList<>();
        getPlaylistMusics();
    }

    // get playlist music in soundclound
    @Background
    void getPlaylistMusics() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://api.soundcloud.com").build();
        Api api = restAdapter.create(Api.class);
        generateMusics(api.getMusicsOnline(FieldFinal.CLIENT_ID));
        setUiMusicsOnline();
    }

    // set ui after load complex music in play list
    @UiThread
    void setUiMusicsOnline() {
        mRecyclerExpandableMusicsAdapter = new RecyclerExpandableMusicsAdapter(getActivity(),mParentObjects);
        mRecyclerViewMusicsOnline.setAdapter(mRecyclerExpandableMusicsAdapter);
        setOnClickListener();
    }

    // get list musics when search
    @Background
    void getPlaylistSearch(String strSearch) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://api.soundcloud.com").build();
        Api api = restAdapter.create(Api.class);
        RootMusicOnline rootMusicOnline = api.getMusicsSearchOnline(strSearch,FieldFinal.CLIENT_ID,10);
        generateMusicsSearch(rootMusicOnline.getChildMusicOnlines(), strSearch);
        setUiMusicSearch();
    }

    @UiThread
    void setUiMusicSearch() {
        mImgSearch.setVisibility(View.VISIBLE);
        mProgressBarSearch.setVisibility(View.GONE);
        mRecyclerExpandableMusicsAdapter = new RecyclerExpandableMusicsAdapter(getActivity(),mParentObjects);
        mRecyclerViewMusicsOnline.setAdapter(mRecyclerExpandableMusicsAdapter);
        setOnClickListener();
    }

    @Click(R.id.imgSearch)
    void setListenerSearch() {
        mImgSearch.setVisibility(View.GONE);
        mProgressBarSearch.setVisibility(View.VISIBLE);
        String strSearch = mEdtSearch.getText().toString();
        getPlaylistSearch(strSearch);
    }


    private void generateMusics(ArrayList<TypeMusicOnline> typeMusicOnlines) {

        for (TypeMusicOnline typeMusicOnline : typeMusicOnlines) {
            ArrayList<Object> childList = new ArrayList<>();
            childList.addAll(typeMusicOnline.getChildMusicOnlines());
            typeMusicOnline.setChildObjectList(childList);
            mParentObjects.add(typeMusicOnline);
        }
    }

    private void generateMusicsSearch(ArrayList<ChildMusicOnline> childMusicOnlines,String search) {
        TypeMusicOnline typeMusicOnline = new TypeMusicOnline();
        ArrayList<Object> childList = new ArrayList<>();
        childList.addAll(childMusicOnlines);
        typeMusicOnline.setChildObjectList(childList);
        typeMusicOnline.setTypeMusicOnline(search);
        typeMusicOnline.setChildMusicOnlines(childMusicOnlines);
        mParentObjects.add(0, typeMusicOnline);
    }

    private void setOnClickListener() {
        mRecyclerExpandableMusicsAdapter.setOnChildItemClick(new RecyclerExpandableMusicsAdapter.OnChildItemClick() {
            @Override
            public void onClick(int position) {
                Log.v(TAG,position+"");
                getListMusic(position);
            }
        });

        mRecyclerExpandableMusicsAdapter.addExpandCollapseListener(new ExpandCollapseListener() {
            @Override
            public void onRecyclerViewItemExpanded(int i) {
                TypeMusicOnline typeMusicOnline = (TypeMusicOnline) mParentObjects.get(i);
                typeMusicOnline.setIsExpanded(true);
            }

            @Override
            public void onRecyclerViewItemCollapsed(int i) {
                TypeMusicOnline typeMusicOnline = (TypeMusicOnline) mParentObjects.get(i);
                typeMusicOnline.setIsExpanded(false);
            }
        });
    }

    // get position list musics and position music in this list
    private void getListMusic(int position) {

        int positionList = 0;
        int n = position;
        int positionInList = position - 1;

        for(int i = 0; i < n ; i++) {
            TypeMusicOnline typeMusicOnline = (TypeMusicOnline) mParentObjects.get(i);
            if (typeMusicOnline.isExpanded()) {
                for (int j = 0; j < typeMusicOnline.getChildMusicOnlines().size(); j++) {
                    n--;
                }
            }
            positionList = i;
        }



        for( int i = 0; i < positionList; i++) {
            TypeMusicOnline typeMusicOnline = (TypeMusicOnline) mParentObjects.get(i);
            if(typeMusicOnline.isExpanded()) {
                for (int j = 0; j < typeMusicOnline.getChildMusicOnlines().size(); j++) {
                    positionInList --;
                }
            }
            positionInList--;
        }

        if(positionInList == mPositionList) {
            ManagerPlay managerPlay = ManagerPlay.getInstance();
            managerPlay.getCurrentMediaPlayer().release();
            managerPlay.playSoundOnline(positionInList);
        } else {
            TypeMusicOnline typeMusicOnline = (TypeMusicOnline) mParentObjects.get(positionList);
            playSoundOnline((ArrayList<ChildMusicOnline>) typeMusicOnline.getChildMusicOnlines(), positionInList);
            mPositionList = positionList;

        }


    }

    // play music online
    private void playSoundOnline(ArrayList<ChildMusicOnline> childMusicOnlines,int position) {
        ManagerPlay managerPlay = ManagerPlay.getInstance();
        managerPlay.getCurrentMediaPlayer().release();
        managerPlay.playSoundOnline(childMusicOnlines, position);
    }
}
