package cn.iam007.ui.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import cn.iam007.HttpExceptionButFoundCache;
import cn.iam007.R;
import cn.iam007.model.ContentInfo;
import cn.iam007.ui.adapter.FindAdapter;
import cn.iam007.ui.widget.RecommendAdsLayout;
import cn.iam007.utils.CommonHttpUtils;
import cn.iam007.utils.logging.LogUtil;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class FragmentFind extends BaseFragment {

    private ListView mListView;
    private FindAdapter mFindAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find,
                container,
                false);// 关联布局文件
        RecommendAdsLayout viewpagerLayout = new RecommendAdsLayout(getActivity());

        mListView = (ListView) view.findViewById(R.id.find_list);
        mListView.addHeaderView(viewpagerLayout.getView());

        mFindAdapter = new FindAdapter();
        mListView.setAdapter(mFindAdapter);

        init();
        LogUtil.d(TAG, "FragmentRecommend: onCreateView");
        return view;
    }

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
