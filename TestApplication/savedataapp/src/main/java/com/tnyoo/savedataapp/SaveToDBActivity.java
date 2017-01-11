package com.tnyoo.savedataapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tnyoo.savedataapp.dbUtils.FeedReaderContract;
import com.tnyoo.savedataapp.dbUtils.FeedReaderDbHelper;

public class SaveToDBActivity extends ActionBarActivity {
    private static final String TAG = "SAVE";
    private FeedReaderDbHelper frDBHelper;
    private EditText entryIdText;
    private EditText titleText;
    private EditText contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to_db);
        frDBHelper = new FeedReaderDbHelper(this);

        entryIdText = (EditText) findViewById(R.id.entryIdText);
        titleText = (EditText) findViewById(R.id.titleText);
        contentText = (EditText) findViewById(R.id.contentText);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add:
                addToDB(entryIdText.getText().toString(), titleText.getText().toString(), contentText.getText().toString());
                break;
            case R.id.query:
                queryFromDB(entryIdText.getText().toString(), true);
                break;
            case R.id.update:
                updateToDB(entryIdText.getText().toString(), contentText.getText().toString());
                break;
            case R.id.delete:
                deleteFromDB(entryIdText.getText().toString(), true);
                break;
        }
    }

    public void addToDB(String entryid, String title, String content) {
        SQLiteDatabase db = frDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID, entryid);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT, content);

        /** insert()  方法的第一个参数是table名，第二个参数会使得系统自动对那些 ContentValues没有提供数据的列填充数据
         为 null  ，如果第二个参数传递的是null，那么系统则不会对那些没有提供数据的列进行填充。*/
        long rowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                values);

        Log.i(TAG, "插入数据 entryId: " + entryid + ", title: " + title + ", content: " + content);
        Toast.makeText(this, values.toString() + ", 插入数据成功，rowId: " + rowId, Toast.LENGTH_SHORT).show();
    }

    public void queryFromDB(String entryid, boolean queryAll) {
        SQLiteDatabase db = frDBHelper.getReadableDatabase();
        // Define a columns that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = new String[]{
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " DESC";

        Cursor cursor;

        if (!queryAll) {
            // Define 'where' part of query.
            String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(entryid)};

            cursor = db.query(FeedReaderContract.FeedEntry.TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        } else {
            cursor = db.query(FeedReaderContract.FeedEntry.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    sortOrder);
        }


        //读取查询到的数据
        long id = -1;
        String entryId = "";
        String title = "";
        String content = "";
        cursor.moveToFirst();
        // 使用while + cursor.isAfterLast方法判断是否到末尾节点，比for + cursor.count()好用
        while (!cursor.isAfterLast()) {
            id = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));//id的索引位置

            entryId = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID));//entryid的索引位置

            title = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));//title的索引位置

            content = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT));//content的索引位置

            cursor.moveToNext();//移动浮标

            Log.i(TAG, "查询到数据 id: " + id + ", entryId: " + entryId + ", title: " + title + ", content: " + content);
        }
        cursor.close();
        Toast.makeText(this, "查询数据成功:" + "id: " + id + ", entryId: " + entryId + ", title: " + title + ", content: " + content, Toast.LENGTH_SHORT).show();
    }

    public void updateToDB(String entryid, String content) {
        SQLiteDatabase db = frDBHelper.getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT, content);
        // Which row to update, based on the ID
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(entryid)};
        int count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        Toast.makeText(this, "更新数据成功，count: " + count, Toast.LENGTH_SHORT).show();
    }

    public void deleteFromDB(String entryid, boolean deleteAll) {
        SQLiteDatabase db = frDBHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(entryid)};
        // Issue SQL statement.
        long rowId;
        if (!deleteAll)
            rowId = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
        else
            rowId = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);
//        frDBHelper.deleteDatabase(db);// 删除整个数据库.
        Toast.makeText(this, "删除数据成功，rowId: " + rowId, Toast.LENGTH_SHORT).show();
    }

}
