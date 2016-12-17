package com.jirdy.adtestapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import com.inmobi.sdk.InMobiSdk;
import com.jirdy.adtestapp.ads.MogoAds;
import com.jirdy.adtestapp.ads.WanDouJia;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.constants.Constants;
import com.qq.e.comm.managers.GDTADManager;
import com.qq.e.comm.util.GDTLogger;

public class MainActivity extends Activity {

    private static final String TAG = "Jirdy.ADTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*------------------添加横幅广告-------------------*/
        try {
            final Activity mActivity = MainActivity.this;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //放横幅广告的容器.
                    final LinearLayout container = (LinearLayout) mActivity.findViewById(R.id.banner_container);
                    if (container != null) {
                        //豌豆荚广告
//                        WanDouJia.addBanner(mActivity, container);
                        MogoAds.addBanner(mActivity, container);
                    }

                    InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);

                    // 创建Banner广告AdView对象
                    // appId : 在 http://e.qq.com/dev/ 能看到的app唯一字符串
                    // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
                    BannerView banner = new BannerView(mActivity, ADSize.BANNER, "1105399676", "5040513153139803");
                    //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
                    Log.e(TAG, "QQ BannerView Create");
                    banner.setShowClose(true);
                    banner.setRefresh(0);
                    banner.setADListener(new AbstractBannerADListener() {

                        @Override
                        public void onNoAD(int arg0) {
                            Log.e(TAG, "QQ BannerNoAD，eCode=" + arg0);
                        }

                        @Override
                        public void onADReceiv() {
                            Log.e(TAG, "QQ ONBannerReceive");
                        }
                    });

                    container.addView(banner);
                    /* 发起广告请求，收到广告数据后会展示数据   */
                    banner.loadAD();
                    Log.e(TAG, "QQ BannerView isShown: " + banner.isShown());

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        /*------------------添加横幅广告-------------------*/
    }
}
