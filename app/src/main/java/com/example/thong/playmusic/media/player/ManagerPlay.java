package com.example.thong.playmusic.media.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.example.thong.playmusic.config.FieldFinal;
import com.example.thong.playmusic.model.Tracks;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by thong on 31/08/2015.
 */
public class ManagerPlay {

    private static final String TAG = "MANAGERPLAY";
    private static ManagerPlay mManagerPlay;
    private MediaPlayer mMediaPlayer;
    private ArrayList<Tracks> mTrackses;
    private int numberMedia;
    private boolean mIsPause;
    private OnSuccessPlayer mOnSuccessPlayer;
    private boolean mIsRepeat;

    public boolean isRepeat() {
        return mIsRepeat;
    }

    public void setIsRepeat(boolean isRepeat) {
        this.mIsRepeat = isRepeat;
    }

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
    public void playSound(final Context context, int position, ArrayList<Tracks> trackses) {

        if (mTrackses != null) {
            mTrackses.clear();
        }
        mTrackses = new ArrayList<>();
        mTrackses.addAll(trackses);
        numberMedia = position;

        Uri uri = Uri.parse(mTrackses.get(numberMedia).getStream_url());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mTrackses.get(numberMedia));
                }
            }
        });
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setListener(context);
    }

    public void playSound(final Context context, int position) {

        numberMedia = position;

        Uri uri = Uri.parse(mTrackses.get(numberMedia).getStream_url());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mTrackses.get(numberMedia));
                }
            }
        });
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setListener(context);
    }

    private void playSound(final Context context) {

        Uri uri = Uri.parse(mTrackses.get(numberMedia).getStream_url());
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mTrackses.get(numberMedia));
                }
            }
        });
        try {
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setListener(context);
    }

    public void playSoundOnline(ArrayList<Tracks> childMusicOnlines, final int position) {

        if (mTrackses != null) {
            mTrackses.clear();
        } else {
            mTrackses = new ArrayList<>();
        }

        mTrackses.addAll(childMusicOnlines);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mTrackses.get(position).getStream_url() + "?client_id=" + FieldFinal.CLIENT_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mTrackses.get(position));
                }
            }
        });

        numberMedia = position;

    }

    public void playSoundOnline(final int position) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(mTrackses.get(position).getStream_url() + "?client_id=" + FieldFinal.CLIENT_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(mTrackses.get(position));
                }
            }
        });

        numberMedia = position;
    }

    public void playSoundOnline(final Tracks tracks) {


        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(tracks.getStream_url() + "?client_id=" + FieldFinal.CLIENT_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer.start();
                if (mOnSuccessPlayer != null) {
                    mOnSuccessPlayer.onSuccess(tracks);
                }
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    // pause a music
    public void onPause() {

        if (mMediaPlayer != null) {

            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
    }

    // start a music
    public void onStart() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    // next a music in list
    public void onNext(Context context) {

        if (mTrackses != null) {
            if (numberMedia < mTrackses.size() - 1) {

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

    public ArrayList<Tracks> getListMusics() {
        if (mTrackses != null) {
            return mTrackses;
        } else {
            return null;
        }
    }

    //get current MediaPlayer
    public MediaPlayer getCurrentMediaPlayer() {

        if (mMediaPlayer != null) {
            return mMediaPlayer;
        } else {
            return null;
        }
    }

    public Tracks getCurrentInfoMediaPlayer() {
        if (mTrackses != null) {
            return mTrackses.get(numberMedia);
        } else {
            return null;
        }
    }

    private void setListener(final Context context) {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if (isRepeat()) {
                    mediaPlayer.start();
                } else {
                    onNext(context);
                }
            }
        });
    }

    public interface OnSuccessPlayer {
        void onSuccess(Tracks childMusicOnline);
    }
}
