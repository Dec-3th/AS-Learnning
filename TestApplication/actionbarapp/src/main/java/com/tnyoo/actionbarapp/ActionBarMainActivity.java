package com.tnyoo.actionbarapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * 本类展示Action Bar的基本使用方法，包含Action Bar的配置显示和按钮响应事件
 */
public class ActionBarMainActivity extends ActionBarActivity {

    private static final String TAG = "ActionBar_Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_actionbar);
        Log.d(TAG, "ActionBarMainActivity onCreate() returned: " + "first activity");
    }

    /**
     * 启动另一个Activity
     **/
    public void openUpHomeActivity(View view) {
        Toast.makeText(getBaseContext(), "启动UpHomeActivity", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, UpHomeActivity.class);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);//ActionBar的配置放在res/menu/menu_main.xml
        return true;
    }

    /**
     * Action Bar 操作按钮响应事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                Toast.makeText(getBaseContext(), "Click Action Bar: 搜索", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "Click Action Bar: 设置", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_update:
                Toast.makeText(getBaseContext(), "Click Action Bar: 检查更新", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_contact:
                Toast.makeText(getBaseContext(), "Click Action Bar: 联系我们", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
