package org.devio.hi.ui.banner.core;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import org.devio.hi.ui.banner.core.HiBannerAdapter;
import org.devio.hi.ui.banner.core.HiBannerMo;
import org.devio.hi.ui.banner.core.IBindAdapter;
import org.devio.hi.ui.banner.indicator.HiIndicator;

import java.util.List;

public interface IHiBanner {

 void setBannerDatas(@LayoutRes int layoutResId, @NonNull List<? extends HiBannerMo> models);

 void setBannerDatas( @NonNull List<? extends HiBannerMo> models);

 void setAutoPlay(boolean autoPlay);

 void setLoop(boolean loop);

 void setIntervalTime(int intervalTime);

 void setBindAdapter(IBindAdapter adapter);

 void setBannerClickListener(OnBannerClickListener listener);

 void setHiIndicator(HiIndicator indicator);

 void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

 void setScrollDuration(int duration);

 interface OnBannerClickListener{
 void onBannerClick(@NonNull HiBannerAdapter.HiBannerViewHolder viewHolder, @NonNull HiBannerMo bannerMo, int position);
 }

}
