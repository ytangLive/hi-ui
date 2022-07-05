package org.devio.hi.ui.app.demo.tab

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.devio.hi.ui.app.R
import org.devio.hi.ui.tab.top.HiTabLayoutTop
import org.devio.hi.ui.tab.top.HiTabTopInfo
import java.util.ArrayList

class HiTabTopDemoActivity : AppCompatActivity() {

    var tabsStr = arrayOf(
        "热门",
        "服装",
        "数码",
        "鞋子",
        "零食",
        "家电",
        "汽车",
        "百货",
        "家居",
        "装修",
        "运动"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_tab_top_demo);
        initTabTop()
    }

    private fun initTabTop() {
        val hiTabTopLayout = findViewById<HiTabLayoutTop>(R.id.tab_top_layout)
        val infoList: MutableList<HiTabTopInfo<*>> = ArrayList()
        val defaultColor = resources.getColor(R.color.tabBottomDefaultColor)
        val tintColor = resources.getColor(R.color.tabBottomTintColor)
        for (s in tabsStr) {
            val info: HiTabTopInfo<Int> = HiTabTopInfo(s, defaultColor, tintColor)
            infoList.add(info)
        }
        hiTabTopLayout.inflateInfo(infoList);
        hiTabTopLayout.addTabSelectedListener { index, _:HiTabTopInfo<*>?, nextInfo ->
            Toast.makeText(this, nextInfo.name, Toast.LENGTH_SHORT)
        }
        hiTabTopLayout.defaultSelect(infoList.get(0));

    }
}