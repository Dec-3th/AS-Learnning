package com.tnyoo.savedataapp.dbUtils;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/2/22.
 */
public final class FeedReaderContract {

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        public static final String COLUMN_NAME_CONTENT = "content";
    }

}
