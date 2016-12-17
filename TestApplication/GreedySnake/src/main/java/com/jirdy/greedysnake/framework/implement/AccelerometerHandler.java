package com.jirdy.greedysnake.framework.implement;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 传感器事件处理，没有对频幕方向的改变进行适配兼容。
 * 注意：由于Android处理所有的传感器的方式一样，故将本类中TYPE_ACCELEROMETER改为其他传感器的话，
 *      就能监控其他种类传感器变化，如罗盘Sensor.TYPE_ORIENTATION。
 * Created by Administrator on 2016/7/5.
 */
public class AccelerometerHandler implements SensorEventListener {
    float accelX;
    float accelY;
    float accelZ;

    public AccelerometerHandler(Context context) {

        // 1、获取SensorManager（SensorManager是android系统提供的系统服务）。
        SensorManager manager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        // 2、判断手机上是否安装了传感器（获取所有类型为TYPE_ACCELEROMETER的传感器，判断是否获取到）。*/
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {

            // 3、从重力传感器List中获取一个传感器（一般手机上只装有一个传感器）。
            Sensor accelerometer = manager.getSensorList(
                    Sensor.TYPE_ACCELEROMETER).get(0);

            // 4、向SensorManager注册传感器的监听器。
            //SensorManager.SENSOR_DELAY_GAME表示监听器对传感器的监听频率（即多久检查一次传感器的值，判断改变）
            manager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }

    /**
     * 当新的感应器事件到达时调用该方法（即传感器的值改变时）.
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        //从SensorEvent获取屏幕锁定竖屏向下时，当前传感器x,y,z的值
        accelX = event.values[0];
        accelY = event.values[1];
        accelZ = event.values[2];
    }

    public float getAccelX() {
        return accelX;
    }

    public float getAccelY() {
        return accelY;
    }

    public float getAccelZ() {
        return accelZ;
    }
}