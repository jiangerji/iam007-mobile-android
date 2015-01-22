package cn.iam007.app.mall;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.base.BaseActivity;
import cn.iam007.app.mall.home.MyFragmentPagerAdapter;

import com.baidu.mobstat.StatService;

public class TestMainActivity extends BaseActivity {
    private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    private ViewPager mPager;

    @Override
    protected int getFlag() {
        return FLAG_SEARCH_VIEW | FLAG_DISABLE_HOME_AS_UP;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initView();
    }

    private View mRecommentBtn = null;
    private View mLiveBtn = null;
    private View mGameBtn = null;
    private View mAccountBtn = null;

    private MyFragmentPagerAdapter mFragmentPagerAdapter;

    private int mPanelTitleNormalColor = 0;
    private int mPanelTitleSelectedColor = 0;

    private ArrayList<TextView> mPanelTitles = new ArrayList<TextView>();
    private ArrayList<View> mPanelImageNormal = new ArrayList<View>();
    private ArrayList<View> mPanelImageSelected = new ArrayList<View>();

    /*
     * 初始化ViewPager
     */
    public void initView() {
        mPager = (ViewPager) findViewById(R.id.viewpager);

        // 给ViewPager设置适配器
        mFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mPager.setOffscreenPageLimit(mFragmentPagerAdapter.getCount());//
        mPager.setAdapter(mFragmentPagerAdapter);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mRecommentBtn = findViewById(R.id.recomment_btn);
        mRecommentBtn.setOnClickListener(mBtnClickListener);
        mPanelImageNormal.add(findViewById(R.id.recomment_img));
        mPanelImageSelected.add(findViewById(R.id.recomment_img_selected));
        mPanelTitles.add((TextView) findViewById(R.id.recommend_title));

        mLiveBtn = findViewById(R.id.live_btn);
        mLiveBtn.setOnClickListener(mBtnClickListener);
        mPanelImageNormal.add(findViewById(R.id.live_img));
        mPanelImageSelected.add(findViewById(R.id.live_img_selected));
        mPanelTitles.add((TextView) findViewById(R.id.live_title));

        mGameBtn = findViewById(R.id.game_btn);
        mGameBtn.setOnClickListener(mBtnClickListener);
        mPanelImageNormal.add(findViewById(R.id.game_img));
        mPanelImageSelected.add(findViewById(R.id.game_img_selected));
        mPanelTitles.add((TextView) findViewById(R.id.game_title));

        mAccountBtn = findViewById(R.id.account_btn);
        mAccountBtn.setOnClickListener(mBtnClickListener);
        mPanelImageNormal.add(findViewById(R.id.account_img));
        mPanelImageSelected.add(findViewById(R.id.account_img_selected));
        mPanelTitles.add((TextView) findViewById(R.id.account_title));

        setCurrentPage(0);// 设置当前显示标签页为第一页

        mPanelTitleNormalColor = getResources().getColor(R.color.panel_title_normal_color);
        mPanelTitleSelectedColor = getResources().getColor(R.color.panel_title_selected_color);
    }

    private OnClickListener mBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            int pageIndex = -1;
            if (id == R.id.recomment_btn) {
                pageIndex = 0;
            } else if (id == R.id.live_btn) {
                pageIndex = 1;
            } else if (id == R.id.game_btn) {
                pageIndex = 2;
            } else if (id == R.id.account_btn) {
                pageIndex = 3;
            }

            if (pageIndex >= 0 && pageIndex < mFragmentPagerAdapter.getCount()) {
                setCurrentPage(pageIndex);
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setViewAlpha(View view, float alpha) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnim = new AlphaAnimation(alpha, alpha);
            alphaAnim.setDuration(0); // Make animation instant
            alphaAnim.setFillAfter(true); // Tell it to persist after the animation ends
            view.startAnimation(alphaAnim);
        }
    }

    private boolean mBackFromOtherPlace = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (mCurrentPageIndex >= 0 && mBackFromOtherPlace) {
            StatService.onPageStart(this, "fragment:" + mCurrentPageIndex);
            LogUtil.d("service", "page start " + mCurrentPageIndex);
        }
        mBackFromOtherPlace = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBackFromOtherPlace = true;
        if (mCurrentPageIndex >= 0) {
            LogUtil.d("service", "page end " + mCurrentPageIndex);
            StatService.onPageEnd(this, "fragment:" + mCurrentPageIndex);
        }
    }

    private int mCurrentPageIndex = -1;

    private void setCurrentPage(int index) {
        if (mCurrentPageIndex != index) {
            if (mCurrentPageIndex >= 0) {
                LogUtil.d("service", "page end " + mCurrentPageIndex);
                StatService.onPageEnd(this, "fragment:" + mCurrentPageIndex);
            }
            LogUtil.d("service", "page start " + index);
            StatService.onPageStart(this, "fragment:" + index);
            mCurrentPageIndex = index;
        }

        if (index >= 0 && index < mFragmentPagerAdapter.getCount()) {
            mPager.setCurrentItem(index, false);
            mAccountBtn.setSelected(false);
            mGameBtn.setSelected(false);
            mLiveBtn.setSelected(false);
            mRecommentBtn.setSelected(false);

            for (int i = 0; i < mPanelImageNormal.size(); i++) {
                if (i != index) {
                    setViewAlpha(mPanelImageNormal.get(i), 1);
                    setViewAlpha(mPanelImageSelected.get(i), 0);
                    mPanelTitles.get(i).setTextColor(mPanelTitleNormalColor);
                } else {
                    setViewAlpha(mPanelImageNormal.get(i), 0);
                    setViewAlpha(mPanelImageSelected.get(i), 1);
                    mPanelTitles.get(i).setTextColor(mPanelTitleSelectedColor);
                }
            }

            switch (index) {
            case 0:
                mRecommentBtn.setSelected(true);
                break;

            case 1:
                mLiveBtn.setSelected(true);
                break;

            case 2:
                mGameBtn.setSelected(true);
                break;

            case 3:
                mAccountBtn.setSelected(true);
                break;

            default:
                break;
            }
        }
    }

    private void changePanelDisplay(
            int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset < 0.0000001) {
            setCurrentPage(position);
            return;
        }

        changePanelTitleColor(position, positionOffset, positionOffsetPixels);
        changePanelImage(position, positionOffset, positionOffsetPixels);
    }

    private void changePanelImage(
            int position, float positionOffset, int positionOffsetPixels) {
        setViewAlpha(mPanelImageNormal.get(position), positionOffset);
        setViewAlpha(mPanelImageSelected.get(position), 1 - positionOffset);

        setViewAlpha(mPanelImageNormal.get(position + 1), 1 - positionOffset);
        setViewAlpha(mPanelImageSelected.get(position + 1), positionOffset);
    }

    private void changePanelTitleColor(
            int position, float positionOffset, int positionOffsetPixels) {
        /*
         * 根据viewerpager渐变修改文字的颜色
         */
        int normalColorRed = Color.red(mPanelTitleNormalColor);
        int normalColorBlue = Color.blue(mPanelTitleNormalColor);
        int normalColorGreen = Color.green(mPanelTitleNormalColor);

        int selectedColorRed = Color.red(mPanelTitleSelectedColor);
        int selectedColorBlue = Color.blue(mPanelTitleSelectedColor);
        int selectedColorGreen = Color.green(mPanelTitleSelectedColor);

        // 设置左边item的title
        int leftRed = (int) ((1.0 - positionOffset) * selectedColorRed + positionOffset
                * normalColorRed);
        int leftBlue = (int) ((1.0 - positionOffset) * selectedColorBlue + positionOffset
                * normalColorBlue);
        int leftGreen = (int) ((1.0 - positionOffset) * selectedColorGreen + positionOffset
                * normalColorGreen);
        mPanelTitles.get(position).setTextColor(Color.argb(255,
                leftRed, leftGreen, leftBlue));

        // 设置右边item的title
        int rightRed = (int) ((1.0 - positionOffset) * normalColorRed + positionOffset
                * selectedColorRed);
        int rightBlue = (int) ((1.0 - positionOffset) * normalColorBlue + positionOffset
                * selectedColorBlue);
        int rightGreen = (int) ((1.0 - positionOffset) * normalColorGreen + positionOffset
                * selectedColorGreen);
        mPanelTitles.get(position + 1).setTextColor(Color.argb(255,
                rightRed, rightGreen, rightBlue));
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            debug("onPageScrollStateChanged:" + state);
        }

        @Override
        public void onPageScrolled(
                int position, float positionOffset, int positionOffsetPixels) {
            changePanelDisplay(position,
                    positionOffset,
                    positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            setCurrentPage(position);
            debug("onPageSelected:" + position);
        }

    }

}
