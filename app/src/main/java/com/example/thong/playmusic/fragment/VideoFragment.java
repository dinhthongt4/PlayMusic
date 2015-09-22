package com.example.thong.playmusic.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.activity.VideoViewActivity_;
import com.example.thong.playmusic.adapter.ListVideoAdapter;
import com.example.thong.playmusic.api.Api;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.model.Item;
import com.example.thong.playmusic.model.ListVideos;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by thong on 9/7/15.
 */

@EFragment(R.layout.fragment_videos)
public class VideoFragment extends Fragment {

    public static final String API = "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=UCyAojDE5b7HoWX6rzYUn5Mg&type=video&maxResults=5&key=AIzaSyBtEYk0NLi1DcEjYx8Z1TDDWulmmTajV4s";
    private static final String TAG = "VideoFragment";
    private static final String SEARCH_API = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=mot%20nha&type=video&key=AIzaSyBtEYk0NLi1DcEjYx8Z1TDDWulmmTajV4s";
    private ArrayList<Item> mItems;
    private ListVideoAdapter mListVideoAdapter;
    private int mTypeHeader = 3;

    @ViewById(R.id.list_video)
    StickyListHeadersListView mStickyListHeadersListView;

    @ViewById(R.id.edtSearch)
    EditText mEdtSearch;

    @ViewById(R.id.imgSearch)
    ImageView mImgSearch;

    @ViewById(R.id.progressBarSearch)
    ProgressBar mProgressBarSearch;

    @AfterViews
    public void init() {
        mItems = new ArrayList<>();
        mListVideoAdapter = new ListVideoAdapter(mItems, getActivity());
        mStickyListHeadersListView.setAdapter(mListVideoAdapter);
        setListener();
        getVideosSearch("snippet", "Remix", "video", 5, FieldFinal.KEY_YOUTUBE, "Remix", 0);
        getVideosSearch("snippet", "Zing mp3", "video", 5, FieldFinal.KEY_YOUTUBE, "Zing mp3", 1);
        getVideosSearch("snippet", "WestLife", "video", 5, FieldFinal.KEY_YOUTUBE, "WestLife", 2);

    }

    @UiThread
    void setUIVideo() {
        mListVideoAdapter.notifyDataSetChanged();
    }

    @UiThread
    void setUIVideoSearch() {
        mListVideoAdapter.notifyDataSetChanged();
        mStickyListHeadersListView.smoothScrollToPosition(0);
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
            listVideos.getItems().get(i).setType(typeMusic);
            listVideos.getItems().get(i).setHeaderID(headerId);
            listVideos.getItems().get(i).setTypeItem(0);
        }
        mItems.addAll(positionSearch, listVideos.getItems());

        Item item = new Item();
        item.setType(typeMusic);
        item.setHeaderID(headerId);
        item.setTypeItem(1);
        item.setNumberList(maxResults);
        mItems.add(maxResults + positionSearch, item);

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

    void setListener() {
        mListVideoAdapter.setOnLoadMoreListener(new ListVideoAdapter.OnLoadMoreListener() {
            @Override
            public void onClickLoadMore(String typeHeader, int typeItem, int headerID, int numberList) {
                getVideosSearch("snippet", typeHeader, "video", numberList + 5, FieldFinal.KEY_YOUTUBE, typeHeader, mTypeHeader);
            }
        });

        mStickyListHeadersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VideoViewActivity_.intent(getActivity()).extra("id",mItems.get(i).getId().getVideoId()).start();
            }
        });
    }
}
