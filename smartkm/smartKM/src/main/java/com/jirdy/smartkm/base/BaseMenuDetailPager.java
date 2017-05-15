package com.jirdy.smartkm.base;

import android.app.Activity;
import android.view.View;

/**
 * 侧边栏 菜单详情页 基类
 * Created by december on 17-5-13.
 */

public abstract class BaseMenuDetailPager {

    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData(){

    }
}
