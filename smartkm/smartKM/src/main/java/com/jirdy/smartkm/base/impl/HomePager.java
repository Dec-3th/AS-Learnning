package com.jirdy.smartkm.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jirdy.smartkm.base.BasePager;

/**
 * Created by december on 17-5-12.
 */
public class HomePager extends BasePager {


    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        title.setText("主页");

        //隐藏Menu菜单，Menu菜单目的是呼出侧边栏，这里也要需要禁用侧边栏
        menuButton.setVisibility(View.GONE);

        TextView contentText = new TextView(mActivity);
        contentText.setText("主页");
        contentText.setTextColor(Color.BLACK);
        contentText.setTextSize(30);
        contentText.setGravity(Gravity.CENTER);

        frameLayoutContent.addView(contentText);
    }
}
