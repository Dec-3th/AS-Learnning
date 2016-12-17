package com.jirdy.listview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jirdy.listview.Activitys.AddBookActivity;
import com.jirdy.listview.Activitys.BookDetailActivity;
import com.jirdy.listview.Fragments.BookListFragment;
import com.jirdy.listview.Fragments.TableFragment;
import com.jirdy.listview.model.Book;
import com.jirdy.listview.model.Table;
import com.jirdy.listview.utils.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static String TAG = "List_Activity";
    public static boolean debugmodel = true;
//    private static final int REFRESH_COMPLETE = 0X110;
//    private BookListAdapter adapter;
//    private List<Book> bookList;
    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private OnDBDataChanged onDBDataChangedList;//读书打卡
    private OnDBDataChanged onDBDataChangedTable;//读书打卡
    private RadioButton rbBookList, rbBookGraph, rbDiscovery, rbMe;
    //    private SwipeRefreshLayout mSwipeLayout;

    /*
    为了让fragment与activity交互，你可以在Fragment 类中定义一个接口，并且在activity中实现这个接口。Fragment在他们生
    命周期的onAttach()方法中获取接口的实现，然后调用接口的方法来与Activity交互。
    */
    public interface OnDBDataChanged {
        public void refreshList();
    }

    public static void debug(String TAG, String str) {
        if (MainActivity.debugmodel)
            Log.i(TAG, str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_CustomActionBar);
        setContentView(R.layout.activity_main);
        initView();

//        bookList = new ArrayList<Book>();//新建数据源，为了能正常更新listview，该数据源不能变（即bookList不能指向新的List，new出来的，只能拷贝到bookList中）
//        refreshList();
//        debug(TAG, "onStart bookList size:" + bookList.size());
//        if (findViewById(R.id.book_list) != null && bookList != null) {
//
//            ListView listView = (ListView) findViewById(R.id.book_list);
//            listView.setOnItemClickListener(this);
//            adapter = new BookListAdapter(this, bookList);
//            listView.setAdapter(adapter);

//            /**下拉刷新列表 begin**/
//            mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe);
//            mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                    android.R.color.holo_orange_light, android.R.color.holo_red_light);
//
//            mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//                @Override
//                public void onRefresh() {
//                    //正在刷新
//                    mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);
//                }
//            });
//            mSwipeLayout.setProgressViewOffset(false, 0, (int) TypedValue
//                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
//                            .getDisplayMetrics()));
//            /**下拉刷新列表 end**/
//        }

    }

    private void initView() {
        /**
         * RadioGroup部分
         */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbBookList = (RadioButton) findViewById(R.id.rb_book_list);
        rbBookGraph = (RadioButton) findViewById(R.id.rb_book_graph);
        rbDiscovery = (RadioButton) findViewById(R.id.rb_discovery);
        rbMe = (RadioButton) findViewById(R.id.rb_me);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_book_list:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        setTitle("读书进度表");
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_book_graph:
                        setTitle("读书进度一览表");
                        viewPager.setCurrentItem(1, false);
                        break;
//                    case R.id.rb_discovery:
//                        viewPager.setCurrentItem(2, false);
//                        break;
//                    case R.id.rb_me:
//                        viewPager.setCurrentItem(3, false);
//                        break;
                }
            }
        });

        /**
         * ViewPager部分
         */
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        BookListFragment bookListFragment = new BookListFragment();
        onDBDataChangedList = bookListFragment;
        TableFragment bookTableFragment = new TableFragment();
//        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        onDBDataChangedTable = bookTableFragment;
        List<Fragment> alFragment = new ArrayList<Fragment>();
        alFragment.add(bookListFragment);
        alFragment.add(bookTableFragment);
//        alFragment.add(discoveryFragment);
//        alFragment.add(bookListFragment);

        //ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), alFragment));
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Toast.makeText(getBaseContext(), "onPageScrolled: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getBaseContext(), "onPageSelected: " + position, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_book_list);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_book_graph);
                        break;
//                    case 2:
//                        radioGroup.check(R.id.rb_discovery);
//                        break;
//                    case 3:
//                        radioGroup.check(R.id.rb_me);
//                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_book:
                Intent addIntent = new Intent(this.getBaseContext(), AddBookActivity.class);
//                addIntent.putExtra("type", 0);//传入0为添加新书
                startActivityForResult(addIntent, 100);
                return true;
            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "Click Action Bar: 设置", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_read_remind:
                Toast.makeText(getBaseContext(), "设置读书提醒", Toast.LENGTH_SHORT).show();
//                Intent remindIntent = new Intent(this.getBaseContext(), ReadCardActivity.class);
//                remindIntent.putExtra("book_name", 0);//传入0为添加新书
//                startActivity(remindIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        debug(TAG, "onMainActivityResult resultCode:" + resultCode + ", requestCode:" + requestCode);
        if (resultCode == RESULT_OK) {
            //把你的刷新数据的处理逻辑放在这里，当你在activity2返回的时候将执行这里的代码
            onDBDataChangedList.refreshList();//返回时刷新ListView
            onDBDataChangedTable.refreshList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    /* ------------下拉刷新列表 begin----------------- */
//    private Handler mHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            //刷新完成
//            switch (msg.what) {
//                case REFRESH_COMPLETE:
//                    refreshList();
//                    mSwipeLayout.setRefreshing(false);
//                    break;
//            }
//        }
//
//    };
//    /* ------------下拉刷新列表 end------------------- */

    //增加T 删除 修改T 查询T
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        long bookId = -1;
        Object obj = adapterView.getItemAtPosition(i);
        if(obj instanceof Book) {
            Book book = (Book) adapterView.getItemAtPosition(i);
            bookId = book.getBookId();
        }else if(obj instanceof Table.TableRow){
            Table.TableRow tableRow = (Table.TableRow)adapterView.getItemAtPosition(i);
            bookId = tableRow.getBookId();
        }

        Toast.makeText(this, "点击id: " + bookId, Toast.LENGTH_SHORT).show();

        if(bookId != -1) {
            Intent updateIntent = new Intent(this.getBaseContext(), BookDetailActivity.class);
            updateIntent.putExtra("book_id", bookId);//传入要更新的书的id
            startActivityForResult(updateIntent, 200);
        }
    }

}
