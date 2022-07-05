package org.devio.hi.ui.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import org.devio.hi.ui.R;
import org.devio.hi.ui.banner.core.HiBannerDelegate;
import org.devio.hi.ui.banner.core.HiBannerMo;
import org.devio.hi.ui.banner.core.IBindAdapter;
import org.devio.hi.ui.banner.core.IHiBanner;
import org.devio.hi.ui.banner.indicator.HiIndicator;

import java.util.List;

public class HiBanner extends FrameLayout implements IHiBanner {

 private HiBannerDelegate mBannerDelegate;

 public HiBanner(@NonNull Context context) {
 this(context, null);
 }

 public HiBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
 this(context, attrs, 0);
 }

 public HiBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
 super(context, attrs, defStyleAttr);
 mBannerDelegate = new HiBannerDelegate(context, this);
 initCustomAttrs(context, attrs);
 }

 private void initCustomAttrs(@NonNull Context context, @Nullable AttributeSet attrs){
 TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HiBanner);
 boolean autoPlay = typedArray.getBoolean(R.styleable.HiBanner_autoPlay, true);
 boolean loop = typedArray.getBoolean(R.styleable.HiBanner_loop, false);
 int intervalTime = typedArray.getInteger(R.styleable.HiBanner_intervalTime, -1);
 setAutoPlay(autoPlay);
 setLoop(loop);
 setIntervalTime(intervalTime);
 }

 @Override
 public void setBannerDatas(int layoutResId, @NonNull List<? extends HiBannerMo> models) {
 mBannerDelegate.setBannerDatas(layoutResId, models);
 }

 @Override
 public void setBannerDatas(@NonNull List<? extends HiBannerMo> models) {
 mBannerDelegate.setBannerDatas(models);
 }

 @Override
 public void setHiIndicator(HiIndicator hiIndicator) {
 mBannerDelegate.setHiIndicator(hiIndicator);
 }

 @Override
 public void setAutoPlay(boolean autoPlay) {
 mBannerDelegate.setAutoPlay(autoPlay);
 }

 @Override
 public void setLoop(boolean loop) {
 mBannerDelegate.setLoop(loop);
 }

 @Override
 public void setIntervalTime(int intervalTime) {
 mBannerDelegate.setIntervalTime(intervalTime);
 }

 @Override
 public void setBindAdapter(IBindAdapter bindAdapter) {
 mBannerDelegate.setBindAdapter(bindAdapter);
 }

 @Override
 public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
 mBannerDelegate.setOnPageChangeListener(onPageChangeListener);
 }

 @Override
 public void setScrollDuration(int duration) {
 mBannerDelegate.setScrollDuration(duration);
 }

 @Override
 public void setBannerClickListener(OnBannerClickListener listener){
 mBannerDelegate.setBannerClickListener(listener);
 }
}
