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
    public void playSound(final Context context,int position, ArrayList<ChildMusicOnline> childMusicOnlines) {

        if(mChildMusicOnlines != null) {
            mChildMusicOnlines.clear();
        }
        mChildMusicOnlines = new ArrayList<>();
        mChildMusicOnlines.addAll(childMusicOnlines);
        numberMedia = position;

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

    public void playSound(final Context context,int position) {

        numberMedia = position;

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

    private void playSound(final Context context) {

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

        Log.v(TAG, childMusicOnlines.get(position).getUrlStream() + "?client_id=" + FieldFinal.CLIENT_ID);

        if(mChildMusicOnlines != null) {
            mChildMusicOnlines.clear();
        } else {
            mChildMusicOnlines = new ArrayList<>();
        }

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
        if(mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    // start a music
    public void onStart() {
        if(mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    // next a music in list
    public void onNext(Context context) {

        if(mChildMusicOnlines != null) {
        if (numberMedia < mChildMusicOnlines.size() - 1) {

            if (mMediaPlayer != null) {
                mMediaPlayer.release();
            }
            numberMedia++;
            playSound(context);
        }
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
        if(mChildMusicOnlines != null) {
            return mChildMusicOnlines;
        } else {
            return null;
        }
    }

    //get current MediaPlayer
    public MediaPlayer getCurrentMediaPlayer() {

        if(mMediaPlayer != null) {
            return mMediaPlayer;
        }
        else {
            return null;
        }
    }

    public ChildMusicOnline getCurrentInfoMediaPlayer() {
        return mChildMusicOnlines.get(numberMedia);
    }

    public interface OnSuccessPlayer {
        void onSuccess(ChildMusicOnline childMusicOnline);
    }
}
