package com.jirdy.smartkm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jirdy.smartkm.utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {
    public static final String TAG = "JR.GuideActivity";

    private ViewPager viewPager;
    private int[] mImageIds = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    private ArrayList<ImageView> imageViewArrayList;
    private LinearLayout llContainer;
    private ImageView redPointImg;
    private int pointWith; //小圆点间距
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        redPointImg = (ImageView) findViewById(R.id.img_red_point);
        startButton = (Button)findViewById(R.id.start_button);

        imageViewArrayList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            //初始化ImageView
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(mImageIds[i]);
            imageViewArrayList.add(imageView);


            //初始化小圆点View
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.shape_circle_default);
            //为小圆点View 增加布局参数（增加间距）
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i > 0)
                params.leftMargin = 18; //从第二个小圆点开始设置左边距（作为间距）
            point.setLayoutParams(params); //为View设置参数

            llContainer.addView(point);
        }

        viewPager = (ViewPager) findViewById(R.id.guide_view_pager);
        viewPager.setAdapter(new MyAdapter()); //为ViewPager设置适配器（为每一页添加适配View）
        viewPager.setOnPageChangeListener(myPageChangeListener);//Page滑动（位置改变）监听

        /**
         * 页面绘制完成后 计算两个小圆点间距 （没绘制之前拿不到边距值）
         * 小红点View监听 视图树 通过绘制的View位置 计算两个小圆点位置
         *
         * getViewTreeObserver-视图树的观察者，添加全局布局监听
         */
        redPointImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //Layout绘制完成后调用
            @Override
            public void onGlobalLayout() {
                //只监听一次，进入方法内就移除监听
                redPointImg.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                //第二个小圆点View左边距 减去 第一个小圆点View左边距
                pointWith = llContainer.getChildAt(1).getLeft() - llContainer.getChildAt(0).getLeft();
                Log.i(TAG, "pointWith: " + pointWith);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //新手引导已完成
                PrefUtils.putBoolean("is_guide_show", true, getApplicationContext());

                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                finish();
            }
        });

    }


    /**
     * Page滑动（位置改变）监听
     */
    ViewPager.OnPageChangeListener myPageChangeListener = new ViewPager.OnPageChangeListener() {

        //页面滑动过程中不断回调的方法

        /**
         * @param position 当前页面位置
         * @param positionOffset 当前页面滑动时偏移百分比
         * @param positionOffsetPixels 当前页面滑动时偏移像素值
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.i(TAG, "当前页面位置 :" + position + "当前页面滑动时偏移百分比 :" + positionOffset);
            // + "当前页面滑动时偏移像素值 :" + positionOffsetPixels);

            /*
             方法一 相对于屏幕左边距计算（借用ViewPager每页对应的小灰点的位置来计算）
             llContainer.getChildAt(position)获取当前页面对应的点（灰） 加上偏移值
             修改小红点左边距 以达到 移动小红点目的
             */
//            redPointImg.setX(llContainer.getChildAt(position).getX() + (pointWith * positionOffset));

            /*
            方法二 使用相对于父布局的左边距来计算 （借用父布局计算）
             */
            //计算当前小红点 左边距(相对于父布局RelativeLayout的左边距）
            int pLeftMargin = (int) (pointWith * (position + positionOffset));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) redPointImg.getLayoutParams();
            params.leftMargin = pLeftMargin;

            redPointImg.setLayoutParams(params);
        }

        @Override
        public void onPageSelected(int position) {
            //最后一个页面，显示开始体验按钮
            if(position == mImageIds.length -1 )
                startButton.setVisibility(View.VISIBLE);
            else
                startButton.setVisibility(View.GONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * PageView的适配器
     */
    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
//            Log.i(TAG, ">getCount "+mImageIds.length);
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
//            Log.i(TAG, ">isViewFromObject "+(view == object));
            return view == object;
        }

        //初始化每一个布局对象
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView imageView = new ImageView(getApplicationContext());
//            imageView.setBackgroundResource(mImageIds[position]);
            ImageView imageView = imageViewArrayList.get(position);

            container.addView(imageView);
//            Log.i(TAG, ">container.addView GuideImage" + position);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            Log.i(TAG, ">container.removeView GuideImage" + position);
            container.removeView((View) object);
        }

    }
}
