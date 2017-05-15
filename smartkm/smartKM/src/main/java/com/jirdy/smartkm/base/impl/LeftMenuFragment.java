package com.jirdy.smartkm.base.impl;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jirdy.smartkm.MainActivity;
import com.jirdy.smartkm.R;
import com.jirdy.smartkm.base.BaseFragment;
import com.jirdy.smartkm.domain.NewsMenuData;

import java.util.ArrayList;


/**
 * 侧边栏Fragment
 * A simple {@link Fragment} subclass.
 */
public class LeftMenuFragment extends BaseFragment {

    public static final String TAG = "JR.LeftMenuFragment";

    private ListView lv_menu;
    private ArrayList<NewsMenuData.NewsData> mMenuList;
    private MyAdapter myAdapter;
    private int mCurrentItem;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);

        lv_menu = (ListView) view.findViewById(R.id.lv_left_menu);

        return view;
    }

    /**
     * 通知新闻中心页面，切换页面（侧边栏只有拿到新闻中心的引用，才能切换页面）
     * @param position 当前点击的位置
     */
    private void setCurrentMenuDetailPager(int position) {
        //如何获取新闻中心对象 NewsCenterPager
        //1.获取 MainActivity
        //2.通过 MainActivity 获取 ContentFragment 对象
        //3.通过 ContentFragment 获取 NewsCenterPager 对象
        MainActivity mainUI = (MainActivity) mActivity;
        ContentFragment contentFragment = mainUI.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();

        //给新闻中心页面填充布局（通知新闻中心页面，切换页面）
        newsCenterPager.setCurrentMenuDetailPager(position);

        //切换页面后，要隐藏侧边栏
        mainUI.slidingMenuToggle();
    }

    public void setData(ArrayList<NewsMenuData.NewsData> data) {
        mMenuList = data;

        myAdapter = new MyAdapter();
        //有了数据以后，再为ListView设置Adapter
        lv_menu.setAdapter(myAdapter);

        lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mCurrentItem = position;
//                Log.i(TAG, "onItemSelected: " +  mCurrentItem);
                myAdapter.notifyDataSetChanged(); //通知Adapter刷新数据

                //通知新闻中心页面，切换页面
                setCurrentMenuDetailPager(position);
            }

        });

        mCurrentItem = 0; //重新切换到新闻中心时，重新设置数据，需要重置当前页面位置
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public NewsMenuData.NewsData getItem(int i) {
            return mMenuList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int postion, View view, ViewGroup viewGroup) {
            View mView = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            TextView textView = (TextView) mView.findViewById(R.id.tv_menu_list_item);

            NewsMenuData.NewsData newsData = getItem(postion);
            textView.setText(newsData.title);

            /**
             * 因为侧边栏条目，按下之后要变色，不是Click事件（按下放开，就不在选中了）
             * 更类似与RadioButton的Check事件（按下就选中，直至点击同组的其他项）
             * Check事件是RadioButton独有的，其他控件可使用控件的Enable/Disable属性来达到同样的效果
             * 一组控件，设置按下的为Enable，其他Disable，同理切换
             */
            if (mCurrentItem == postion) {
//                Log.i(TAG, "onItemSelected: " +  newsData.title);
                textView.setEnabled(true);
            } else {
                textView.setEnabled(false);
            }

            return (View) textView.getParent(); //添加布局是添加包含TextView的那个父布局，否则无法添加
//            return  textView;
        }
    }
}
