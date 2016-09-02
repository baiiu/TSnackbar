package com.baiiu.tsnackbar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class TSnackBarManager {
    private volatile static TSnackBarManager instance;
    private final Handler mHandler;
    TSnackBar mCurrentSnackBar;

    private static final int MSG_TIMEOUT = 0;

    public static final int LENGTH_SHORT = 1500;
    public static final int LENGTH_LONG = 2750;

    public static TSnackBarManager instance() {
        if (instance == null) {
            synchronized (TSnackBarManager.class) {
                if (instance == null)
                    instance = new TSnackBarManager();
            }
        }
        return instance;
    }

    private TSnackBarManager() {
        mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                dismissView();
                return true;
            }
        });
    }


    public void show(int duration, TSnackBar nextSnackBar) {
        if (nextSnackBar == null) {
            return;
        }

        cancelTimeout();

        if (mCurrentSnackBar != null) {
            mCurrentSnackBar.clearView();
            mCurrentSnackBar = null;
        }

        nextSnackBar.showView();

        if (duration != TSnackBar.LENGTH_INDEFINITE) {
            mCurrentSnackBar = nextSnackBar;
            scheduleTimeout(duration);
        }
    }

    private void dismissView() {
        if (mCurrentSnackBar != null) {
            mCurrentSnackBar.dismissView();
        }
    }


    public void clearCurrentSnackbar() {
        if (mCurrentSnackBar != null) {
            mCurrentSnackBar.clearView();
            mCurrentSnackBar = null;
        }
    }

    public void scheduleTimeout(int duration) {
        int handlerDuration = TSnackBar.ANIMATION_DURATION;
        if (duration == TSnackBar.LENGTH_SHORT) {
            handlerDuration += LENGTH_SHORT;
        } else if (duration == TSnackBar.LENGTH_LONG) {
            handlerDuration += LENGTH_LONG;
        } else {
            handlerDuration += duration;
        }

        mHandler.sendMessageDelayed(Message.obtain(mHandler, MSG_TIMEOUT), handlerDuration);
    }

    public void cancelTimeout() {
        mHandler.removeMessages(MSG_TIMEOUT);//清除当前message
    }

    public void restoreTimeout() {
        if (mCurrentSnackBar != null) {
            scheduleTimeout(mCurrentSnackBar.getDuration());
        }
    }


}
