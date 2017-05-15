package com.jirdy.smartkm.base;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jirdy.smartkm.MainActivity;
import com.jirdy.smartkm.R;

/**
 * 五个标签页的基类
 * Created by december on 17-5-11.
 */

public abstract class BasePager {

    public View mRootView; //basePager根布局
    public Activity mActivity;
    public ImageButton menuButton;
    public TextView title;
    public FrameLayout frameLayoutContent;

    public BasePager(Activity activity) {
        mActivity = activity;
        initView(); //创建时初始化数据
    }

    /**
     * 初始化布局
     */
    public void initView(){
        /**
         * 将布局填充成View对象，最常用的两种办法是：View类的方法inflate和LayoutInflater类的inflate方法，
         */

        //通过Activity找到布局文件，用View填充好
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);

        //通过跟布局来find里面的控件
        menuButton = (ImageButton) mRootView.findViewById(R.id.menu_img_btn);
        title = (TextView) mRootView.findViewById(R.id.menu_text_title);
        frameLayoutContent = (FrameLayout) mRootView.findViewById(R.id.basepager_content_framlayout);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //通过MainActivity 调用方法展开或关闭侧边栏
                MainActivity mainUI = (MainActivity) mActivity;

                mainUI.slidingMenuToggle();
            }
        });
    }

    /**
     * 初始化数据
     */
    public abstract void initData();
}
