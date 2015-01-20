package cn.iam007.app.mall.plugin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.iam007.app.common.utils.CommonHttpUtils;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.base.BaseFragment;
import cn.iam007.app.mall.plugin.model.PluginItem;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PluginViewerFragment extends BaseFragment {
    private final static String TAG = "plugin";

    private GridView mGridView;
    private PluginItemAdapter mAdapter = new PluginItemAdapter();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
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
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtil.d("get plugin list failed!" + arg0 + " " + arg1);
            }
        });
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
                //                String pluginId = pluginObject.optString("id"); // 插件的唯一标示
                //                String pluginName = pluginObject.optString("name"); // 插件的名称
                //                String pluginDesc = pluginObject.optString("desc"); // 插件的描述
                //                String pluginIcon = pluginObject.optString("icon"); // 插件的图标
                //
                //                // 获取插件文件信息
                //                String pluginFileMD5 = pluginObject.optString("md5");
                //                String pluginUrl = pluginObject.optString("url");

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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.plugin_item, null);
            }

            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            TextView name = (TextView) convertView.findViewById(R.id.name);

            PluginItem item = getItem(position);

            ImageLoader.getInstance().displayImage(item.getPluginIcon(), icon);
            name.setText(item.getPluginName());

            return convertView;
        }

    }

}
