package cn.iam007.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import cn.iam007.common.utils.ImageUtils;

@SuppressLint("UseSparseArrays")
public class ImageViewPager extends RelativeLayout {

    private ViewPager mViewPager = null;
    private BottomIndicator mIndicator = null;
    private _ImageAdapter mImageAdapter = null;

    public ImageViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mViewPager = new ViewPager(context);
        LayoutParams viewPagerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(viewPagerLayoutParams);
        addView(mViewPager);

        mIndicator = new BottomIndicator(context);
        mIndicator.setVisibility(View.INVISIBLE);
        LayoutParams indicatorLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        indicatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        indicatorLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mIndicator.setLayoutParams(indicatorLayoutParams);
        addView(mIndicator);

        mImageAdapter = new _ImageAdapter();
        mViewPager.setAdapter(mImageAdapter);
    }

    public void addImageUrl(String url) {
        mImageUrls.add(url);
    }

    public void refresh() {
        if (mImageAdapter.getCount() > 1) {
            mIndicator.setVisibility(View.VISIBLE);
        } else {
            mIndicator.setVisibility(View.INVISIBLE);
        }
        mIndicator.setNumber(mImageAdapter.getCount());
        mImageAdapter.notifyDataSetChanged();

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = (int) (widthSize * 320.f / 720);

        setMeasuredDimension(widthSize, heightSize);

        //        mViewPager.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
        //                widthSize), MeasureSpec.makeMeasureSpec(MeasureSpec.EXACTLY,
        //                heightSize));
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = widthSize;
        layoutParams.height = heightSize;
        setLayoutParams(layoutParams);
    }

    private ArrayList<String> mImageUrls = new ArrayList<String>();
    private HashMap<Integer, ImageView> mImageViews = new HashMap<Integer, ImageView>();

    private class _ImageAdapter extends PagerAdapter {

        // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(mImageViews.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            ImageView imageView = mImageViews.get(position);
            if (imageView == null) {
                imageView = new ImageView(getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ScaleType.CENTER_INSIDE);
                mImageViews.put(position, imageView);
            }
            if (mImageUrls.get(position) != null) {
                Log.d("ImageViewPager",
                        "show " + position + " " + mImageUrls.get(position));
                ImageUtils.showImageByUrl(mImageUrls.get(position),
                        imageView,
                        ImageUtils.getOptionsFadeIn());
            }

            view.addView(imageView);
            return imageView;
        }

    }
}
