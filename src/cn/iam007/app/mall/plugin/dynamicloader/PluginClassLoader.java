package cn.iam007.app.mall.plugin.dynamicloader;

import java.io.File;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Application;
import cn.iam007.app.common.cache.CacheConfiguration;
import cn.iam007.app.mall.IAM007Application;
import cn.iam007.app.mall.plugin.model.PluginFileSpec;
import dalvik.system.DexClassLoader;

public class PluginClassLoader extends DexClassLoader {
    //    FileSpec file;
    PluginFileSpec fileSpec;
    PluginClassLoader[] deps;

    //    @Deprecated
    //    PluginClassLoader(FileSpec file, String dexPath, String optimizedDirectory,
    //            String libraryPath, ClassLoader parent, PluginClassLoader[] deps) {
    //        super(dexPath, optimizedDirectory, libraryPath, parent);
    //        //        this.file = file;
    //        this.deps = deps;
    //    }

    PluginClassLoader(PluginFileSpec fileSpec, String dexPath,
            String optimizedDirectory,
            String libraryPath, ClassLoader parent, PluginClassLoader[] deps) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
        this.fileSpec = fileSpec;
        this.deps = deps;
    }

    /**
     * @return the fileSpec
     */
    public PluginFileSpec getFileSpec() {
        return fileSpec;
    }

    /**
     * @param fileSpec
     *            the fileSpec to set
     */
    public void setFileSpec(PluginFileSpec fileSpec) {
        this.fileSpec = fileSpec;
    }

    @SuppressLint("NewApi")
    @Override
    protected Class<?> loadClass(String className, boolean resolve)
            throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(className);
        if (clazz != null) {
            return clazz;
        }

        try {
            clazz = getParent().loadClass(className);
        } catch (ClassNotFoundException e) {
        }

        if (clazz != null) {
            return clazz;
        }

        if (deps != null) {
            for (PluginClassLoader c : deps) {
                try {
                    clazz = c.findClass(className);
                    break;
                } catch (ClassNotFoundException e) {
                }
            }
        }

        if (clazz != null) {
            return clazz;
        }

        clazz = findClass(className);
        return clazz;
    }

    static final HashMap<String, PluginClassLoader> loaders = new HashMap<String, PluginClassLoader>();

    /**
     * return the classloader of the plugin, if the plugin apk file is not
     * exsites on the disk, return null
     * 
     * @param pluginId
     * @return
     */
    public static
            PluginClassLoader getClassLoader(PluginFileSpec pluginFileSpec) {
        String pluginId = pluginFileSpec.getPluginId();

        PluginClassLoader classLoader = loaders.get(pluginId);
        if (classLoader != null) {
            return classLoader;
        }

        // TODO: 加载该插件依赖的插件

        // 加载classloader
        Application application = IAM007Application.getCurrentApplication();
        File dir = CacheConfiguration.getCacheDirPlugin();

        // 获取插件apk
        dir = new File(dir, pluginId);
        File path = new File(dir, pluginFileSpec.getPluginMD5() + ".apk");
        if (!path.isFile()) {
            return null;
        }

        File outdir = new File(dir, "dex");
        outdir.mkdir();
        classLoader = new PluginClassLoader(pluginFileSpec,
                path.getAbsolutePath(),
                outdir.getAbsolutePath(),
                null,
                application.getClassLoader(),
                null);
        loaders.put(pluginId, classLoader);
        return classLoader;
    }

    /**
     * return null if not available on the disk
     */
    //    @Deprecated
    //    public static
    //            PluginClassLoader getClassLoader(SiteSpec site, FileSpec file) {
    //        PluginClassLoader cl = loaders.get(file.id());
    //        if (cl != null) {
    //            return cl;
    //        }
    //
    //        // 加载该插件依赖的插件的的classloader
    //        String[] deps = file.deps();
    //        PluginClassLoader[] ps = null;
    //        if (deps != null) {
    //            ps = new PluginClassLoader[deps.length];
    //            for (int i = 0; i < deps.length; i++) {
    //                FileSpec pf = site.getFile(deps[i]);
    //                if (pf == null) {
    //                    return null;
    //                }
    //
    //                PluginClassLoader l = getClassLoader(site, pf);
    //                if (l == null) {
    //                    return null;
    //                }
    //                ps[i] = l;
    //            }
    //        }
    //
    //        Application application = IAM007Application.getCurrentApplication();
    //        File dir = CacheConfiguration.getCacheDirPlugin();
    //
    //        // 获取插件apk
    //        dir = new File(dir, file.id());
    //        File path = new File(dir, TextUtils.isEmpty(file.md5()) ? "1.apk"
    //                : file.md5() + ".apk");
    //        if (!path.isFile()) {
    //            return null;
    //        }
    //
    //        File outdir = new File(dir, "dex");
    //        outdir.mkdir();
    //        cl = new PluginClassLoader(file,
    //                path.getAbsolutePath(),
    //                outdir.getAbsolutePath(),
    //                null,
    //                application.getClassLoader(),
    //                ps);
    //        loaders.put(file.id(), cl);
    //        return cl;
    //    }
}
