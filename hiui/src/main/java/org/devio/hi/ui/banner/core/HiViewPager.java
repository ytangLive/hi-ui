package org.devio.hi.ui.banner.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class HiViewPager extends ViewPager {

    private int mIntervalTime;
    private boolean mAutoPlay;
    private boolean isLayout = false;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            next();
            handler.postDelayed(this, mIntervalTime);
        }
    };

    public HiViewPager(@NonNull Context context) {
        super(context);
    }

    public HiViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                start();
                break;
            default:
                stop();
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setScrollDuration(int duration) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        isLayout = true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isLayout && getAdapter() != null && getAdapter().getCount() > 0) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mFirstLayout");
                mScroller.setAccessible(true);
                mScroller.set(this, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (((Activity) getContext()).isFinishing()) {
            super.onDetachedFromWindow();
        }
        stop();
    }

    private void start() {
        handler.removeCallbacksAndMessages(null);
        if (mAutoPlay) {
            handler.postDelayed(runnable, mIntervalTime);
        }
    }

    private void stop() {
        handler.removeCallbacksAndMessages(null);
    }

    private int next() {
        int nextPosition = -1;
        if (getAdapter() == null || getAdapter().getCount() <= 1) {
            stop();
            return nextPosition;
        }
        nextPosition = getCurrentItem() + 1;
        if (nextPosition >= getAdapter().getCount()) {
            nextPosition = ((HiBannerAdapter) getAdapter()).getFirstItem();
        }
        setCurrentItem(nextPosition, true);
        return nextPosition;
    }

    public void setIntervalTime(int mIntervalTime) {
        this.mIntervalTime = mIntervalTime;
    }

    public void setAutoPlay(boolean mAutoPlay) {
        this.mAutoPlay = mAutoPlay;
        if (!mAutoPlay) {
            stop();
        }
    }
}
