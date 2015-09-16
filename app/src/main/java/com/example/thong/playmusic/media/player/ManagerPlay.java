package com.example.thong.playmusic.media.player;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.model.ChildMusicOnline;
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
    private ArrayList<ChildMusicOnline> mChildMusicOnlines;
    private int numberMedia;
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

        Log.v(TAG,mChildMusicOnlines.get(numberMedia).getUrlStream());

        Uri uri = Uri.parse(mChildMusicOnlines.get(numberMedia).getUrlStream());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mChildMusicOnlines.get(numberMedia));
                }
            }
        });
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                onNext(context);
            }
        });
    }

    public void playSoundOnline(ArrayList<ChildMusicOnline> childMusicOnlines, final int position) {

        Log.v(TAG,childMusicOnlines.get(position).getUrlStream()+"?client_id="+ FieldFinal.CLIENT_ID);

        mChildMusicOnlines.clear();
        mChildMusicOnlines.addAll(childMusicOnlines);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mChildMusicOnlines.get(position).getUrlStream()+"?client_id="+ FieldFinal.CLIENT_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mChildMusicOnlines.get(position));
                }
            }
        });

        numberMedia = position;
    }

    public void playSoundOnline( final int position) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mChildMusicOnlines.get(position).getUrlStream()+"?client_id="+ FieldFinal.CLIENT_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mChildMusicOnlines.get(position));
                }
            }
        });

        numberMedia = position;
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
        if (numberMedia < mChildMusicOnlines.size() - 1) {

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


    public void setmOnSuccessPlayer(OnSuccessPlayer onSuccessPlayer) {
        mOnSuccessPlayer = onSuccessPlayer;
    }

    public ArrayList<ChildMusicOnline> getListMusics() {
        return mChildMusicOnlines;
    }

    // Load information all of file sound
    public void scanSdcard(ContentResolver contentResolver) {
        mChildMusicOnlines = new ArrayList<>();
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

                    ChildMusicOnline childMusicOnline = new ChildMusicOnline();

                    childMusicOnline.setArtist(cursor.getString(1));
                    childMusicOnline.setUrlStream(cursor.getString(2));
                    childMusicOnline.setTitle(cursor.getString(3));
                    childMusicOnline.setDuration(Long.parseLong(cursor.getString(4)));
                    mChildMusicOnlines.add(childMusicOnline);
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

    public ChildMusicOnline getCurrentInfoMediaPlayer() {
        return mChildMusicOnlines.get(numberMedia);
    }

    public interface OnSuccessPlayer {
        void onSuccess(ChildMusicOnline childMusicOnline);
    }
}
