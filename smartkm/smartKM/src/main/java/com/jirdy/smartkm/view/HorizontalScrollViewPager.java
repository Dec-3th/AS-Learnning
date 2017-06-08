package com.jirdy.smartkm.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Switch;

/**
 * 头条新闻的自定义ViewPager
 * Created by jinrui on 2017/5/15.
 */

public class HorizontalScrollViewPager extends ViewPager {

    private int startX;
    private int startY;

    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 分情况决定父控件是否需要拦截（父控件ListView，父控件拦截不到，则继续往上走）
     * 1.上下滑需要父控件拦截
     * 2.如果当前是第一个Pager，右滑需要父控件拦截（右滑呼出侧边栏）
     * 3.如果当前是最后一个Pager，左滑需要父控件拦截（左滑切换到下一个列表page)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        /**
         * 要判断触摸事件类型（左右滑动，还是上下滑动）
         * 需要通过按下的起点坐标 和 手指滑动结束的终点的坐标，来计算
         */
        switch (event.getAction()) {
            /** 处特殊情况父控件需要拦截，其他情况都以第一次按下的设置为准，父控件不拦截 **/
            case MotionEvent.ACTION_DOWN: //手指按下的时候，请求父控件不要拦截
                //记录起点坐标
                startX = (int) event.getX();
                startY = (int) event.getY();

                //请求父控件不拦截事件
                getParent().requestDisallowInterceptTouchEvent(true);

                break;
            case MotionEvent.ACTION_MOVE: //手指移动的时候，根据情况看，要不要请求父控件拦截
                //记录终点坐标
                int endX = (int) event.getX();
                int endY = (int) event.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dx) > Math.abs(dy)) { //左右滑

                    if (dx > 0) { //向右滑动

                        //如果当前是第一个Pager，右滑需要父控件拦截
                        if (getCurrentItem() == 0) {
                            //请求父控件拦截事件
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }

                    } else { //向左滑动

                        //如果当前是最后一个Pager，左滑需要父控件拦截
                        if (getCurrentItem() == (getAdapter().getCount() - 1)) {
                            //请求父控件拦截事件
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }

                } else { //上下滑

                    //请求父控件拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            default:
                break;
        }


        return super.dispatchTouchEvent(event);
    }
}
