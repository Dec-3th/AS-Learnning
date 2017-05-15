package com.jirdy.smartkm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jirdy.smartkm.base.impl.ContentFragment;
import com.jirdy.smartkm.base.impl.LeftMenuFragment;

/**
 * 添加SlidingMenu侧边栏流程：
 * 0.引入SlidingMenu开源包库
 * 1.继承SlidingFragmentActivity或者SlidingActivity...等等取决于你原来的布局是Activity还是Fragment（还有ListActivity)
 * 2.onCreate实现改为public
 * 3.Activity布局中添加SlidingMenu侧边栏布局
 * 4.onCreate中设置布局
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_CONTENT = "TAG_CONTENT";
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉标题 (在设置布局文件前调用）
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设定Activity布局
        setContentView(R.layout.activity_main);

        //设定侧边栏布局
        setBehindContentView(R.layout.left_menu_framelayout);

        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindWidth(200); //设置侧边栏宽度像素
//        slidingMenu.setBehindOffset(200);
        slidingMenu.setShadowWidth(100);  //阴影像素

        //初始化fragment对象
        initFragment();
    }


    /**
     * 初始化fragment对象
     */
    private void initFragment() {
        //Fragment管理器
        FragmentManager fragmentManager = getSupportFragmentManager();

        //开启一个事务，关于事务：可以进行批量操作，操作失败可以回滚，不会导致中间错误，另外能增加程序稳定性，提高效率
        //替换帧布局，将Activity和侧边栏布局中的FrameLayout换成Fragment（填充的是Fragment的布局）
        //后面添加的标记Tag，后面可以通过TAG来获取Fragment
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.left_menu_framelayout, new LeftMenuFragment(), TAG_LEFT_MENU);
        ft.replace(R.id.main_activity_framelayout, new ContentFragment(), TAG_CONTENT);
        ft.commit();//提交事务

        //通过TAG来获取Fragment 示例
        //fragmentManager.findFragmentByTag(TAG_CONTENT);
    }

    /**
     * 侧边栏展开或者收起的开关
     */
    public void slidingMenuToggle() {
        //获取侧边栏，进行开关操作
        SlidingMenu slidingMenu = getSlidingMenu();

        //SlidingMenu的toggle方法：其实是一个开关，如果状态为开，调用则关；如果状态为关，调用则打开
        slidingMenu.toggle();
    }


    public void setSlidingMenuEnable(boolean enable) {

//        //先将BaseFragment中保存的Activity 转换为MainActivity
//        MainActivity mainActivity = (MainActivity) mActivity;

        //再通过mainActivity拿到侧边栏，然后设置侧边栏是否可呼出
        SlidingMenu slidingMenu = getSlidingMenu();

        if (enable) {
            //设置全屏幕可滑动呼出侧边栏
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            //设置无法通过触摸呼出侧边栏（禁用侧边栏）
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    /**
     * 获取LeftMenuFragment
     *
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (LeftMenuFragment) fragmentManager.findFragmentByTag(TAG_LEFT_MENU);
    }

    /**
     * 获取ContentFragment
     *
     * @return
     */
    public ContentFragment getContentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (ContentFragment) fragmentManager.findFragmentByTag(TAG_CONTENT);
    }

}
