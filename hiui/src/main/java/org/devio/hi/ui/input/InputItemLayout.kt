package org.devio.hi.ui.input

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity.CENTER
import android.view.Gravity.LEFT
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.devio.hi.ui.R

class InputItemLayout
@JvmOverloads constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int = 0) :
    LinearLayout(context, attributeSet, defStyle) {

    private lateinit var titleView: TextView
    private lateinit var editText: EditText
    private var bottomLine: Line
    private var topLine: Line
    private val topPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bottomPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

        orientation = HORIZONTAL

        val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.InputItemLayout)
        val title = typeArray.getString(R.styleable.InputItemLayout_title)
        val titleResId = typeArray.getResourceId(R.styleable.InputItemLayout_titleTextAppearance, 0)
        parseTitleStyle(title, titleResId)

        val inputResId = typeArray.getResourceId(R.styleable.InputItemLayout_inputTextAppearance, 0)
        val hint = typeArray.getString(R.styleable.InputItemLayout_hint)
        val inputType = typeArray.getInteger(R.styleable.InputItemLayout_inputType, 0)
        parseInputStyle(inputResId, hint, inputType)

        val topResId = typeArray.getResourceId(R.styleable.InputItemLayout_topLineAppearance, 0)
        val bottomResId =
            typeArray.getResourceId(R.styleable.InputItemLayout_bottomLineAppearance, 0)
        topLine = parseLineStyle(topResId)
        bottomLine = parseLineStyle(bottomResId)

        if (topLine.enable) {
            topPaint.color = topLine.color
            topPaint.style = Paint.Style.FILL_AND_STROKE
            topPaint.strokeWidth = topLine.height
        }

        if (bottomLine.enable) {
            bottomPaint.color = bottomLine.color
            bottomPaint.style = Paint.Style.FILL_AND_STROKE
            bottomPaint.strokeWidth = bottomLine.height
        }

        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (topLine.enable) {
            canvas!!.drawLine(
                topLine.leftMargin,
                0f,
                width - topLine.rightMargin,
                0f,
                topPaint
            )
        }

        if (bottomLine.enable) {
            canvas!!.drawLine(
                bottomLine.leftMargin,
                height - bottomLine.height,
                width - bottomLine.rightMargin,
                height - bottomLine.height,
                bottomPaint
            )
        }

    }

    private fun parseLineStyle(resId: Int): Line {
        val line = Line()
        val array = context.obtainStyledAttributes(resId, R.styleable.lineAppearance)
        line.color = array.getColor(
            R.styleable.lineAppearance_color,
            ContextCompat.getColor(context, R.color.color_d1d2)
        )
        line.height = array.getDimensionPixelOffset(R.styleable.lineAppearance_height, 0).toFloat()
        line.leftMargin =
            array.getDimensionPixelOffset(R.styleable.lineAppearance_leftMargin, 0).toFloat()
        line.rightMargin =
            array.getDimensionPixelOffset(R.styleable.lineAppearance_rightMargin, 0).toFloat()
        line.enable = array.getBoolean(R.styleable.lineAppearance_enable, false)

        array.recycle()
        return line
    }

    private fun parseInputStyle(inputResId: Int, hint: String?, inputType: Int) {
        val array = context.obtainStyledAttributes(inputResId, R.styleable.inputTextAppearance)
        val hintColor = array.getColor(
            R.styleable.inputTextAppearance_hintColor,
            ContextCompat.getColor(context, R.color.color_d1d2)

        )
        val inputColor = array.getColor(
            R.styleable.inputTextAppearance_inputColor,
            ContextCompat.getColor(context, R.color.color_565)
        )
        val textSize = array.getDimensionPixelSize(
            R.styleable.inputTextAppearance_textSize,
            applyUnit(TypedValue.COMPLEX_UNIT_SP, 15f)
        )
        val maxInputLength = array.getInteger(R.styleable.inputTextAppearance_maxInputLength, 0)

        editText = EditText(context)
        editText.hint = hint
        editText.setHintTextColor(hintColor)
        editText.setTextColor(inputColor)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        editText.setBackgroundColor(Color.TRANSPARENT)

        if (maxInputLength > 0) {
            editText.filters = arrayOf(InputFilter.LengthFilter(maxInputLength))
        }

        /**
         * <enum name="text" value="0"></enum>
         * <enum name="password" value="1"></enum>
         * <enum name="number" value="2"></enum>
         */
        if (inputType == 0) {
            editText.inputType = InputType.TYPE_CLASS_TEXT
        } else if (inputType == 1) {
            editText.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or (InputType.TYPE_CLASS_TEXT)
        } else if (inputType == 2) {
            editText.inputType = InputType.TYPE_CLASS_NUMBER
        }

        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        layoutParams.weight = 1f
        editText.layoutParams = layoutParams
        editText.gravity = LEFT or CENTER

        addView(editText)
        array.recycle()

    }

    private fun parseTitleStyle(title: String?, titleResId: Int) {
        val array = context.obtainStyledAttributes(titleResId, R.styleable.titleTextAppearance)
        val titleColor = array.getColor(
            R.styleable.titleTextAppearance_titleColor,
            ContextCompat.getColor(context, R.color.color_565)
        )
        val titleSize = array.getDimensionPixelOffset(
            R.styleable.titleTextAppearance_titleSize,
            applyUnit(TypedValue.COMPLEX_UNIT_DIP, 15f)
        )
        val minWidth = array.getDimensionPixelOffset(R.styleable.titleTextAppearance_minWidth, 0)

        titleView = TextView(context)
        titleView.text = title
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize.toFloat())//sp---当做sp在转换一次
        titleView.setTextColor(titleColor)
        titleView.minWidth = minWidth
        titleView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        titleView.gravity = LEFT or CENTER

        addView(titleView)

        array.recycle()

    }

    private fun applyUnit(complexUnitDip: Int, dp: Float): Int {
        return TypedValue.applyDimension(complexUnitDip, dp, resources.displayMetrics).toInt()
    }


    inner class Line {
        var color = 0
        var height = 0f
        var leftMargin = 0f
        var rightMargin = 0f
        var enable: Boolean = false
    }

    fun getTitleView(): TextView {
        return titleView
    }

    fun getEditText(): EditText {
        return editText
    }
}