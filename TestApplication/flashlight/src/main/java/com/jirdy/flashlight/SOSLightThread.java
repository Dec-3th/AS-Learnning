package com.jirdy.flashlight;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Jirdy on 2016/5/12.
 */
public class SOSLightThread extends Thread {

    private boolean isClose = false;
    private boolean isPause = true;
    public static final int OPEN_LIGHT = 0x0010;
    public static final int CLOSE_LIGHT = 0x0020;
    private CameraLight cameraLight;

    public SOSLightThread(CameraLight cameraLight) {
        this.cameraLight = cameraLight;
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OPEN_LIGHT:
                    cameraLight.openLight(true);
                    break;
                case CLOSE_LIGHT:
                    cameraLight.closeLight();
                    break;
                case 1000:

                    break;
            }
        }

    };


    @Override
    public void run() {
        while (!isClose && !isInterrupted()) {

            if (!isPause) {
                /**
                 * SOS信号原理：一轮：三短，三长，三短，如下。
                 * 注意：数字表示开关闪光灯，基数开偶数关，- 表示一个单位的发光时间，* 表示一个单位间隔时间。
                 * 1-2 * 3-4 * 5-6 *** 7---8 * 9---10 * 11---12 *** 12-14 * 15-16 * 17-18
                 */
                for (int j = 1; j <= 18; j++) {

                    if (j == 1 || j == 7 || j == 13)//下一组发光前的间隔
                        threadSleep(1000);

                    if (j <= 6 || j > 12) {//三个短光
                        if (j % 2 == 0) {
                            mHandler.obtainMessage(CLOSE_LIGHT).sendToTarget();
                        } else {
                            mHandler.obtainMessage(OPEN_LIGHT).sendToTarget();
                        }
                        threadSleep(400);
                        continue;
                    }

                    if (j > 6 && j <= 12) {//中间的3个长光
                        if (j % 2 == 0) {
                            mHandler.obtainMessage(CLOSE_LIGHT).sendToTarget();
                        } else {
                            mHandler.obtainMessage(OPEN_LIGHT).sendToTarget();
                        }
                        threadSleep(900);
                    }
                }
                threadSleep(1500);//下一轮求救信号发出前间隔
            } else {
                onThreadWait();
            }

        }
    }


    private void threadSleep(long ms) {
//        try {
//            synchronized (this) {
//                this.wait(ms);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isPause() {
        return isPause;
    }

    /**
     * 暂停线程
     */
    public synchronized void onThreadPause() {
        isPause = true;
    }

    /**
     * 线程等待,不提供给外部调用
     */
    private void onThreadWait() {
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程继续运行
     */
    public synchronized void onThreadResume() {
        isPause = false;
        this.notify();
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }

    /**
     * 关闭线程
     */
    public synchronized void closeThread() {
        try {
            notify();
            setClose(true);
            interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
