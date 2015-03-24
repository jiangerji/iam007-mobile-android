package cn.iam007.app.mall.product;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.iam007.app.common.utils.ImageUtils;
import cn.iam007.app.common.utils.PlatformUtils;
import cn.iam007.app.mall.R;

public class ProductAdapter extends BaseAdapter {

    private ArrayList<ProductInfo> mProductInfos = new ArrayList<ProductInfo>();

    @Override
    public int getCount() {
        return mProductInfos.size();
    }

    @Override
    public ProductInfo getItem(int position) {
        return mProductInfos.get(position);
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
            convertView = inflater.inflate(R.layout.product_adapter_item, null);

            thumbnail = (ImageView) convertView.findViewById(R.id.cover);
            LayoutParams layoutParams = thumbnail.getLayoutParams();
            int width = (int) (220.0 * PlatformUtils.getScreenWidth(parent.getContext()) / 720);
            int height = width;//(int) (220.0 * PlatformUtils.getScreenHeight(parent.getContext()) / 1280);
            layoutParams.width = width;
            layoutParams.height = height;
            thumbnail.setLayoutParams(layoutParams);
        } else {
            thumbnail = (ImageView) convertView.findViewById(R.id.cover);
            thumbnail.setImageBitmap(null);
        }

        ProductInfo info = getItem(position);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView priceTag = (TextView) convertView.findViewById(R.id.pricetag);
        TextView discount = (TextView) convertView.findViewById(R.id.discount);
        priceTag.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        title.setText(info.getTitle());
        price.setText("￥" + info.getPrice());
        priceTag.setText("￥" + info.getPriceTag());
        try {
            discount.setText(String.format("%.1f折",
                    10 * info.getPrice() / info.getPriceTag()));
        } catch (Exception e) {
        }

        ImageUtils.showImageByUrl(info.getCover(), thumbnail);

        return convertView;
    }

    public void addProductInfos(ArrayList<ProductInfo> productInfos) {
        mProductInfos.addAll(productInfos);
        notifyDataSetChanged();
    }
}
