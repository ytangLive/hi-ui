package org.devio.hi.ui.refresh;

public interface HiRefresh {

    /**
     * 刷新时是否禁止滚动
     *
     * @param disableRefreshScroll 否禁止滚动
     */
    void setDisableRefreshScroll(boolean disableRefreshScroll);

    /**
     * 设置下拉刷新的监听器
     *
     * @param refreshListener 刷新的监听器
     */
    void setRefreshListener(HiRefreshListener refreshListener);

    /**
     * 刷新完成
     */
    void refreshFinished();

    interface HiRefreshListener {

        void onRefresh();

        boolean enableRefresh();
    }
}
