package com.tnyoo.actionbarapp;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * 本类显示子Activity如何通过Action Bar的向上导航按钮，回到逻辑父Activity，向上导航按钮的配置在Manifest和onCreate中
 * 点击按钮的响应事件在onOptionsItemSelected中
 */
public class UpHomeActivity extends ActionBarActivity {

    private static final String TAG = "ActionBar_UpHome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uphome);

        //然后，通过调用setDisplayHomeAsUpEnabled() 来把app icon设置成可用的向上按钮：
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d(TAG, "UpHomeActivity onCreate() returned: " + "second activity");
    }

    /**
     * 启动另一个Activity
     **/
    public void openCustomTheme(View view) {
        Toast.makeText(getBaseContext(), "启动CustomThemeActivity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ThemeActivity.class);
        startActivity(intent);
    }

    /**
     * 为 action bar布局菜单条目，onCreateOptionsMenu()回调方法用来inflate菜单资源, 从而获取Menu对象
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // 对action bar的Up/Home按钮做出反应
            case R.id.action_search:
                Toast.makeText(getBaseContext(), "Click Action Bar: 搜索", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);

                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {//这将在下一个Activity测试
                    Toast.makeText(getBaseContext(), "向上导航到最近的一个父activity", Toast.LENGTH_SHORT).show();
                    // 这个activity不是这个app任务的一部分, 所以当向上导航时创建
                    // 用合成后退栈(synthesized back stack)创建一个新任务。
                    TaskStackBuilder.create(this)
                            // 添加这个activity的所有父activity到后退栈中
                            .addNextIntentWithParentStack(upIntent)
                                    // 向上导航到最近的一个父activity
                            .startActivities();
                } else {
                    // 这个activity是这个app任务的一部分, 所以向上导航至逻辑父activity.
                    NavUtils.navigateUpTo(this, upIntent);
                    Toast.makeText(getBaseContext(), "这个activity是这个app任务的一部分, 向上导航至逻辑父activity.", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
