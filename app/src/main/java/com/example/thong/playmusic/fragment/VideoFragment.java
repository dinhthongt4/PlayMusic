package com.example.thong.playmusic.fragment;

import android.support.v4.app.Fragment;

import com.example.thong.playmusic.R;
import com.example.thong.playmusic.adapter.ListVideoAdapter;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.model.Item;
import com.example.thong.playmusic.model.ListVideos;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
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
    private ArrayList<Item> mItems;
    private ListVideoAdapter mListVideoAdapter;

    @ViewById(R.id.list_video)
    StickyListHeadersListView mStickyListHeadersListView;

    @AfterViews
    public void init() {
        mItems = new ArrayList<>();
        mListVideoAdapter = new ListVideoAdapter(mItems,getActivity());
        mStickyListHeadersListView.setAdapter(mListVideoAdapter);
        getVideos("snippet", FieldFinal.ID_ZING_MP3, "video", 5, FieldFinal.KEY_YOUTUBE, "Zing mp3",0);
        getVideos("snippet", FieldFinal.ID_REMIX, "video", 5, FieldFinal.KEY_YOUTUBE,"Remix",1);
        getVideos("snippet", FieldFinal.ID_WESTLIFE, "video", 5, FieldFinal.KEY_YOUTUBE,"WestLife",2);
    }

    @Background
    void getVideos(String path, String channelId, String type, int maxResults, String key, String typeMusic, int headerId) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://www.googleapis.com").build();
        GetDataAPI getDataAPI = restAdapter.create(GetDataAPI.class);
        ListVideos listVideos = getDataAPI.getVideo(path,channelId,type,maxResults,key);
        for (int i = 0 ; i < listVideos.getItems().size(); i ++) {
            listVideos.getItems().get(i).setType(typeMusic);
            listVideos.getItems().get(i).setHeaderID(headerId);
        }
        mItems.addAll(listVideos.getItems());
        setUIVideo();
    }

    @UiThread
    void setUIVideo() {
        mListVideoAdapter.notifyDataSetChanged();
    }

    public interface GetDataAPI {
        @GET("/youtube/v3/search")
        ListVideos getVideo(@Query("part") String path, @Query("channelId") String channelId,
                            @Query("type") String type, @Query("maxResults") int maxResults, @Query("key") String key);
    }
}
