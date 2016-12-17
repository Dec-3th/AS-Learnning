package com.jirdy.listview.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jirdy.listview.R;
import com.jirdy.listview.dbUtils.ReadProgressDBManager;
import com.jirdy.listview.model.Book;

public class AddBookActivity extends AppCompatActivity {
    public static String TAG = "Read.AddActivity";

    private EditText editBookName;
    private EditText editBookAuthor;
    private EditText editBookFinishPage;
    private EditText editBookTotalPage;
    //    private EditText editBookType;
    private Spinner bookTypeSpinner;

    private ReadProgressDBManager rpDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        rpDBManager = new ReadProgressDBManager(this);

        editBookName = (EditText) findViewById(R.id.add_edit_bookName);
        editBookAuthor = (EditText) findViewById(R.id.add_edit_bookAuthor);
//        editBookType = (EditText) findViewById(R.id.add_edit_bookType);
        editBookFinishPage = (EditText) findViewById(R.id.add_edit_finishPage);
        editBookTotalPage = (EditText) findViewById(R.id.add_edit_totalPage);
        bookTypeSpinner = (Spinner) findViewById(R.id.add_spinner_bookType);
    }

    public void addBookOnClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_btn_ok:
                if (addNewBook())
                    this.setResult(RESULT_OK, null);//设置返回数据

                this.finish();//关闭Activity
                break;
            case R.id.add_btn_cancel:
                this.finish();
                break;
            default:
                break;
        }
    }

    protected boolean addNewBook() {
        Book book = new Book();
        book.setBookName(editBookName.getText().toString());
        book.setBookAuthor(editBookAuthor.getText().toString());
//        book.setBookType(editBookType.getText().toString());
        book.setBookType((String) bookTypeSpinner.getSelectedItem());

        int finishpage = Integer.parseInt(editBookFinishPage.getText().toString());
        int totalpage = Integer.parseInt(editBookTotalPage.getText().toString());
        book.setBookFinishedPage(finishpage);
        book.setBookTotalPage(totalpage);

        if (rpDBManager.addBook(book)) {
            Toast.makeText(this, "添加成功 ^ ^", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "添加失败 T T", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
