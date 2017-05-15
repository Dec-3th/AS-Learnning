package com.jirdy.smartkm.base.impl.meundetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jirdy.smartkm.MainActivity;
import com.jirdy.smartkm.R;
import com.jirdy.smartkm.base.BaseMenuDetailPager;
import com.jirdy.smartkm.domain.NewsMenuData;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 *
 * ViewPagerIndicator使用流程:
 * 1. 引入Library库
 * 2. 布局文件中配置TabPageIndicator
 * 3. 将指针和Viewpager关联起来
 * 4. 重写getPageTitle方法,返回每个页面的标题(PagerAdapter中的方法)
 * 5. 设置activity主题样式
 * 6. 修改源码中的样式(修改图片, 文字颜色)
 *
 * Created by december on 17-5-13.
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private static final String TAG = "JR.NewsMenuDetailPager";
    private ViewPager mViewPager;
    private ArrayList<TabDetailPager> mTabPagers; //页签页面集合
    private ArrayList<NewsMenuData.NewsTabData> mTabList; //页签网络数据集合
    private TabPageIndicator tabPageIndicator;
    private ImageView nextPage;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenuData.NewsTabData> children) {
        super(activity);
        mTabList = children;
    }

    @Override
    public View initView() {

//        TextView contentText = new TextView(mActivity);
//        contentText.setText("菜单详情页-新闻");
//        contentText.setTextColor(Color.BLACK);
//        contentText.setTextSize(22);
//        contentText.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_news, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail_news);
        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);

        nextPage = (ImageView) view.findViewById(R.id.img_btn_nextpage);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        mTabPagers = new ArrayList<>();
        for (NewsMenuData.NewsTabData tabData : mTabList) {
            mTabPagers.add(new TabDetailPager(mActivity, tabData));
        }

        mViewPager.setAdapter(new NewsMenuAdapter());
//        mViewPager.setOnPageChangeListener(mPageChangeListener);

        //在ViewPager数据初始化完成之后，再将页面指示器和ViewPager关联起来
        tabPageIndicator.setViewPager(mViewPager);
        /**
         * 当viewpager和TabPageIndicator指针绑定时,需要将页面切换监听设置给TabPageIndicator指针
         * 不能设置给原来的 ViewPager 否则监听不到事件
         **/
        tabPageIndicator.setOnPageChangeListener(mPageChangeListener);

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int current = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(++current);
            }
        });

    }

    ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            Log.i(TAG, "onPageSelected： " + position);
            final MainActivity mainUI = (MainActivity) mActivity;
            if (position == 0) {
                mainUI.setSlidingMenuEnable(true);//第一个页签启用侧边栏
            } else {
                mainUI.setSlidingMenuEnable(false);//其他页签禁用侧边栏，保证ViewPager可以正常切换
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


    };

    class NewsMenuAdapter extends PagerAdapter {

        // 返回页面指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position).title;
        }

        @Override
        public int getCount() {
            return mTabPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabPager = mTabPagers.get(position);
            container.addView(tabPager.mRootView);
            tabPager.initData();

            return tabPager.mRootView;

//            TextView textView = new TextView(mActivity);
//            textView.setText("菜单详情页签TTT");
//            textView.setTextColor(Color.RED);
//            textView.setTextSize(20);
//            textView.setGravity(Gravity.CENTER);
//
//            container.addView(textView);
//            return textView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
