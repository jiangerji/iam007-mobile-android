package cn.iam007.app.mall.home;

import java.util.Hashtable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import cn.iam007.app.common.utils.logging.LogUtil;
import cn.iam007.app.mall.dynamic.DynamicFragment;
import cn.iam007.app.mall.product.ProductFragment;
import cn.iam007.app.mall.product.TagsFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private Hashtable<Integer, Fragment> mFragmentTable = new Hashtable<Integer, Fragment>();

    private Class<?> mFragmentClass[] = { RecommendFragment.class,
            DynamicFragment.class, TagsFragment.class,
            ProductFragment.class };

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return mFragmentClass.length;
    }

    @Override
    public Fragment getItem(int position) {
        LogUtil.d("MyFragmentPagerAdapter", "getItem:" + position);
        Fragment fragment = mFragmentTable.get(position);
        if (fragment == null) {
            Class<?> fragmentClass = mFragmentClass[position];
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                mFragmentTable.put(position, fragment);
            } catch (Exception e) {
                LogUtil.d("excetpion", "MyFragmentPagerAdapter:" + e.toString());
            }
        }
        return fragment;
    }

}
