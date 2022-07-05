package org.devio.hi.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.devio.hi.library.util.HiDisplayUtil;
import org.devio.hi.library.util.HiViewUtil;
import org.devio.hi.ui.R;
import org.devio.hi.ui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HiTabLayoutBottom extends FrameLayout implements IHiTabLayout<HiTabBottom, HiTabBottomInfo<?>> {

 private List<OnTabSelectedListener<HiTabBottomInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
 private HiTabBottomInfo<?> selectedInfo;

 private float bottomAlpha = 1f;
 private static float tabBottomHeight = 50f;
 private float tabBottomLineHeight = 0.5f;
 private String bottomLineColor = "#dfe0e1";
 private List<HiTabBottomInfo<?>> infoList;

 private static final String TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM";

 public HiTabLayoutBottom(@NonNull Context context) {
 this(context,null);
 }

 public HiTabLayoutBottom(@NonNull Context context, @Nullable AttributeSet attrs) {
 this(context, attrs, 0);
 }

 public HiTabLayoutBottom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
 this(context, attrs, defStyleAttr, 0);
 }

 public HiTabLayoutBottom(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
 super(context, attrs, defStyleAttr, defStyleRes);
 }

 @Override
 public HiTabBottom findTab(@NonNull HiTabBottomInfo<?> data) {
 ViewGroup tabRoot = findViewWithTag(TAG_TAB_BOTTOM);
 for (int i =0; i < tabRoot.getChildCount(); i ++){
 View childView = tabRoot.getChildAt(i);
 if(childView instanceof HiTabBottom){
 HiTabBottom tab = (HiTabBottom) childView;
 if(tab.getTabInfo() == data){
 return tab;
 }
 }
 }
 return null;
 }

 @Override
 public void addTabSelectedListener(OnTabSelectedListener<HiTabBottomInfo<?>> listener) {
 this.tabSelectedChangeListeners.add(listener);
 }

 @Override
 public void defaultSelect(@NonNull HiTabBottomInfo<?> defaultInfo) {
 onSelected(defaultInfo);
 }

 @Override
 public void inflateInfo(@NonNull List<HiTabBottomInfo<?>> infoList) {
 if (infoList == null) {
 return;
 }
 this.infoList = infoList;

 int count = getChildCount();
 for (int i = count - 1; i > 0; i--){
 removeViewAt(i);
 }

 Iterator<OnTabSelectedListener<HiTabBottomInfo<?>>> iterable = tabSelectedChangeListeners.iterator();
 while (iterable.hasNext()){
 if(iterable.next() instanceof OnTabSelectedListener){
 iterable.remove();
 }
 }

 addBackgroundView();
 selectedInfo = null;
 FrameLayout fl = new FrameLayout(getContext());
 fl.setTag(TAG_TAB_BOTTOM);
 int width = HiDisplayUtil.getDisplayWidthInPx(getContext()) / infoList.size();
 int height = HiDisplayUtil.dp2px(tabBottomHeight, getResources());
 for (int i = 0; i < infoList.size(); i++){
 HiTabBottomInfo<?> tabBottomInfo = infoList.get(i);
 HiTabBottom tabBottom = new HiTabBottom(getContext());
 tabBottom.setHiTabInfo(tabBottomInfo);
 tabBottom.setOnClickListener(new OnClickListener() {
 @Override
 public void onClick(View v) {
 onSelected(tabBottomInfo);
 }
 });
 tabSelectedChangeListeners.add(tabBottom);

 LayoutParams params = new LayoutParams(width, height);
 params.gravity = Gravity.BOTTOM;
 params.leftMargin = i * width;

 fl.addView(tabBottom, params);
 }

 LayoutParams flParams = new LayoutParams
 (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
 flParams.gravity = Gravity.BOTTOM;
 addBottomLine();
 addView(fl, flParams);

 fixContentView();
 }

 private void onSelected(HiTabBottomInfo<?> nextInfo) {
 for (OnTabSelectedListener<HiTabBottomInfo<?>>onTabSelectedListener : tabSelectedChangeListeners){
 onTabSelectedListener.onTabSelectedChange(infoList.indexOf(nextInfo),selectedInfo,nextInfo);
 }
 selectedInfo = nextInfo;
 }

 private void addBottomLine() {
 View bottomLine = new View(getContext());
 bottomLine.setBackgroundColor(Color.parseColor(bottomLineColor));

 LayoutParams bottomLineParams =
 new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(tabBottomLineHeight, getResources()));
 bottomLineParams.gravity = Gravity.BOTTOM;
 bottomLineParams.bottomMargin = HiDisplayUtil.dp2px(tabBottomHeight - tabBottomLineHeight, getResources());
 addView(bottomLine, bottomLineParams);
 bottomLine.setAlpha(bottomAlpha);
 }

 private void addBackgroundView() {
 View view = LayoutInflater.from(getContext()).inflate(R.layout.hi_bottom_layout_bg,null);
 view.setBackgroundColor(Color.WHITE);
 LayoutParams params =
 new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(tabBottomHeight, getResources()));
 params.gravity = Gravity.BOTTOM;
 addView(view, params);
 view.setAlpha(bottomAlpha);
 }

 public void setBottomAlpha(float bottomAlpha) {
 this.bottomAlpha = bottomAlpha;
 }

 /**
  * 修复内容区域的底部Padding
  */
 private void fixContentView() {
 if(!(getChildAt(0) instanceof ViewGroup)){
 return;
 }

 ViewGroup rootView = (ViewGroup) getChildAt(0);
 ViewGroup targetView = HiViewUtil.findTypeView(rootView, RecyclerView.class);
 if(targetView == null){
 targetView = HiViewUtil.findTypeView(rootView, ScrollView.class);
 }
 if(targetView == null){
 targetView = HiViewUtil.findTypeView(rootView, AbsListView.class);
 }
 if(targetView != null){
 targetView.setPadding(0,0,0,HiDisplayUtil.dp2px(tabBottomHeight ,getResources()));
 targetView.setClipToPadding(false);
 }
 }

 public static void clipBottomPadding(ViewGroup targetView){
 if(targetView != null){
 targetView.setPadding(0,0,0,HiDisplayUtil.dp2px(tabBottomHeight));
 targetView.setClipToPadding(false);
 }
 }
}
