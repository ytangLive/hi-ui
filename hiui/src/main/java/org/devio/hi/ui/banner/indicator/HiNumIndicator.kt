package org.devio.hi.ui.banner.indicator

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.R

class HiNumIndicator @JvmOverloads constructor
 (context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0) : FrameLayout(context,attrs, defStyleAttr), HiIndicator<FrameLayout?> {

 companion object{
 private const val VWC = ViewGroup.LayoutParams.WRAP_CONTENT
 }

 /**
  * 指示点左右内间距
  */
 private var mPointLeftRightPadding = 0

 /**
  * 指示点上下内间距
  */
 private var mPointTopBottomPadding = 0

 init {
 mPointLeftRightPadding = HiDisplayUtil.dp2px(10f, context.resources)
 mPointTopBottomPadding = HiDisplayUtil.dp2px(10f, context.resources)
 }

 override fun get(): FrameLayout {
 return this
 }

 override fun onInflate(count: Int) {
 val viewGroup = LinearLayout(context)
 viewGroup.orientation = LinearLayout.HORIZONTAL
 viewGroup.setPadding(0, 0, mPointLeftRightPadding, mPointTopBottomPadding)

 val indexTv = TextView(context)
 indexTv.id = R.id.indicator_index;
 indexTv.setTextColor(Color.WHITE)
 viewGroup.addView(indexTv)

 val symbolTv = TextView(context)
 symbolTv.text = "/"
 symbolTv.setTextColor(Color.WHITE)
 viewGroup.addView(symbolTv)

 val countTv = TextView(context)
 countTv.text = count.toString()
 countTv.setTextColor(Color.WHITE)
 viewGroup.addView(countTv)

 val params = FrameLayout.LayoutParams(VWC,VWC)
 params.gravity = Gravity.BOTTOM or Gravity.END

 addView(viewGroup, params)
 }

 override fun onPageChange(current: Int, count: Int) {
 val viewGroup = getChildAt(0) as ViewGroup
 if(viewGroup != null){
 val countTv = viewGroup.findViewById<TextView>(R.id.indicator_index)
 countTv.text = (current + 1).toString();
 }
 }
}