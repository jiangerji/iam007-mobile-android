package cn.iam007.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.iam007.utils.logging.LogUtil;

public class ContentInfo {
    private final static String TAG = "ContentInfo";

    private String id; // content id
    private String category; // content category
    private String title; // content title
    private String url; // content url
    private String buyUrl; // content buy url
    private String thumbnail; // content thumbnail
    private String hits; // content hits

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
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
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
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
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
     * @return the thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail
     *            the thumbnail to set
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the hits
     */
    public String getHits() {
        return hits;
    }

    /**
     * @param hits
     *            the hits to set
     */
    public void setHits(String hits) {
        this.hits = hits;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id:" + getId() + " ");
        sb.append("category:" + getCategory() + " ");
        sb.append("title:" + getTitle() + " ");
        sb.append("url:" + getUrl() + " ");
        sb.append("buy url:" + getBuyUrl() + " ");
        sb.append("thumbnail:" + getThumbnail() + " ");
        sb.append("hits:" + getHits() + " ");
        return sb.toString();
    }

    public static ArrayList<ContentInfo> parseJson(JSONArray contents) {
        ArrayList<ContentInfo> arrayList = new ArrayList<ContentInfo>();
        JSONObject content;

        try {

            for (int i = 0; i < contents.length(); i++) {
                content = contents.getJSONObject(i);

                ContentInfo contentInfo = new ContentInfo();
                contentInfo.setId(content.getString("i"));
                contentInfo.setUrl(content.getString("u"));
                contentInfo.setBuyUrl(content.getString("b"));
                contentInfo.setCategory(content.getString("c"));
                contentInfo.setHits(content.getString("h"));
                contentInfo.setThumbnail(content.getString("t"));
                contentInfo.setTitle(content.getString("n"));

                arrayList.add(contentInfo);
                LogUtil.d(TAG, "content Info:" + contentInfo.toString());
            }
        } catch (Exception e) {
            LogUtil.d(TAG, "parseJson:" + e.toString());
        }

        return arrayList;
    }
}
