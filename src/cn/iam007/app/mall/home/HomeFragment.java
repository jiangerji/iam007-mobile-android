package cn.iam007.app.mall.home;

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
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseFragment;
import cn.iam007.app.mall.dynamic.DynamicAdapter;
import cn.iam007.app.mall.widget.RecommendAdsLayout;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class HomeFragment extends BaseFragment {

    private ListView mListView;
    private DynamicAdapter mFindAdapter;

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_find,
                container,
                false);// 关联布局文件
        RecommendAdsLayout viewpagerLayout = new RecommendAdsLayout(getActivity());

        mListView = (ListView) view.findViewById(R.id.find_list);
        mListView.addHeaderView(viewpagerLayout.getView());

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {

            }

        });

        mFindAdapter = new DynamicAdapter();
        mListView.setAdapter(mFindAdapter);

        init();
        LogUtil.d(TAG, "FragmentRecommend: onCreateView");
        return view;
    }

    private void init() {
        String action = "latest";
        CommonHttpUtils.get(action, null, mCallBack, "home.latest");
    }

    private RequestCallBack<String> mCallBack = new RequestCallBack<String>() {

        @Override
        public void onFailure(HttpException error, String msg) {
            if (error instanceof HttpExceptionButFoundCache) {
                parseResult(msg);
                setInitViewFinish();
            }
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
        } catch (Exception e) {
        }
    }

}
