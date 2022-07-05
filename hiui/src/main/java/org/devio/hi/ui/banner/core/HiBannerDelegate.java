package org.devio.hi.ui.banner.core;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import org.devio.hi.ui.R;
import org.devio.hi.ui.banner.HiBanner;
import org.devio.hi.ui.banner.indicator.HiCircleIndicator;
import org.devio.hi.ui.banner.indicator.HiIndicator;

import java.util.List;

public class HiBannerDelegate implements IHiBanner, ViewPager.OnPageChangeListener {

 private Context mContext;
 private HiBanner mBanner;
 private List<? extends HiBannerMo> bannerMos;

 private HiIndicator mIndicator;
 private HiBannerAdapter mBannerAdapter;
 private HiViewPager mViewPage;
 private ViewPager.OnPageChangeListener mOnPageChangeListener;
 private OnBannerClickListener bannerClickListener;
 private boolean mAutoPlay;
 private boolean mLoop;
 private int mIntervalTime = 5000;
 private int mScrollDuration = -1;


 public HiBannerDelegate(Context mContext, HiBanner mBanner) {
 this.mContext = mContext;
 this.mBanner = mBanner;
 }

 private void init(@LayoutRes int layoutResId){
 if(mIndicator == null){
 mIndicator = new HiCircleIndicator(mContext);
 }
 if(mBannerAdapter == null){
 mBannerAdapter = new HiBannerAdapter(mContext);
 }

 mViewPage = new HiViewPager(mContext);
 mBannerAdapter.setLayoutResId(layoutResId);
 mBannerAdapter.setLoop(mLoop);
 mBannerAdapter.setAutoPlay(mAutoPlay);
 mBannerAdapter.setOnBannerClickListener(bannerClickListener);
 mBannerAdapter.setBannerDatas(bannerMos);

 mViewPage.setAdapter(mBannerAdapter);
 mViewPage.setScrollDuration(mScrollDuration);
 mViewPage.setIntervalTime(mIntervalTime);
 mViewPage.setAutoPlay(mAutoPlay);
 mViewPage.addOnPageChangeListener(this);
 mIndicator.onInflate(mBannerAdapter.getRealCount());

 FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
 FrameLayout.LayoutParams.MATCH_PARENT);
 if(mLoop || mAutoPlay && mBannerAdapter.getRealCount() != 0){
 int firstItem = mBannerAdapter.getFirstItem();
 mViewPage.setCurrentItem(firstItem, false);
 }

 mBanner.removeAllViews();
 mBanner.addView(mViewPage, layoutParams);
 mBanner.addView(mIndicator.get(), layoutParams);
 }

 @Override
 public void setBannerDatas(int layoutResId, @NonNull List<? extends HiBannerMo> models) {
 bannerMos = models;
 init(layoutResId);
 }

 @Override
 public void setBannerDatas(@NonNull List<? extends HiBannerMo> models) {
 setBannerDatas(R.layout.hi_banner_item_image, models);

 }

 @Override
 public void setAutoPlay(boolean autoPlay) {
 mAutoPlay = autoPlay;
 if(mBannerAdapter != null){
 mBannerAdapter.setAutoPlay(autoPlay);
 }
 if(mViewPage != null){
 mViewPage.setAutoPlay(autoPlay);
 }
 }

 @Override
 public void setLoop(boolean loop) {
 mLoop = loop;
 }

 @Override
 public void setIntervalTime(int intervalTime) {
 if(intervalTime > 0){
 mIntervalTime = intervalTime;
 }
 }

 @Override
 public void setBindAdapter(IBindAdapter adapter) {
 if(mBannerAdapter != null){
 mBannerAdapter.setBindAdapter(adapter);
 }
 }

 @Override
 public void setBannerClickListener(OnBannerClickListener listener) {
 bannerClickListener = listener;
 mBannerAdapter.setOnBannerClickListener(bannerClickListener);
 }

 @Override
 public void setHiIndicator(HiIndicator indicator) {
 mIndicator = indicator;
 }

 @Override
 public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
 mOnPageChangeListener = listener;
 }

 @Override
 public void setScrollDuration(int duration) {
 if(mViewPage != null){
 mViewPage.setScrollDuration(duration);
 }
 }

 @Override
 public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
 if(mOnPageChangeListener != null && mBannerAdapter.getRealCount() != 0){
 mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
 }
 }

 @Override
 public void onPageSelected(int position) {
 if(mBannerAdapter.getRealCount() == 0){
 return;
 }
 int realPosition = position % mBannerAdapter.getRealCount();
 if(mOnPageChangeListener != null){
 mOnPageChangeListener.onPageSelected(position);
 }
 if(mIndicator != null){
 mIndicator.onPageChange(realPosition, mBannerAdapter.getRealCount());
 }
 }

 @Override
 public void onPageScrollStateChanged(int state) {
 if(mOnPageChangeListener != null){
 mOnPageChangeListener.onPageScrollStateChanged(state);
 }
 }
}
