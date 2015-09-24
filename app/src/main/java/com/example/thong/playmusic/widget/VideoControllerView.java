package com.example.thong.playmusic.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thong.playmusic.R;

import java.lang.ref.WeakReference;
import java.util.Formatter;
import java.util.Locale;


public class VideoControllerView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "VideoControllerView";

    public MediaPlayerControl mPlayer;
    private Context mContext;
    private ViewGroup mAnchor;
    private View mRoot;
    private SeekBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private boolean mShowing;
    private boolean mDragging;
    private static final int DEFAULT_TIMEOUT = 100000000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private int state = 0;
    private boolean check_speed_x;
    private float mDoubleSpeed = 1.0f;


    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    public ImageButton mPauseButton;
    private ImageView mFullButton;
    LinearLayout mLinearController;


    private Handler mHandler = new MessageHandler(this);
    public static boolean check_in = false;

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = null;
        mContext = context;
        Log.i(TAG, TAG);
    }

    public VideoControllerView(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        Log.i(TAG, TAG);
    }

    public VideoControllerView(Context context, int state, boolean check) {
        this(context, true);
        this.state = state;
        check_in = check;
        Log.i(TAG, TAG);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (mRoot != null)
            initControllerView(mRoot);
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    /**
     * Set the view that acts as the anchor for the control view.
     * This can for example be a VideoView, or your Activity's main view.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    public void setAnchorView(ViewGroup view) {
        mAnchor = view;

        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    /**
     * Create the view that holds the widgets that control playback.

     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.custom_media_controller, null);
        mRoot.setOnClickListener((new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    doPauseResume();
                    show(DEFAULT_TIMEOUT);
                } catch (Exception ex) {
                }
            }
        }));
        initControllerView(mRoot);
        return mRoot;
    }

    private void check_Speed(boolean check) {
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Toast.makeText(getContext(), "dd", Toast.LENGTH_LONG).show();

        }
        return super.onKeyDown(keyCode, event);
    }

    private void initControllerView(View v) {
        mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        mFullButton = (ImageView) v.findViewById(R.id.zoomscreen);
        mLinearController = (LinearLayout) v.findViewById(R.id.linearController);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(this);
        }
        if (mFullButton != null) {
            mFullButton.requestFocus();
            mFullButton.setOnClickListener(this);
        }

        mProgress = (SeekBar) v.findViewById(R.id.media_controller_progress);
        if (mProgress != null) {
            mProgress.setOnSeekBarChangeListener(mSeekListener);
            mProgress.setMax(1000);
        }

        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        show(DEFAULT_TIMEOUT);
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after default timeout milliseconds of inactivity.
     */
    public void show() {
        show(DEFAULT_TIMEOUT);
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show
     *                the controller until hide() is called.
     */
    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }

            LayoutParams tlp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
            );

            mAnchor.addView(this, tlp);
            mShowing = true;
        }
        updatePausePlay();
        check_Speed(check_speed_x);
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    /**
     * Remove the controller from the screen.
     */

    public void hide() {
        if (mAnchor == null) {
            return;
        }

        try {
            mAnchor.removeView(this);
            mHandler.removeMessages(SHOW_PROGRESS);
        } catch (IllegalArgumentException ex) {
            Log.w("MediaController", "already removed");
        }
        mShowing = false;
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void setProgress() {
        int position = 0;
        if (!check_in) {
            if (mPlayer == null || mDragging) {
                return;
            }

            position = (int) (mPlayer.getCurrentPosition()  * mDoubleSpeed);
            int duration = mPlayer.getDuration();
            if (mProgress != null) {
                if (duration > 0) {
                    // use long to avoid overflow
                    long pos = 1000L * position / duration;
                    mProgress.setProgress((int) pos);
                }
                int percent = mPlayer.getBufferPercentage();
                mProgress.setSecondaryProgress(percent * 10);
            }

            if (mEndTime != null) {
                mEndTime.setText(stringForTime(duration));
            }
            if (mCurrentTime != null) {
                mCurrentTime.setText(stringForTime(position));
            }
        }
    }

    public void updatePausePlay() {
        if (!check_in) {
            if (mRoot == null || mPauseButton == null || mPlayer == null) {
                return;
            }
            if (this.state == 1) {
                mPauseButton.setVisibility(INVISIBLE);
            } else if (this.state == 0) {
                mPauseButton.setVisibility(VISIBLE);
                mPauseButton.setBackgroundResource(R.drawable.ic_pause_video);
            }
        }
    }

    private void doPauseResume() {
        if (!check_in) {
            if (mPlayer == null) {
                return;
            }
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                state = 0;
            } else {
                mPlayer.start();
                state = 1;
            }
        }
        updatePausePlay();
    }

    public void toVideoHead() {
        if (! check_in) {
            if (mPlayer == null) {
                return;
            }
            mPlayer.seekTo(0);
            mPlayer.pause();
            state = 0;
            updatePausePlay();
        }
    }


    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            mHandler.removeMessages(SHOW_PROGRESS);
        }

        public void onProgressChanged(@NonNull SeekBar bar, int progress, boolean fromuser) {
            if (!check_in) {
                if (mPlayer == null) {
                    return;
                }
                if (!fromuser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }

                long duration = mPlayer.getDuration();
                long newposition = (duration * progress) / 1000L;
                long seekTo;
                if (check_speed_x) {
                    seekTo = (int) (newposition / mDoubleSpeed);
                } else {
                    seekTo = newposition;
                }
                mPlayer.seekTo((int) seekTo);
                if (mCurrentTime != null)
                    mCurrentTime.setText(stringForTime((int) newposition));
            }

        }

        public void onStopTrackingTouch(@NonNull SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(DEFAULT_TIMEOUT);
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {

            case R.id.pause:
                playPause();
                break;
            case R.id.zoomscreen:
                mPlayer.fullScreenToggle();
                break;
        }
    }

    public void playPause() {
        doPauseResume();
        show(DEFAULT_TIMEOUT);
    }

    public interface MediaPlayerControl {
        void start();
        void pause();
        int getDuration();
        int getCurrentPosition();
        void seekTo(int pos);
        boolean isPlaying();
        int getBufferPercentage();
        boolean canPause();
        void fullScreenToggle();
        void playNormalVideo();
        void playDoubleSpeedVideo();
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<VideoControllerView> mView;

        MessageHandler(VideoControllerView view) {
            mView = new WeakReference<VideoControllerView>(view);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (!check_in) {
                VideoControllerView view = mView.get();
                if (view == null || view.mPlayer == null) {
                    return;
                }

                switch (msg.what) {
                    case FADE_OUT:
                        view.hide();
                        break;
                    case SHOW_PROGRESS:
                        view.setProgress();
                        if (!view.mDragging && view.mShowing && view.mPlayer.isPlaying()) {
                            msg = obtainMessage(SHOW_PROGRESS);
                            sendMessageDelayed(msg, 300);
                        }
                        break;
                }
            }
        }
    }
}
