package org.devio.hi.ui.app.demo.tab

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.app.R
import org.devio.hi.ui.tab.bottom.HiTabBottom
import org.devio.hi.ui.tab.bottom.HiTabBottomInfo
import org.devio.hi.ui.tab.bottom.HiTabLayoutBottom
import java.util.ArrayList

class HiTabBottomDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_tab_bottom_demo);
        initTabBottom()
    }

    private fun initTabBottom() {
        val tabLayoutBottom = findViewById<HiTabLayoutBottom>(R.id.hitablayout)
        tabLayoutBottom.setBottomAlpha(0.8f);
        val bottomInfoList: MutableList<HiTabBottomInfo<*>> = ArrayList()
        val homeInfo = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            "#ff656667",
            "#ffd44949",
            getString(R.string.if_home),
            null
        )
        val infoRecommend = HiTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            "#ff656667",
            "#ffd44949",
            getString(R.string.if_favorite),
            null
        )

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire);
        val infoCustom = HiTabBottomInfo<String>(
            "自定义图标",
            bitmap,
            bitmap
        )

        val infoChat = HiTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            "#ff656667",
            "#ffd44949",
            getString(R.string.if_recommend),
            null
        )
        val infoProfile = HiTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            "#ff656667",
            "#ffd44949",
            getString(R.string.if_profile),
            null
        )
        bottomInfoList.add(homeInfo)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCustom);
        bottomInfoList.add(infoChat)
        bottomInfoList.add(infoProfile)
        tabLayoutBottom.inflateInfo(bottomInfoList)

        tabLayoutBottom.defaultSelect(homeInfo);
        tabLayoutBottom.addTabSelectedListener { index, prevInfo, nextInfo ->
            Toast.makeText(this,nextInfo.name,Toast.LENGTH_SHORT).show()
        }

        val tabBottom:HiTabBottom = tabLayoutBottom.findTab(infoCustom);
        tabBottom.apply {
            resetHeight((HiDisplayUtil.dp2px(66f, resources)))
        }
    }
}