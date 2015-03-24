package cn.iam007.app.mall.product;

import java.util.ArrayList;

import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import cn.iam007.app.common.config.AppConstants;
import cn.iam007.app.common.exception.HttpExceptionButFoundCache;
import cn.iam007.app.common.utils.CommonHttpUtils;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseFragment;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class TagsFragment extends BaseFragment {

    private GridView mTagsGrid;
    private TagsAdapter mTagsAdapter = new TagsAdapter();

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container) {
        mTagsGrid = (GridView) inflater.inflate(R.layout.fragment_tags,
                container, false);

        loadTags();
        mTagsGrid.setAdapter(mTagsAdapter);

        return mTagsGrid;
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

    private void loadTags() {
        String action = AppConstants.ALIYUN_HOST + "iam007/aliyun/tags";
        CommonHttpUtils.get(action,
                null,
                mCallBack,
                "TagsFragment.loadTags",
                60);
    }

    private void parseResult(String result) {
        try {
            JSONObject object = new JSONObject(result);
            ArrayList<TagsInfo> tagInfos = TagsInfo.parseJson(object.getJSONArray("data"));
            mTagsAdapter.addTags(tagInfos);
        } catch (Exception e) {
        }
    }

}
