package cn.iam007.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.iam007.R;
import cn.iam007.common.utils.ImageUtils;
import cn.iam007.common.utils.PlatformUtils;
import cn.iam007.model.ContentInfo;

public class FindAdapter extends BaseAdapter {

    private ArrayList<ContentInfo> mContentInfos = new ArrayList<ContentInfo>();

    public void addContentInfos(ArrayList<ContentInfo> contentInfos) {
        mContentInfos.addAll(contentInfos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContentInfos.size();
    }

    @Override
    public ContentInfo getItem(int position) {
        return mContentInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView thumbnail = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.find_adapter_item, null);

            thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            LayoutParams layoutParams = thumbnail.getLayoutParams();
            int width = (int) (189.0 * PlatformUtils.getScreenWidth(parent.getContext()) / 720);
            int height = (int) (140.0 * PlatformUtils.getScreenHeight(parent.getContext()) / 1280);
            layoutParams.width = width;
            layoutParams.height = height;
            thumbnail.setLayoutParams(layoutParams);
        } else {
            thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            thumbnail.setImageBitmap(null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView intro = (TextView) convertView.findViewById(R.id.intro);

        ContentInfo contentInfo = (ContentInfo) getItem(position);
        title.setText(contentInfo.getTitle());
        intro.setText(contentInfo.getIntro());

        ImageUtils.showImage(contentInfo.getThumbnail(), thumbnail);

        return convertView;
    }
}
