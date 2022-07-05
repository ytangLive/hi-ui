package org.devio.hi.ui.tab.bottom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.devio.hi.ui.R;
import org.devio.hi.ui.tab.common.IHiTab;

public class HiTabBottom extends RelativeLayout implements IHiTab<HiTabBottomInfo<?>> {

 private HiTabBottomInfo tabInfo;
 private ImageView bitMapView;
 private TextView iconView;
 private TextView nameView;

 public HiTabBottom(Context context) {
 this(context,null);
 }

 public HiTabBottom(Context context, AttributeSet attrs) {
 this(context, attrs, 0);
 }

 public HiTabBottom(Context context, AttributeSet attrs, int defStyleAttr) {
 this(context, attrs, defStyleAttr, 0);
 }

 public HiTabBottom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
 super(context, attrs, defStyleAttr, defStyleRes);
 init();
 }

 public HiTabBottomInfo getTabInfo() {
 return tabInfo;
 }

 public ImageView getBitMapView() {
 return bitMapView;
 }

 public TextView getIconView() {
 return iconView;
 }

 public TextView getNameView() {
 return nameView;
 }

 private void init(){
 LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_bottom, this);
 bitMapView = findViewById(R.id.iv_image);
 iconView = findViewById(R.id.tv_icon);
 nameView = findViewById(R.id.tv_name);
 }

 @Override
 public void setHiTabInfo(@NonNull HiTabBottomInfo tabInfo) {
 this.tabInfo = tabInfo;
 inflateTabInfo(true, false);
 }

 @Override
 public void resetHeight(int height) {
 ViewGroup.LayoutParams layoutParams = getLayoutParams();
 layoutParams.height = height;
 setLayoutParams(layoutParams);
 nameView.setVisibility(GONE);
 }

 @Override
 public void onTabSelectedChange(int index, @NonNull HiTabBottomInfo prevInfo, @NonNull HiTabBottomInfo nextInfo) {
 if(tabInfo != prevInfo && tabInfo != nextInfo || prevInfo == nextInfo){
 return;
 }
 if(tabInfo == prevInfo){
 inflateTabInfo(false, false);
 }else {
 inflateTabInfo(false, true);
 }

 }

 private void inflateTabInfo(boolean isInit, boolean selected){
 if(this.tabInfo.tabType == HiTabBottomInfo.TabType.ICON){
 if(isInit){
 bitMapView.setVisibility(GONE);
 iconView.setVisibility(VISIBLE);
 Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), tabInfo.iconFont);
 iconView.setTypeface(typeface);
 if(!TextUtils.isEmpty(tabInfo.name)){
 nameView.setText(tabInfo.name);
 }
 }
 if(selected){
 iconView.setText(TextUtils.isEmpty(tabInfo.selectedIconName) ? tabInfo.defaultIconName : tabInfo.selectedIconName);
 iconView.setTextColor(getTextColor(tabInfo.tintColor));
 nameView.setTextColor(getTextColor(tabInfo.tintColor));
 }else{
 iconView.setText(tabInfo.defaultIconName);
 iconView.setTextColor(getTextColor(tabInfo.defaultColor));
 nameView.setTextColor(getTextColor(tabInfo.defaultColor));
 }

 }else{
 if(isInit){
 bitMapView.setVisibility(VISIBLE);
 iconView.setVisibility(GONE);
 if(!TextUtils.isEmpty(tabInfo.name)){
 nameView.setText(tabInfo.name);
 }
 }
 if(selected){
 bitMapView.setImageBitmap(tabInfo.selectedBitmap);
 }else{
 bitMapView.setImageBitmap(tabInfo.defaultBitmap);
 }
 }
 }

 private int getTextColor(Object color){
 if(color instanceof String){
 return Color.parseColor((String)color);
 }else {
 return (int) color;
 }
 }

}
