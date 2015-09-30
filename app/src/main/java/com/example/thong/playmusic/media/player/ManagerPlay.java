package com.example.thong.playmusic.media.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

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
    private boolean mIsOnline;
    private OnChangeDuration mOnChangeDuration;
    private Handler mHandler;

    public boolean isOnline() {
        return mIsOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.mIsOnline = isOnline;
    }

    public int getNumberMedia() {
        return numberMedia;
    }

    public void setNumberMedia(int numberMedia) {
        this.numberMedia = numberMedia;
    }

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

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mOnChangeDuration != null) {
                    mOnChangeDuration.getDuration((Integer) msg.obj);
                }
            }
        };
        Thread thread = new Thread(new MyRunable());
        thread.start();
    }

    public static synchronized ManagerPlay getInstance() {
        if (mManagerPlay == null) {
            mManagerPlay = new ManagerPlay();
        }
        return mManagerPlay;
    }

    // play a music
    public void playSound(final Context context, int position, ArrayList<Tracks> trackses) {
        mIsOnline = false;
        if (mTrackses != null) {
            mTrackses.clear();
        }
        mTrackses = new ArrayList<>();
        mTrackses.addAll(trackses);
        numberMedia = position;
        mMediaPlayer.reset();
        Uri uri = Uri.parse(mTrackses.get(numberMedia).getStream_url());

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

        mIsOnline = false;
        numberMedia = position;

        Uri uri = Uri.parse(mTrackses.get(numberMedia).getStream_url());
        mMediaPlayer.reset();
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

        mIsOnline = false;
        Uri uri = Uri.parse(mTrackses.get(numberMedia).getStream_url());
        mMediaPlayer.reset();
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

    public void playSoundOnline(ArrayList<Tracks> childMusicOnlines, final int position, Context context) {

        mIsOnline = true;
        if (mTrackses != null) {
            mTrackses.clear();
        } else {
            mTrackses = new ArrayList<>();
        }

        mTrackses.addAll(childMusicOnlines);

        mMediaPlayer.reset();
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
        setListener(context);
    }

    public void playSoundOnline(final int position, Context context) {
        mIsOnline = true;
        mMediaPlayer.reset();
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
        setListener(context);

    }

    public void playSoundOnline(final Tracks tracks, Context context) {

        mIsOnline = true;
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
        setListener(context);
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

                mMediaPlayer.stop();
                numberMedia++;

                if (mIsOnline) {
                    playSoundOnline(numberMedia, context);
                } else {
                    playSound(context);

                }

            }
        }
    }

    // back a music in list
    public void onBack(Context context) {
        if (numberMedia > 0) {
            numberMedia--;
            mMediaPlayer.stop();
            if (mIsOnline) {
                playSoundOnline(numberMedia, context);
            } else {
                playSound(context);

            }
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

    public void setOnChangeDuration(OnChangeDuration onChangeDuration) {
        mOnChangeDuration = onChangeDuration;


    }

    public interface OnSuccessPlayer {
        void onSuccess(Tracks childMusicOnline);
    }

    public interface OnChangeDuration {
        void getDuration(int duration);
    }

    class MyRunable implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    Message message = mHandler.obtainMessage();
                    message.obj = mMediaPlayer.getCurrentPosition();
                    mHandler.sendMessage(message);
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
