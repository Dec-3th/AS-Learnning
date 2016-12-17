package com.jirdy.androidbasics.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 关键：该Activity实现了View.OnTouchListener来监听触屏事件，
 * 并通过实现 onTouch(View v, MotionEvent event)接口来获取触屏事件的参数。
 */
public class SingleTouchTest extends AppCompatActivity implements View.OnTouchListener {

    StringBuilder builder = new StringBuilder();
    /*
    将手指触摸点坐标显示在textView中，同时显示触发的MotionEvent种类
     */
    TextView textView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);

        textView.setText("Touch and drag (one finger only)!");

        /*
        将textView 注册为 Activity的布局，故这里使用textView.setOnTouchListener(this)，
        即使用textView来接收触屏事件（作为整个Activity的布局）
        */
        textView.setOnTouchListener(this);//设置触屏监听
        setContentView(textView);
    }

    public boolean onTouch(View v, MotionEvent event) {
        builder.setLength(0);//每次事件刷新，清空StringBuilder

        //判断MotionEvent事件种类，将触发的事件种类tag放入builder
        switch (event.getAction()) {//获取事件类型
            case MotionEvent.ACTION_DOWN:
                builder.append("down, ");
                break;
            case MotionEvent.ACTION_MOVE:
                builder.append("move, ");
                break;
            case MotionEvent.ACTION_CANCEL:
                builder.append("cancel, ");
                break;
            case MotionEvent.ACTION_UP:
                builder.append("up, ");
                break;
        }

        //通过event获取手指触摸点的坐标，并显示到textView
        builder.append(event.getX());
        builder.append(", ");
        builder.append(event.getY());

        String text = builder.toString();
        Log.d("TouchTest", text);
        textView.setText(text);

        return true;//永远返回true（ tell the TextView that we just consumed the event）
    }
}
