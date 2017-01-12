package com.jirdy.androidbasics.test;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.os.Bundle;
import android.view.WindowManager;

public class WakeLockFullScreen extends Activity {
    private Context context;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        //WakeLock获取
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "my wake lock");

//        //设置全屏，隐藏应用标题栏，系统通知栏、
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏应用标题栏。
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //wake lock 释放
        wakeLock.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //wake lock 启动
        wakeLock.acquire();
    }
}
