package com.jirdy.flashlight.ads;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.adview.AdsMogoLayout;
import com.adsmogo.controller.listener.AdsMogoListener;

/**
 * Created by Administrator on 2016/5/17.
 * 芒果广告平台
 */
public class MogoAds {
    public static final String TAG = "Jirdy.Light.MogoAds";
    private static String mogoID = "82a6c4f89b244377b9d1e1f5fb9ed7c7";//me:82a6c4f89b244377b9d1e1f5fb9ed7c7 mogo:3685b5d22e2f43649a4f61afde5c9ee4
    private static AdsMogoLayout adsMogoLayoutCode;

    public static void addBanner(final Activity mActivity, final ViewGroup container){

        com.adsmogo.util.L.debug=true;//输出芒果日志

        //快速添加banner广告，无需手动add到视图中,芒果SDK会自动将广告
        adsMogoLayoutCode = new AdsMogoLayout(mActivity,
                mogoID);
        adsMogoLayoutCode.setAdsMogoListener(new MyAdsMogoListener());

        container.addView(adsMogoLayoutCode, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    }

    public static void debug(String TAG, String str) {
//        if (MainActivity.debugmodel)
        Log.i(TAG, str);
    }

    public static void cleanAds(){
        //清除广告资源
        AdsMogoLayout.clear();
        // 清除adsMogoLayout实例所产生用于多线程缓冲机制的线程池
        adsMogoLayoutCode.clearThread();
    }

}
