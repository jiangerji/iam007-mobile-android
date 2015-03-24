package cn.iam007.app.mall.home;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.iam007.app.common.utils.ImageUtils;
import cn.iam007.app.mall.R;
import cn.iam007.app.mall.webview.WebViewActivity;

public class RecommendLayoutItem {

    private Context mContext;
    private String mName;
    private ArrayList<RecommendItem> mItems = new ArrayList<RecommendLayoutItem.RecommendItem>();
    private boolean mInitSucc = false;

    public RecommendLayoutItem(Context context, JSONObject value) {
        mContext = context;
        try {
            this.mName = value.optString("name");

            JSONArray items = value.getJSONArray("data");
            JSONObject item = null;
            String postId = null;
            String title = null;
            String icon = null;
            String ref = null;
            for (int i = 0; i < items.length(); i++) {
                item = items.getJSONObject(i);

                postId = item.optString("postId");
                title = item.optString("title");
                icon = item.optString("icon");
                ref = item.optString("ref");

                mItems.add(new RecommendItem(title, postId, icon, ref));
            }

            mInitSucc = true;
        } catch (Exception e) {
        }
    }

    /**
     * 返回该推荐区域的view
     * 
     * @return
     */
    public View getView() {
        View view = null;
        ViewGroup root = null;

        if (!mInitSucc) {
            return view;
        }

        int size = mItems.size();

        if (size > 0) {

            switch (size) {
            case 2:
                view = init_2(root);
                break;

            case 3:
                view = init_3(root);
                break;

            case 4:
                view = init_4(root);
                break;

            default:
                view = init_1(root);
                break;
            }
        }

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;
        view.setLayoutParams(layoutParams);

        return view;
    }

    private View init_1(ViewGroup root) {
        View view = View.inflate(mContext,
                R.layout.fragment_recommend_layout_1,
                root);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(mName);

        RecommendItem item = mItems.get(0);
        View left = view.findViewById(R.id.left);

        initRecommentItem(left, item);

        //        TextView title = (TextView) view.findViewById(R.id.skutitle);
        //        title.setText(item.getTitle());
        //
        //        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        //        ImageUtils.showImageByUrl(item.getIcon(), icon);

        return view;
    }

    private View init_2(ViewGroup root) {
        View view = View.inflate(mContext,
                R.layout.fragment_recommend_layout_2,
                root);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(mName);

        RecommendItem leftItem = mItems.get(0);
        View left = view.findViewById(R.id.left);
        initRecommentItem(left, leftItem);

        RecommendItem rightItem = mItems.get(1);
        View right = view.findViewById(R.id.right);
        initRecommentItem(right, rightItem);

        return view;
    }

    private View init_3(ViewGroup root) {
        View view = View.inflate(mContext,
                R.layout.fragment_recommend_layout_3,
                root);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(mName);

        RecommendItem leftItem = mItems.get(0);
        View left = view.findViewById(R.id.left);
        initRecommentItem(left, leftItem);

        RecommendItem rightTopItem = mItems.get(1);
        View rightTop = view.findViewById(R.id.righttop);
        initRecommentItem(rightTop, rightTopItem);

        RecommendItem rightBottomItem = mItems.get(2);
        View rightBottom = view.findViewById(R.id.rightbottom);
        initRecommentItem(rightBottom, rightBottomItem);
        return view;
    }

    private View init_4(ViewGroup root) {
        View view = View.inflate(mContext,
                R.layout.fragment_recommend_layout_4,
                root);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(mName);

        RecommendItem topItem = mItems.get(0);
        View top = view.findViewById(R.id.top);
        initRecommentItem(top, topItem);

        RecommendItem bottomLeftItem = mItems.get(1);
        View bottomLeft = view.findViewById(R.id.bottom_left);
        initRecommentItem(bottomLeft, bottomLeftItem);

        RecommendItem bottomMiddleItem = mItems.get(2);
        View bottomMiddle = view.findViewById(R.id.bottom_middle);
        initRecommentItem(bottomMiddle, bottomMiddleItem);

        RecommendItem bottomRightItem = mItems.get(3);
        View bottomRight = view.findViewById(R.id.bottom_right);
        initRecommentItem(bottomRight, bottomRightItem);
        return view;
    }

    private void initRecommentItem(View view, RecommendItem item) {
        TextView title = (TextView) view.findViewById(R.id.skutitle);
        title.setText(item.getTitle());

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        ImageUtils.showImageByUrl(item.getIcon(), icon);

        view.setTag(item);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RecommendItem item = (RecommendItem) v.getTag();
                String ref = item.getRef();
                if (URLUtil.isNetworkUrl(ref)) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, WebViewActivity.class);
                    intent.putExtra("url", ref);
                    intent.putExtra("title", item.getTitle());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    class RecommendItem {
        String title;
        String postId;
        String icon;
        String ref;

        public RecommendItem(String title, String postId, String icon,
                String ref) {
            this.title = title;
            this.postId = postId;
            this.icon = icon;
            this.ref = ref;
        }

        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title
         *            the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return the postId
         */
        public String getPostId() {
            return postId;
        }

        /**
         * @param postId
         *            the postId to set
         */
        public void setPostId(String postId) {
            this.postId = postId;
        }

        /**
         * @return the icon
         */
        public String getIcon() {
            return icon;
        }

        /**
         * @param icon
         *            the icon to set
         */
        public void setIcon(String icon) {
            this.icon = icon;
        }

        /**
         * @return the ref
         */
        public String getRef() {
            return ref;
        }

        /**
         * @param ref
         *            the ref to set
         */
        public void setRef(String ref) {
            this.ref = ref;
        }
    }

}
