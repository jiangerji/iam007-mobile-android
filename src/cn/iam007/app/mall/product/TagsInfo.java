package cn.iam007.app.mall.product;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TagsInfo {

    private String id; // tags id
    private String icon; // tag icon
    private String name; // tag name
    private String slug; // tag slug
    private int count; // the count of the articles belong to the tag

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
     * @return the slug
     */
    public String getSlug() {
        return slug;
    }

    /**
     * @param slug
     *            the slug to set
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    public static ArrayList<TagsInfo> parseJson(JSONArray jsonArray) {
        ArrayList<TagsInfo> mTagsInfos = new ArrayList<TagsInfo>();

        JSONObject jsonObject = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);

                TagsInfo tagsInfo = new TagsInfo();
                tagsInfo.setId(jsonObject.optString("id"));
                tagsInfo.setIcon(jsonObject.optString("icon"));
                tagsInfo.setSlug(jsonObject.optString("slug"));
                tagsInfo.setCount(jsonObject.optInt("count"));
                tagsInfo.setName(jsonObject.optString("name"));

                mTagsInfos.add(tagsInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mTagsInfos;
    }

}
