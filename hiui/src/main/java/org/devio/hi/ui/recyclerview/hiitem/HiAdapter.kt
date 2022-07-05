package org.devio.hi.ui.recyclerview.hiitem

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.devio.hi.ui.item.HiViewHolder
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

class HiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var recyclerViewRef: WeakReference<RecyclerView>? = null
    private var mContext: Context? = null
    private var inflater: LayoutInflater? = null
    private val dataSets = ArrayList<HiDataItem<*, out RecyclerView.ViewHolder>>()
    private val typeArrays = SparseArray<HiDataItem<*, out RecyclerView.ViewHolder>>()
//..private val typePositions = SparseIntArray()

    private val headers = SparseArray<View>()
    private val footers = SparseArray<View>()
    private var BASE_ITEM_TYPE_HEADER = 1000000
    private var BASE_ITEM_TYPE_FOOTER = 2000000

    init {
        mContext = context
        inflater = LayoutInflater.from(context)
    }

    fun addHeaderView(header: View) {
        if (headers.indexOfValue(header) < 0) {
            headers.put(BASE_ITEM_TYPE_HEADER++, header)
        }
        notifyItemInserted(headers.size() - 1)
    }

    fun removeHeaderView(header: View) {
        val indexOfValue = headers.indexOfValue(header)
        if (indexOfValue < 0) return
        headers.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue)
    }

    fun addFooterView(footer: View) {
        if (footers.indexOfValue(footer) < 0) {
            footers.put(BASE_ITEM_TYPE_FOOTER++, footer)
            notifyItemInserted(itemCount - 1)
        }
    }

    fun removeFooterView(footer: View) {
        val indexOfValue = footers.indexOfValue(footer)
        if (indexOfValue < 0) return
        footers.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue + getHeaderSize() + dataSets.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (headers.indexOfKey(viewType) >= 0) {
            val view = headers[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }
        if (footers.indexOfKey(viewType) >= 0) {
            val view = footers[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }

        val dataItem = typeArrays.get(viewType)
//val position = typePositions.get(viewType)
//val dataItem = dataSets[position]
        if (dataItem != null) {
            val viewHolder = dataItem.onCreateViewHolder(parent)
            if (viewHolder != null) {
                return viewHolder
            }
            var itemView = dataItem.getItemView(parent)
            if (itemView == null) {
                val layoutRes = dataItem.getItemLayoutRes()
                if (layoutRes < 0) {
                    RuntimeException("dataItem:" + dataItem.javaClass.name + " must override getItemView or getItemLayoutRes")
                }
                itemView = inflater!!.inflate(layoutRes, parent, false)
            }

            return createViewHolderInternal(dataItem.javaClass, itemView)
        }
        return object : RecyclerView.ViewHolder(View(mContext)) {}
    }

    private fun createViewHolderInternal(
        clazz: Class<HiDataItem<*, out RecyclerView.ViewHolder>>,
        itemView: View?
    ): RecyclerView.ViewHolder {
        val superclass = clazz.genericSuperclass
        if (superclass is ParameterizedType) {
            val args = superclass.actualTypeArguments
            for (argument in args) {
                if (argument is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(
                        argument
                    )
                ) {
                    try {
                        return argument.getConstructor(View::class.java)
                            .newInstance(itemView) as RecyclerView.ViewHolder
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }

        return HiViewHolder(itemView!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position)) return

        val dataItem = getItem(position - getHeaderSize())
        dataItem?.let {
            it.onBindData(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderPosition(position)) {
            return headers.keyAt(position)
        }
        if (isFooterPosition(position)) {
            return footers.keyAt(position - getHeaderSize() - dataSets.size)
        }

        val itemPosition = position - getHeaderSize()
        val dataItem = dataSets[itemPosition]
        dataItem.setAdapter(this)
        val type = dataItem.javaClass.hashCode()
        if (typeArrays.indexOfKey(type) < 0) {
            typeArrays.put(type, dataItem)
        }
//if(typePositions.indexOfKey(type) < 0){
//..typePositions.put(type, itemPosition)
//}
        return type
    }

    override fun getItemCount(): Int {
        return dataSets.size + headers.size() + footers.size()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        recyclerViewRef = WeakReference(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (isHeaderPosition(position) || isFooterPosition(position)) {
                        return spanCount
                    }
                    val itemPosition = position - getHeaderSize()
                    if (position < dataSets.size) {
                        val dataItem = dataSets[itemPosition]
                        if (dataItem != null) {
                            val spanSize = dataItem.getSpanSize()
                            return if (spanSize <= 0) spanCount else spanSize
                        }

                    }
                    return spanCount
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerViewRef?.clear()
    }

    open fun getAttachRecyclerView(): RecyclerView? {
        return recyclerViewRef?.get()
    }


    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        val recyclerView = getAttachRecyclerView()
        val layoutManager = recyclerView!!.layoutManager

        val position = recyclerView.getChildAdapterPosition(holder.itemView)
        val itemPosition = position - getHeaderSize()
        val dateItem = getItem(itemPosition) ?: return

        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (isHeaderPosition(position) || isFooterPosition(position)) {
                        return spanCount
                    }
                    val itemPosition = position - getHeaderSize()
                    if (position < dataSets.size) {
                        val dataItem = dataSets[itemPosition]
                        if (dataItem != null) {
                            val spanSize = dataItem.getSpanSize()
                            return if (spanSize <= 0) spanCount else spanSize
                        }

                    }
                    return spanCount
                }
            }
        }

        dateItem.onViewAttachedToWindow(holder)

    }

    fun getItem(position: Int): HiDataItem<*, RecyclerView.ViewHolder>? {
        if (position < 0 || position >= dataSets.size)
            return null

        return dataSets[position] as HiDataItem<*, RecyclerView.ViewHolder>
    }

    fun addItemAt(
        position: Int,
        item: HiDataItem<*, out RecyclerView.ViewHolder>,
        notify: Boolean
    ) {
        if (position > 0) {
            dataSets.add(position, item)
        } else {
            dataSets.add(item)
        }

        if (notify) {
            val notifyPos = if (position > 0) position else dataSets.size - 1
            notifyItemInserted(notifyPos)
        }
    }

    fun addItems(items: List<HiDataItem<*, out RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = dataSets.size;
        for (item in items) {
            dataSets.add(item)
        }
        if (notify) {
            notifyItemRangeInserted(start, items.size)
        }

    }

    private fun removeItemAt(
        position: Int,
        notify: Boolean
    ): HiDataItem<*, out RecyclerView.ViewHolder>? {
        if (position >= 0 && position < dataSets.size) {
            val dataItem = dataSets.removeAt(position)
            if (notify) {
                notifyItemRemoved(position)
            }
            return dataItem
        } else {
            return null
        }
    }

    fun removeItem(item: HiDataItem<*, out RecyclerView.ViewHolder>, notify: Boolean) {
        val index = dataSets.indexOf(item)
        if (index >= 0) {
            removeItemAt(index, notify)
        }
    }

    fun refreshItem(item: HiDataItem<*, out RecyclerView.ViewHolder>) {
        val index = dataSets.indexOf(item)
        if (index >= 0) {
            notifyItemChanged(index)
        }
    }

    private fun isHeaderPosition(position: Int): Boolean {
        return position < getHeaderSize()
    }

    private fun isFooterPosition(position: Int): Boolean {
        return position >= dataSets.size + getHeaderSize()
    }

    fun getHeaderSize(): Int {
        return headers.size()
    }

    fun getFooterSize(): Int {
        return footers.size()
    }

    fun clearItems() {
        dataSets.clear()
        notifyDataSetChanged()
    }

}