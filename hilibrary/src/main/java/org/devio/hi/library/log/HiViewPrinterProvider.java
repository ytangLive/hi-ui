package org.devio.hi.library.log;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.devio.hi.library.util.HiDisplayUtil;

public class HiViewPrinterProvider {
    private FrameLayout rootView;
    private View floatingView;
    private boolean isOpen;
    private FrameLayout logView;
    private RecyclerView recyclerView;

    public HiViewPrinterProvider(FrameLayout rootView, RecyclerView recyclerView) {
        this.rootView = rootView;
        this.recyclerView = recyclerView;
    }

    private static final String TAG_FLOATING_VIEW = "TAG_FLOATING_VIEW";
    private static final String TAG_LOG_VIEW = "TAG_LOG_VIEW";

    public void showFloatingView(){
        if(rootView.findViewWithTag(TAG_FLOATING_VIEW) != null){
            return;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END|Gravity.BOTTOM;

        floatingView = genFloatingView();
        floatingView.setBackgroundColor(Color.BLACK);
        floatingView.setAlpha(0.8f);
        params.bottomMargin = HiDisplayUtil.dp2px(100, rootView.getResources());
        floatingView.setLayoutParams(params);
        floatingView.setTag(TAG_FLOATING_VIEW);

        rootView.addView(floatingView);
    }

    private View genFloatingView() {
        if(floatingView !=null){
            return floatingView;
        }
        TextView textView = new TextView(rootView.getContext());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpen){
                    showLogView();
                    isOpen = true;
                }
            }
        });

        textView.setText("HIKog");
        textView.setTextColor(Color.WHITE);
        return floatingView = textView;

    }

    private void showLogView() {
        if(rootView.findViewWithTag(TAG_LOG_VIEW) != null){
            return;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, HiDisplayUtil.dp2px(160, rootView.getResources()));
        params.gravity = Gravity.BOTTOM;

        logView = genLogView();
        logView.setTag(TAG_LOG_VIEW);

        rootView.addView(logView,params);

    }

    private FrameLayout genLogView(){
        if(logView != null){
            return logView;
        }

        FrameLayout logView = new FrameLayout(rootView.getContext());
        logView.setBackgroundColor(Color.BLACK);
        logView.addView(recyclerView);

        TextView closeView = new TextView(logView.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.END;
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeLogView();
            }
        });
        closeView.setText("Close");
        closeView.setTextColor(Color.WHITE);

        logView.addView(closeView, params);

        return this.logView = logView;
    }

    private void closeLogView(){
        if(logView != null){
            isOpen = false;
            rootView.removeView(logView);
        }
    }
}
