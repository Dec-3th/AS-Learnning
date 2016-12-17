package com.jirdy.listview.dbUtils;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/2/22.
 */
public final class ReadProgressContract {

    public static abstract class Books implements BaseColumns {

        public static final String TABLE_NAME = "readbooks";
//        public static final String COLUMN_NAME_BOOK_ID = "BookId";// 唯一字段 Unique
        public static final String COLUMN_NAME_BOOK_NAME = "Name";// 书名 string BookName
        public static final String COLUMN_NAME_BOOK_AUTHOR = "Author";//  作者 string BookAuthor
        public static final String COLUMN_NAME_BOOK_TYPE = "Type";//  作者 string BookAuthor

        public static final String COLUMN_NAME_TOTAL_PAGE = "TotalPage";// 总页数 int TotalPage
        public static final String COLUMN_NAME_FINISH_PAGE = "FinishedPage";// 已读页数 int FinishPage

        public static final String COLUMN_NAME_READ_DAYS = "ReadDays";
        public static final String COLUMN_NAME_READ_STATE = "ReadState";

        public static final String COLUMN_NAME_CREATE_TIME = "CreateTime";
        public static final String COLUMN_NAME_FINISH_TIME = "FinishTime";

    }

}
