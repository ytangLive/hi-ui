package org.devio.hi.ui.tab.top;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.devio.hi.ui.R;
import org.devio.hi.ui.tab.common.IHiTab;

public class HiTabTop extends LinearLayout implements IHiTab<HiTabTopInfo<?>> {
    private HiTabTopInfo tabInfo;
    private ImageView bitMapView;
    private TextView nameView;
    private View indicatorView;

    public HiTabTop(Context context) {
        this(context, null);
    }

    public HiTabTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HiTabTopInfo getTabInfo() {
        return tabInfo;
    }

    public ImageView getBitMapView() {
        return bitMapView;
    }

    public TextView getNameView() {
        return nameView;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_tab_top, this);
        bitMapView = findViewById(R.id.iv_image);
        nameView = findViewById(R.id.tv_name);
        indicatorView = findViewById(R.id.tab_top_indicator);
    }

    @Override
    public void setHiTabInfo(@NonNull HiTabTopInfo<?> tabInfo) {
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
    public void onTabSelectedChange(int index, @NonNull HiTabTopInfo<?> prevInfo, @NonNull HiTabTopInfo<?> nextInfo) {
        if (tabInfo != prevInfo && tabInfo != nextInfo || prevInfo == nextInfo) {
            return;
        }
        if (tabInfo == prevInfo) {
            inflateTabInfo(false, false);
        } else {
            inflateTabInfo(false, true);
        }

    }

    private void inflateTabInfo(boolean isInit, boolean selected) {
        if (this.tabInfo.tabType == HiTabTopInfo.TabType.TEXT) {
            if (isInit) {
                bitMapView.setVisibility(GONE);
                nameView.setVisibility(VISIBLE);
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    nameView.setText(tabInfo.name);
                }
            }
            if (selected) {
                nameView.setTextColor(getTextColor(tabInfo.tintColor));
                indicatorView.setVisibility(VISIBLE);
            } else {
                nameView.setTextColor(getTextColor(tabInfo.defaultColor));
                indicatorView.setVisibility(GONE);
            }

        } else {
            if (isInit) {
                bitMapView.setVisibility(VISIBLE);
                nameView.setVisibility(GONE);
            }
            if (selected) {
                bitMapView.setImageBitmap(tabInfo.selectedBitmap);
                indicatorView.setVisibility(VISIBLE);
            } else {
                bitMapView.setImageBitmap(tabInfo.defaultBitmap);
                indicatorView.setVisibility(GONE);
            }
        }
    }

    private int getTextColor(Object color) {
        if (color instanceof String) {
            return Color.parseColor((String) color);
        } else {
            return (int) color;
        }
    }
}

