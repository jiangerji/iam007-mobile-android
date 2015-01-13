package cn.iam007.app.mall.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected final static String TAG = "fragment";

    /**
     * 是否有网络连接
     * 
     * @return
     */
    protected final boolean isNetworkConnect() {
        ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }
}
