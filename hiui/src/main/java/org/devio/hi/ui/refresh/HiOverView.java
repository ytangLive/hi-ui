package org.devio.hi.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.devio.hi.library.util.HiDisplayUtil;

public abstract class HiOverView extends FrameLayout {

    public enum HiRefreshState {
        /**
         * 初始态
         */
        STATE_INIT,
        /**
         * Header展示的状态
         */
        STATE_VISIBLE,
        /**
         * 超出可刷新距离的状态
         */
        STATE_OVER,
        /**
         * 刷新中的状态
         */
        STATE_REFRESH,
        /**
         * 超出刷新位置松开手后的状态
         */
        STATE_OVER_RELEASE
    }

    protected HiRefreshState refreshState = HiRefreshState.STATE_INIT;

    /**
     * 触发下拉刷新 需要的最小高度
     */
    public int mPullRefreshHeight;
    /**
     * 最小阻尼
     */
    public float minDamp = 1.6f;
    /**
     * 最大阻尼
     */
    public float maxDamp = 2.2f;

    public HiOverView(@NonNull Context context) {
        super(context);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        preInit();
    }

    public HiOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preInit();
    }

    void preInit() {
        mPullRefreshHeight = HiDisplayUtil.dp2px(76, getResources());
        init();
    }

    public abstract void init();

    protected abstract void onScroll(int scrollY, int pullRefreshHeight);

    /**
     * 显示Overlay
     */
    protected abstract void onVisible();

    /**
     * 超过Overlay，释放就会加载
     */
    protected abstract void onOver();

    /**
     * 开始加载
     */
    protected abstract void onRefresh();

    /**
     * 加载完成
     */
    protected abstract void onFinish();

    public HiRefreshState getRefreshState() {
        return refreshState;
    }

    public void setRefreshState(HiRefreshState refreshState) {
        this.refreshState = refreshState;
    }
}
