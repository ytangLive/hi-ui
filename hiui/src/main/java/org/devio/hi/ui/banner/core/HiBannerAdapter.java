package org.devio.hi.ui.banner.core;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class HiBannerAdapter extends PagerAdapter {

    private Context mContext;
    private SparseArray<HiBannerViewHolder> mCachedViews;
    private List<? extends HiBannerMo> models;
    private IBindAdapter bindAdapter;
    private IHiBanner.OnBannerClickListener onBannerClickListener;

    private boolean mAutoPlay;
    private boolean mLoop;
    private int mLayoutResId = -1;

    public HiBannerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    void setBannerDatas(@NonNull List<? extends HiBannerMo> models) {
        this.models = models;
        initCachedView();
        notifyDataSetChanged();
    }

    public void initCachedView() {
        mCachedViews = new SparseArray<>();
        for (int i = 0; i < models.size(); i++) {
            HiBannerViewHolder viewHolder = new HiBannerViewHolder(createView(null));
            mCachedViews.put(i, viewHolder);
        }
    }

    private View createView(ViewGroup parent) {
        if (mLayoutResId == -1) {
            throw new IllegalArgumentException("you must be set setLayoutResId first");
        }
        return LayoutInflater.from(mContext).inflate(mLayoutResId, parent, false);
    }

    @Override
    public int getCount() {
        return mAutoPlay ? Integer.MAX_VALUE : (mLoop ? Integer.MAX_VALUE : getRealCount());
    }

    public int getFirstItem() {
        return Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % getRealCount();
    }

    public int getRealCount() {
        return models != null ? models.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setAutoPlay(boolean mAutoPlay) {
        this.mAutoPlay = mAutoPlay;
    }

    public void setLoop(boolean mLoop) {
        this.mLoop = mLoop;
    }

    public void setBindAdapter(IBindAdapter bindAdapter) {
        this.bindAdapter = bindAdapter;
    }

    public void setLayoutResId(@LayoutRes int mLayoutResId) {
        this.mLayoutResId = mLayoutResId;
    }

    public void setOnBannerClickListener(IHiBanner.OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //让item每次都会刷新
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) {
            realPosition = position % getRealCount();
        }

        HiBannerViewHolder viewHolder = mCachedViews.get(realPosition);
        HiBannerMo bannerMo = models.get(realPosition);
        if (viewHolder != null) {
            if (container.equals(viewHolder.rootView.getParent())) {
                container.removeView(viewHolder.rootView);
            }
            if (viewHolder.rootView.getParent() != null) {
                ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
            }
            onBind(viewHolder, bannerMo, realPosition);

            container.addView(viewHolder.rootView);

            return viewHolder.rootView;
        }

        return super.instantiateItem(container, position);
    }

    protected void onBind(@NonNull final HiBannerViewHolder viewHolder, @NonNull final HiBannerMo model, final int position) {
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerClickListener != null) {
                    onBannerClickListener.onBannerClick(viewHolder, model, position);
                }
            }
        });

        if (bindAdapter != null) {
            bindAdapter.onBind(viewHolder, model, position);
        }
    }

    public static class HiBannerViewHolder {
        private View rootView;
        private SparseArray<View> viewHolderSparseArr;

        public HiBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public View getRootView() {
            return rootView;
        }

        public <V extends View> V findViewById(@IdRes int id) {
            if (!(rootView instanceof ViewGroup)) {
                return (V) rootView;
            }
            if (viewHolderSparseArr == null) {
                viewHolderSparseArr = new SparseArray<>();
            }
            V childView = (V) viewHolderSparseArr.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                viewHolderSparseArr.put(id, childView);
            }
            return childView;
        }
    }
}
