package org.devio.hi.ui.tab.bottom;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;

public class HiTabBottomInfo<Color> {

    public enum TabType {
        BITMAP, ICON
    }

    public Class<? extends Fragment> fragment;
    public String name;
    public Bitmap defaultBitmap;
    public Bitmap selectedBitmap;
    public String iconFont;
    public Color defaultColor;
    public Color tintColor;
    public TabType tabType;

    /**
     * Tips：在Java代码中直接设置iconfont字符串无效，需要定义在string.xml
     */
    public String defaultIconName;
    public String selectedIconName;

    public HiTabBottomInfo(String name, Bitmap defaultBitmap, Bitmap selectedBitmap) {
        this.name = name;
        this.defaultBitmap = defaultBitmap;
        this.selectedBitmap = selectedBitmap;
        this.tabType = TabType.BITMAP;
    }

    public HiTabBottomInfo(String name, String iconFont, Color defaultColor, Color tintColor, String defaultIconName, String selectedIconName) {
        this.name = name;
        this.iconFont = iconFont;
        this.defaultColor = defaultColor;
        this.tintColor = tintColor;
        this.defaultIconName = defaultIconName;
        this.selectedIconName = selectedIconName;
        this.tabType = TabType.ICON;
    }
}
