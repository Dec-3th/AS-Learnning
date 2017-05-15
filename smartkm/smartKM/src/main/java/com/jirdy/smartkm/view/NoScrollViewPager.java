package com.jirdy.smartkm.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义 不能滑动的ViewPager
 * Created by jinrui on 2017/5/12.
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //决定事件是否中断
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //始终返回false，不拦截事件，让嵌套的子ViewPage有机会响应触摸事件
        return false;
    }

    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);

        //子类什么都不做，从而屏蔽父ViewPager的触摸（滑动）屏幕事件
        //重写父类ViewPager的onTouchEvent为什么都不做，直接返回true
        return true;
    }
}
