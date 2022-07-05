package org.devio.hi.ui.item

import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

open class HiViewHolder(val view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
    override val containerView get() = view

    private val viewCache = SparseArray<View>()
    fun <T : View> findViewById(@IdRes viewId: Int): T? {
        var view = viewCache.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            viewCache.put(viewId, view)
        }
        return view as? T
    }
}