package com.example.thong.playmusic.media.player;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.thong.playmusic.model.MediaInfo;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by thong on 31/08/2015.
 */
public class ManagerPlay {

    private static final String TAG = "MANAGERPLAY";
    private static ManagerPlay mManagerPlay;
    private MediaPlayer mMediaPlayer ;
    private ArrayList<MediaInfo> mMediaInfos;
    private int numberMedia;
    private OnChangeDuration mOnChangeDuration;
    private boolean mIsPause;
    private OnSuccessPlayer mOnSuccessPlayer;

    public boolean getIsPause() {
        return mIsPause;
    }

    public void setIsPause(boolean isPause) {
        this.mIsPause = isPause;
    }

    private ManagerPlay() {

    }

    public static synchronized ManagerPlay getInstance() {
        if (mManagerPlay == null) {
            mManagerPlay = new ManagerPlay();
        }
        return mManagerPlay;
    }

    // play a music
    public void playSound(final Context context) {

        Uri uri = Uri.parse(mMediaInfos.get(numberMedia).getPath());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                if(mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mMediaInfos.get(numberMedia));
                }
                new Thread(new DurationThread()).start();
            }
        });
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // pause a music
    public void onPause() {
        mMediaPlayer.pause();
    }

    // start a music
    public void onStart() {
        mMediaPlayer.start();
    }

    // next a music in list
    public void onNext(Context context) {
        if (numberMedia < mMediaInfos.size() - 1) {

            if (mMediaPlayer != null) {
                mMediaPlayer.release();
            }
            numberMedia++;
            playSound(context);
        }
    }

    // back a music in list
    public void onBack(Context context) {
        if (numberMedia > 0) {
            numberMedia--;
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
            }
            playSound(context);
        }
    }

    public void setOnChangeDuration(OnChangeDuration onChangeDuration) {
        mOnChangeDuration = onChangeDuration;
    }

    public void setmOnSuccessPlayer(OnSuccessPlayer onSuccessPlayer) {
        mOnSuccessPlayer = onSuccessPlayer;
    }

    public ArrayList<MediaInfo> getListMusics() {
        return mMediaInfos;
    }

    // Load information all of file sound
    public void scanSdcard(ContentResolver contentResolver) {
        mMediaInfos = new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = contentResolver.query(uri, projection, selection, null, sortOrder);

            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    MediaInfo mediaInfo = new MediaInfo();

                    mediaInfo.setArtist(cursor.getString(1));
                    mediaInfo.setPath(cursor.getString(2));
                    mediaInfo.setName(cursor.getString(3));
                    mediaInfo.setDuration(cursor.getString(4));
                    mMediaInfos.add(mediaInfo);
                    cursor.moveToNext();
                }

            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //get current MediaPlayer
    public MediaPlayer getCurrentMediaPlayer() {
        return mMediaPlayer;
    }

    public MediaInfo getCurrentInfoMediaPlayer() {
        return mMediaInfos.get(numberMedia);
    }
    // get duration current
    private class DurationThread implements Runnable {

        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mOnChangeDuration != null) {
                    mOnChangeDuration.onChanger(mMediaPlayer);
                }
            }
        }
    }

    // callback duration
    public interface OnChangeDuration {
        void onChanger(MediaPlayer mediaPlayer);
    }

    public interface OnSuccessPlayer {
        void onSuccess(MediaInfo mediaInfo);
    }
}
