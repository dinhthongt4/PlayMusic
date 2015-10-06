package com.example.thong.playmusic.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.thong.playmusic.MainActivity_;
import com.example.thong.playmusic.R;
import com.example.thong.playmusic.activity.UIPlayMusicActivity_;
import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.media.player.ManagerPlay;

import org.androidannotations.annotations.EService;


/**
 * Created by thong on 31/08/2015.
 */

@EService
public class MediaPlayerService extends Service {

    private static final String TAG = "MediaPlayerService";
    private ManagerPlay mManagerPlay;
    private IBinder mIBinder;
    private static boolean currentVersionSupportBigNotification = false;
    int NOTIFICATION_ID = 1111;

    @Override
    public void onCreate() {
        mManagerPlay = ManagerPlay.getInstance();
        removeNotification();
        Log.v(TAG,"Create");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notification();

        mManagerPlay.setChangeMediaPlayer(new ManagerPlay.OnChangeMediaPlayer() {
            @Override
            public void onChange() {
                Log.v(TAG, "Pause");
                notification();
                Intent intent1 = new Intent();
                intent1.setAction(FieldFinal.ACTION_CHANGE_MEDIA);
                sendBroadcast(intent1);
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Destroy");
    }

    @SuppressLint("NewApi")
    public void notification() {
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(),R.layout.notifycation_custom);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_add_album).build();

        setListenerNotification(simpleContentView);

        notification.contentView = simpleContentView;
        if(currentVersionSupportBigNotification){
            notification.bigContentView = simpleContentView;
        }

        if(mManagerPlay.getCurrentInfoMediaPlayer() != null) {
            notification.contentView.setTextViewText(R.id.txtNameMediaPlayer, mManagerPlay.getCurrentInfoMediaPlayer().getTitle());
            notification.contentView.setTextViewText(R.id.txtArtistMediaPlayer, mManagerPlay.getCurrentInfoMediaPlayer().getArtist());
            if(currentVersionSupportBigNotification){
                notification.bigContentView.setTextViewText(R.id.txtNameMediaPlayer, mManagerPlay.getCurrentInfoMediaPlayer().getTitle());
                notification.bigContentView.setTextViewText(R.id.txtArtistMediaPlayer, mManagerPlay.getCurrentInfoMediaPlayer().getArtist());
            }
        }

        if(mManagerPlay.getIsPause()) {
            notification.contentView.setImageViewResource(R.id.imgPause, R.drawable.ic_play);
        } else {
            notification.contentView.setImageViewResource(R.id.imgPause, R.drawable.ic_pause);
        }



        Intent intent = new Intent(this, UIPlayMusicActivity_.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        notification.contentIntent = pendingIntent;

        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(NOTIFICATION_ID, notification);
    }

    public void removeNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    private void setListenerNotification(RemoteViews remoteViews) {
        Intent exit = new Intent(FieldFinal.NOTIFY_EXIT);
        Intent back = new Intent(FieldFinal.NOTIFY_BACK);
        Intent next = new Intent(FieldFinal.NOTIFY_NEXT);
        Intent play = new Intent(FieldFinal.NOTIFY_PLAY);

        PendingIntent pExit = PendingIntent.getBroadcast(getApplicationContext(), 0, exit, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imgStop, pExit);

        PendingIntent pBack = PendingIntent.getBroadcast(getApplicationContext(), 0, back, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imgBack, pBack);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imgNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imgPause, pPlay);
    }
}
