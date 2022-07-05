package org.devio.hi.library.util;

import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class HiViewUtil {
    /**
     * 获取指定类型的子View
     *
     * @param group viewGroup
     * @param cls   如：RecyclerView.class
     * @param <T>
     * @return 指定类型的View
     */
    public static <T> T findTypeView(@NotNull  ViewGroup group, Class<T> cls){
        Deque<View> deque = new ArrayDeque<>();
        deque.add(group);
        while (!deque.isEmpty()){
            View view = deque.removeFirst();
            if(cls.isInstance(view)){
                return cls.cast(view);
            }else if(view instanceof ViewGroup){
                ViewGroup container = (ViewGroup)view;
                for (int i = 0, count = container.getChildCount(); i < count; i++){
                    View child = container.getChildAt(i);
                    deque.add(child);
                }
            }
        }

        return null;
    }
}
