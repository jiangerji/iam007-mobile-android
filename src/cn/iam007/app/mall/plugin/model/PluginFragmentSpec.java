package cn.iam007.app.mall.plugin.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class PluginFragmentSpec implements Parcelable {
    // 相当于该fragment在该apk包中的别名
    private String code;

    // 该fragment的完整包名
    private String name;

    public PluginFragmentSpec(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public PluginFragmentSpec(JSONObject json) throws JSONException {
        code = json.optString("code");
        name = json.getString("name");
    }

    public String code() {
        return code;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("xxx://").append(name).append(':');
        sb.append('(');
        if (code == null) {
            sb.append('.');
        } else {
            sb.append(code);
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(code);
        out.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<PluginFragmentSpec> CREATOR = new Parcelable.Creator<PluginFragmentSpec>() {
        public PluginFragmentSpec createFromParcel(Parcel in) {
            return new PluginFragmentSpec(in);
        }

        public PluginFragmentSpec[] newArray(int size) {
            return new PluginFragmentSpec[size];
        }
    };

    protected PluginFragmentSpec(Parcel in) {
        code = in.readString();
        name = in.readString();
    }
}
