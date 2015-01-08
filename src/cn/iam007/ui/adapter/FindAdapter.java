package cn.iam007.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.iam007.R;
import cn.iam007.model.ContentInfo;
import cn.iam007.utils.ImageUtils;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean init = false;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.find_adapter_item, null);
            init = true;
        }

        final ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView intro = (TextView) convertView.findViewById(R.id.intro);

        if (init) {
            thumbnail.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                        @SuppressWarnings("deprecation")
                        @Override
                        public void onGlobalLayout() {
                            int height = thumbnail.getHeight();
                            if (height > 0) {
                                int width = (int) (185.0f * height / 137.0f);
                                LayoutParams layoutParams = thumbnail.getLayoutParams();
                                layoutParams.width = width;
                                layoutParams.height = height;
                                thumbnail.setLayoutParams(layoutParams);

                                thumbnail.getViewTreeObserver()
                                        .removeGlobalOnLayoutListener(this);
                            }
                        }
                    });
        }

        ContentInfo contentInfo = (ContentInfo) getItem(position);
        title.setText(contentInfo.getTitle());
        intro.setText(contentInfo.getIntro());
        thumbnail.setImageBitmap(null);

        ImageUtils.showImage(contentInfo.getThumbnail(), thumbnail);

        return convertView;
    }

}
