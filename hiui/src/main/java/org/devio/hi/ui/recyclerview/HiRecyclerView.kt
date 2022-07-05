package org.devio.hi.ui.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.devio.hi.library.log.HiLog
import org.devio.hi.ui.R
import org.devio.hi.ui.recyclerview.hiitem.HiAdapter

open class HiRecyclerView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {

    private var loadMoreScrollListener: OnScrollListener? = null
    private var footerView: View? = null
    private var isLoadingMore: Boolean = false

    inner class LoadMoreScrollListener(val callback: () -> Unit, val prefetchSize: Int) :
        OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (isLoadingMore) return

            val hiAdapter = adapter as HiAdapter
            val itemCount = hiAdapter.itemCount
            if (itemCount <= 0) {
                return
            }

            val canScrollVertical = recyclerView.canScrollVertically(1)
            val lastVisibleItem = findLastVisibleItemPosition(recyclerView)
            val arriveBottom = lastVisibleItem >= (itemCount - 1)
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING && (canScrollVertical || arriveBottom)) {
                addFooterView()
            }

            if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                return
            }

            val arrivePrefetchPosition = itemCount - lastVisibleItem <= prefetchSize
            if (!arrivePrefetchPosition) {
                return
            }

            callback()
            isLoadingMore = true
        }
    }

    fun enableLoadMore(callback: () -> Unit, prefetchSize: Int) {
        if (adapter !is HiAdapter) {
            HiLog.e("enableLoadMore must use hiadapter")
            return
        }
        loadMoreScrollListener = LoadMoreScrollListener(callback, prefetchSize)
        loadMoreScrollListener?.let {
            addOnScrollListener(it)
        }
    }

    fun disableLoadMore() {
        if (adapter !is HiAdapter) {
            HiLog.e("disableLoadMore must use hiadapter")
            return
        }

        val hiAdapter = adapter as HiAdapter
        footerView?.let {
            if (it.parent != null) {
                hiAdapter.removeFooterView(it)
            }
        }

        loadMoreScrollListener?.let {
            removeOnScrollListener(it)
            loadMoreScrollListener = null
            footerView = null
            isLoadingMore = false
        }
    }

    fun isLoading(): Boolean {
        return isLoadingMore
    }

    fun loadFinished(success: Boolean) {
        if (adapter !is HiAdapter) {
            HiLog.e("loadFinished must use hiadapter")
            return
        }

        isLoadingMore = false
        val hiAdapter = adapter as HiAdapter
        if (!success) {
            footerView?.let {
                if (it.parent != null) {
                    hiAdapter.removeFooterView(it)
                }
            }
        }
    }

    private fun findLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        when (val layoutManager = recyclerView.layoutManager) {
            is LinearLayoutManager -> {
                return layoutManager.findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                return layoutManager.findLastVisibleItemPositions(null)[0]
            }
        }
        return -1
    }

    private fun addFooterView() {
        if (adapter !is HiAdapter) {
            HiLog.e("addFooterView must use hiadapter")
            return
        }
        val footerView = getFooterView()
        val hiAdapter = adapter as HiAdapter
        //但是，这里有个坑。。。在一些边界场景下。会出现多次添加的情况， 添加之前先 remove --》hiAdapter。.removeFooterView()
        hiAdapter.removeFooterView(footerView)
        //主要是为了避免 removeFooterView 不及时，在边界场景下可能会出现，footerView还没从recyclervIEW上移除掉，但我们又调用了addFooterView，
        //造成的重复添加的情况，此时会抛出 add view must call removeview form it parent first exception
        if (footerView.parent != null) {
            footerView.post {
                addFooterView()
            }
        } else {
            hiAdapter.addFooterView(footerView)
        }

    }

    private fun getFooterView(): View {
        if (footerView == null) {
            footerView = LayoutInflater.from(context).inflate(R.layout.layout_footer_loading, this@HiRecyclerView, false)
        }
        return footerView!!
    }

}