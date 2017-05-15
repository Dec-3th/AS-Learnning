package com.jirdy.smartkm.base.impl;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jirdy.smartkm.MainActivity;
import com.jirdy.smartkm.R;
import com.jirdy.smartkm.base.BaseFragment;
import com.jirdy.smartkm.base.BasePager;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页Fragment
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends BaseFragment {

    public static final String TAG = "JR.ContentFragment";
    private List<BasePager> pagers;
    private ViewPager viewPager;
    private RadioGroup radioGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);

        viewPager = (ViewPager) view.findViewById(R.id.content_view_pager);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);

        return view;
    }

    /**
     * 初始化数据 子类可以不实现
     */
    public void initData(){
        //页面顺序是：首页 新闻 智慧服务 政务 设置
        pagers = new ArrayList<>();
        pagers.add(new HomePager(mActivity));
        pagers.add(new NewsCenterPager(mActivity));
        pagers.add(new SmartServicePager(mActivity));
        pagers.add(new GovAffairsPager(mActivity));
        pagers.add(new SettingPager(mActivity));
        viewPager.setAdapter(new MyPagerAdapter());

        radioGroup.setOnCheckedChangeListener(mRgCheckedChangeListener);

        pagers.get(0).initData(); //第一次进入时初始化首页数据
        MainActivity mainUI = (MainActivity) mActivity;
        mainUI.setSlidingMenuEnable(false);//第一次进入时禁用侧边栏
    }


    RadioGroup.OnCheckedChangeListener mRgCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

            //先将BaseFragment中保存的Activity 转换为MainActivity
            MainActivity mainUI = (MainActivity) mActivity;

            switch (i){
                //除了首页，其他数据在点击RadioButton切换页面时，再初始化，节省流量
                case R.id.rb_home:

                    //viewPager.setCurrentItem(0; //带页面切换特效切换页面
                    viewPager.setCurrentItem(0,false); //切换到首页 不带页面切换特效
                    pagers.get(0).initData(); //初始化数据
                    mainUI.setSlidingMenuEnable(false);//禁用侧边栏

                    break;
                case R.id.rb_newscenter:

                    viewPager.setCurrentItem(1,false); //切换到新闻中心 不带页面切换特效
                    pagers.get(1).initData(); //初始化数据
                    mainUI.setSlidingMenuEnable(true);//开启侧边栏

                    break;
                case R.id.rb_smartservice:

                    viewPager.setCurrentItem(2,false); //切换到智慧服务 不带页面切换特效
                    pagers.get(2).initData(); //初始化数据
                    mainUI.setSlidingMenuEnable(true);//开启侧边栏

                    break;
                case R.id.rb_govaffairs:

                    viewPager.setCurrentItem(3,false); //切换到政务 不带页面切换特效
                    pagers.get(3).initData(); //初始化数据
                    mainUI.setSlidingMenuEnable(true);//开启侧边栏

                    break;
                case R.id.rb_setting:

                    viewPager.setCurrentItem(4,false); //切换到设置 不带页面切换特效
                    pagers.get(4).initData(); //初始化数据
                    mainUI.setSlidingMenuEnable(false);//禁用侧边栏

                    break;
            }
        }
    };

    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) pagers.get(1);
    }


    /**
     * PageView的适配器 用于显示ViewPager，为每一个Pager添加页面
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
//            Log.i(TAG, ">getCount "+mImageIds.length);
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
//            Log.i(TAG, ">isViewFromObject "+(view == object));
            return view == object;
        }

        //初始化每一个布局对象
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = pagers.get(position); //获取Fragment
//            Log.i(TAG, "page: " +  pager.toString());

            /**
              pager.mRootView就是ViewPager的布局文件base_page.xml的View填充对象
             （通过View.inflate将布局填充成View对象）
             */
            container.addView(pager.mRootView); //将页面布局添加到容器中

//            pager.initData(); //初始化数据 设为在切换页面后初始化，免得浪费流量

            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
