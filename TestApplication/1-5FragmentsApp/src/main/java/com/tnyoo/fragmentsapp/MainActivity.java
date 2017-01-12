package com.tnyoo.fragmentsapp;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
        implements HeadlinesFragment.OnHeadlineSelectedListener{ //需要实现HeadlinesFragment的接口
    private int mCurrentPosition = -1;

    /**
     * 当你用XML布局文件的方式将Fragment添加进activity时，你的Fragment是不能被动态移除的,如activity_main.xml(large)。
     * 下面的headFrag是在activity运行时被添加进来，故可以在运行时被移除或替换掉。添加、删除、更新、查询
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            //要在用户交互的时候把fragment切入与切出，你必须在activity启动后，再将fragment添加进activity
            HeadlinesFragment headlinesFragment = new HeadlinesFragment();
            headlinesFragment.setArguments(getIntent().getExtras());

            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            fragTrans.add(R.id.fragment_container, headlinesFragment);//使用FragmentTransaction添加移除替换fragment.

            /*最后必须commit，可以使用同一个 FragmentTransaction进行多次fragment事务后再commit.*/
            fragTrans.commit();

            //添加fragment到activity简便写法：
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, headFrag).commit();
        }
    }


    /*
    所有fragment之间的交互需要通过他们关联的activity，两个fragment之间不应该直接交互,
    方法：在Fragment 类中定义一个接口，并且在activity中实现这个接口，onArticleSelected就是接口的实现。
     */
    @Override
    public void onArticleSelected(int position) {
        mCurrentPosition = position;
        //针对large size屏幕和一般屏幕情况
        //当前从布局中获取ArticleFragment，如果为null则当前布局不是large布局，否则是large布局（两个fragment都显示）.
        ArticleFragment articleFragment = (ArticleFragment)getSupportFragmentManager()
                .findFragmentById(R.id.article_fragment);

        if(articleFragment != null){//布局已存在，更新显示的article就行（large屏幕）.
            articleFragment.updateArticleView(position);
        }else {
            shiftFragment(position);//否则需要新建并显示fragment.
        }
    }


    /**
     * 切换Fragment，并将一个参数传给fragment.
     * @param position
     */
    public void shiftFragment(int position) {

        //创建Fragment，将选定文章的位置 作为参数从activity传给fragment.
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putInt(ArticleFragment.ARG_POSITION, position);
        articleFragment.setArguments(args);

        //替换Fragment.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, articleFragment);
 
        /*
        当你执行fragment事务的时候，例如移除或者替换，你经常要适当地让用户可以向后导航与"撤销"这次改变。为了让用
        户向后导航fragment事务，你必须在FragmentTransaction提交（commit）前调用addToBackStack()方法将该fragment放入返回栈以供恢复。
        */
        fragmentTransaction.addToBackStack(null);//null为可选的String参数为事务指定唯一的名字（非必须）。
        fragmentTransaction.commit();
        /*
         注意：
         当你移除或者替换一个fragment并把它放入返回栈中时，被移除的fragment的生命周期是stopped(不是destoryed).
         当用户返回重新恢复这个fragment,它的生命周期是restart。如果你没把fragment放入返回栈中，那么当他
         被移除或者替换时，它的生命周期是destoryed。
         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void writeSharedPreference(View view){
        Toast.makeText(this, "writeSharedPreference", Toast.LENGTH_SHORT).show();
    }

    public void readSharedPreference(View view){
        Toast.makeText(this, "readSharedPreference", Toast.LENGTH_SHORT).show();
    }
}
