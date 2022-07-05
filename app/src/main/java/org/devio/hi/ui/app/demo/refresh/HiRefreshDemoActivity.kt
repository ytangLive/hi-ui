package org.devio.hi.ui.app.demo.refresh

import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import org.devio.hi.ui.app.R
import org.devio.hi.ui.refresh.HiRefresh
import org.devio.hi.ui.refresh.HiRefreshLayout
import org.devio.hi.ui.refresh.HiTextOverView

class HiRefreshDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_refresh)
        val refreshLayout = findViewById<HiRefreshLayout>(R.id.refresh_layout)
        val textOverView = HiTextOverView(this);
        refreshLayout.setOverView(textOverView)
        refreshLayout.setRefreshListener(object : HiRefresh.HiRefreshListener{
            override fun onRefresh() {
                Handler().postDelayed({refreshLayout.refreshFinished()},300)
            }

            override fun enableRefresh(): Boolean {
                return true
            }
        })
        refreshLayout.setDisableRefreshScroll(false)
    }
}