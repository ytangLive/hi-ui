package org.devio.hi.ui.icfont

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class IconFontButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet?,
    defStyle: Int = 0
) :
    AppCompatButton(context, attributeSet, defStyle) {

    init {
        val typeFace = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
        typeface = typeFace
    }
}