package com.example.thong.playmusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.thong.playmusic.media.player.ManagerPlay;

import org.androidannotations.annotations.EService;


/**
 * Created by thong on 31/08/2015.
 */

@EService
public class MediaPlayerService extends Service {

    private static final String TAG = "MediaPlayerService";
    private ManagerPlay mManagerPlay;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mManagerPlay = ManagerPlay.getInstance();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
