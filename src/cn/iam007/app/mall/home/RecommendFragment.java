package cn.iam007.app.mall.home;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import cn.iam007.app.common.config.AppConstants;
import cn.iam007.app.common.exception.HttpExceptionButFoundCache;
import cn.iam007.app.common.utils.CommonHttpUtils;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseFragment;
import cn.iam007.app.mall.widget.RecommendAdsLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class RecommendFragment extends BaseFragment {

    private PullToRefreshScrollView mScrollView;
    private LinearLayout mContainer;
    private Handler mHandler;

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container) {
        mScrollView = (PullToRefreshScrollView) inflater.inflate(R.layout.fragment_recommend,
                container,
                false);// 关联布局文件
        mScrollView.setMode(Mode.PULL_FROM_START);
        mScrollView.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ScrollView> refreshView) {
                initRecommendView();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ScrollView> refreshView) {
                initRecommendView();
            }
        });
        mContainer = (LinearLayout) mScrollView.findViewById(R.id.container);
        RecommendAdsLayout viewpagerLayout = new RecommendAdsLayout(getActivity());
        mContainer.addView(viewpagerLayout.getView());

        mHandler = new Handler(mHandlerCallback);
        initRecommendView();

        return mScrollView;
    }

    private final static int INIT_RECOMMEND_LAYOUT = 1;
    private Callback mHandlerCallback = new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
            case INIT_RECOMMEND_LAYOUT:

                for (RecommendLayoutItem item : mRecommendLayoutItems) {
                    mContainer.addView(item.getView());
                }

                mScrollView.requestLayout();
                setInitViewFinish();
                break;

            default:
                break;
            }

            return true;
        }
    };

    private boolean mInInitRecommendView = false;

    private void initRecommendView() {
        if (mInInitRecommendView) {
            return;
        }

        mInInitRecommendView = true;
        int count = mContainer.getChildCount();
        if (count > 1) {
            mContainer.removeViews(1, count - 1);
        }

        String action = AppConstants.ALIYUN_HOST + "iam007/aliyun/recommend";
        CommonHttpUtils.get(action,
                null,
                mCallBack,
                "RecommendFragment.initRecommendView");
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException error, String msg) {
            if (error instanceof HttpExceptionButFoundCache) {
                parseResult(msg);
            } else {
                setInitViewFinish();
            }

            mScrollView.onRefreshComplete();
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            parseResult(responseInfo.result);

            mScrollView.onRefreshComplete();
        }
    };

    private ArrayList<RecommendLayoutItem> mRecommendLayoutItems = new ArrayList<RecommendLayoutItem>();

    private void parseResult(String result) {
        try {
            JSONObject recommendLayouts = new JSONObject(result);
            JSONArray layouts = recommendLayouts.getJSONArray("data");
            ArrayList<RecommendLayoutItem> tempRecommendLayoutItems = new ArrayList<RecommendLayoutItem>();
            for (int i = 0; i < layouts.length(); i++) {
                tempRecommendLayoutItems.add(new RecommendLayoutItem(getActivity(),
                        layouts.getJSONObject(i)));
            }
            mRecommendLayoutItems.clear();
            mRecommendLayoutItems.addAll(tempRecommendLayoutItems);

            mHandler.sendEmptyMessage(INIT_RECOMMEND_LAYOUT);
        } catch (Exception e) {
            mInInitRecommendView = false;
        }
    }

    @Override
    protected void setInitViewFinish() {
        super.setInitViewFinish();

        mInInitRecommendView = false;
    }

}
