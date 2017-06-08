package com.jirdy.smartkm.base.impl.meundetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jirdy.smartkm.NewsDetailActivity;
import com.jirdy.smartkm.R;
import com.jirdy.smartkm.base.BaseMenuDetailPager;
import com.jirdy.smartkm.domain.NewsMenuData;
import com.jirdy.smartkm.domain.NewsTabBean;
import com.jirdy.smartkm.global.Constants;
import com.jirdy.smartkm.utils.PrefUtils;
import com.jirdy.smartkm.view.HorizontalScrollViewPager;
import com.jirdy.smartkm.view.RefreshListView;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.jirdy.smartkm.global.Constants.SERVER_IP;
import static com.jirdy.smartkm.global.Constants.SERVER_URL;

/**
 * Created by december on 17-5-14.
 */

public class TabDetailPager extends BaseMenuDetailPager {

    private static final String TAG = "JR.TabDetailPager";
    //    private TextView textView;
    private HorizontalScrollViewPager mHoriViewPager;
    private RefreshListView mListView;
    private CirclePageIndicator circlePageIndicator;
    private TextView topnewsTitle;

    private NewsMenuData.NewsTabData mTabData;//新闻页签分类网络信息（来自于新闻详情页NewsMenuDetailPager）
    private NewsTabBean mNewsTabData; //网络返回的新闻列表数据（Tab内的数据）
    private List<NewsTabBean.DataBean.TopnewsBean> mTopNewsList; //新闻Tab下 头条新闻的网络数据
    private List<NewsTabBean.DataBean.NewsBean> mNewsList; //新闻Tab下 新闻的网络数据

    private NewsAdapter mNewsAdapter;
    private TopNewsAdapter mTopNewsAdapter;

    private String mUrl; //获取新闻列表的url
    private String mMoreUrl; //获取更多数据（下一页） 的url
    /*
     * 头条新闻广告条的轮播条效果：即每过2s切换一次页面，循环切换
     */
    private boolean isOpenHandlerLooper = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            //设置页面循环滑动
            int currentItem = mHoriViewPager.getCurrentItem();
            if (currentItem < mTopNewsList.size() - 1) {
                currentItem++;
            } else {
                currentItem = 0;
            }

            mHoriViewPager.setCurrentItem(currentItem);

            //延迟2s发送消息，切换广告条
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    };

    public TabDetailPager(Activity activity, NewsMenuData.NewsTabData tabData) {
        super(activity);
        mTabData = tabData;
        mUrl = Constants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.page_tab_detail_news, null);
        mListView = (RefreshListView) view.findViewById(R.id.lv_tap_detail_news);

        /**
         * 为ListView添加头布局
         * 注意：下面的mHoriViewPager、circlePageIndicator、topnewsTitle控件都在list_header_topnews布局当中
         * 故使用findViewById需要在list_header_topnews布局当中查找，而不是原来的page_tab_detail_news布局中
         */
        View headView = View.inflate(mActivity, R.layout.list_header_topnews, null);
        mListView.addHeaderView(headView);
        mHoriViewPager = (HorizontalScrollViewPager) headView.findViewById(R.id.hvp_tap_detail_news);
        circlePageIndicator = (CirclePageIndicator) headView.findViewById(R.id.topnews_indicator);
        topnewsTitle = (TextView) headView.findViewById(R.id.tv_topnews_title);

        /** 为ListView设置下拉刷新回调 **/
        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //访问网络，进行数据刷新
                getDataFromServer();
            }

            @Override
            public void loadMore() {
                if (mMoreUrl != null) {
                    Log.i(TAG, "加载下一页数据...");
                    getMoreDataFromServer();
                } else {
                    //没有下一页，收起加载更多布局
                    mListView.onRefreshComplete(true);
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /** 为ListView设置ListItem点击监听事件 **/
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i(TAG, "onItemClick pos: " + position);

                TextView tvTitle = (TextView) view.findViewById(R.id.tv_news_title);
                tvTitle.setTextColor(Color.GRAY);

                NewsTabBean.DataBean.NewsBean news = mNewsList.get(position);

                //将已读状态持续化到本地
                //key: read_ids; value: 1324,1325,1326,...
                String readIds = PrefUtils.getString("read_ids", "", mActivity);
//                Log.i(TAG, "news.id: " + news.id + "readIds: " + readIds);
                if (!readIds.contains(news.id)) { //以前未添加过，才添加到本地， 不重复添加
                    readIds = readIds + news.id + ",";
                    PrefUtils.putString("read_ids", readIds, mActivity);
                }

                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", news.url);
                mActivity.startActivity(intent);

            }
        });

        /**
         * 为头条新闻ViewPager设置触摸监听事件:
         * 按下 -> 停止ViewPager自动轮播事件，即停止滑动页面
         * 抬起或取消 -> 从新唤起轮播事件，每过2秒换一个页面
         **/
        mHoriViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //移除所有回调和消息
                        mHandler.removeCallbacksAndMessages(null);
                        isOpenHandlerLooper = false;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        /**
                         * 当按下之后不立即抬起，而是上下滑/下拉刷新，这样ACTION_UP事件就被ListView拦截了
                         * ViewPager这时候触发的是ACTION_CANCEL事件，其他情况出发ACTION_UP事件
                         */
                        //继续播放广告条
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                        isOpenHandlerLooper = true;
                        break;
                    default:

                        break;
                }
                return false;
            }
        });


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

        //请求网络数据，无论请求成功还是失败，都通知头布局刷新完成
        getDataFromServer();
    }

    private void getDataFromServer() {
        RequestParams params = new RequestParams(mUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
//                Log.i(TAG, "请求到新闻详情数据: " + result);
                processResult(result, false);
                mListView.onRefreshComplete(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "获取新闻详情数据出错! " + ex.getMessage(), Toast.LENGTH_LONG).show();
                mListView.onRefreshComplete(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mListView.onRefreshComplete(false);
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getMoreDataFromServer() {
        RequestParams params = new RequestParams(mMoreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
//                Log.i(TAG, "请求到新闻详情数据: " + result);
                processResult(result, true);
                mListView.onRefreshComplete(true);// 收起加载更多布局
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(x.app(), "加载更多数据出错! " + ex.getMessage(), Toast.LENGTH_LONG).show();
                mListView.onRefreshComplete(false);// 收起加载更多布局
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mListView.onRefreshComplete(false);// 收起加载更多布局
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void processResult(String result, boolean isLoadMore) {
        result = result.replaceAll("10.0.2.2", SERVER_IP);
        Log.i(TAG, "请求到新闻详情数据: " + result);
        Gson gson = new Gson();
        mNewsTabData = gson.fromJson(result, NewsTabBean.class);
        Log.i(TAG, "解析数据: " + mNewsTabData);

        //在成功获取数据以后，拿到获取下一页数据的url链接
        if (!TextUtils.isEmpty(mNewsTabData.data.more)) {
            mMoreUrl = SERVER_URL + mNewsTabData.data.more;
        } else {
            mMoreUrl = null;
        }

        if (!isLoadMore) { //第一次加载

            //初始化头条新闻数据List
            mTopNewsList = mNewsTabData.data.topnews;

            if (mTopNewsList != null) {
                //数据获取成功之后，设置ViewPage适配器和指示器
                mTopNewsAdapter = new TopNewsAdapter();
                mHoriViewPager.setAdapter(mTopNewsAdapter);

                //头条新闻指示器设置
                circlePageIndicator.setViewPager(mHoriViewPager);
                circlePageIndicator.setSnap(true); //滑动时使用快照，去除滑动特效
                circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        topnewsTitle.setText(mTopNewsList.get(position).title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {
                    }
                });

                //每次刷新时重置数据为第一页
                circlePageIndicator.onPageSelected(0);
                topnewsTitle.setText(mTopNewsList.get(0).title); //第一次启动时给标题赋值

                //只在首次进入页面，获取到数据之后调起轮播事件，之后mHandler内部会一直循环轮播
                if(!isOpenHandlerLooper){
                    //延迟2s发送消息，切换广告条
                    mHandler.sendEmptyMessageDelayed(0, 2000);
                    isOpenHandlerLooper = true;
                }
            }

            //初始化新闻列表数据List
            mNewsList = mNewsTabData.data.news;
            if (mNewsList != null) {
                //数据获取成功之后，设置ListView适配器
                mNewsAdapter = new NewsAdapter();
                mListView.setAdapter(mNewsAdapter);
            }

        } else { //下拉到底部 加载更多时 加载

            //拿到 从网络获取的加载更多的数据
            List<NewsTabBean.DataBean.NewsBean> moreDataList = mNewsTabData.data.news;
            mNewsList.addAll(moreDataList); //将数据添加到原来List的末尾
            mNewsAdapter.notifyDataSetChanged(); //刷新ListView
        }

    }

    class TopNewsAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            /**
             * 如何请求网络图片并填充到ImageView，xUtils中有相应的工具类可以完美的完成此操作
             * 获取图片资源，使用链接下载图片，将图片设置给ImageView，考虑内存溢出问题，图片缓存问题，
             * 只需x.image().bind()来搞定，也可以添加图片选项 和 Callback参数
             */
            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    // 默认自动适应大小
                    // .setSize(...)
                    //.setIgnoreGif(false)
                    // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                    //.setUs eMemCache(false)
                    //设置图片填充控件效果，FIT_XY表示填充父窗体
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    //当图片还未下载完成时，设置一张 加载中图片 来显示
                    .setLoadingDrawableId(R.mipmap.topnews_item_default).build();

            x.image().bind(imageView, mTopNewsList.get(position).topimage, imageOptions);

            container.addView(imageView);
            return imageView;
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


    class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {

            ViewHolder viewHolder;
            //第一次调用时需要初始化convertView
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);

                viewHolder = new ViewHolder();
                viewHolder.ivNews = (ImageView) convertView.findViewById(R.id.iv_news_img);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_news_title);
                viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_news_date);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ImageOptions imageOptions = new ImageOptions.Builder()
                    //设置图片填充控件效果，FIT_XY表示填充父窗体
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    //当图片还未下载完成时，设置一张 加载中图片 来显示
                    .setLoadingDrawableId(R.mipmap.pic_item_list_default).build();

            NewsTabBean.DataBean.NewsBean news = mNewsList.get(position);
            viewHolder.tvTitle.setText(news.title);
            viewHolder.tvDate.setText(news.pubdate);
            x.image().bind(viewHolder.ivNews, news.listimage, imageOptions);

            //标记已读和未读
            String readIds = PrefUtils.getString("read_ids", "", mActivity);
//            Log.i(TAG, "news.id: " + news.id + "readIds: " + readIds);
            if (readIds.contains(news.id)) { //已读
                viewHolder.tvTitle.setTextColor(Color.GRAY);
            } else { // 未读
                viewHolder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

    //新建ViewHolder来hold多个View
    static class ViewHolder {
        public ImageView ivNews;
        public TextView tvTitle;
        public TextView tvDate;
    }

}
