package com.example.thong.playmusic.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.thong.playmusic.MainActivity_;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.media.player.ManagerPlay;
import com.example.thong.playmusic.service.MediaPlayerService;
import com.example.thong.playmusic.service.MediaPlayerService_;

/**
 * Created by thongdt on 05/10/2015.
 */
public class NotificationBroadcast extends BroadcastReceiver {

    private static final String TAG = "NotificationBroadcast";
    private ManagerPlay mManagerPlay;

    @Override
    public void onReceive(Context context, Intent intent) {

        mManagerPlay = ManagerPlay.getInstance();

        if (intent.getAction().equals(FieldFinal.NOTIFY_PLAY)) {

            Log.v(TAG,"Play");

            if(mManagerPlay.getIsPause()) {
                mManagerPlay.setIsPause(false);
                mManagerPlay.onStart(context);
            } else {
                mManagerPlay.setIsPause(true);
                mManagerPlay.onPause();
            }
        } else if (intent.getAction().equals(FieldFinal.NOTIFY_BACK)) {
            mManagerPlay.onBack(context);

        } else if (intent.getAction().equals(FieldFinal.NOTIFY_NEXT)) {
            mManagerPlay.onNext(context);
        } else if (intent.getAction().equals(FieldFinal.NOTIFY_EXIT)) {
            Intent intent1 = new Intent(context, MediaPlayerService_.class);
            context.stopService(intent1);
            if(!mManagerPlay.getIsPause()) {
                mManagerPlay.setIsPause(true);
                mManagerPlay.onPause();
            }
        }
    }
}
