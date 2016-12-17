package com.jirdy.listview.utils;

import com.jirdy.listview.dbUtils.ReadProgressContract;
import com.jirdy.listview.model.Book;

/**
 * Created by Administrator on 2016/4/23.
 */
public class BookUtils {

    /**
     * 比较两本书的参数是否一样。
     * （排除bookId，createTime这些自动生成的参数不进行比较）。
     * @param book1
     * @param book2
     * @return
     */
    public static boolean compareBooks(Book book1, Book book2) {

        if (book1.getBookName() != book2.getBookName())
            return false;
        if (book1.getBookAuthor() != book2.getBookAuthor())
            return false;
        if (book1.getBookType() != book2.getBookType())
            return false;

        if (book1.getBookTotalPage() != book2.getBookTotalPage())
            return false;
        if (book1.getBookFinishedPage() != book2.getBookFinishedPage())
            return false;

        if (book1.getReadDays() != book2.getReadDays())
            return false;
        if (book1.getReadState() != book2.getReadState())
            return false;

        if (book1.getFinishTime() != book2.getFinishTime())
            return false;

        return true;
    }

    /**
     * 取两本书不一样的参数，构成一本新的书，最后设置bookId为原bookId，返回。
     * （排除bookId，createTime, finishTime这些自动生成的参数不进行比较）。
     * @param new_book
     * @param old_book
     * @return
     */
    public static Book getDifferBook(Book new_book, Book old_book) {

        Book differBook = new Book();

        if (new_book.getBookName() != old_book.getBookName())
            differBook.setBookName(new_book.getBookName());
        if (new_book.getBookAuthor() != old_book.getBookAuthor())
            differBook.setBookAuthor(new_book.getBookAuthor());
        if (new_book.getBookType() != old_book.getBookType())
            differBook.setBookType(new_book.getBookType());

        if (new_book.getBookTotalPage() != old_book.getBookTotalPage())
            differBook.setBookTotalPage(new_book.getBookTotalPage());
        if (new_book.getBookFinishedPage() != old_book.getBookFinishedPage())
            differBook.setBookFinishedPage(new_book.getBookFinishedPage());

        if (new_book.getReadDays() != old_book.getReadDays())
            differBook.setReadDays(new_book.getReadDays());
        if (new_book.getReadState() != old_book.getReadState())
            differBook.setReadState(new_book.getReadState());

        if (new_book.getFinishTime() != old_book.getFinishTime())
            differBook.setFinishTime(new_book.getFinishTime());
        differBook.setBookId(old_book.getBookId());//最后设置bookId为原bookId
//        if (new_book.getFinishTime() != old_book.getFinishTime())
//            return false;
//        if (new_book.getCreateTime() != old_book.getCreateTime())
//            return false;

        return differBook;
    }
}
