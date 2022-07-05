package org.devio.hi.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import org.devio.hi.library.util.HiScrollUtil;
import org.devio.hi.ui.refresh.HiOverView.HiRefreshState;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HiRefreshLayout extends FrameLayout implements HiRefresh {

    private GestureDetector gestureDetector;
    private AutoScroll autoScroll;
    private HiRefreshListener refreshListener;
    private HiRefreshState refreshState;
    private HiOverView overView;
    private int mLastY;
    //刷新时是否禁止滚动
    private boolean disableRefreshScroll;

    public HiRefreshLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HiRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gestureDetector = new GestureDetector(getContext(), hiGestureDetector);
        autoScroll = new AutoScroll();
    }

    @Override
    public void setDisableRefreshScroll(boolean disableRefreshScroll) {
        this.disableRefreshScroll = disableRefreshScroll;
    }

    @Override
    public void setRefreshListener(HiRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setOverView(HiOverView overView) {
        if (this.overView != null) {
            removeView(overView);
        }
        this.overView = overView;
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(overView, 0, params);
    }

    @Override
    public void refreshFinished() {
        final View head = getChildAt(0);

        refreshState = HiRefreshState.STATE_INIT;
        overView.setRefreshState(HiRefreshState.STATE_INIT);
        mLastY = 0;
        overView.onFinish();
        if (head.getBottom() > 0) {
            recover(head.getBottom());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (!autoScroll.isFinished()) {
            return false;
        }

        View header = getChildAt(0);
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL ||
                ev.getAction() == MotionEvent.ACTION_POINTER_INDEX_MASK) {
            if (header.getBottom() > 0 && refreshState != HiRefreshState.STATE_REFRESH) {
                recover(header.getBottom());
                return false;
            }
            mLastY = 0;
        }

        boolean consumed = gestureDetector.onTouchEvent(ev);

        //TODO: 让父类接受不到真实的事件
        if (consumed || (refreshState != HiRefreshState.STATE_INIT
                && refreshState != HiRefreshState.STATE_REFRESH)
                && header.getBottom() != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
            return super.dispatchTouchEvent(ev);
        }


        if (consumed) {
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        View header = getChildAt(0);
        View child = getChildAt(1);
        if (header != null && child != null) {
            int childTop = child.getTop();
            if (refreshState == HiRefreshState.STATE_REFRESH) {
                header.layout(0, overView.mPullRefreshHeight - header.getMeasuredHeight(), right, overView.mPullRefreshHeight);
                child.layout(0, overView.mPullRefreshHeight, right, overView.mPullRefreshHeight + child.getMeasuredHeight());
            } else {
                header.layout(0, childTop - header.getMeasuredHeight(), right, childTop);
                child.layout(0, childTop, right, childTop + child.getMeasuredHeight());
            }
        }
    }

    private HiGestureDetector hiGestureDetector = new HiGestureDetector() {
        /**
         * @param e1
         * @param e2
         * @param disX
         * @param disY
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float disX, float disY) {
            Log.println(Log.INFO, "disY", "disY==========" + disY);
            if (Math.abs(disX) > Math.abs(disY) || (refreshListener != null && !refreshListener.enableRefresh())) {
                return false;
            }
            if (refreshState == HiRefreshState.STATE_REFRESH && disableRefreshScroll) {
                return true;
            }

            View head = getChildAt(0);
            View child = HiScrollUtil.findScrollableChild(HiRefreshLayout.this);
            if (HiScrollUtil.childScrolled(child)) {
                //如果列表发生了滚动则不处理
                return false;
            }

            if ((refreshState != HiRefreshState.STATE_REFRESH || head.getBottom() <= overView.mPullRefreshHeight)
                    && (head.getBottom() > 0 || disY <= 0.0f)) {
                if (refreshState != HiRefreshState.STATE_OVER_RELEASE) {
                    int speed;
                    if (child.getTop() < overView.mPullRefreshHeight) {
                        speed = (int) (mLastY / overView.minDamp);
                    } else {
                        speed = (int) (mLastY / overView.maxDamp);
                    }
                    boolean consumed = moveDown(speed, true);
                    mLastY = (int) -disY;

                    return consumed;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    };

    private class AutoScroll implements Runnable {
        private Scroller scroller;
        private int mLastY;
        private boolean mIsFinished;

        public AutoScroll() {
            scroller = new Scroller(getContext(), new LinearInterpolator());
            mIsFinished = true;
        }

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                moveDown(mLastY - scroller.getCurrY(), false);
                mLastY = scroller.getCurrY();
                mIsFinished = false;
                post(this);
            } else {
                removeCallbacks(this);
                mIsFinished = true;
            }
        }

        public void recover(int dis) {
            if (dis < 0) {
                return;
            }
            removeCallbacks(this);
            mLastY = 0;
            mIsFinished = false;
            scroller.startScroll(0, 0, 0, dis, 300);
            post(this);
        }

        public boolean isFinished() {
            return mIsFinished;
        }
    }

    private void recover(int dis) {
        if (refreshListener != null && dis > overView.mPullRefreshHeight) {
            autoScroll.recover(dis - overView.mPullRefreshHeight);
            refreshState = HiOverView.HiRefreshState.STATE_OVER_RELEASE;
        } else {
            autoScroll.recover(dis);
        }
    }

    private boolean moveDown(int offsetY, boolean nonAuto) {
        View header = getChildAt(0);
        View child = getChildAt(1);
        int childTop = child.getTop() + offsetY;

        if (childTop <= 0) {
            offsetY = -child.getTop();
            mLastY = 0;
            if (refreshState != HiRefreshState.STATE_REFRESH) {
                overView.setRefreshState(HiRefreshState.STATE_INIT);
                refreshState = HiRefreshState.STATE_INIT;
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
        } else if (childTop <= overView.mPullRefreshHeight) {
            if (refreshState != HiRefreshState.STATE_VISIBLE && nonAuto) {
                refreshState = HiRefreshState.STATE_VISIBLE;
                overView.setRefreshState(HiRefreshState.STATE_VISIBLE);
                overView.onVisible();
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);
            if (childTop == overView.mPullRefreshHeight && refreshState == HiRefreshState.STATE_OVER_RELEASE && !nonAuto) {
                refresh();
            }

        } else if (childTop > overView.mPullRefreshHeight) {
            if (refreshState != HiRefreshState.STATE_OVER && nonAuto) {
                refreshState = HiRefreshState.STATE_OVER;
                overView.setRefreshState(HiRefreshState.STATE_OVER);
                overView.onOver();
            }
            header.offsetTopAndBottom(offsetY);
            child.offsetTopAndBottom(offsetY);

            Log.println(Log.INFO, "STATE_OVER", "offsetY==========" + offsetY);
        }

        if (overView != null) {
            overView.onScroll(header.getBottom(), overView.mPullRefreshHeight);
        }
        return true;
    }

    private void refresh() {
        if (refreshListener != null) {
            refreshState = HiRefreshState.STATE_REFRESH;
            overView.onRefresh();
            overView.setRefreshState(HiRefreshState.STATE_REFRESH);
            refreshListener.onRefresh();
        }
    }
}
