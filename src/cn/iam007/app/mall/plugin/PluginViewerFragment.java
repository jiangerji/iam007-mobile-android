package cn.iam007.app.mall.plugin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.iam007.app.common.exception.HttpExceptionButFoundCache;
import cn.iam007.app.common.utils.CommonHttpUtils;
import cn.iam007.app.common.utils.ImageUtils;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseFragment;
import cn.iam007.app.mall.plugin.model.PluginItem;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class PluginViewerFragment extends BaseFragment {
    private final static String TAG = "plugin";

    private GridView mGridView;
    private PluginItemAdapter mAdapter = new PluginItemAdapter();

    @Override
    public View onInitView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_plugin_viewer,
                container,
                false);// 关联布局文件
        mGridView = (GridView) view.findViewById(R.id.plugin_list);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> parent, View view, int position, long id) {
                PluginItem pluginItem = mAdapter.getItem((int) id);
                pluginItem.start(getActivity());
            }
        });

        init();
        return view;
    }

    private void init() {
        String action = "plugin";
        CommonHttpUtils.get(action, null, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String result = arg0.result;
                LogUtil.d("get plug list:");
                LogUtil.d(result);
                parsePluginList(result);
                setInitViewFinish();
            }

            @Override
            public void onFailure(HttpException exception, String result) {
                //                LogUtil.d("get plugin list failed!" + exception + " " + result);
                if (exception instanceof HttpExceptionButFoundCache) {
                    parsePluginList(result);
                }
                setInitViewFinish();
            }
        }, "plugins", 30 * 60);
    }

    private void parsePluginList(String jsonResult) {
        try {
            JSONObject object = new JSONObject(jsonResult);
            int error = object.optInt("e", -1);
            LogUtil.d("  error:" + error);
            JSONArray pluginList = object.getJSONArray("d");
            mPluginItems.clear();
            for (int i = 0; i < pluginList.length(); i++) {
                JSONObject pluginObject = pluginList.getJSONObject(i);
                PluginItem pluginItem = new PluginItem(pluginObject);
                pluginItem.install();
                mPluginItems.add(pluginItem);
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<PluginItem> mPluginItems = new ArrayList<PluginItem>();

    class PluginItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPluginItems.size();
        }

        @Override
        public PluginItem getItem(int position) {
            return mPluginItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        @SuppressWarnings("deprecation")
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView icon = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.plugin_item, null);
                icon = (ImageView) convertView.findViewById(R.id.icon);
                final ImageView pluginIcon = icon;
                pluginIcon.getViewTreeObserver()
                        .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {
                                int width = pluginIcon.getWidth();
                                LayoutParams layoutParams = pluginIcon.getLayoutParams();
                                layoutParams.height = width;
                                pluginIcon.setLayoutParams(layoutParams);

                                pluginIcon.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                        });
            } else {
                icon = (ImageView) convertView.findViewById(R.id.icon);
            }

            TextView name = (TextView) convertView.findViewById(R.id.name);

            PluginItem item = getItem(position);

            ImageUtils.showImageByUrl(item.getPluginIcon(), icon);
            name.setText(item.getPluginName());

            return convertView;
        }

    }

}
