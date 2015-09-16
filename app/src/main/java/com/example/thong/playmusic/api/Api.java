package com.example.thong.playmusic.api;

import com.example.thong.playmusic.model.ListVideos;
import com.example.thong.playmusic.model.RootMusicOnline;
import com.example.thong.playmusic.model.TypeMusicOnline;

import java.util.ArrayList;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by thong on 9/11/15.
 */
public interface Api {
    @GET("/youtube/v3/search")
    ListVideos getVideoSearch(@Query("part") String path, @Query("q") String search,
                              @Query("type") String type, @Query("maxResults") int maxResults, @Query("key") String key);

    @GET("/users/173546767/playlists")
    ArrayList<TypeMusicOnline> getMusicsOnline(@Query("client_id") String clientID);

    @GET("/search")
    RootMusicOnline getMusicsSearchOnline(@Query("q") String q, @Query("client_id") String clientID, @Query("limit") int limit);
}
