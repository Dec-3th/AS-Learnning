package com.tnyoo.savedataapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SaveToPreferenceActivity extends ActionBarActivity {

    TextView readTextView;
    TextView writeTextView;
    EditText scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to_preference);

        readTextView = (TextView)findViewById(R.id.readHighScore);
        writeTextView = (TextView)findViewById(R.id.writeHighScore);
        scoreText = (EditText)findViewById(R.id.scoredata);
    }


    /*
    当又相对较小的key-value集合需要保存，你应该使用SharedPreferences APIs。 SharedPreferences 对象指向了一
个保存key-value pairs的文件，并为读写他们提供了简单的方法。
     */

    /**
     * 写入SharedPreference
     * 获取SharedPreference，有两种方法可获取：
     * 1、getSharedPreferences(String name, int mode)，name参数指定文件名获取shared preference文件。
     * 2、getPreferences(int mode)，获取activity下的默认的shared preference文件。
     * @param view
     */
    public void writeSharedPreference(View view) {

        String score = scoreText.getText().toString();
        //在activity下新建一个私有SharedPreference文件，文件名为high_score_recodes，只能本app访问.
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.high_score_file)
                , Context.MODE_PRIVATE);
//      SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        //为了写shared preferences文件，需要通过执行edit()来创建一个SharedPreferences.Editor。
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.high_score), score);
        editor.apply();//使用apply()方法来替代commit()，因为后者有可能会卡到UI Thread

        writeTextView.setText("写入最高分：\n" + score);
    }

    /**
     * 读取SharedPreference
     * @param view
     */
    public void readSharedPreference(View view) {
        String defaultValue = getResources().getString(R.string.high_score_default);
        // 获取文件名为high_score_recodes的SharedPreference文件.
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.high_score_file)
                , Context.MODE_PRIVATE);
//        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        String highScore = sharedPreferences.getString(getString(R.string.high_score), defaultValue);

        readTextView.setText("读取最高分：\n" + highScore);
    }
}
