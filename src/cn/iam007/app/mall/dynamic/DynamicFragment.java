package cn.iam007.app.mall.dynamic;

import java.util.ArrayList;

import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.iam007.app.common.exception.HttpExceptionButFoundCache;
import cn.iam007.app.common.model.ContentInfo;
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

public class DynamicFragment extends BaseFragment {

    private PullToRefreshListView mPullRefreshListView;
    private DynamicAdapter mFindAdapter;

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
                loadMoreContent();
            }
        });

        mFindAdapter = new DynamicAdapter();
        mPullRefreshListView.setAdapter(mFindAdapter);
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

            ContentInfo contentInfo = mFindAdapter.getItem((int) id);
            IntentUtil.launchContentActivity(getActivity(), contentInfo);
        }
    };

    private int mIndex = 0;
    private int mLimit = 20;

    private void loadMoreContent() {
        String action = "latest";

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("limit", "" + mLimit);
        params.addQueryStringParameter("pn", "" + mIndex);
        //TODO: 暂时没有加入缓存
        CommonHttpUtils.get(action, params, mCallBack, "dynamic_latest");
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException error, String msg) {
            if (error instanceof HttpExceptionButFoundCache) {
                parseResult(msg);
            }
            setInitViewFinish();
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            parseResult(responseInfo.result);
            setInitViewFinish();
        }
    };

    private void parseResult(String content) {
        try {
            JSONObject object = new JSONObject(content);
            ArrayList<ContentInfo> contentInfos = ContentInfo.parseJson(object.getJSONArray("d"));
            mFindAdapter.addContentInfos(contentInfos);

            if (contentInfos.size() >= mLimit) {
                mIndex++;
            }
        } catch (Exception e) {
        }

        mPullRefreshListView.onRefreshComplete();
    }

}
