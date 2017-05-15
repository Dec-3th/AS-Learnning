package com.jirdy.smartkm.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jirdy.smartkm.MainActivity;
import com.jirdy.smartkm.base.BasePager;

/**
 * Created by december on 17-5-12.
 */

public class GovAffairsPager extends BasePager {


    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override

    public void initData() {
        title.setText("政务");

        TextView contentText = new TextView(mActivity);
        contentText.setText("政务");
        contentText.setTextColor(Color.BLACK);
        contentText.setTextSize(30);
        contentText.setGravity(Gravity.CENTER);

        frameLayoutContent.addView(contentText);
    }
}
