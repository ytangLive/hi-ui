package org.devio.hi.ui.tab.common;

import android.view.ViewGroup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IHiTabLayout <Tab extends ViewGroup, D> {

 Tab findTab(@NotNull D data);

 void addTabSelectedListener(OnTabSelectedListener<D> listener);

 void defaultSelect(@NotNull D defaultInfo);

 void inflateInfo(@NotNull List<D> infoList);

 interface OnTabSelectedListener<D> {
 void onTabSelectedChange(int index, @NotNull D prevInfo, @NotNull D nextInfo);
 }

}
