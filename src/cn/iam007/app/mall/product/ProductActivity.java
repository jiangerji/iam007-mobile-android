package cn.iam007.app.mall.product;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import cn.iam007.app.common.utils.ContentUtil;
import cn.iam007.app.common.utils.ContentUtil.BoolCallback;
import cn.iam007.app.common.utils.ImageUtils;
import cn.iam007.app.common.utils.IntentUtil;
import cn.iam007.app.common.utils.PlatformUtils;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseActivity;
import cn.iam007.app.mall.widget.ShareDialog;
import cn.iam007.app.mall.widget.WebViewWrapper;

public class ProductActivity extends BaseActivity {

    private WebViewWrapper mWebContent; // 显示商品图文详情

    private View mThumbnailsRoot = null;

    private View mBuyBtn = null;
    private View mCollectBtn = null; // 关注，取消关注按钮
    private TextView mCollectState = null;
    private boolean mCollected = false; // 是否已经关注

    private ViewPager mContentThumbnails;
    private _ThumbnailsAdapter mThumbnailsAdapter = new _ThumbnailsAdapter();

    private TextView mThumbnailIndex = null; // 表示当前thumbnail的index
    private TextView mContentIntro = null;

    private View mShareBtn = null;// 分享按钮

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_product);

        mWebContent = (WebViewWrapper) findViewById(R.id.webContent);

        mBuyBtn = findViewById(R.id.goto_buy);
        mBuyBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentUtil.launchWebviewActivity(ProductActivity.this,
                        mProductInfo.getId(),
                        mProductInfo.getName(),
                        mProductInfo.getBuyUrl());
            }
        });

        mThumbnailsRoot = findViewById(R.id.thumbnails_root);
        LayoutParams layoutParams = mThumbnailsRoot.getLayoutParams();
        layoutParams.width = (int) (PlatformUtils.getScreenWidth(this));
        layoutParams.height = (int) (layoutParams.width * 0.8);
        mThumbnailsRoot.setLayoutParams(layoutParams);

        mContentThumbnails = (ViewPager) findViewById(R.id.content_thumbnails);
        mContentThumbnails.setAdapter(mThumbnailsAdapter);
        mContentThumbnails.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mThumbnailIndex.setText("" + (arg0 + 1) + "/"
                        + mThumbnailsAdapter.getCount());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1 && mThumbnailsAdapter.getCount() > 1) {
                    disableGestureDetector();
                } else {
                    enableGestureDetector();
                }

            }
        });

        mThumbnailIndex = (TextView) findViewById(R.id.thumbnail_index);
        mContentIntro = (TextView) findViewById(R.id.content_intro);

        mCollectBtn = findViewById(R.id.pd_collect);
        mCollectState = (TextView) findViewById(R.id.pd_collect_state);
        mCollectBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mCollectBtn.setEnabled(false);
                if (mCollected) {
                    mCollectBtn.setSelected(false);
                    mCollectState.setText(R.string.pd_collect);
                    ContentUtil.collect(mProductInfo.getId(),
                            new BoolCallback() {

                                @Override
                                public void onFinish(boolean state, String msg) {
                                    mCollectBtn.setEnabled(true);
                                    if (state) {
                                        mCollected = false;
                                    } else {
                                        mCollectBtn.setSelected(true);
                                    }
                                }
                            });
                } else {
                    mCollectBtn.setSelected(true);
                    mCollectState.setText(R.string.pd_collected);
                    ContentUtil.uncollect(mProductInfo.getId(),
                            new BoolCallback() {

                                @Override
                                public void onFinish(boolean state, String msg) {
                                    mCollectBtn.setEnabled(true);
                                    if (state) {
                                        mCollected = true;
                                    } else {
                                        mCollectBtn.setSelected(false);
                                    }
                                }
                            });
                }
            }
        });
        mShareBtn = findViewById(R.id.pd_share);
        mShareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ShareDialog dialog = new ShareDialog(ProductActivity.this);
                dialog.show();
            }
        });

        init();
    }

    private ProductInfo mProductInfo;

    private void init() {
        Intent intent = getIntent();
        mProductInfo = intent.getParcelableExtra("product");

        setTitle(mProductInfo.getName());
        mContentIntro.setText(mProductInfo.getTitle());
        //        getContentDetail();

        // 初始化商品示意图
        mImageUrls.add(mProductInfo.getCover());
        mThumbnailIndex.setText("1/1");
        mThumbnailIndex.setVisibility(View.INVISIBLE);
        mThumbnailsAdapter.notifyDataSetChanged();

        if (TextUtils.isEmpty(mProductInfo.getBuyUrl())) {
            mBuyBtn.setEnabled(false);
        }

        mWebContent.loadUrl(mProductInfo.getDetail());
    }

    private ArrayList<String> mImageUrls = new ArrayList<String>();
    private HashMap<Integer, ImageView> mImageViews = new HashMap<Integer, ImageView>();

    private class _ThumbnailsAdapter extends PagerAdapter {

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
                imageView = new ImageView(view.getContext());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ScaleType.FIT_CENTER);
                mImageViews.put(position, imageView);

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

    @Override
    protected void onFlingRight() {
        super.onFlingRight();
        finish();
    }
}
