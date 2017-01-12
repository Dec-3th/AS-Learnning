package com.jirdy.androidbasics.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class SurfaceViewTest extends Activity {

    FastRenderView renderView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        renderView = new FastRenderView(this);
        setContentView(renderView);
    }

    protected void onResume() {
        super.onResume();
        //启动FastRenderView线程，
        renderView.resume();
    }

    protected void onPause() {
        super.onPause();
        //完全的关闭FastRenderView线程，下一次要使用该线程需要重新创建并启动
        renderView.pause();
    }

    /*
    为了不阻塞主线程，我们使用SurfaceView implements Runnable，在里面启用新的线程，执行UI的更新绘制工作。
    Surface是一个原始缓冲区，用于屏幕中渲染特定的视图。
     */
    class FastRenderView extends SurfaceView implements Runnable {

        Thread renderThread = null;
        /*
        SurfaceHolder为Surface的包装，提供了lockCanvas（锁定Surface）和unlockAndPost（解锁Surface）方法
        能够形成一个同步锁，来安全的在画布上绘图。
         */
        SurfaceHolder holder;

        /*
        running变量volatile（不稳定）关键词修饰。
        volatile 修饰的成员变量在每次被线程访问时，都强迫从共享内存中重读该成员变量的值。而且，当成员变量发生变化时，
        强迫线程将变化值回写到共享内存。这样在任何时刻，两个不同的线程总是看到某个成员变量的同一个值。

        volatile关键字就是提示VM：对于这个成员变量不能保存它的私有拷贝，而应直接与共享成员变量交互。
        使用建议：在两个或者更多的线程访问的成员变量上使用volatile。当要访问的变量已在synchronized代码块中，或者为常量时，不必使用。
        由于使用屏蔽掉了VM中必要的代码优化，所以在效率上比较低，因此一定在必要时才使用此关键字。
         */
        volatile boolean running = false;

        public FastRenderView(Context context) {
            super(context);
            holder = getHolder();
        }

        //恢复线程的运行
        public void resume() {
            running = true;
            /*
            每次调用该方法都新建一个Thread，
            new Thread(this)这里的this直接设置FastRenderView实例自身为Runnable对象来执行
            (we set the FastRenderView instance itself as the Runnable of the thread)。
             */
            renderThread = new Thread(this);
            renderThread.start();
        }

        public void run() {

            while(running) {//当running为false时立即停止线程的执行。

                //判断当前Surface是否有效（已经创建好可以使用），若Surface无效，跳过该次循环，不执行后面操作。
                if(!holder.getSurface().isValid())
                    continue;

                Canvas canvas = holder.lockCanvas();//锁定画布
                canvas.drawColor(Color.GRAY);//绘图
                holder.unlockCanvasAndPost(canvas);//解锁画布
            }
        }

        /**
         * 完全停止该线程，停止以后该方法才结束(return)
         */
        public void pause() {
            running = false;
            while(true) {
                try {

                    // 阻塞当前线程，等待thread执行完毕，可以带参数，参数为最多等待多长时间
                    renderThread.join();
                    return;//由于调用join()，该方法在renderThread完全停止之前，不会return。
                } catch (InterruptedException e) {
                    // Thread.join()可以被interrupt，调用AsyncTask.cancel(true);即可退出等待
                    // retry
                }
            }
        }
    }
}
