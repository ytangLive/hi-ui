package org.devio.hi.ui.empty

import org.devio.hi.ui.R
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_empty_view.view.*

class EmptyView : LinearLayout {

    private var title: TextView
    private var icon: TextView
    private var desc: TextView
    private var button: MaterialButton

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true)

        icon = findViewById(R.id.empty_icon)
        title = findViewById(R.id.empty_title)
        desc = findViewById(R.id.empty_text)
        button = findViewById(R.id.empty_action)
    }

    fun setIcon(@StringRes iconRes: Int) {
        icon!!.setText(iconRes)
    }

    fun setTitle(text: String) {
        title.text = text
        title.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
    }

    fun setDesc(text: String) {
        desc.text = text
        desc.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
    }

    @JvmOverloads
    fun setHelpAction(@StringRes actionId: Int = R.string.if_detail, listener: OnClickListener) {
        empty_tips.setText(actionId)
        empty_tips.setOnClickListener(listener)
        empty_tips.visibility = if (actionId == -1) View.GONE else View.VISIBLE
    }

    fun setButton(text: String, listener: OnClickListener) {
        if (TextUtils.isEmpty(text)) {
            button!!.visibility = View.GONE
        } else {
            button!!.visibility = View.VISIBLE
            button!!.text = text
            button!!.setOnClickListener(listener)
        }
    }

}