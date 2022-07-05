package org.devio.hi.ui.navigation

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.StringRes
import org.devio.hi.library.util.HiRes
import org.devio.hi.ui.R
import org.devio.hi.ui.icfont.IconFontButton
import java.lang.IllegalStateException
import java.util.ArrayList

class HiNavigationBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var titleView: TextView? = null
    private var subTitleView: TextView? = null
    private var titleContainer: LinearLayout? = null

    //左右按钮
    private var mLeftLastViewId = View.NO_ID
    private val mLeftViewList = ArrayList<View>()
    private var mRightLastViewId = View.NO_ID
    private val mRightViewList = ArrayList<View>()

    private val navAttrs = AttrsParse.parseMenuItemAttr(context, attrs, defStyleAttr)

    init {
        if (!TextUtils.isEmpty(navAttrs.navTitle)) {
            setTitle(navAttrs.navTitle!!)
        }

        if (!TextUtils.isEmpty(navAttrs.navSubtitle)) {
            setSubTitle(navAttrs.navSubtitle!!)
        }

        if (navAttrs.lineHeight > 0) {
            addLineView()
        }
    }

    fun setNavListener(listener: OnClickListener) {
        if (!TextUtils.isEmpty(navAttrs.navIconStr)) {
            val navBackView = addLeftTextButton(navAttrs.navIconStr!!, R.id.id_nav_left_back_view)
            navBackView.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.navIconSize)
            navBackView.setTextColor(navAttrs.navIconColor)
            navBackView.setOnClickListener(listener)
        }
    }

    fun setCenterView(view: View) {
        var params = view.layoutParams
        if(params == null){
            params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }else if(params !is LayoutParams){
            params = LayoutParams(params)
        }

        val centerViewParams = params as LayoutParams
        centerViewParams.addRule(LEFT_OF, mRightLastViewId)
        centerViewParams.addRule(RIGHT_OF, mLeftLastViewId)
        centerViewParams.addRule(CENTER_VERTICAL)
        addView(view, centerViewParams)
    }

    fun addLeftTextButton(@StringRes stringRes: Int, idBtn: Int): Button {
        return addLeftTextButton(HiRes.getString(stringRes), idBtn)
    }

    fun addLeftTextButton(navIconStr: String, idBtn: Int): Button {
        val button = generateTextButton()
        button.id = idBtn
        button.text = navIconStr
        if (mLeftViewList.isEmpty()) {
            button.setPadding(navAttrs.horPadding * 2, navAttrs.topPadding, navAttrs.horPadding, 0)
        } else {
            button.setPadding(navAttrs.horPadding, navAttrs.topPadding, navAttrs.horPadding, 0)
        }

        addLeftView(button, generateTextButtonLayoutParams())
        return button
    }

    private fun addLeftView(
        button: IconFontButton,
        params: RelativeLayout.LayoutParams
    ) {
        val viewId = button.id
        if (viewId == View.NO_ID) {
            throw IllegalStateException("right view must has an unique id.")
        }
        params.addRule(CENTER_VERTICAL)
        if (mLeftLastViewId == NO_ID) {
            params.addRule(ALIGN_PARENT_LEFT)
        } else {
            params.addRule(RIGHT_OF, mLeftLastViewId)
        }

        mLeftLastViewId = viewId
        params.alignWithParent = true //alignParentIfMissing
        button.layoutParams = params

        mLeftViewList.add(button)
        addView(button, params)
    }

    fun addRightTextButton(@StringRes stringRes: Int, viewId: Int): Button {
        return addRightTextButton(HiRes.getString(stringRes), viewId)
    }

    fun addRightTextButton(buttonText: String, viewId: Int): Button {
        val button = generateTextButton()
        button.text = buttonText
        button.id = viewId
        if (mRightViewList.isEmpty()) {
            button.setPadding(navAttrs.horPadding, navAttrs.topPadding, navAttrs.horPadding * 2, 0)
        } else {
            button.setPadding(navAttrs.horPadding, navAttrs.topPadding, navAttrs.horPadding, 0)
        }

        addRightView(button, generateTextButtonLayoutParams())
        return button
    }

    private fun addRightView(
        view: View,
        params: LayoutParams
    ) {
        val viewId = view.id
        if (viewId == View.NO_ID) {
            throw IllegalStateException("right view must has an unique id.")
        }
        if (mRightLastViewId == View.NO_ID) {
            params.addRule(ALIGN_PARENT_RIGHT, viewId)
        } else {
            params.addRule(LEFT_OF, mRightLastViewId)
        }
        mRightLastViewId = viewId
        params.alignWithParent = true //alignParentIfMissing
        mRightViewList.add(view)
        addView(view, params)
    }

    private fun generateTextButton(): IconFontButton {
        val button = IconFontButton(context, null)
        button.setBackgroundResource(0)
        button.minWidth = 0
        button.minimumWidth = 0
        button.minHeight = 0
        button.minHeight = 0
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.btnTextSize)
        button.setTextColor(navAttrs.navIconColor)
        button.gravity = Gravity.CENTER
        button.includeFontPadding = false
        return button
    }

    private fun generateTextButtonLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    }

    fun setTitle(navTitle: String) {
        ensureTitleView()
        titleView?.text = navTitle
        titleView?.visibility = if (TextUtils.isEmpty(navTitle)) View.GONE else View.VISIBLE
    }

    fun setSubTitle(navSubtitle: String) {
        ensureSubtitleView()
        updateTitleViewStyle()
        subTitleView?.text = navSubtitle
        subTitleView?.visibility = if (TextUtils.isEmpty(navSubtitle)) View.GONE else View.VISIBLE
    }

    private fun ensureTitleView() {
        if (titleView == null) {
            titleView = TextView(context)
            titleView?.apply {
                gravity = Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttrs.titleTextColor)

                updateTitleViewStyle()
                ensureTitleContainer()

                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.topMargin = navAttrs.topPadding
                titleContainer?.addView(titleView, 0, params)
            }
        }
    }

    private fun ensureSubtitleView() {
        if (subTitleView == null) {
            subTitleView = TextView(context)
            subTitleView?.apply {
                gravity = Gravity.CENTER
                isSingleLine = true
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttrs.subTitleTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.subTitleSize)

                ensureTitleContainer()
                titleContainer?.addView(subTitleView)
            }
        }
    }

    private fun updateTitleViewStyle() {
        if (subTitleView == null) {
            titleView?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                navAttrs.titleTextSize
            )
            titleView?.typeface = Typeface.DEFAULT_BOLD
        } else {
            titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.titleTextSizeWithSubTitle)
            titleView?.typeface = Typeface.DEFAULT
        }

    }

    private fun ensureTitleContainer() {
        if (titleContainer == null) {
            titleContainer = LinearLayout(context)
            titleContainer?.apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
                params.addRule(CENTER_IN_PARENT)
                this@HiNavigationBar.addView(titleContainer, params)
            }
        }
    }

    private fun addLineView() {
        val view = View(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, navAttrs.lineHeight)
        params.addRule(ALIGN_PARENT_BOTTOM)
        view.layoutParams = params
        view.setBackgroundColor(navAttrs.lineColor)
        addView(view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (titleContainer != null) {
            var leftUseSpace = paddingLeft
            for (view in mLeftViewList) {
                leftUseSpace += view.measuredWidth
            }
            //计算出标题栏右侧已占用的空间
            var rightUseSpace = paddingRight
            for (view in mRightViewList) {
                rightUseSpace += view.measuredWidth
            }

            val titleContainerWidth = titleContainer!!.measuredWidth
            val remainingSpace = measuredWidth - Math.max(leftUseSpace, rightUseSpace) * 2
            if (remainingSpace < titleContainerWidth) {
                val size = MeasureSpec.makeMeasureSpec(remainingSpace, MeasureSpec.EXACTLY)
                titleContainer!!.measure(size, heightMeasureSpec)
            }
        }
    }

}