package com.jirdy.listview.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jirdy.listview.MainActivity;
import com.jirdy.listview.R;
import com.jirdy.listview.dbUtils.ReadProgressDBManager;
import com.jirdy.listview.model.Book;
import com.jirdy.listview.utils.BookUtils;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {

    public static String TAG = "Read.DetailActivity";

    //    private int openType;
    private EditText editName;
    private EditText editAuthor;
    private EditText editFinishPage;
    private EditText editTotalPage;
//    private EditText editBookType;
    private List<EditText> editTextList;
    private Spinner bookTypeSpinner;
    private LinearLayout buttonlayout;

    private TextView textReadState;
    private TextView textCreateTime;
    private TextView textFinishTime;

//    private Button readCardButton;

    private ReadProgressDBManager rpDBManager;
    private Book old_book = null;

    public static void debug(String TAG, String str) {
        if (MainActivity.debugmodel)
            Log.i(TAG, str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        rpDBManager = new ReadProgressDBManager(this);
        editTextList = new ArrayList<>();

        Long bookId = getIntent().getLongExtra("book_id", Book.default_long);
        if (bookId != Book.default_long)
            old_book = rpDBManager.queryBook(bookId).get(0);
        if (bookId == Book.default_long || old_book == null) {
            Toast.makeText(this, "哎呀~ 获取读书记录失败, 再试一次吧", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        debug(TAG, "书本详情：" + old_book);

        buttonlayout = (LinearLayout) findViewById(R.id.detail_button_comfirm);
        editName = (EditText) findViewById(R.id.detail_edit_bookName);
        editAuthor = (EditText) findViewById(R.id.detail_edit_bookAuthor);
//      editType = (EditText) findViewById(R.id.detail_edit_bookType);
        editFinishPage = (EditText) findViewById(R.id.detail_edit_finishPage);
        editTotalPage = (EditText) findViewById(R.id.detail_edit_totalPage);
        bookTypeSpinner = (Spinner) findViewById(R.id.detail_spinner_bookType);

        editTextList.add(editName);
        editTextList.add(editAuthor);
        editTextList.add(editFinishPage);
        editTextList.add(editTotalPage);

        textReadState = (TextView) findViewById(R.id.detail_text_readstate);
        textCreateTime = (TextView) findViewById(R.id.detail_text_createtime);
        textFinishTime = (TextView) findViewById(R.id.detail_text_finishtime);

        if (old_book != null) {
            editName.setText(old_book.getBookName());
            editAuthor.setText(old_book.getBookAuthor());
//            editBookType.setText(old_book.getBookType());
//            debug(TAG, "bookTypeSpinner.getScrollBarSize()：" + bookTypeSpinner.le() +", " + bookTypeSpinner.getItemAtPosition(3));

            for (int i = 0; i < 6; i++) {
                if (((String) bookTypeSpinner.getItemAtPosition(i)).equalsIgnoreCase(old_book.getBookType())) {
                    bookTypeSpinner.setSelection(i);
//                    debug(TAG, bookTypeSpinner.getItemAtPosition(i) + " setSelection");
                }
            }
            editFinishPage.setText(String.valueOf(old_book.getBookFinishedPage()));
            editTotalPage.setText(String.valueOf(old_book.getBookTotalPage()));

            textReadState.setText(getString(R.string.book_detail_text_readstate) + old_book.getReadStateStr());
            textCreateTime.setText(getString(R.string.book_detail_text_createtime) + old_book.getCreateTimeStr());
            if (old_book.getReadState() == 1)//已读
                textFinishTime.setText(getString(R.string.book_detail_text_finishtime) + old_book.getFinishTimeStr());
            else
                textFinishTime.setText(getString(R.string.book_detail_text_finishtime));
        }
    }


    public void bookDetailOnClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.detail_button_readcard://读书打卡
                if (old_book != null) {
                    Intent readCardIntent = new Intent(this.getBaseContext(), ReadCardActivity.class);
                    readCardIntent.putExtra("book_name", old_book.getBookName());//传入0为添加新书
                    readCardIntent.putExtra("book_finish_page", old_book.getBookFinishedPage());//传入0为添加新书
                    readCardIntent.putExtra("book_total_page", old_book.getBookTotalPage());//传入0为添加新书
                    startActivityForResult(readCardIntent, 100);
                    this.setResult(RESULT_OK, null);//使用activityResult通知ListView更新数据
                }
                break;
            case R.id.detail_button_modify:
                setEditNameTextEnable(true);
                buttonlayout.setVisibility(View.VISIBLE);
                break;
            case R.id.detail_button_delete:
                if (old_book != null) {
                    if (rpDBManager.deleteBook(old_book)) {
                        Toast.makeText(this, "删除成功 ^ ^", Toast.LENGTH_SHORT).show();
                        this.setResult(RESULT_OK, null);//使用activityResult通知ListView更新数据
                        this.finish();
                    } else {
                        Toast.makeText(this, "删除失败 T T", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.detail_btn_ok:
                if (old_book != null) {
                    if (updateBook(old_book))
                        this.setResult(RESULT_OK, null);//使用activityResult通知ListView更新数据
                    setEditNameTextEnable(false);
                    buttonlayout.setVisibility(View.GONE);
//                    this.finish();//关闭Activity
                }
                break;
            case R.id.detail_btn_cancel:
                setEditNameTextEnable(false);
                buttonlayout.setVisibility(View.GONE);
//                this.finish();
                break;
            default:
                break;
        }
    }

    public void setEditNameTextEnable(boolean type){
        for(EditText et: editTextList){
            et.setEnabled(type);
            if(type)
                et.setTextColor(Color.BLACK);
            else
                et.setTextColor(getResources().getColor(R.color.textcolor));//#787575
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    protected boolean updateBook(Book old_book) {
        Book new_book = new Book();
        new_book.setBookName(editName.getText().toString());
        new_book.setBookAuthor(editAuthor.getText().toString());
//        new_book.setBookType(editBookType.getText().toString());
        new_book.setBookType((String) bookTypeSpinner.getSelectedItem());

        int finishPage = Integer.parseInt(editFinishPage.getText().toString());
        int totalPage = Integer.parseInt(editTotalPage.getText().toString());
        new_book.setBookFinishedPage(finishPage);
        new_book.setBookTotalPage(totalPage);

        if (!BookUtils.compareBooks(new_book, old_book)) {//两本书参数不一样

            Book differBook = BookUtils.getDifferBook(new_book, old_book);//取两本书不一样的参数作为更新的部分
//            debug(TAG, "differBook: " + differBook);
            if (rpDBManager.updateBook(differBook)) {
                Toast.makeText(this, "修改成功 ^ ^", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "修改失败 T T", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "没有任何改动 0.0", Toast.LENGTH_SHORT).show();
            return false;
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
        debug(TAG, "onBookDetailActivityResult resultCode:" + resultCode + ", requestCode:" + requestCode);
        if (resultCode == RESULT_OK) {
            int finishPage = data.getIntExtra("finish_page", old_book.getBookFinishedPage());
            debug(TAG, "finishPage: " + finishPage);

            Book new_book = new Book();
            new_book.setBookId(old_book.getBookId());
            new_book.setReadDays(old_book.getReadDays() + 1);

            if (finishPage != old_book.getBookFinishedPage())
                new_book.setBookFinishedPage(finishPage);

            if (rpDBManager.updateBook(new_book)) {
                editFinishPage.setText(String.valueOf(finishPage));
                Toast.makeText(this, "修改成功 ^ ^", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "修改失败 T T", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
