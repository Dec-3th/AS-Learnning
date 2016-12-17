package com.jirdy.listview.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jirdy.listview.R;
import com.jirdy.listview.model.Book;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public class BookListAdapter extends ArrayAdapter<Book> {
    public static String TAG = "Jirdy.Read. List_Adapter";
//    private List<Integer> progress = null;

    //    public BookListAdapter(Context context, List<String> objects, List<Integer> progressList) {
    public BookListAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
//        this.progress = progressList;
    }

    @Override
    public boolean isEnabled(int position) {
//        if (listTag.contains(getItem(position))) {
//            return false;
//        }
        return super.isEnabled(position);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, null);
        Book book = getItem(position);

//        Log.i(TAG, "更新View：" + book.getBookName());

        TextView namePic = (TextView) view.findViewById(R.id.booklist_item_pic_name);
        namePic.setText(book.getBookName());

        TextView progressView = (TextView) view.findViewById(R.id.booklist_item_pic_progress);
        progressView.setText(" " + String.valueOf(book.getBookReadProgress()) + "% ");


        TextView nameView = (TextView) view.findViewById(R.id.booklist_item_name);
        nameView.setText(book.getBookName());

        TextView authorView = (TextView) view.findViewById(R.id.booklist_item_athor);
        authorView.setText("    ---" + book.getBookAuthor());

        TextView finishView = (TextView) view.findViewById(R.id.booklist_item_finish);
        finishView.setText(" 已读:" + String.valueOf(book.getBookFinishedPage()));

        TextView totalView = (TextView) view.findViewById(R.id.booklist_item_total);
        totalView.setText(" 总页数:"+String.valueOf(book.getBookTotalPage()));

        TextView readDays = (TextView)view.findViewById(R.id.booklist_item_read_days);
        readDays.setText(String.valueOf(book.getBookFinishedPage()) + "天");
//        TextView progressView = (TextView) view.findViewById(R.id.booklist_item_progress);
//        progressView.setText(" 进度:" + String.valueOf(book.getBookReadProgress()) + "%.");

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.booklist_item_progressbar);
        progressBar.setProgress(book.getBookReadProgress());

        return view;
    }
}
