//package com.jirdy.greedysnake.framework.implement;
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//
///**
// * 罗盘处理，和重力传感器全部一样，只是传感器名字改了，从TYPE_ACCELEROMETER改为TYPE_ORIENTATION
// * 其他传感器也是和本类基本一样的处理方式。(在本游戏中用不到，姑且注释掉）
// * Created by Administrator on 2016/7/5.
// */
//public class CompassHandler implements SensorEventListener {
//
//    float yaw;
//    float pitch;
//    float roll;
//
//    public CompassHandler(Context context) {
//
//        SensorManager manager = (SensorManager) context
//                .getSystemService(Context.SENSOR_SERVICE);
//
//        if (manager.getSensorList(Sensor.TYPE_ORIENTATION).size() != 0){
//
//            Sensor compass = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
//
//            manager.registerListener(this, compass,
//                    SensorManager.SENSOR_DELAY_GAME);
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // nothing to do here
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        yaw = event.values[0];
//        pitch = event.values[1];
//        roll = event.values[2];
//    }
//
//    public float getYaw() {
//        return yaw;
//    }
//
//    public float getPitch() {
//        return pitch;
//    }
//
//    public float getRoll() {
//        return roll;
//    }
//
//}
