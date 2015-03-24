package cn.iam007.app.mall.product;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.iam007.app.common.utils.ImageUtils;
import cn.iam007.app.common.utils.PlatformUtils;
import cn.iam007.app.mall.R;

public class TagsAdapter extends BaseAdapter {

    private ArrayList<TagsInfo> mTagsInfos = new ArrayList<TagsInfo>();

    @Override
    public int getCount() {
        return mTagsInfos.size();
    }

    @Override
    public TagsInfo getItem(int position) {
        return mTagsInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView1(int position, View convertView, ViewGroup parent) {
        TagsInfo info = getItem(position);
        ImageView icon = null;

        if (convertView == null) {
            //            convertView = new ImageView(parent.getContext());
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tags_adapter_item, null);
            int width = PlatformUtils.getScreenWidth(null) / 3;
            convertView.setLayoutParams(new AbsListView.LayoutParams(width,
                    width));
        }

        icon = (ImageView) convertView.findViewById(R.id.icon);
        ImageUtils.showImageByUrl(info.getIcon(), icon);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ImageView tagIcon = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tags_adapter_item, null);
            int width = PlatformUtils.getScreenWidth(null) / 3;
            convertView.setLayoutParams(new AbsListView.LayoutParams(width,
                    AbsListView.LayoutParams.WRAP_CONTENT));

            tagIcon = (ImageView) convertView.findViewById(R.id.icon);
            int imageWidth = (int) (width * 2.0f / 3);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageWidth,
                    imageWidth);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.topMargin = (imageWidth / 4);
            tagIcon.setLayoutParams(layoutParams);

            TextView name = (TextView) convertView.findViewById(R.id.name);
            final View rightLine = convertView.findViewById(R.id.rightline);
            final View tempView = convertView;

            convertView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                        @Override
                        public void onGlobalLayout() {
                            LayoutParams layoutParams = rightLine.getLayoutParams();
                            if (tempView.getHeight() > 0) {
                                layoutParams.height = tempView.getHeight();
                                rightLine.setLayoutParams(layoutParams);
                                tempView.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                        }
                    });

            viewHolder = new ViewHolder();
            viewHolder.icon = tagIcon;
            viewHolder.name = name;
            viewHolder.rightLine = rightLine;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TagsInfo info = getItem(position);
        viewHolder.name.setText(info.getName());
        ImageUtils.showImageByUrl(info.getIcon(), viewHolder.icon);

        //        if (((position + 1) % 3) == 0) {
        //            viewHolder.rightLine.setVisibility(View.INVISIBLE);
        //        } else {
        //            viewHolder.rightLine.setVisibility(View.VISIBLE);
        //        }

        return convertView;
    }

    public void addTags(ArrayList<TagsInfo> tagInfos) {
        mTagsInfos.addAll(tagInfos);
        notifyDataSetChanged();
    }

    class ViewHolder {
        private ImageView icon;
        private View rightLine;
        private TextView name;
    }

}
