package org.devio.hi.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.devio.hi.ui.R;

public class HiTextOverView extends HiOverView {

    ImageView imageView;
    TextView textView;

    public HiTextOverView(@NonNull Context context) {
        super(context);
    }

    public HiTextOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HiTextOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.hi_refresh_overview, this, true);
        imageView = findViewById(R.id.iv_rotate);
        textView = findViewById(R.id.text);
    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {
        textView.setText("下拉刷新");
    }

    @Override
    protected void onOver() {
        textView.setText("松开刷新");
    }

    @Override
    protected void onRefresh() {
        textView.setText("正在刷新...");
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        animation.setInterpolator(linearInterpolator);
        imageView.startAnimation(animation);
    }

    @Override
    protected void onFinish() {
        imageView.clearAnimation();
    }
}
