package cn.iam007.app.mall.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.iam007.app.mall.R;

public abstract class BaseFragment extends Fragment {
    protected final static String TAG = "fragment";

    /**
     * 用于显示子类fragment的内容
     */
    private FrameLayout mContainer = null;

    /**
     * 用于显示等待状态
     */
    private View mLoadingView = null;

    private TextView mLoadingHint = null;

    private LayoutInflater mLayoutInflater = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base,
                container,
                false);

        mContainer = (FrameLayout) rootView.findViewById(R.id.container);
        mContainer.setVisibility(View.INVISIBLE);
        mLoadingView = rootView.findViewById(R.id.loading);
        mLoadingHint = (TextView) rootView.findViewById(R.id.loadingHint);

        if (mFragmentIsVisible) {
            initView(inflater, mContainer);
        }

        mFirstLaunch = false;
        mLayoutInflater = inflater;
        return rootView;
    }

    protected final void hideLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.INVISIBLE);
        }
    }

    protected final void showLoading() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化子类的UI界面
     * 
     * @param container
     * @return
     */
    public abstract
            View onInitView(LayoutInflater inflater, ViewGroup container);

    private void initView(LayoutInflater inflater, ViewGroup container) {
        View view = onInitView(inflater, mContainer);
        if (view != null) {
            mContainer.addView(view);
        }
        mInitedView = true;
    }

    /**
     * 设置初始化view结束
     */
    protected void setInitViewFinish() {
        hideLoadingView();
        if (mContainer.getVisibility() == View.INVISIBLE) {
            mContainer.setVisibility(View.VISIBLE);
            mContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.pre_in));
        }
    }

    private boolean mFragmentIsVisible = false;
    private boolean mInitedView = false;
    private boolean mFirstLaunch = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mFragmentIsVisible = isVisibleToUser;

        if (isVisibleToUser) {
            if ((!mFirstLaunch) && (!mInitedView)) {
                initView(mLayoutInflater, mContainer);
            }
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 该fragment UI显示
     */
    protected void onVisible() {

    }

    /**
     * 该fragment UI隐藏
     */
    protected void onInvisible() {

    }

    /**
     * 是否有网络连接
     * 
     * @return
     */
    protected final boolean isNetworkConnect() {
        ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }

}
