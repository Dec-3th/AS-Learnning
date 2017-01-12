package com.jirdy.androidbasics.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class RenderViewTest extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //将RenderView设置为该Activity的布局，并传入Context。onDraw(Canvas canvas)由系统调用，这里canvas为该Activity的默认画布。
        setContentView(new RenderView(this));
    }


    class RenderView extends View {
        Random rand = new Random();

        public RenderView(Context context) {
            super(context);
        }

        /**
         * 在每次该View需要自己刷新自己的时候，View.onDraw()方法被android系统调用，进行刷新。
         * 加入invalidate();后系统一空闲就对View进行刷新。
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            //每次刷新屏幕，都使用随机颜色填充屏幕，以让我们知道屏幕刷新了。
            //随机颜色选取方式：drawRGB(int r,int g, int b)随机给R G B三个通道赋予颜色，以形成最终的随机颜色。
            canvas.drawRGB(rand.nextInt(256), rand.nextInt(256),
                    rand.nextInt(256));//For each color component(成分), we simply specify a random number between 0 and 255

            /*
             invalidate()方法 ：
             说明：请求重绘View树，即draw()过程，假如视图发生大小没有变化就不会调用layout()过程，并且只绘制那些“需要重绘的”
                视图，即谁(View的话，只绘制该View ；ViewGroup，则绘制整个ViewGroup)请求invalidate()方法，就绘制该视图。
            一般引起invalidate()操作的函数如下：
            1、直接调用invalidate()方法，请求重新draw()，但只会绘制调用者本身。
            2、setSelection()方法 ：请求重新draw()，但只会绘制调用者本身。
            3、setVisibility()方法 ： 当View可视状态在INVISIBLE转换VISIBLE时，会间接调用invalidate()方法，继而绘制该View。
            4、setEnabled()方法 ： 请求重新draw()，但不会重新绘制任何视图包括该调用者本身。
             */
            invalidate();
        }
    }
}