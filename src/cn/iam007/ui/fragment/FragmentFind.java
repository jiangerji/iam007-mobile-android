package cn.iam007.ui.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.iam007.HttpExceptionButFoundCache;
import cn.iam007.R;
import cn.iam007.model.ContentInfo;
import cn.iam007.ui.adapter.FindAdapter;
import cn.iam007.utils.CommonHttpUtils;
import cn.iam007.utils.IntentUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class FragmentFind extends BaseFragment {

    private PullToRefreshListView mPullRefreshListView;
    private FindAdapter mFindAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mPullRefreshListView = (PullToRefreshListView) inflater.inflate(R.layout.fragment_find,
                container,
                false);
        //                .findViewById(R.id.find_list);
        mPullRefreshListView.setMode(Mode.DISABLED);

        mFindAdapter = new FindAdapter();
        mPullRefreshListView.setAdapter(mFindAdapter);
        mPullRefreshListView.setOnItemClickListener(mOnItemClickListener);

        init();

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

    private void init() {
        String action = "latest";
        //TODO: 暂时没有加入缓存
        CommonHttpUtils.get(action, null, mCallBack);
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException error, String msg) {
            if (error instanceof HttpExceptionButFoundCache) {
                parseResult(msg);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            parseResult(responseInfo.result);
        }
    };

    private void parseResult(String content) {
        try {
            JSONObject object = new JSONObject(content);
            ArrayList<ContentInfo> contentInfos = ContentInfo.parseJson(object.getJSONArray("d"));
            mFindAdapter.addContentInfos(contentInfos);
        } catch (Exception e) {
        }
    }

}
