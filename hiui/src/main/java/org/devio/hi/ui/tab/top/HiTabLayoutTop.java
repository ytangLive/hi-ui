package org.devio.hi.ui.tab.top;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import org.devio.hi.library.util.HiDisplayUtil;
import org.devio.hi.ui.tab.common.IHiTabLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HiTabLayoutTop extends HorizontalScrollView implements IHiTabLayout<HiTabTop, HiTabTopInfo<?>> {

    private List<IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<?>>> tabSelectedChangeListeners = new ArrayList<>();
    private HiTabTopInfo<?> selectedInfo;
    private List<HiTabTopInfo<?>> infoList;

    public HiTabLayoutTop(Context context) {
        this(context, null);
    }

    public HiTabLayoutTop(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HiTabLayoutTop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void onSelected(@NonNull HiTabTopInfo<?> nextInfo) {
        for (OnTabSelectedListener<HiTabTopInfo<?>> listener : tabSelectedChangeListeners) {
            listener.onTabSelectedChange(infoList.indexOf(nextInfo), selectedInfo, nextInfo);
        }
        this.selectedInfo = nextInfo;

        scrollTo(nextInfo);
    }

    int tabWidth = 0;

    private void scrollTo(HiTabTopInfo<?> topInfo) {
        HiTabTop tabTop = findTab(topInfo);
        int scrollWidth = 0;
        if (tabTop != null) {
            tabWidth = tabTop.getWidth();
            int tabIndex = infoList.indexOf(topInfo);
            int[] loc = new int[2];
            tabTop.getLocationInWindow(loc);
            if ((loc[0] + tabWidth / 2) < HiDisplayUtil.getDisplayWidthInPx(getContext()) / 2) {
                scrollWidth = rangeScrollWidth(tabIndex, -1);
            } else {
                scrollWidth = rangeScrollWidth(tabIndex, 1);
            }
        }
        scrollTo(getScrollX() + scrollWidth, 0);
    }

    private int rangeScrollWidth(int index, int range) {
        int scrollWidth = 0;
        for (int i = 0; i < Math.abs(range); i++) {
            int next;
            if (range < 0) {
                next = index + i + range;
            } else {
                next = range - i + index;
            }
            if (next >= 0 && next < infoList.size()) {
                if (range < 0) {
                    scrollWidth -= scrollWidth(next, false);
                } else {
                    scrollWidth += scrollWidth(next, true);
                }
            }
        }
        return scrollWidth;
    }

    private int scrollWidth(int index, boolean toRight) {
        HiTabTop target = findTab(infoList.get(index));
        if (target == null) {
            return 0;
        }
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (toRight) {
            if (rect.right > tabWidth) {
                return tabWidth;
            } else {
                return tabWidth - rect.right;
            }
        } else {
            if (rect.left <= -tabWidth) {
                return tabWidth;
            } else if (rect.left > 0) {
                return rect.left;
            }
        }
        return 0;
    }

    @Override
    public HiTabTop findTab(@NonNull HiTabTopInfo<?> data) {
        ViewGroup tabRoot = getRootLayout(false);
        for (int i = 0; i < tabRoot.getChildCount(); i++) {
            View childView = tabRoot.getChildAt(i);
            if (childView instanceof HiTabTop) {
                HiTabTop tab = (HiTabTop) childView;
                if (tab.getTabInfo() == data) {
                    return tab;
                }
            }
        }
        return null;
    }

    @Override
    public void addTabSelectedListener(OnTabSelectedListener<HiTabTopInfo<?>> listener) {
        tabSelectedChangeListeners.add(listener);
    }

    @Override
    public void defaultSelect(@NonNull HiTabTopInfo<?> defaultInfo) {
        onSelected(defaultInfo);
    }

    @Override
    public void inflateInfo(@NonNull List<HiTabTopInfo<?>> infoList) {
        if (infoList == null) {
            return;
        }
        this.infoList = infoList;

        Iterator<OnTabSelectedListener<HiTabTopInfo<?>>> iterable = tabSelectedChangeListeners.iterator();
        while (iterable.hasNext()) {
            if (iterable.next() instanceof OnTabSelectedListener) {
                iterable.remove();
            }
        }

        LinearLayout rootLayout = getRootLayout(true);
        for (int i = 0; i < infoList.size(); i++) {
            HiTabTopInfo<?> info = infoList.get(i);
            HiTabTop tabTop = new HiTabTop(getContext());
            tabTop.setHiTabInfo(info);
            tabTop.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelected(info);
                }
            });
            tabSelectedChangeListeners.add(tabTop);
            rootLayout.addView(tabTop);
        }
    }

    private LinearLayout getRootLayout(boolean clear) {
        LinearLayout rootLayout = (LinearLayout) getChildAt(0);
        if (rootLayout == null) {
            rootLayout = new LinearLayout(getContext());
            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(rootLayout, params);
        }
        if (clear) {
            rootLayout.removeAllViews();
        }
        return rootLayout;
    }
}
