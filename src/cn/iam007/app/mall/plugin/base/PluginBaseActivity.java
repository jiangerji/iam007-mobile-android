package cn.iam007.app.mall.plugin.base;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.iam007.app.mall.plugin.PluginManager;
import cn.iam007.app.mall.plugin.model.PluginFileSpec;
import cn.iam007.app.mall.plugin.model.PluginFragmentSpec;
import cn.iam007.app.mall.plugin.model.PluginItem;

/**
 * 主Activity容器，负责启动并装载Fragment
 * <p>
 * 启动前所有依赖的资源必须加载完毕（由urlMapping和LoaderActivity负责）
 * <p>
 * Intent参数：<br>
 * _site:SiteSpec，指定的site地图<br>
 * _code:String，ClassLoader所需要载入的FileID，如果为空则使用APK自带ClassLoader<br>
 * _fragment:String，Fragment的类名<br>
 * 
 * @author Yimin
 * 
 */
public class PluginBaseActivity extends FragmentActivity {

    //    ArrayList<PluginFragmentSpec> mFragmentSpecs = null;
    private String mPluginId;
    protected PluginFileSpec mPluginFileSpec;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent intent = getIntent();

        mPluginId = intent.getStringExtra("_pluginId");
        mPluginFileSpec = PluginManager.getPluginItem(mPluginId)
                .getPluginFileSpec();
        //
        //        mFragmentSpecs = intent.getParcelableArrayListExtra("_fragmentSpecs");
    }

    protected PluginFileSpec getPluginFileSpec() {
        return mPluginFileSpec;
    }

    public final String getPluginId() {
        return mPluginId;
    }

    /**
     * 读取插件的配置文件
     * TODO: 应该每个插件有固定的目录
     * 
     * @return
     */
    //    public SiteSpec readSite(String pluginId) {
    //        File dir = new File(CacheConfiguration.getCacheDirPlugin(), pluginId);
    //        File local = new File(dir, "a.txt");
    //        local = new File("/data/data/cn.iam007.app.mall/files/plugin/sample.helloworld.20130703.1/a.txt");
    //        //        for (File iFile : dir.listFiles()) {
    //        //            LogUtil.d("file", "" + iFile);
    //        //        }
    //        //        if (local.length() > 0) {
    //        try {
    //            FileInputStream fis = new FileInputStream(local);
    //            byte[] bytes = new byte[fis.available()];
    //            int l = fis.read(bytes);
    //            fis.close();
    //            String str = new String(bytes, 0, l, "UTF-8");
    //            JSONObject json = new JSONObject(str);
    //            return new SiteSpec(json);
    //        } catch (Exception e) {
    //            Log.w("loader", "fail to load site.txt from " + local, e);
    //        }
    //        //        }
    //        return new SiteSpec("empty.0", "0", new FileSpec[0],
    //                new FragmentSpec[0]);
    //    }

    // 开始解析插件的信息
    public final Intent baseUrlMap(Intent intent) {
        //        do {
        //            // already specify a class, no need to map url
        //            //            if (intent.getComponent() != null)
        //            //                break;
        //            //
        //            // only process my scheme uri
        //            Uri uri = intent.getData();
        //            if (uri == null) {
        //                break;
        //            }
        //
        //            if (uri.getScheme() == null) {
        //                break;
        //            }
        //
        //            if (!(PluginConstants.PRIMARY_SCHEME.equalsIgnoreCase(uri.getScheme()))) {
        //                break;
        //            }
        //
        //            String pluginId = intent.getStringExtra("pluginId");
        //
        //            SiteSpec site = null;
        //            if (intent.hasExtra("_site")) {
        //                site = intent.getParcelableExtra("_site");
        //            }
        //
        //            if (site == null) {
        //                site = readSite(pluginId);
        //                intent.putExtra("_site", site);
        //            }
        //
        //            String host = uri.getHost();
        //            if (TextUtils.isEmpty(host))
        //                break;
        //            host = host.toLowerCase(Locale.US);
        //            FragmentSpec fragment = site.getFragment(host);
        //            if (fragment == null)
        //                break;
        //            intent.putExtra("_fragment", fragment.name());
        //
        //            // class loader
        //            ClassLoader classLoader;
        //            if (TextUtils.isEmpty(fragment.code())) {
        //                classLoader = getClassLoader();
        //            } else {
        //                intent.putExtra("_code", fragment.code());
        //                FileSpec fs = site.getFile(fragment.code());
        //                if (fs == null)
        //                    break;
        //                classLoader = PluginClassLoader.getClassLoader(site, fs);
        //                if (classLoader == null)
        //                    break;
        //            }
        //
        //        } while (false);

        boolean validation = false;

        do {
            Uri uri = intent.getData();
            if (uri == null) {
                break;
            }

            if (uri.getScheme() == null) {
                break;
            }

            if (!(PluginConstants.PRIMARY_SCHEME.equalsIgnoreCase(uri.getScheme()))) {
                break;
            }

            PluginItem pluginItem = PluginManager.getPluginItem(mPluginId);
            if (pluginItem == null) {
                break;
            }
            //            PluginFileSpec pluginFileSpec = pluginItem.getPluginFileSpec();
            String fragmentCode = uri.getHost();
            PluginFragmentSpec fragmentSpec = pluginItem.getFragment(fragmentCode);

            intent = new Intent();
            intent.setClass(this, PluginActivity.class);
            intent.setData(uri);
            intent.putExtra("_pluginId", mPluginId);
            //        intent.putExtra("_fileSpec", mPluginFileSpec);
            //        intent.putParcelableArrayListExtra("_fragmentSpecs", mFragmentSpecs);
            intent.putExtra("_fragment", fragmentSpec.name());

            validation = true;

        } while (false);

        return intent;
    }

    @Override
    public void startActivity(Intent intent) {
        intent = urlMap(intent);
        super.startActivity(intent);
    }

    public Intent urlMap(Intent intent) {
        return baseUrlMap(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent = urlMap(intent);
        super.startActivityForResult(intent, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent,
            int requestCode) {
        intent = urlMap(intent);
        super.startActivityFromFragment(fragment, intent, requestCode);
    }

    public void startActivity(String urlSchema) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlSchema)));
    }

    public void startActivityForResult(String urlSchema, int requestCode) {
        startActivityForResult(
                new Intent(Intent.ACTION_VIEW, Uri.parse(urlSchema)),
                requestCode);
    }
}
