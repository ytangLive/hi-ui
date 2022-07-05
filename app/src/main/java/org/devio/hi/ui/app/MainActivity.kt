package org.devio.hi.ui.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.devio.hi.ui.app.demo.banner.HiBannerDemoActivity
import org.devio.hi.ui.app.demo.refresh.HiRefreshDemoActivity
import org.devio.hi.ui.app.demo.tab.HiTabBottomDemoActivity
import org.devio.hi.ui.app.demo.tab.HiTabTopDemoActivity
import org.devio.hi.ui.tab.bottom.HiTabBottom
import org.devio.hi.ui.tab.bottom.HiTabBottomInfo

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val tabBottom = findViewById<HiTabBottom>(R.id.tab_bottom);
//
//        val homeInfo = HiTabBottomInfo(
//            "首页",
//            "fonts/iconfont.ttf",
//            "#ff656667",
//            "#ffd44949",
//            getString(R.string.if_home),
//            null
//        );
//        homeInfo.tabType = HiTabBottomInfo.TabType.ICON;
//        tabBottom.setHiTabInfo(homeInfo);
        

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tv_tab_bottom -> {
                 startActivity(Intent(this,HiTabBottomDemoActivity::class.java))
            }
            R.id.tv_hi_refresh-> {
                startActivity(Intent(this, HiRefreshDemoActivity::class.java))
            }
            R.id.tv_hi_top-> {
                startActivity(Intent(this, HiTabTopDemoActivity::class.java))
            }
            R.id.tv_hi_banner-> {
                startActivity(Intent(this, HiBannerDemoActivity::class.java))
            }
        }
    }
}