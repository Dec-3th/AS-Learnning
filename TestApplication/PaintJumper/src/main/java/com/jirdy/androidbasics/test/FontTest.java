package com.jirdy.androidbasics.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FontTest extends Activity {

    class RenderView extends View {

        Paint paint;
        Typeface font;
        Rect bounds = new Rect();

        public RenderView(Context context) {
            super(context);
            paint = new Paint();
            font = Typeface.createFromAsset(context.getAssets(), "xirod.ttf");
        }

        protected void onDraw(Canvas canvas) {

            //设置画布背景。
            canvas.drawRGB(0, 0, 0);
            //设置笔刷颜色，设置笔刷绘制的文本字体和文本大小，以及字体在绘制的矩形框内的绘制位置为居中。
            paint.setColor(Color.YELLOW);
            paint.setTypeface(font);
            paint.setTextSize(50);
            paint.setTextAlign(Paint.Align.CENTER);//居中

            //在屏幕横向居中，距离屏幕顶部100的位置绘制文本。
            canvas.drawText("This is a test!", canvas.getWidth() / 2, 100,
                    paint);

            String text = "This is another test o_O";
            //设置笔刷颜色，设置笔刷绘制的文本字体和文本大小，以及字体在矩形框内的绘制位置为居左。
            paint.setColor(Color.WHITE);
            paint.setTextSize(35);
            paint.getTextBounds(text, 0, text.length(), bounds);//计算文字填充的矩形大小，得到文本的实际宽高，放入bounds。
/*
关于计算文字宽高

1. 粗略计算文字宽度：
Log.d(TAG, "measureText=" + paint.measureText(str));

2. 计算文字所在矩形，可以得到宽高：
Rect rect = new Rect();
paint.getTextBounds(str, 0, str.length(), rect);
int w = rect.width();
int h = rect.height();
Log.d(TAG, "w=" +w+"  h="+h);
*/
            //在屏幕右边（刚好能放下整个文本位置），距离屏幕顶部200的位置绘制文本，这里练习一下getTextBounds。
            paint.setTextAlign(Paint.Align.LEFT);//居左
            canvas.drawText(text, canvas.getWidth() - bounds.width(), 200,//绘制位置为在屏幕右边（刚好能放下整个文本位置）。
                    paint);

            paint.setColor(Color.GREEN);
            //设置文本与屏幕的右边缘对齐,也可使用Paint.Align.Right来设置（与上面绘制效果一致。
            paint.setTextAlign(Paint.Align.RIGHT);//居右
            canvas.drawText(text, canvas.getWidth() - 1, 250,//绘制位置为canvas.getWidth() - 1。
                    paint);

            //告诉系统一有空闲就刷新画面。
            invalidate();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题栏

        //设置全屏，没有通知栏转态栏。
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //设置本例中RenderView为整个Activity的布局。
        setContentView(new RenderView(this));
    }

}