package org.devio.hi.ui.banner.indicator;

import android.view.View;

public interface HiIndicator<T extends View> {
 T get();

 void onInflate(int count);

 void onPageChange(int current, int count);
}
