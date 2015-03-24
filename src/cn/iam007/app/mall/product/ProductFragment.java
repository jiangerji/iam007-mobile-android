package cn.iam007.app.mall.product;

import java.util.ArrayList;

import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.iam007.app.common.exception.HttpExceptionButFoundCache;
import cn.iam007.app.common.utils.CommonHttpUtils;
import cn.iam007.app.common.utils.IntentUtil;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseFragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class ProductFragment extends BaseFragment {

    PullToRefreshListView mPullRefreshListView = null;
    ProductAdapter mProductAdapter;

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container) {
        mPullRefreshListView = (PullToRefreshListView) inflater.inflate(R.layout.fragment_dynamic,
                container,
                false);
        mPullRefreshListView.setMode(Mode.MANUAL_REFRESH_ONLY);
        mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                mPullRefreshListView.setRefreshing();
                loadMoreContent();
            }
        });
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //                loadMoreContent();

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                //                loadMoreContent();
            }
        });

        mProductAdapter = new ProductAdapter();
        mPullRefreshListView.setAdapter(mProductAdapter);
        mPullRefreshListView.setOnItemClickListener(mOnItemClickListener);

        loadMoreContent();

        return mPullRefreshListView;
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(
                AdapterView<?> parent, View view, int position, long id) {
            if (id == -1) {
                // 点击到head view或foot view上
                return;
            }

            ProductInfo productInfo = mProductAdapter.getItem((int) id);
            IntentUtil.launchProductActivity(getActivity(), productInfo);
        }
    };

    private int mIndex = 0;
    private int mLimit = 20;
    private boolean mInLoadMoreContent = false;

    private void loadMoreContent() {
        String action = "getProducts";
        if (mInLoadMoreContent) {
            return;
        }

        mInLoadMoreContent = true;

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("limit", "" + mLimit);
        params.addQueryStringParameter("pn", "" + mIndex);

        CommonHttpUtils.get(action, params, mCallBack, "products_" + mIndex
                + "_" + mLimit);
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException error, String msg) {
            if (error instanceof HttpExceptionButFoundCache) {
                parseResult(msg);
            }
            setInitViewFinish();
            mInLoadMoreContent = false;
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            parseResult(responseInfo.result);
            setInitViewFinish();
            mInLoadMoreContent = false;
        }
    };

    private void parseResult(String content) {
        try {
            JSONObject object = new JSONObject(content);
            ArrayList<ProductInfo> productInfos = ProductInfo.parseJson(object.getJSONArray("d"));
            mProductAdapter.addProductInfos(productInfos);

            if (productInfos.size() >= mLimit) {
                mIndex++;
            }
        } catch (Exception e) {
        }

        mPullRefreshListView.onRefreshComplete();
    }

}
