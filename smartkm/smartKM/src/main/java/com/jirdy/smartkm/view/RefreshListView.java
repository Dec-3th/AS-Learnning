package com.jirdy.smartkm.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jirdy.smartkm.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jinrui on 2017/5/16.
 */
public class RefreshListView extends ListView {

    private static final String TAG = "JR.RefreshListView";
    //定义几种刷新状态
    private static final int STATE_PULL_TO_REFRESH = 0; //下拉刷新
    private static final int STATE_RELEASE_TO_REFRESH = 1;//松开刷新
    private static final int STATE_REFRESHING = 2;//正在刷新
    private int mCurrentState = STATE_PULL_TO_REFRESH; //默认状态是下拉刷新

    private View mHeaderView; //ListView下拉刷新头布局
    private int mHeaderHeight; //头布局高度
    private View mFooterView; //ListView下拉刷新脚布局
    private int mFooterHeight; //脚布局高度
    private boolean isLoadingMore = false; //是否正在 加载更多过程中

    private TextView tvRefreshTitle;
    private ImageView ivRefreshArrow;
    private ProgressBar pbRefreshLoading;
    private TextView tvRefreshTime;

    private RotateAnimation animUp; //箭头向上旋转动画
    private RotateAnimation animDown;//箭头向下旋转动画


    private int startY = -1; //赋一个初始值

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    public void initHeaderView() {
        //在View的子类中，可以通过getContext获取当前上下文，进而获取需要的布局
        mHeaderView = View.inflate(getContext(), R.layout.list_header_refresh, null);
        this.addHeaderView(mHeaderView); //添加头布局

        /**
         * 隐藏头布局的方法：
         * 1.获取头布局高度；
         * 2.设置负的paddingTop，布局就会往上走；
         * 3.设置paddingTop为负的头布局高度，头布局就被隐藏了
         */
//        int height = mHeaderView.getHeight(); //此方法获取不到布局高度，因为布局还未绘制完成

        //在绘制之前就要获取布局高度
        mHeaderView.measure(0, 0); //布局还未绘制之前只能手动测量布局，调用measure方法进行测量
        mHeaderHeight = mHeaderView.getMeasuredHeight();//获取测量出的布局高度
        mHeaderView.setPadding(0, -mHeaderHeight, 0, 0); //隐藏头布局

        tvRefreshTitle = (TextView) mHeaderView.findViewById(R.id.tv_refresh_title);
        ivRefreshArrow = (ImageView) mHeaderView.findViewById(R.id.iv_refresh_arrow);
        pbRefreshLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_refresh_loading);
        tvRefreshTime = (TextView) mHeaderView.findViewById(R.id.tv_refresh_time);

        initAnimation(); //初始化动画效果
        setmCurrentTime(); //设置一个初始的刷新时间，即刚进界面刷新的时间
    }

    public void initFooterView() {

        mFooterView = View.inflate(getContext(), R.layout.list_footer_refresh, null);

        this.addFooterView(mFooterView); //为ListView添加脚布局

        //在绘制之前就要获取布局高度
        mFooterView.measure(0, 0);//布局还未绘制之前只能手动测量布局，调用measure方法进行测量
        mFooterHeight = mFooterView.getMeasuredHeight();//获取测量出的脚布局高度

        //隐藏脚布局
        //隐藏的相对位置为该控件的顶部，而不是手机屏幕顶部，故设为负的脚布局高度就可将脚布局，隐藏到脚布局顶部的缝隙中
        mFooterView.setPadding(0, -mFooterHeight, 0, 0);

        //为了滑动到底显示脚布局，这里为ListView设置一个滑动监听
        this.setOnScrollListener(new OnScrollListener() {
            //滑动结束会调用该方法
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) { //滑动状态为idle空闲，表示滑动结束

                    int lastVisiblePosition = getLastVisiblePosition(); //获取当前界面可见的最后一个Item的位置
                    /*
                    所有数据都加载完毕，再往下拉就要从网络加载更多数据。
                    前提：ListView到底 且 当前不在加载更多过程中
                    isLoadingMore是为了，当网络卡顿，用户多次下滑加载时，不重复加载数据到List中
                     */
                    if (lastVisiblePosition >= getCount() - 1 && !isLoadingMore) {
//                        Log.i(TAG, "到底了...");
                        isLoadingMore = true;
                        mFooterView.setPadding(0, 0, 0, 0);//显示脚布局 paddingTop = 0

                        //为了将脚布局显示到屏幕，将脚布局设置为当前要展示的Item，脚布局位置倒数第一个
                        //ListView设置当前要展示的Item位置
                        setSelection(getCount() - 1); //跳到加载更多item的位置去显示

                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.loadMore();
                        }
                    }

                }

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    //复写ListView的OnTouchEvent方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //这里只关注上下滑动，只记录y值变动
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                //如果用户按住头条新闻往下滑动，ACTION_DOWN事件会被头条新闻的ViewPager给拦截掉
                // 此时startY值获取不到，需要在ACTION_MOVE中重新获取startY
                if (startY == -1) {
                    startY = (int) event.getY();//如果
                }

                //如果当前状态正在刷新，什么都不做，直接跳出ACTION_MOVE事件
                // 让super.onTouchEvent(event)将ACTION_MOVE事件传给父控件处理
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }

                int endY = (int) event.getY();
                int dy = endY - startY;

                //向下滑动，且当前显示的是第一个Item，才允许下拉刷新
                if (dy > 0 && (this.getFirstVisiblePosition() == 0)) {//getFirstVisiblePosition获取当前第一个可见的Item位置

                    int paddingTop = -mHeaderHeight + dy;//计算当前paddingTop值
                    mHeaderView.setPadding(0, paddingTop, 0, 0);//重新设置头布局paddingTop

                    if (paddingTop >= 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        //切换到松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState(); //刷新界面
                    } else if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        //切换到下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState(); //刷新界面
                    }
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                //起始坐标归零
                startY = -1;

                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    //如果当前状态是松开刷新，松开后切换为正在刷新
                    mCurrentState = STATE_REFRESHING;
                    refreshState();

                    //完全显示头布局，设paddingTop=0
                    mHeaderView.setPadding(0, 0, 0, 0);//重新设置头布局paddingTop

                    //下拉刷新回调，通知界面刷新数据
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh();
                    }

                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    //如果当前状态是下拉刷新，松开后隐藏头布局
                    mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);//重新设置头布局paddingTop
                }

                break;
            default:

                break;

        }
        return super.onTouchEvent(event);
    }

    private void initAnimation() {
        //箭头逆时针向上旋转180度动画，相对自身旋转
        //参数：float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(500); //0.5秒
        animUp.setFillAfter(true);//动画完成后保持状态

        //箭头顺时针向下旋转180度动画，相对自身旋转
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(500); //0.5秒
        animDown.setFillAfter(true);//动画完成后保持状态
    }

    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvRefreshTitle.setText("下拉刷新");
                ivRefreshArrow.startAnimation(animDown);
                ivRefreshArrow.setVisibility(VISIBLE); //显示箭头
                pbRefreshLoading.setVisibility(INVISIBLE); //不显示进度环
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvRefreshTitle.setText("松开刷新");
                ivRefreshArrow.startAnimation(animUp);
                ivRefreshArrow.setVisibility(VISIBLE); //显示箭头
                pbRefreshLoading.setVisibility(INVISIBLE); //不显示进度环
                break;
            case STATE_REFRESHING:
                tvRefreshTitle.setText("正在刷新");
                pbRefreshLoading.setVisibility(VISIBLE); //显示进度环
                ivRefreshArrow.clearAnimation(); //这里必须清除动画，才能够隐藏控件，否则无法隐藏
                ivRefreshArrow.setVisibility(INVISIBLE); //不显示箭头
                break;
        }

    }

    /**
     * 下拉刷新回调，让页面能进行数据刷新
     */
    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public interface OnRefreshListener {
        //下拉刷新的回调
        public void onRefresh();

        //ListView到底了，加载更多回调
        public void loadMore();
    }

    private void setmCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvRefreshTime.setText(dateFormat.format(new Date()));
    }

    //下拉刷新成功，隐藏头布局，初始化头布局内控件数据
    public void onRefreshComplete(boolean isSuccess) {
        Log.i(TAG, "onRefreshComplete");
        //隐藏头布局，重置当前刷新状态
        mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);
        mCurrentState = STATE_PULL_TO_REFRESH;

        //初始化控件到下拉刷新状态
        tvRefreshTitle.setText("下拉刷新");
        ivRefreshArrow.setVisibility(VISIBLE); //显示箭头
        pbRefreshLoading.setVisibility(INVISIBLE); //不显示进度环

        //记录上次成功刷新的时间，刷新失败不需要记录时间
        if (isSuccess) {
            setmCurrentTime();
        }

        if (isLoadingMore) { //如果是加载更多状态，隐藏脚布局
            //加载完毕，修改状态，隐藏脚布局
            isLoadingMore = false;
            mFooterView.setPadding(0, -mFooterHeight, 0, 0);//显示脚布局 paddingTop = 0
        }
    }

//    private OnItemClickListener mOnItemClickListener;

    /**
     * 复写Item点击方法，在里面重新为super设置一个OnItemClickListener
     * 在这个监听中将点击位置pos减去2，减掉ListView中添加的两个头布局，
     * 这样ListView中View位置和ArrayList中数据位置就对应了。
     * @param listener
     */
    @Override
    public void setOnItemClickListener(@Nullable final OnItemClickListener listener) {
        //当该方法被调用时

//        super.setOnItemClickListener(listener);
        //为super重新设置ItemClick
        super.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Log.i(TAG, "onItemClick super pos: " + position);
                if (listener != null)
                    listener.onItemClick(adapterView, view, position - 2, l);
            }
        });

    }
}
