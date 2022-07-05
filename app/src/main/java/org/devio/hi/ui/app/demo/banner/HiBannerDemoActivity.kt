package org.devio.hi.ui.app.demo.banner

import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.devio.hi.ui.app.R
import org.devio.hi.ui.banner.HiBanner
import org.devio.hi.ui.banner.indicator.HiCircleIndicator
import org.devio.hi.ui.banner.indicator.HiIndicator
import org.devio.hi.ui.banner.indicator.HiNumIndicator

class HiBannerDemoActivity : AppCompatActivity() {
    private var hiIndicator:HiIndicator<*>? = null;

    private var urls = arrayOf(
        "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
        "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_banner_demo)

        var circleIndicator = HiCircleIndicator(this)
        initView(circleIndicator, false)

        findViewById<View>(R.id.tv_switch).setOnClickListener {
            //            mHiBanner.setAutoPlay(false)
            if (hiIndicator is HiCircleIndicator) {
                initView(HiNumIndicator(this), false)
            } else {
                initView(HiCircleIndicator(this), false)
            }
        }
        val switch = findViewById<Switch>(R.id.auto_play)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            initView(hiIndicator, isChecked)
        }
    }

    private fun initView(indicator :HiIndicator<*>?, autoPlay: Boolean){
        this.hiIndicator = indicator
        val banner = findViewById<HiBanner>(R.id.banner)
        val models:MutableList<BannerMo> = ArrayList()
        for (i in urls.indices){
            val model = BannerMo()
            model.url = urls[i];
            models.add(i, model)
        }
        banner.setIntervalTime(2000)
        banner.setAutoPlay(autoPlay)
        banner.setHiIndicator(indicator)
        banner.setBannerDatas(R.layout.banner_item_layout, models)
        banner.setLoop(true)
        banner.setBindAdapter { viewHolder, model, _ ->
            val imageView = viewHolder.findViewById<ImageView>(R.id.iv_image)
            val textView = viewHolder.findViewById<TextView>(R.id.tv_title)
            Glide.with(this@HiBannerDemoActivity).load(model.url).into(imageView)
            textView.text = model.url
        }
    }
}