package com.jirdy.listview.dbUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jirdy.listview.MainActivity;

/**
 * 该类是SQLiteDatabase一个辅助类。这个类主要生成一个数据库，并对数据库的版本进行管理。
 * Created by Administrator on 2016/2/22.
 */
public class ReadProgressDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "Jirdy.Read.DBHelper";
    // If you change the database schema, you must increment the database version.
    //若改变数据库的结构（如：改变表结构(列名）），，必须提高版本，否则报错。
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ReadProgress.db";
    private static final String CHAR_TYPE = " VARCHAR";
    private static final String DATE_TYPE_AUTO = " DATETIME DEFAULT CURRENT_TIMESTAMP";//创建时自动插入当前时间
    private static final String DATE_TYPE = " DATETIME";//创建时自动插入当前时间
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    /**
     * 创建数据库的语句
     * String sql="create table tb3(idINTEGER PRIMARY KEY,timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, weight DOUBLE)";
     */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ReadProgressContract.Books.TABLE_NAME + " (" +
                    ReadProgressContract.Books._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    ReadProgressContract.Books.COLUMN_NAME_BOOK_NAME + CHAR_TYPE + COMMA_SEP +
                    ReadProgressContract.Books.COLUMN_NAME_BOOK_AUTHOR + CHAR_TYPE + COMMA_SEP +
                    ReadProgressContract.Books.COLUMN_NAME_BOOK_TYPE + CHAR_TYPE + COMMA_SEP +

                    ReadProgressContract.Books.COLUMN_NAME_TOTAL_PAGE + INT_TYPE + COMMA_SEP +
                    ReadProgressContract.Books.COLUMN_NAME_FINISH_PAGE + INT_TYPE + COMMA_SEP +

                    ReadProgressContract.Books.COLUMN_NAME_READ_DAYS + INT_TYPE + COMMA_SEP +
                    ReadProgressContract.Books.COLUMN_NAME_READ_STATE + INT_TYPE + COMMA_SEP +

//                    ReadProgressContract.Books.COLUMN_NAME_CREATE_TIME + DATE_TYPE_AUTO + COMMA_SEP +
//                    ReadProgressContract.Books.COLUMN_NAME_FINISH_TIME + DATE_TYPE +
                    ReadProgressContract.Books.COLUMN_NAME_CREATE_TIME + INT_TYPE + COMMA_SEP +
                    ReadProgressContract.Books.COLUMN_NAME_FINISH_TIME + INT_TYPE +
                    //...Any other options for the CREATE command
                    " )";

    /**
     * 删除数据库的语句
     */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ReadProgressContract.Books.TABLE_NAME;


    public ReadProgressDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 在数据库第一次生成的时候会调用这个方法，也就是说，只有在创建数据库的时候才会调用，当然也有一些其它的情况，一般我们在这个方法里边生成数据库表。
     * OnCrate是在getWritableDatabase或getReadableDatabase的时候调用的（第一次建表的时候调用）。
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        ReadProgressDBManager.debug(TAG, "onCreate: Create Database");
    }

    /**
     * 当数据库需要升级的时候，Android系统会主动的调用这个方法。一般我们在这个方法里边删除数据表，并建立新的数据表;
     * 当然是否还需要做其他的操作，完全取决于应用的需求。
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        ReadProgressDBManager.debug(TAG, "onUpgrade: Upgrade Database");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
        ReadProgressDBManager.debug(TAG, "onDowngrade: Downgrade Database from versoin " + oldVersion + " to version " + newVersion);
    }

    public void deleteDatabase(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
        ReadProgressDBManager.debug(TAG, "deleteDatabase: Delete Database");
    }

}
