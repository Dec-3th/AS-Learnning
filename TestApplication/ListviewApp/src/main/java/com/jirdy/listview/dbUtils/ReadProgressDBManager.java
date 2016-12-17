package com.jirdy.listview.dbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jirdy.listview.MainActivity;
import com.jirdy.listview.model.Book;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ReadProgressDBManager {

    private static final String TAG = "Jirdy.Read.DB";
    private ReadProgressDBHelper RPDBHelper;
    private static final boolean debug = false;

    public ReadProgressDBManager(Context context) {
        RPDBHelper = new ReadProgressDBHelper(context);
    }

    public static void debug(String TAG, String str) {
        if (debug)
            Log.i(TAG, str);
    }

    /**
     * 在db中插入一条书本信息
     * [bookName](不能为空）， [bookAuthor]， [bookTotalPage](不能为空），
     * [bookFinishedPage](不能为空）， [readDays]
     */
    public boolean addBook(Book book) {
        long rowId = Book.default_long;
        rowId = addToDB(book.getBookName(), book.getBookAuthor(), book.getBookType(), book.getBookTotalPage(),
                book.getBookFinishedPage());
        return (rowId != Book.default_long);
    }

    /**
     * 从db中删除书本信息
     * [_id] database中存储的主键(不能为空）
     */
    public boolean deleteBook(Book book) {
        long rowId = Book.default_long;
        checkBookId(book);
        if (queryBook(book.getBookId()).size() > 0)
            rowId = deleteFromDB(book.getBookId());
        return (rowId != Book.default_long);
    }

    /**
     * 更新书本信息
     * [_id] database中存储的主键(不能为空）. [bookName][bookAuthor] [bookTotalPage]]
     * [bookFinishedPage] [readDays][finishTime]
     */
    public boolean updateBook(Book book) {
        checkBookId(book);
        int count = 0;
        count = updateBookInfo(book.getBookId(), book.getBookName(), book.getBookAuthor(), book.getBookType(),
                book.getBookTotalPage(), book.getBookFinishedPage(), book.getReadDays(), book.getReadState(), book.getFinishTime());
        return (count > 0);
    }

    /**
     * 查询书本信息
     * [_id] database中存储的主键(不能为空）
     */
    public List<Book> queryBook(Long bookId) {
//        List<Book> bookList = queryFromDB(bookId, -1, false);
//        if (bookList.size() == 1)
//            return bookList.get(0);
//        else
//            return null;
        return queryFromDB(bookId, -1, false);
    }

    /**
     * 查询书本信息
     * [readState]
     */
    public List<Book> queryBookWithState(int readState) {
        return queryFromDB(-1l, readState, false);
    }

    /**
     * 查询所有书本信息
     */
    public List<Book> queryAllBooks() {
        return queryFromDB(null, -100, true);
    }

    public void deleteDB() {
        SQLiteDatabase db = RPDBHelper.getWritableDatabase();
        RPDBHelper.deleteDatabase(db);
    }

    private long addToDB(String bookName, String bookAuthor, String bookType, int totalPage, int finishPage) {
        SQLiteDatabase db = RPDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ReadProgressContract.Books.COLUMN_NAME_BOOK_NAME, bookName);
        values.put(ReadProgressContract.Books.COLUMN_NAME_BOOK_AUTHOR, bookAuthor);
        values.put(ReadProgressContract.Books.COLUMN_NAME_BOOK_TYPE, bookType);

        values.put(ReadProgressContract.Books.COLUMN_NAME_TOTAL_PAGE, totalPage);
        values.put(ReadProgressContract.Books.COLUMN_NAME_FINISH_PAGE, finishPage);

        values.put(ReadProgressContract.Books.COLUMN_NAME_READ_DAYS, Book.init_int);
        values.put(ReadProgressContract.Books.COLUMN_NAME_READ_STATE, Book.init_int);

        values.put(ReadProgressContract.Books.COLUMN_NAME_CREATE_TIME, new Date().getTime());

        //CreateTime由创建时数据库自动插入，不需手动插入 nullColumnHack

        /** insert()  方法的第一个参数是table名，第二个参数(FeedEntry.COLUMN_NAME_NULLABLE)会使得系统自动对那些
         * ContentValues没有提供数据的列填充数据为 null  ，如果第二个参数传递的是null，那么系统则不会对那些没有提供数据的列进行填充。*/
        long rowId = db.insert(ReadProgressContract.Books.TABLE_NAME,
                null,
                values);

        debug(TAG, "rowId:" + rowId + "插入数据 bookName: " + bookName + ", bookAuthor: " + bookAuthor);
        return rowId;
    }

    private long deleteFromDB(Long _id) {
        SQLiteDatabase db = RPDBHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = ReadProgressContract.Books._ID + " LIKE ?";
//        String selection = ReadProgressContract.Books.COLUMN_NAME_BOOK_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(_id)};
        // Issue SQL statement.
        long rowId = db.delete(ReadProgressContract.Books.TABLE_NAME, selection, selectionArgs);

//        RPDBHelper.deleteDatabase(db);// 删除整个数据库.
        debug(TAG, "删除数据成功，rowId: " + rowId);
        return rowId;
    }

    /**
     * 更新书本信息
     *
     * @param _id              database中存储的主键.
     * @param bookName         （null表示不更新书名）
     * @param bookAuthor       （null表示不更新书作者）
     * @param bookTotalPage    （-1表示不更新书总页数）
     * @param bookFinishedPage （-1表示不更新书已读页数）
     * @param readDays         （-1表示不更新书已读天数）
     * @param finishTime       （null表示不更新书完成时间）
     */
    private int updateBookInfo(Long _id, String bookName, String bookAuthor, String bookType, int bookTotalPage,
                               int bookFinishedPage, int readDays, int readState, long finishTime) {
        SQLiteDatabase db = RPDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();// New value for one column

        if (bookName != Book.default_string)
            values.put(ReadProgressContract.Books.COLUMN_NAME_BOOK_NAME, bookName);
        if (bookAuthor != Book.default_string)
            values.put(ReadProgressContract.Books.COLUMN_NAME_BOOK_AUTHOR, bookAuthor);
        if (bookType != Book.default_string)
            values.put(ReadProgressContract.Books.COLUMN_NAME_BOOK_TYPE, bookType);

        if (bookTotalPage != Book.default_int)
            values.put(ReadProgressContract.Books.COLUMN_NAME_TOTAL_PAGE, bookTotalPage);
        if (bookFinishedPage != Book.default_int)
            values.put(ReadProgressContract.Books.COLUMN_NAME_FINISH_PAGE, bookFinishedPage);

        if (readDays != Book.default_int)
            values.put(ReadProgressContract.Books.COLUMN_NAME_READ_DAYS, readDays);
        if (readState != Book.default_int)
            values.put(ReadProgressContract.Books.COLUMN_NAME_READ_STATE, readState);

        if (finishTime != Book.default_long)
            values.put(ReadProgressContract.Books.COLUMN_NAME_FINISH_TIME, finishTime);
/*
        if (bookName != Book.default_string && bookName != book_old.getBookName())
            values.put(ReadProgressContract.Books.COLUMN_NAME_BOOK_NAME, bookName);
        if (bookAuthor != Book.default_string && bookAuthor != book_old.getBookAuthor())
            values.put(ReadProgressContract.Books.COLUMN_NAME_BOOK_AUTHOR, bookAuthor);
        if (bookTotalPage != Book.default_int && bookTotalPage != book_old.getBookTotalPage())
            values.put(ReadProgressContract.Books.COLUMN_NAME_TOTAL_PAGE, bookTotalPage);
        if (bookFinishedPage != Book.default_int && bookFinishedPage != book_old.getBookFinishedPage())
            values.put(ReadProgressContract.Books.COLUMN_NAME_FINISH_PAGE, bookFinishedPage);
        if (readDays != Book.default_int && readDays != book_old.getReadDays())
            values.put(ReadProgressContract.Books.COLUMN_NAME_READ_DAYS, readDays);
        if (finishTime != Book.default_string && finishTime != book_old.getFinishTime())
            values.put(ReadProgressContract.Books.COLUMN_NAME_FINISH_TIME, finishTime);
            */

        int count = 0;
        if (values.size() > 0) {
            // Which row to update, based on the ID
            String selection = ReadProgressContract.Books._ID + " LIKE ?";

            String[] selectionArgs = {String.valueOf(_id)};
            count = db.update(
                    ReadProgressContract.Books.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

            debug(TAG, "更新数据成功，count: " + count);
        } else
            debug(TAG, "没有数据需要更新！");
        return count;
    }

//    private int countBookByState(int readState){
//        SQLiteDatabase db = RPDBHelper.getReadableDatabase();
//        int count  = 0;
//        // Define 'where' part of query.
//        String selection = ReadProgressContract.Books.COLUMN_NAME_READ_STATE + " LIKE ?";
//        // Specify arguments in placeholder order.
//        String[] selectionArgs = {String.valueOf(readState)};
//        String[] columns = new String[]{
//                ReadProgressContract.Books._ID};
//
//        Cursor cursor = db.query(ReadProgressContract.Books.TABLE_NAME, // table
//                columns,// String[] columns
//                selection,// String selection: 条件字句，相当于where
//                selectionArgs,// String[] selectionArgs
//                null,// String groupBy
//                null,// String having
//                null);// String orderBy
//
//        return cursor.getColumnCount();
//    }

    /**
     * '
     * 查询指定书的信息或查询全部书信息.
     *
     * @param _id      书在列表中位置index，当queryAll为false时有效.
     * @param queryAll boolean 是否查询全部数据.
     * @return 查询到的书单列表.
     */
    private List<Book> queryFromDB(Long _id, int readState, boolean queryAll) {

        SQLiteDatabase db = RPDBHelper.getReadableDatabase();
        // Define a columns that specifies which columns from the database
        // you will actually use after this query.
        String[] columns = new String[]{
                ReadProgressContract.Books._ID,
                ReadProgressContract.Books.COLUMN_NAME_BOOK_NAME,
                ReadProgressContract.Books.COLUMN_NAME_BOOK_AUTHOR,
                ReadProgressContract.Books.COLUMN_NAME_BOOK_TYPE,

                ReadProgressContract.Books.COLUMN_NAME_TOTAL_PAGE,
                ReadProgressContract.Books.COLUMN_NAME_FINISH_PAGE,

                ReadProgressContract.Books.COLUMN_NAME_READ_DAYS,
                ReadProgressContract.Books.COLUMN_NAME_READ_STATE,

                ReadProgressContract.Books.COLUMN_NAME_CREATE_TIME,
                ReadProgressContract.Books.COLUMN_NAME_FINISH_TIME,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ReadProgressContract.Books._ID + " DESC";

        Cursor cursor = null;
        if (queryAll) {
            cursor = db.query(ReadProgressContract.Books.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    sortOrder);

        } else if (_id != null) {/* 根据图书id查询单本书 */
            // Define 'where' part of query.
            String selection = ReadProgressContract.Books._ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(_id)};

            cursor = db.query(ReadProgressContract.Books.TABLE_NAME, // table
                    columns,// String[] columns
                    selection,// String selection: 条件字句，相当于where
                    selectionArgs,// String[] selectionArgs
                    null,// String groupBy
                    null,// String having
                    sortOrder);// String orderBy
        } else if (readState != -100) {/* 根据读书完成情况来查询一类书，比如：在读，已读，未读 */
            // Define 'where' part of query.
            String selection = ReadProgressContract.Books.COLUMN_NAME_READ_STATE + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = {String.valueOf(readState)};

            cursor = db.query(ReadProgressContract.Books.TABLE_NAME, // table
                    columns,// String[] columns
                    selection,// String selection: 条件字句，相当于where
                    selectionArgs,// String[] selectionArgs
                    null,// String groupBy
                    null,// String having
                    sortOrder);// String orderBy
        }

        return getDataFromCursor(cursor);
    }

    /**
     * 使用查询返回的浮标cursor（指针），移动获取数据并返回。
     *
     * @param cursor 查询返回的浮标
     * @return 查询数据
     */
    private List<Book> getDataFromCursor(Cursor cursor) {
        if (cursor == null)
            return null;

        List<Book> bookList = new ArrayList<Book>();
        //读取查询到的数据
        // 使用while + cursor.isAfterLast方法判断是否到末尾节点，比for + cursor.count()好用
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Book book = new Book();

            book.setBookId(cursor.getLong(getCoumnIndex(cursor, ReadProgressContract.Books._ID)));
            book.setBookName(cursor.getString(getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_BOOK_NAME)));
            book.setBookAuthor(cursor.getString(getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_BOOK_AUTHOR)));
            book.setBookType(cursor.getString(getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_BOOK_TYPE)));

            book.setBookFinishedPage(cursor.getInt(getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_FINISH_PAGE)));
            book.setBookTotalPage(cursor.getInt(getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_TOTAL_PAGE)));

            book.setReadDays(cursor.getInt(getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_READ_DAYS)));
            book.setReadState(cursor.getInt(getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_READ_STATE)));

            //获取书本创建时间
            Long createDate = cursor.getLong(
                    getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_CREATE_TIME));
            if (createDate != Book.default_long && createDate != null)
                book.setCreateTime(createDate);

            //获取书本完成时间
            Long finishDate = cursor.getLong(
                    getCoumnIndex(cursor, ReadProgressContract.Books.COLUMN_NAME_FINISH_TIME));
            if (finishDate != Book.default_long && finishDate != null)
                book.setFinishTime(finishDate);

            bookList.add(book);
            cursor.moveToNext();//移动浮标

            debug(TAG, "查询到数据: " + book.toString());
        }

        return bookList;
    }

    /**
     * 检查bookid的有效性
     *
     * @param book
     */
    private void checkBookId(Book book) {
        if (book.getBookId() == Book.default_long)
            throw new IllegalArgumentException("book_id没有有效值, illegal book_id: " + book.getBookId());
    }

//    /**
//     * 将数据库中取出时间格式化为我们需要格式的时间
//     */
//    private String dateFormat(String myDate) {
//        Date date = null;
//        if (myDate == null)
//            return null;
//        if (myDate == "")
//            return "";
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
////        format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));// 中国北京时间，东八区
//        format.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//        try {
//            date = format.parse(myDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date.toString();
//    }

    /**
     * 根据sql查询获得的浮标和列名，获取列的索引位置
     *
     * @param cursor
     * @param columnName
     * @return
     */
    private int getCoumnIndex(Cursor cursor, String columnName) {
        return cursor.getColumnIndexOrThrow(columnName);
    }

}
