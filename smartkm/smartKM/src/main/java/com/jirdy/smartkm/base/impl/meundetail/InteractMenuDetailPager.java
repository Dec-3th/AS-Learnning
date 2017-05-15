package com.jirdy.smartkm.base.impl.meundetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jirdy.smartkm.base.BaseMenuDetailPager;

/**
 * 菜单详情页-互动
 * Created by december on 17-5-13.
 */

public class InteractMenuDetailPager extends BaseMenuDetailPager {

    public InteractMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {

//        title.setText("新闻");

        TextView contentText = new TextView(mActivity);
        contentText.setText("菜单详情页-互动");
        contentText.setTextColor(Color.BLACK);
        contentText.setTextSize(22);
        contentText.setGravity(Gravity.CENTER);

        return contentText;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
