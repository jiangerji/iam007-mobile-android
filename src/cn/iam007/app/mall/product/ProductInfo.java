package cn.iam007.app.mall.product;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductInfo implements Parcelable {

    private String id; // product id
    private String name; // product name
    private String title; // product title
    private String buyUrl; // product url
    private String cover; // product cover
    private float price; // product price
    private float priceTag;// product price tag
    private String source; // product source, jd, tmall, yhd...?
    private String detail; // product detail url

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the buyUrl
     */
    public String getBuyUrl() {
        return buyUrl;
    }

    /**
     * @param buyUrl
     *            the buyUrl to set
     */
    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }

    /**
     * @return the cover
     */
    public String getCover() {
        return cover;
    }

    /**
     * @param cover
     *            the cover to set
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * @return the price
     */
    public float getPrice() {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(String price) {
        try {
            this.price = Float.valueOf(price);
        } catch (Exception e) {
        }
    }

    /**
     * @return the priceTag
     */
    public float getPriceTag() {
        return priceTag;
    }

    /**
     * @param priceTag
     *            the priceTag to set
     */
    public void setPriceTag(String priceTag) {
        try {
            this.priceTag = Float.valueOf(priceTag);
        } catch (Exception e) {
        }
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail
     *            the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static ArrayList<ProductInfo> parseJson(JSONArray jsonArray) {
        ArrayList<ProductInfo> productInfos = new ArrayList<ProductInfo>();
        JSONObject jsonObject = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);

                ProductInfo productInfo = new ProductInfo();
                productInfo.setId(jsonObject.optString("id"));
                productInfo.setName(jsonObject.optString("name"));
                productInfo.setTitle(jsonObject.optString("title"));
                productInfo.setBuyUrl(jsonObject.optString("buyUrl"));
                productInfo.setCover(jsonObject.optString("cover"));
                productInfo.setPrice(jsonObject.optString("price"));
                productInfo.setPriceTag(jsonObject.optString("priceTag"));
                productInfo.setSource(jsonObject.optString("source"));
                productInfo.setDetail(jsonObject.optString("detail"));

                productInfos.add(productInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return productInfos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(name);
        out.writeString(title);
        out.writeString(cover);
        out.writeString(buyUrl);
        out.writeFloat(price);
        out.writeFloat(priceTag);
        out.writeString(source);
        out.writeString(detail);
    }

    public static final Parcelable.Creator<ProductInfo> CREATOR = new Parcelable.Creator<ProductInfo>() {
        public ProductInfo createFromParcel(Parcel in) {
            return new ProductInfo(in);
        }

        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };

    protected ProductInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        title = in.readString();
        cover = in.readString();
        buyUrl = in.readString();
        price = in.readFloat();
        priceTag = in.readFloat();
        source = in.readString();
        detail = in.readString();
    }

    public ProductInfo() {

    }

}
