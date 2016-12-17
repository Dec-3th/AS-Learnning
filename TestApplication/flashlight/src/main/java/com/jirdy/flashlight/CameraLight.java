package com.jirdy.flashlight;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.jirdy.flashlight.ads.MogoAds;
import com.jirdy.flashlight.ads.WanDouJia;

/*
萌猫手电筒，史上最萌的手电筒，体积小，耗电量低，启动速度快，关键是很萌，想不想有一只猫咪活跃在你的屏幕上，会照亮黑暗，会暖床，会卖萌，求收养。
史上最萌的手电筒，使用方便，占用资源极低，同时针对闪光灯的使用进行了省电优化，内置摩斯电码式的闪光求救功能，妹子出行必备，在黑夜中遇到危险时打开求救信号，让远处的人看到并施救。
 */
public class CameraLight extends Activity {

    private static final String TAG = "Jirdy.Light";

    private Camera mCamera;
    private Camera.Parameters parameters;
    private long mExitTime = 0;
    private ToggleButton mTbtnLight, mTbtnSOSlight;
    private SOSLightThread sosLightThread;

    private CompoundButton.OnCheckedChangeListener mBtnLightListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                mTbtnSOSlight.setChecked(false);
                openLight(false);
            } else {
                closeLight();
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mSOSLightListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mTbtnLight.setChecked(false);
                sosLightThread.onThreadResume();
            } else {
                sosLightThread.onThreadPause();
            }
        }
    };

    /**
     * 打开手电
     */
    protected void openLight(boolean isSOS) {
        if (!sosLightThread.isPause() || isSOS == false) {//如果已经让线程等待，则不开闪光灯.
            parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }

    /**
     * 关闭手电
     */
    protected void closeLight() {
        if (parameters.getFlashMode() != Parameters.FLASH_MODE_OFF) {
            parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initValue();
        sosLightThread = new SOSLightThread(this);
        sosLightThread.start();

        /*------------------添加横幅广告-------------------*/
        try {
            final Activity mActivity = CameraLight.this;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //放横幅广告的容器.
                    final ViewGroup container = (ViewGroup) mActivity.findViewById(R.id.banner_container);
                    if (container != null) {
                        //豌豆荚广告
//                        WanDouJia.addBanner(mActivity, container);
                        MogoAds.addBanner(mActivity, container);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        /*------------------添加横幅广告-------------------*/
    }

    private void initValue() {
        mCamera = Camera.open();
        parameters = mCamera.getParameters();
        mTbtnLight = (ToggleButton) findViewById(R.id.mLightBtn);
        mTbtnSOSlight = (ToggleButton) findViewById(R.id.mSosBtn);

        mTbtnLight.setOnCheckedChangeListener(mBtnLightListener);
        mTbtnSOSlight.setOnCheckedChangeListener(mSOSLightListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                // 这里穿插一个很简单的双击退出的功能，有点意思
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    //关闭开关
                    mTbtnLight.setChecked(false);
                    mTbtnSOSlight.setChecked(false);
                    //关闭灯光，线程进入等待状态
                    sosLightThread.onThreadPause();

                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    //退出前关闭线程
                    sosLightThread.closeThread();
                    finish();
                }
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mCamera.release();
        sosLightThread.interrupt();

        //清理广告
        MogoAds.cleanAds();
        super.onDestroy();
    }

}
