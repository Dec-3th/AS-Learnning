package com.jirdy.adtestapp.ads;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.wandoujia.ads.sdk.Ads;

/**
 * Created by Administrator on 2016/5/17.
 * 豌豆荚官方接入，独立接入：
 * 豌豆荚需在libs文件夹下导入WandoujiaSDK.aar
 * <p/>
 * build.gradle中添加：
 * compile 'com.android.support:support-v4:23.3.0'
 * //引用WandoujiaSDK.aar
 * compile(name: 'WandoujiaSDK', ext: 'aar')//使用本地aar
 * //下面是wandoujia sdk的依赖包
 * compile 'com.google.code.gson:gson:2.3.1'
 * compile 'com.squareup.okhttp:okhttp:2.2.0'
 * compile 'com.squareup.okhttp:okhttp-urlconnection:2.2.0'
 * compile 'com.squareup:otto:1.3.6'
 * compile 'com.squareup.picasso:picasso:2.4.0'
 */
public class WanDouJia {
    public static final String TAG = "Jirdy.WanDouJia";
    private static final String APP_ID = "100039802";
    private static final String SECRET_KEY = "f42b31e480e9769b03947709272c8e9e";
    private static final String BANNER = "d94ad6e63dd990aa294b7e3d2912da15";
//    private static final String INTERSTITIAL = "2d1ccfaaab9a09d4be8eec7d86ccca77";
//    private static final String APP_WALL = "66caff24c98802b40dbb014bbf39f0be";

    public static void addBanner(final Activity activity, final ViewGroup container) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Ads.init(activity, APP_ID, SECRET_KEY);
                    return true;
                } catch (Exception e) {
                    Log.e(TAG, "error ads-sample init failed.", e);
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
//                final ViewGroup container = (ViewGroup) activity.findViewById(R.id.banner_container);
                Log.i(TAG, "onPostExecute: " + success);
                if (success) {
                    /**
                     * pre load
                     */
                    Ads.preLoad(BANNER, Ads.AdFormat.banner);
//                    Ads.preLoad(INTERSTITIAL, Ads.AdFormat.interstitial);
//                    Ads.preLoad(APP_WALL, Ads.AdFormat.appwall);

                    /**
                     * add ad views
                     */
                    View bannerView = Ads.createBannerView(activity, BANNER);
                    if (bannerView != null)
                        container.addView(bannerView, new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));

//                    Button btI = new Button(activity);
//                    btI.setText("interstitial");
//                    btI.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Ads.showInterstitial(activity, INTERSTITIAL);
//                        }
//                    });
//                    container.addView(btI);
//
//                    Button btW = new Button(activity);
//                    btW.setText("app wall");
//                    btW.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Ads.showAppWall(activity, APP_WALL);
//                        }
//                    });
//                    container.addView(btW);
                } else {
//                    TextView errorMsg = new TextView(activity);
//                    errorMsg.setText("init failed");
//                    container.addView(errorMsg);
                    Log.e(TAG, "init Wandoujia ads failed");
                }
            }
        }.execute();
    }
}
