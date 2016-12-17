package com.jirdy.listview.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.jirdy.listview.MainActivity;
import com.jirdy.listview.R;
import com.jirdy.listview.model.Book;

public class ReadCardActivity extends AppCompatActivity {

    public static String TAG = "Read.ReadCardActivity";
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);

        numberPicker = (NumberPicker) findViewById(R.id.read_card_numbicker_finish);
        TextView nameText = (TextView) findViewById(R.id.read_card_bookname);

        Intent intent = this.getIntent();
        String bookName = intent.getStringExtra("book_name");
        nameText.setText(bookName);

        int finishPage = intent.getIntExtra("book_finish_page", Book.default_int);
        if (finishPage == Book.default_int)
            finishPage = 0;
        int totalPage = intent.getIntExtra("book_total_page", Book.default_int);
        if (totalPage == Book.default_int)
            totalPage = 1000;
//        debug(TAG, "查询到书：" + old_book);

        initNumberPicker(totalPage, finishPage);

    }

    private void initNumberPicker(int max, int current) {
//        numberPicker.setFormatter(this);
//        numberPicker.setOnValueChangedListener(this);
//        hourPicker.setOnScrollListener(this);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(current);
    }

    public void readCardOnClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.read_card_confirm:

                debug(TAG, "finishPage: " + numberPicker.getValue());

                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("finish_page", numberPicker.getValue());
                this.setResult(RESULT_OK, intent);
                debug(TAG, "setResult RESULT_OK");
                //关闭Activity
                this.finish();
                break;
//            case R.id.detail_btn_cancel:
//                finish();
//                break;

            default:
                break;
        }
    }

    public static void debug(String TAG, String str) {
        if (MainActivity.debugmodel)
            Log.i(TAG, str);
    }
}
