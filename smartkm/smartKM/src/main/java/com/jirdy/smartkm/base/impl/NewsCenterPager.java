package com.jirdy.smartkm.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.solver.Cache;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jirdy.smartkm.MainActivity;
import com.jirdy.smartkm.base.BaseMenuDetailPager;
import com.jirdy.smartkm.base.BasePager;
import com.jirdy.smartkm.base.impl.meundetail.InteractMenuDetailPager;
import com.jirdy.smartkm.base.impl.meundetail.NewsMenuDetailPager;
import com.jirdy.smartkm.base.impl.meundetail.PhotosMenuDetailPager;
import com.jirdy.smartkm.base.impl.meundetail.TopicMenuDetailPager;
import com.jirdy.smartkm.domain.NewsMenuData;
import com.jirdy.smartkm.global.Constants;
import com.jirdy.smartkm.utils.CacheUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by december on 17-5-12.
 */

public class NewsCenterPager extends BasePager {

    public static final String TAG = "JR.NewsCenterPager";
    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
    private NewsMenuData mNewsMenuData; //新闻中心分类信息数据

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override

    public void initData() {
        title.setText("新闻中心");

//        开发过程中用于 填充框架的数据，这里注释掉
//        TextView contentText = new TextView(mActivity);
//        contentText.setText("新闻中心");
//        contentText.setTextColor(Color.BLACK);
//        contentText.setTextSize(30);
//        contentText.setGravity(Gravity.CENTER);
//
//        frameLayoutContent.removeAllViews();
//        frameLayoutContent.addView(contentText);

        /**
         * 初始化xUtils工具类
         */
        x.Ext.init(mActivity.getApplication());
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        /**
         * 查找本地缓存
         * 有缓存，直接加载
         */
        String cache = CacheUtils.getCache(Constants.CATEGORIES_URL, mActivity);
        if(!TextUtils.isEmpty(cache)){ //TextUtils.isEmpty不仅能判断空null, 还能判断内容是空串""
            //有缓存
            Log.i(TAG, "发现缓存...");
            processResult(cache);
        }
//        else {
//            //没有缓存，请求网络
//            Log.i(TAG, "访问网络...");
//            getDataFromServer();
//        }

        //即使有缓存，仍然调用网络，获取最新数据（这时上面的缓存是为了保证用户体验，没有请求到数据之前页面不是空白）
        getDataFromServer();
    }

    /**
     * 使用xUtils工具
     * 从服务器获取数据
     * 需要联网权限
     */
    private void getDataFromServer() {
        RequestParams params;
//        params = new RequestParams("http://blog.csdn.net/mobile/experts.html");
//        params.setSslSocketFactory(new SSLCertificateSocketFactory(1000)); // 设置ssl
//        params.addQueryStringParameter("wd", "xUtils");

        params = new RequestParams(Constants.CATEGORIES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
//                Toast.makeText(x.app(), "getData: " + result, Toast.LENGTH_LONG).show();
                String data = result.toString(); //获取json字符串
                Log.i(TAG, "请求到新闻中心数据: " + data);

//                data = "{\"retcode\":200,\"data\":[{\"id\":10000,\"title\":\"新闻\",\"type\":1,\"children\":[{\"id\":10007,\"title\":\"北京\",\"type\":1,\"url\":\"/10007/list_1.json\"},{\"id\":10006,\"title\":\"中国\",\"type\":1,\"url\":\"/10006/list_1.json\"},{\"id\":10008,\"title\":\"国际\",\"type\":1,\"url\":\"/10008/list_1.json\"},{\"id\":10010,\"title\":\"体育\",\"type\":1,\"url\":\"/10010/list_1.json\"},{\"id\":10091,\"title\":\"生活\",\"type\":1,\"url\":\"/10091/list_1.json\"},{\"id\":10012,\"title\":\"旅游\",\"type\":1,\"url\":\"/10012/list_1.json\"},{\"id\":10095,\"title\":\"科技\",\"type\":1,\"url\":\"/10095/list_1.json\"},{\"id\":10009,\"title\":\"军事\",\"type\":1,\"url\":\"/10009/list_1.json\"},{\"id\":10093,\"title\":\"时尚\",\"type\":1,\"url\":\"/10093/list_1.json\"},{\"id\":10011,\"title\":\"财经\",\"type\":1,\"url\":\"/10011/list_1.json\"},{\"id\":10094,\"title\":\"育儿\",\"type\":1,\"url\":\"/10094/list_1.json\"},{\"id\":10105,\"title\":\"汽车\",\"type\":1,\"url\":\"/10105/list_1.json\"}]},{\"id\":10002,\"title\":\"专题\",\"type\":10,\"url\":\"/10006/list_1.json\",\"url1\":\"/10007/list1_1.json\"},{\"id\":10003,\"title\":\"组图\",\"type\":2,\"url\":\"/10008/list_1.json\"},{\"id\":10004,\"title\":\"互动\",\"type\":3,\"excurl\":\"\",\"dayurl\":\"\",\"weekurl\":\"\"}],\"extend\":[10007,10006,10008,10014,10012,10091,10009,10010,10095]}";
                processResult(data);

                //从网络成功获取数据，进行缓存
                CacheUtils.setCache(Constants.CATEGORIES_URL, data, mActivity);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "获取新闻中心数据出错! " + ex.getMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "onError! " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "onCancelled!" + cex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {
            }
        });

    }

    private void processResult(String data) {

        //使用Gson解析json，解析后直接将json填充到需要的对象中（对象需要针对json来定义）
        Gson gson = new Gson();
        mNewsMenuData = gson.fromJson(data, NewsMenuData.class);
        Log.i(TAG, "解析结果： " + mNewsMenuData);

        /*
        将解析出的数据传给LeftMenuFragment，显示侧边栏数据
         */
        //通过MainActivity拿到LeftMenuFragment
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        //调用setData方法将网络数据设置给leftMenuFragment
        leftMenuFragment.setData(mNewsMenuData.data);

        //显示侧边栏之后，初始化侧边栏的4个菜单详情页
        mMenuDetailPagers = new ArrayList<>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewsMenuData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        //设置 菜单详情页-新闻 作为初始页面
        setCurrentMenuDetailPager(0);
    }


    /**
     * 切换页面: 目的是修改中间的frameLayoutContent 和 上面的title
     * 根据传过来position 知道用户点击侧边栏哪一项，显示对应的详情页
     * @param position
     */
    public void setCurrentMenuDetailPager(int position) {

        //移除之前所有View对象 清理屏幕 然后再添加新的布局
        frameLayoutContent.removeAllViews();
        //MenuDetailPager中的mRootView就是该详情页的布局（根布局），添加其根布局就添加了整个页面
        frameLayoutContent.addView(mMenuDetailPagers.get(position).mRootView);

        //设置标题 从mNewsMenuData中直接获取
        title.setText(mNewsMenuData.data.get(position).title);

        //初始化页面数据
        mMenuDetailPagers.get(position).initData();
    }
}
