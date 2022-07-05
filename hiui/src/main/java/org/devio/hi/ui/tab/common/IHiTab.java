package org.devio.hi.ui.tab.common;

import androidx.annotation.Px;

import org.jetbrains.annotations.NotNull;

public interface IHiTab<D> extends IHiTabLayout.OnTabSelectedListener<D> {

 void setHiTabInfo(@NotNull D tabInfo);

 void resetHeight(@Px int height);

}