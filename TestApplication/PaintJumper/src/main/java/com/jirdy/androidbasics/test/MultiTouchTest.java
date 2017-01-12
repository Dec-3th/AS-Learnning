package com.jirdy.androidbasics.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

@TargetApi(5)
public class MultiTouchTest extends AppCompatActivity implements View.OnTouchListener {
    StringBuilder builder = new StringBuilder();
    TextView textView;
    float[] x = new float[10];
    float[] y = new float[10];
    boolean[] touched = new boolean[10];
    int[] id = new int[10];

    private void updateTextView() {
        builder.setLength(0);
        for (int i = 0; i < 10; i++) {

            builder.append(touched[i]);
            builder.append(", ");
            builder.append(id[i]);
            builder.append(", ");
            builder.append(x[i]);
            builder.append(", ");
            builder.append(y[i]);
            builder.append("\n");
        }
        textView.setText(builder.toString());
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        textView.setText("Touch and drag (multiple fingers supported)!");
        textView.setOnTouchListener(this);
        setContentView(textView);
        for (int i = 0; i < 10; i++) {
            id[i] = -1;
        }
        updateTextView();
    }

    public boolean onTouch(View v, MotionEvent event) {
        //通过按位与，获取低8位值，其值就是触屏类型。
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        /*
    we perform a bitwise AND operation using the integer we get from the MotionEvent.getAction()
method and a constant called MotionEvent.ACTION_POINTER_ID_MASK.
    That constant has a value of 0xff00, so we essentially make all bits 0, other than bits 8 to 15,
which hold the pointer index of the event. The lower 8 bits of the integer returned by
event.getAction() hold the value of the event type, such as MotionEvent.ACTION_DOWN and its
siblings. We essentially throw away the event type by this bitwise operation. The shift should make
a bit more sense now. We shift by MotionEvent.ACTION_POINTER_ID_SHIFT, which has a value of 8,
so we basically move bits 8 through 15 to bits 0 through 7, arriving at the actual pointer index of
the event. With this, we can then get the coordinates of the event, as well as the pointer identifier.
         */
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >>
                MotionEvent.ACTION_POINTER_ID_SHIFT;//通过位运算和Mask巧妙的计算出当前触摸点id的index。

        int pointerCount = event.getPointerCount();//获取当前触屏的手指个数.

        for (int i = 0; i < 10; i++) {//最多10个手指.
            if (i >= pointerCount) {
                touched[i] = false;
                id[i] = -1;
                continue;
            }
            if (event.getAction() != MotionEvent.ACTION_MOVE&& i != pointerIndex) {//对比计算出的index和实际的顺序.
                // if it's an up/down/cancel/out event, mask the id to see if we should process it for this touch point
                continue;
            }
            // A pointer identifier is an arbitrary number that uniquely identifies one instance of a pointer touching the screen
            int pointerId = event.getPointerId(i);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touched[i] = true;
                    id[i] = pointerId;
                    x[i] = (int) event.getX(i);
                    y[i] = (int) event.getY(i);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_CANCEL:
                    touched[i] = false;
                    id[i] = -1;
                    x[i] = (int) event.getX(i);
                    y[i] = (int) event.getY(i);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touched[i] = true;
                    id[i] = pointerId;
                    x[i] = (int) event.getX(i);
                    y[i] = (int) event.getY(i);
                    break;
            }
        }
        updateTextView();

        return true;// return true, indicating that we processed the touch event.
    }
}
