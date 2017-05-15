package com.jirdy.smartkm.base.impl.meundetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jirdy.smartkm.R;
import com.jirdy.smartkm.base.BaseMenuDetailPager;
import com.jirdy.smartkm.domain.NewsMenuData;
import com.jirdy.smartkm.domain.NewsTabBean;
import com.jirdy.smartkm.global.Constants;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by december on 17-5-14.
 */

public class TabDetailPager extends BaseMenuDetailPager {

    private static final String TAG = "JR.TabDetailPager";
    private NewsMenuData.NewsTabData mTabData;
//    private TextView textView;
    private ViewPager mViewPager;
    private ListView mListView;
    private String mUrl;

    public TabDetailPager(Activity activity, NewsMenuData.NewsTabData tabData) {
        super(activity);
        mTabData = tabData;
        mUrl = Constants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.page_tab_detail_news, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_tap_detail_news);
        mListView = (ListView) view.findViewById(R.id.lv_tap_detail_news);

//        textView = new TextView(mActivity);
//        textView.setText("菜单详情页签");
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(20);
//        textView.setGravity(Gravity.CENTER);

        return view;
    }

    @Override
    public void initData() {
        Log.i(TAG, "initData： " + "页签详情页-" + mTabData.title);
//        textView.setText("页签详情页-" + mTabData.title);

        //请求网络数据
        getDataFromServer();
    }

    private void getDataFromServer() {
        RequestParams params = new RequestParams(mUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "请求到新闻详情数据: " + result);

                processResult(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "获取新闻详情数据出错! " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processResult(String result) {
        Gson gson = new Gson();
        NewsTabBean newsTab = gson.fromJson(result, NewsTabBean.class);
        Log.i(TAG, "解析数据: " + newsTab);
    }


    class TopNewsAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
