package com.jirdy.androidbasics.test;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;

public class BitmapTest extends Activity {

    Bitmap bob565;
    Bitmap bob4444;

    class RenderView extends View {

        Rect dst = new Rect();

        public RenderView(Context context) {
            super(context);

            //RGB_888格式图片，没有包含alpha值（透明度信息），不能包含透明像素，有透明像素也会变成白色，原图bobrgb888.png就包含白色边框。
            //ARGB_8888格式图片，包含有alpha值（透明度），故包含透明像素，在Canvas绘图时，会自动将透明像素与背景进行融合，透明度为100时只能看到背景。
            try {
                //从assets文件夹下获取png图片为输入流，BitmapFactory转化输入流为Bitmap。
                AssetManager assetManager = context.getAssets();
                //导入一张RGB888格式的png图片，该图片包含有不透明（透明度为0)的白色边框，故画在屏幕上时，没有任何颜色混合，还是白色边框。
                InputStream inputStream = assetManager.open("bobrgb888.png");
                bob565 = BitmapFactory.decodeStream(inputStream);
                inputStream.close();//关闭输入流。
                Log.d("ANDBasic",
                        "bobrgb888.png format: " + bob565.getConfig());

                //导入一张ARGB8888格式的png图片，该图片包含有透明边框，故画在屏幕上时，Canvas将自动进行颜色混合，这就能看到下面图层内容。
                inputStream = assetManager.open("bobargb8888.png");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;//设置Bitmap的图片格式为ARGB_4444
                //转化为Bitmap
                bob4444 = BitmapFactory
                        .decodeStream(inputStream, null, options);
                inputStream.close();
                Log.d("ANDBasic",
                        "bobargb8888.png format: " + bob4444.getConfig());

            } catch (IOException e) {
                e.printStackTrace();
                // silently ignored, bad coder monkey, baaad!
            } finally {
                // we should really close our input streams here.
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawRGB(0, 0, 0);
            dst.set(50, 50, 350, 350);
            /*
            第二个参数为Rect src，第三个参数为Rect dst。无论src区域大于或小于dst区域，src区域图片都会自适应的填充在dst区域中。
            src=null，表示画整张图，src!=null表示从图片上截取src大小的矩形，进行作画。
             */
            canvas.drawBitmap(bob565, null, dst, null);//画整张图到dst区域
            canvas.drawBitmap(bob4444, 100, 100, null);//画整张图到(100,100)坐标处。
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Always make sure you call the Bitmap.recycle() method if you no longer need a Bitmap.
        //释放图片占用空间
        bob565.recycle();
        bob4444.recycle();
        bob565 = null;
        bob4444 = null;
    }

}
