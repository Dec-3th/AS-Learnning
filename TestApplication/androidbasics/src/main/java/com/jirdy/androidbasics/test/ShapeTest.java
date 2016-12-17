package com.jirdy.androidbasics.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ShapeTest extends Activity {
    class RenderView extends View {
        Paint paint;
        public RenderView(Context context) {
            super(context);
            paint = new Paint();
        }
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(255, 255, 255);//白色背景填充整张画布
            //设置红色笔刷，画一条左上到右下的屏幕对角线。
            paint.setColor(Color.RED);
            canvas.drawLine(0, 0, canvas.getWidth()-1, canvas.getHeight()-1, paint);

            //设置笔刷风格为描边，笔刷颜色为0x770000ff，在屏幕中心画一个半径为40的圆圈
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0x770000ff);
            paint.setStrokeWidth(3.0f);//设置笔刷宽度为3.0
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, 400, paint);

            //设置笔刷风格为填充，笔刷颜色为0xff00ff00，在屏幕中画一个长方形。
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xff00ff00);
            canvas.drawRect(100, 100, 400, 400, paint);

            paint.setColor(Color.BLUE);
            canvas.drawLine(canvas.getWidth()-1, 0, 0, canvas.getHeight()-1, paint);

            //告诉系统，一有资源就执行上面操作（执行该方法）
            invalidate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new RenderView(this));
    }
}

