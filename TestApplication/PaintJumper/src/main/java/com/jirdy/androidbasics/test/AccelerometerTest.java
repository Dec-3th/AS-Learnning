package com.jirdy.androidbasics.test;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class AccelerometerTest extends Activity implements SensorEventListener {
    TextView textView;
    StringBuilder builder = new StringBuilder();
    int SENSOR_TYPE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        setContentView(textView);
        SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER;//加速器感应测试
//        SENSOR_TYPE = Sensor.TYPE_ORIENTATION;//方向感应（罗盘）测试

        //1、获取SensorManager（SensorManager是android系统提供的系统服务）。
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //2、判断手机上是否安装了传感器（获取所有类型为TYPE_ACCELEROMETER的传感器，判断是否获取到）。
        if (manager.getSensorList(SENSOR_TYPE).size() == 0) {
            textView.setText("No accelerometer installed");

        } else {
            //3、从重力传感器List中获取一个传感器（一般手机上只装有一个传感器）。
            Sensor accelerometer = manager.getSensorList(
                    Sensor.TYPE_ACCELEROMETER).get(0);

            //4、向SensorManager注册传感器的监听器。
            //SensorManager.SENSOR_DELAY_GAME表示监听器对传感器的监听频率（即多久检查一次传感器的值，判断改变）
            if (!manager.registerListener(this, accelerometer, SENSOR_TYPE)) {
                textView.setText("Couldn't register sensor listener");
            }
        }
    }

    int screenRotation;
    //每一维对应一个屏幕方向，前两位表示x轴和y轴值的符号（x:左正右负,y:下正上负），后两位表示SensorEvent.values[i]的i值，1表示y轴值，0表示x轴值。
    static final int ACCELEROMETER_AXIS_SWAP[][] = {//假设x,y,z为传感器的值，sx,sy,sz为以屏幕方向为基准确定的传感器的值。
            {1, -1, 0, 1}, //  ROTATION_0：  此时 sx = x, sy = -y
            {-1, -1, 1, 0}, // ROTATION_90： 此时 sx = -y, sy = -x
            {-1, 1, 0, 1}, //  ROTATION_180：此时 sx = -x, sy = y
            {1, 1, 1, 0}}; //  ROTATION_270：此时 sx = y, sy = x

    /**
     * 当新的感应器事件到达时调用该方法（即传感器的值改变时）.
     *
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {

        //原来的用法，或说是屏幕锁定竖屏向下时的用法
//        float screenX = event.values[0];
//        float screenY = event.values[1];
//        float screenZ = event.values[2];

        //SensorEvent类的values数组存放传感器的值，values[0]为x轴的值，values[1]为y轴，values[2]为z轴。
        final int[] as = ACCELEROMETER_AXIS_SWAP[screenRotation];
        float screenX = (float) as[0] * event.values[as[2]];//符号*(x或y值)
        float screenY = (float) as[1] * event.values[as[3]];
        float screenZ = event.values[2];
        // use screenX, screenY, and screenZ as your accelerometer values now!


        builder.setLength(0);
        builder.append("x: " + screenX);
        builder.append(", y: " + screenY);
        builder.append(", z: " + screenZ);

        textView.setText(builder.toString());
    }

    @Override
    public void onResume() {
        WindowManager windowMgr =
                (WindowManager) this.getSystemService(Activity.WINDOW_SERVICE);
        /*
            getOrientation() is deprecated in Android 8 but is the same as getRotation(),
            which is the rotation from the natural orientation of the device
         */
        screenRotation = windowMgr.getDefaultDisplay().getOrientation();

        super.onResume();
    }


    //当感应器的精确度改变时，调用该方法，accuracy（精度）。
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }
}
