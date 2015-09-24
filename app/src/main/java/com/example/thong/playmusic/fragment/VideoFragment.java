package com.example.thong.playmusic.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.activity.VideoViewActivity_;
import com.example.thong.playmusic.adapter.RecyclerVideoAdapter;
import com.example.thong.playmusic.api.Api;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.model.Item;
import com.example.thong.playmusic.model.ListVideos;
import com.example.thong.playmusic.widget.SpacesItemDecoration;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit.RestAdapter;

/**
 * Created by thong on 9/7/15.
 */

@EFragment(R.layout.fragment_videos)
public class VideoFragment extends Fragment {

    public static final String API = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCyAojDE5b7HoWX6rzYUn5Mg&type=video&maxResults=5&key=AIzaSyBtEYk0NLi1DcEjYx8Z1TDDWulmmTajV4s";
    private static final String TAG = "VideoFragment";
    private static final String SEARCH_API = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=mot%20nha&type=video&key=AIzaSyBtEYk0NLi1DcEjYx8Z1TDDWulmmTajV4s";
    private ArrayList<Item> mItems;
    private int mTypeHeader = 3;
    private RecyclerVideoAdapter mRecyclerVideoAdapter;

    @ViewById(R.id.edtSearch)
    EditText mEdtSearch;

    @ViewById(R.id.imgSearch)
    ImageView mImgSearch;

    @ViewById(R.id.progressBarSearch)
    ProgressBar mProgressBarSearch;

    @ViewById(R.id.recycler_video)
    RecyclerView mRecyclerVideo;

    @AfterViews
    public void init() {
        mItems = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerVideo.setLayoutManager(layoutManager);
        mRecyclerVideoAdapter = new RecyclerVideoAdapter(mItems);
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mRecyclerVideoAdapter);
        int spacingItem = getResources().getDimensionPixelSize(R.dimen.margin_10);
        mRecyclerVideo.addItemDecoration(new SpacesItemDecoration(spacingItem));
        mRecyclerVideo.addItemDecoration(headersDecor);
        mRecyclerVideo.setAdapter(mRecyclerVideoAdapter);



        setListener();
        getVideosSearch("snippet", "Remix", "video", 5, FieldFinal.KEY_YOUTUBE, "Remix", 0);
        getVideosSearch("snippet", "Zing mp3", "video", 5, FieldFinal.KEY_YOUTUBE, "Zing mp3", 1);
        getVideosSearch("snippet", "WestLife", "video", 5, FieldFinal.KEY_YOUTUBE, "WestLife", 2);

    }

    @UiThread
    void setUIVideo() {
        mRecyclerVideoAdapter.notifyDataSetChanged();
    }

    @UiThread
    void setUIVideoSearch() {
        mRecyclerVideoAdapter.notifyDataSetChanged();
        mProgressBarSearch.setVisibility(View.GONE);
        mImgSearch.setVisibility(View.VISIBLE);
    }


    @Click(R.id.imgSearch)
    void listenerSearch() {
        String strSearch = mEdtSearch.getText().toString();
        mProgressBarSearch.setVisibility(View.VISIBLE);
        mImgSearch.setVisibility(View.GONE);
        getVideosSearch("snippet", strSearch, "video", 5, FieldFinal.KEY_YOUTUBE, strSearch, mTypeHeader);
        mTypeHeader++;
    }

    @Background
    void getVideosSearch(String path, String search, String type, int maxResults, String key, String typeMusic, int headerId) {

        int positionSearch = 0;

        if (maxResults > 5) {
            positionSearch = removeList(typeMusic);
        }

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.googleapis.com").build();
        Api getDataAPI = restAdapter.create(Api.class);
        ListVideos listVideos = getDataAPI.getVideoSearch(path, search, type, maxResults, key);
        for (int i = 0; i < listVideos.getItems().size(); i++) {
            Log.v(TAG,typeMusic);
            listVideos.getItems().get(i).setType(typeMusic);
            listVideos.getItems().get(i).setHeaderID(headerId);
            listVideos.getItems().get(i).setTypeItem(0);
        }
        mItems.addAll(positionSearch, listVideos.getItems());

        if (maxResults > 5) {
            setUIVideo();
        } else {
            setUIVideoSearch();
        }
    }

    public int removeList(String typeMusic) {

        int positionSearch = 0;

        for (int i = 0; i < mItems.size(); ) {
            if (mItems.get(i).getType().equals(typeMusic)) {
                positionSearch = i;
                Log.v("i", "" + i);
                mItems.remove(i);
            } else {
                i++;
            }
        }
        return positionSearch;
    }

    private void setListener() {
        mRecyclerVideoAdapter.setOnItemClickListener(new RecyclerVideoAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                VideoViewActivity_.intent(getActivity()).extra("id",mItems.get(position).getId().getVideoId())
                        .extra("url",mItems.get(position).getSnippet().getThumbnail().getHigh().getUrl())
                        .extra("name",mItems.get(position).getSnippet().getTitle())
                        .extra("channel",mItems.get(position).getSnippet().getChannelTitle())
                        .start();
            }
        });
    }
}
