package org.devio.hi.ui.recyclerview.hiitem

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class HiDataItem<DATA, VH : RecyclerView.ViewHolder> constructor(data: DATA? = null) {

    private var mData: DATA? = null

    protected var mAdapter: HiAdapter? = null

    init {
        mData = data
    }

    fun setAdapter(adapter: HiAdapter) {
        mAdapter = adapter
    }

    abstract fun onBindData(viewHolder: VH, position: Int)

    open fun getItemLayoutRes(): Int {
        return -1;
    }

    open fun getSpanSize(): Int {
        return 0
    }

    open fun getItemView(parent: ViewGroup): View? {
        return null
    }

    fun refreshItem() {
        if (mAdapter != null) {
            mAdapter!!.refreshItem(this)
        }
    }

    fun removeItem() {
        if (mAdapter != null) {
            mAdapter!!.removeItem(this, true)
        }
    }

    /**
     * 该item被滑进屏幕
     */
    open fun onViewAttachedToWindow(holder: VH) {

    }

    /**
     * 该item被滑出屏幕
     */
    open fun onViewDetachedFromWindow(holder: VH) {

    }

    open fun onCreateViewHolder(parent: ViewGroup): VH? {
        return null
    }
}