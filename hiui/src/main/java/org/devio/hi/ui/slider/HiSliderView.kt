package org.devio.hi.ui.slider

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.hi_slider_menu_item.view.*
import org.devio.hi.ui.R
import org.devio.hi.ui.item.HiViewHolder

class HiSliderView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private val MENU_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_menu_item
    private val CONTENT_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_content_item

    private val menuItemAttr = AttrsParse.parseMenuItemAttr(context, attributeSet)
    private val menuView = RecyclerView(context)
    private val contentView = RecyclerView(context)

    init {
        orientation = HORIZONTAL

        menuView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        menuView.overScrollMode = View.OVER_SCROLL_NEVER
        menuView.itemAnimator = null

        contentView.layoutParams =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        contentView.overScrollMode = View.OVER_SCROLL_NEVER
        contentView.itemAnimator = null

        addView(menuView)
        addView(contentView)
    }

    fun bindMenuView(
        @LayoutRes layoutRes: Int = MENU_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: (HiViewHolder, Int) -> Unit
    ) {

        menuView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        menuView.adapter = MenuAdapter(layoutRes, itemCount, onBindView, onItemClick)

    }

    fun bindContentView(
        @LayoutRes layoutRes: Int = CONTENT_ITEM_LAYOUT_RES_ID,
        layoutManager: RecyclerView.LayoutManager,
        itemCount: Int,
        itemDecoration: RecyclerView.ItemDecoration?,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: (HiViewHolder, Int) -> Unit
    ) {

        if (contentView.layoutManager == null) {
            contentView.layoutManager = layoutManager
            contentView.adapter = ContentAdapter(layoutRes)
            itemDecoration?.let {
                contentView.addItemDecoration(it)
            }
        }
        val adapter = contentView.adapter as ContentAdapter
        adapter.update(itemCount, onBindView, onItemClick)
        adapter.notifyDataSetChanged()

        contentView.scrollToPosition(0)

    }

    inner class MenuAdapter(
        private val layoutRes: Int,
        private val count: Int,
        val onBindView: (HiViewHolder, Int) -> Unit,
        val onItemClick: (HiViewHolder, Int) -> Unit
    ) : RecyclerView.Adapter<HiViewHolder>() {

        var currentPosition = 0
        var lastPosition = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val menuItem = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
            val params = RecyclerView.LayoutParams(menuItemAttr.width, menuItemAttr.height)
            menuItem.layoutParams = params
            menuItem.menu_item_indicator.setImageDrawable(menuItemAttr.indicator)
            menuItem.menu_item_title.setTextColor(menuItemAttr.textColor)

            return HiViewHolder(menuItem)
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                currentPosition = position
                notifyItemChanged(currentPosition)
                notifyItemChanged(lastPosition)
            }

            if (currentPosition == position) {
                onItemClick(holder, position)
                lastPosition = currentPosition
            }

            applyItemAttr(position, holder)
            onBindView(holder, position)
        }

        override fun getItemCount(): Int {
            return count
        }

        private fun applyItemAttr(position: Int, holder: HiViewHolder) {
            val selected = currentPosition == position
            val titleView: TextView? = holder.itemView.menu_item_title
            val indicatorView: ImageView? = holder.itemView.menu_item_indicator
            if (selected) {
                titleView?.setBackgroundColor(menuItemAttr.selectBackgroundColor)
                titleView?.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    menuItemAttr.selectTextSize.toFloat()
                )
                indicatorView?.visibility = View.VISIBLE
            } else {
                titleView?.setBackgroundColor(menuItemAttr.normalBackgroundColor)
                titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuItemAttr.textSize.toFloat())
                indicatorView?.visibility = View.GONE
            }
            titleView?.isSelected = selected
        }
    }

    inner class ContentAdapter(val layoutRes: Int) : RecyclerView.Adapter<HiViewHolder>() {

        private var count: Int = 0
        private lateinit var onBindView: (HiViewHolder, Int) -> Unit
        private lateinit var onItemClick: (HiViewHolder, Int) -> Unit
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val contentItem = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)

            val contentWidth = width - paddingLeft - paddingLeft - menuItemAttr.width
            val layoutManager = contentView.layoutManager
            var spanCount = 0
            if (layoutManager is GridLayoutManager) {
                spanCount = layoutManager.spanCount
            } else if (layoutManager is StaggeredGridLayoutManager) {
                spanCount = layoutManager.spanCount
            }
            if (spanCount > 0) {
                val itemWidth = contentWidth / spanCount
                val params = RecyclerView.LayoutParams(itemWidth, itemWidth)
                contentItem.layoutParams = params
            }

            return HiViewHolder(contentItem)
        }

        override fun getItemCount(): Int {
            return count
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                onItemClick(holder, position)
            }
            onBindView(holder, position)
        }

        fun update(
            itemCount: Int,
            onBindView: (HiViewHolder, Int) -> Unit,
            onItemClick: (HiViewHolder, Int) -> Unit
        ) {
            this.count = itemCount
            this.onBindView = onBindView
            this.onItemClick = onItemClick
        }

    }
}

