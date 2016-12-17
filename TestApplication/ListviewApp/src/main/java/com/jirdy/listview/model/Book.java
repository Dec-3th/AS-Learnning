package com.jirdy.listview.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/4/21.
 */
public class Book {

    public static final int default_int = -1;
    public static final int init_int = 0;
    public static final String default_string = null;
    public static final Long default_long = -1l;

    private Long bookId;// DB自动生成主键.
    private String bookName;// 书名
    private String bookAuthor;// 作者
    private String bookType;// 阅读状态：未读0 在读1 已读2

    private int bookTotalPage;// 总页数
    private int bookFinishedPage;// 读完页数

    private int readDays;// 读书天数（打卡为准）
    private int readState;//阅读状态：在读0 已读1

    private long createTime;// 创建时自动生成创建时间
    private long finishTime;//完成时需要插入的完成时间

    //添加参数：
    //阅读状态：未读0 在读1 已读2
    //图书分类：
    //另外读书提醒时间放在actionbar上面，设定一个提醒时间：只是提醒读书，而不是提醒读哪本书

    public Book() {
        this.bookId = default_long;
        this.bookName = default_string;
        this.bookAuthor = default_string;
        this.bookType = default_string;

        this.bookTotalPage = default_int;
        this.bookFinishedPage = default_int;

        this.readDays = default_int;
        this.readState = default_int;//默认在读中

        this.createTime = default_long;
        this.finishTime = default_long;
    }

    public Book(Long bookId, String bookName, String bookAuthor, int bookTotalPage, int bookFinishedPage,
                int readDays, long createTime, long finishTime) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;

        this.bookTotalPage = bookTotalPage;
        this.bookFinishedPage = bookFinishedPage;

        this.readDays = readDays;

        this.createTime = createTime;
        this.finishTime = finishTime;
    }

    /**
     * 获取读书进度的整数值.
     *
     * @return
     */
    public int getBookReadProgress() {
        float progress = (float) bookFinishedPage / (float) bookTotalPage;
//        Log.i("List", "F:" + getBookFinishedPage() + ", T:" + getBookTotalPage() + ", progress: " + progress);
        return (int) (progress * 100);
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getBookTotalPage() {
        return bookTotalPage;
    }

    public void setBookTotalPage(int bookTotalPage) {
        this.bookTotalPage = bookTotalPage;
        check();
    }

    public int getBookFinishedPage() {
        return bookFinishedPage;
    }

    public void setBookFinishedPage(int bookFinishedPage) {
        this.bookFinishedPage = bookFinishedPage;
        check();
    }

    public int getReadDays() {
        return readDays;
    }

    public void setReadDays(int readDays) {
        this.readDays = readDays;
    }

    public int getReadState() {
        return readState;
    }

    /**
     * 设置阅读状态：未读0 在读1 已读2
     *
     * @param readState
     */
    public void setReadState(int readState) {
        this.readState = readState;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public String getCreateTimeStr() {
        return fomateShowTime(createTime);
    }

    public String getFinishTimeStr() {
        return fomateShowTime(finishTime);
    }

    public String getReadStateStr() {
        //在读0 已读1
        if (readState == 0)
            return "在读";
        else
            return "已读";
    }

    public static String fomateShowTime(long time) {
        //设置日期格式 yy.MM.dd HH:mm
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        if (time != 0)
            return format.format(new Date(time));
        else {
//            throw new RuntimeException("wrong time format, time: "+time);
            return "";
        }

    }

    //在读0 已读1
    private void check() {
        if (bookFinishedPage >= 0 && bookFinishedPage < bookTotalPage) {
            readState = 0;
            finishTime = 0;
        } else if (bookFinishedPage == bookTotalPage && bookFinishedPage != default_int) {
            readState = 1;
            finishTime = new Date().getTime();
        }
    }

    public String toString() {
        String bookjson = "{_Id:" + bookId +
                ",book_name:" + bookName +
                ",book_author:" + bookAuthor +
                ",book_type:" + bookType +
                ",book_finished_page:" + bookFinishedPage +
                ",book_total_page:" + bookTotalPage +
                ",read_days:" + readDays +
                ",read_state:" + readState +
                ",book_create_time:" + createTime +
                ",book_finish_time:" + finishTime + "}";

        return bookjson;
    }
}