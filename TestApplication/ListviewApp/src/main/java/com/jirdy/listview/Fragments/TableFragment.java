package com.jirdy.listview.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jirdy.listview.MainActivity;
import com.jirdy.listview.R;
import com.jirdy.listview.dbUtils.ReadProgressDBManager;
import com.jirdy.listview.model.Book;
import com.jirdy.listview.model.Table;
import com.jirdy.listview.utils.TableAdapter;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends Fragment implements MainActivity.OnDBDataChanged {

    private static final String TAG = "Jirdy.Read.TableFG";
    public final static String[] mTitle =
            {"编号", "    书名    ", "  作者  ", "类型", "已读页数", "总页数", "状态", "创建时间", "上次阅读", "打卡", "进度"};
    private ListView listView;
    private Activity activity;
    private ArrayList<Table.TableRow> tableList;//tableAdapter的数据源
    private TableAdapter tableAdapter;
    private AdapterView.OnItemClickListener mListener;

    public TableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for activity fragment
        return inflater.inflate(R.layout.fragment_table, container, false);
    }

//    @Override
//    public void refreshTable() {
//
//        if (bookList == null) {
//            bookList = new ArrayList<Book>();//新建数据源，为了能正常更新listview，该数据源不能变（即bookList不能指向新的List，new出来的，只能拷贝到bookList中）
//            debug(TAG,"create bookList");
//        }
//
//        ReadProgressDBManager rpDBManager = new ReadProgressDBManager(context);
//        AllList = rpDBManager.queryAllBooks();
//        bookList.clear();//清空bookList内的内容，但其指针指向不变。
//        bookList.addAll(AllList);
//
//        if (adapter != null) {
//            adapter.notifyDataSetChanged();//数据源更新，通知adapter更新ListView.
//            debug(TAG,"notifyDataSetChanged");
//        }
//        debug(TAG, "refreshList bookList: " + bookList);
//
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        refreshList();

        MainActivity.debug(TAG, "onViewCreated tableList size:" + tableList.size());
        if (getActivity().findViewById(R.id.tableListView) != null && tableList != null) {
            listView = (ListView) getActivity().findViewById(R.id.tableListView);

            tableAdapter = new TableAdapter(activity, tableList);
            listView.setAdapter(tableAdapter);
            listView.setOnItemClickListener(mListener);
            MainActivity.debug(TAG, "show on listView tableList");
//            listView.setOnItemClickListener(new
//                    ItemClickEvent()
//            );
        }
    }

    @Override
    public void refreshList() {
        MainActivity.debug(TAG, "refreshList");
        if (tableList == null) {
            tableList = new ArrayList<Table.TableRow>();//新建数据源，为了能正常更新listview，该数据源不能变（即bookList不能指向新的List，new出来的，只能拷贝到bookList中）
            MainActivity.debug(TAG, "create bookTable");
        }

        ArrayList<Table.TableRow> tempTable = getTableData();
        tableList.clear();//清空bookList内的内容，但其指针指向不变。
        if (tempTable.size() > 0)
            tableList.addAll(tempTable);

        if (tableAdapter != null) {
            tableAdapter.notifyDataSetChanged();//数据源更新，通知adapter更新ListView.
            MainActivity.debug(TAG, "notifyDataSetChanged");
        }
        MainActivity.debug(TAG, "refreshList tableList: " + tableList);
    }

    private ArrayList<Table.TableRow> getTableData() {
        ArrayList<Table.TableRow> tableData = new ArrayList<>();
        /* 查询数据库 */
        ReadProgressDBManager rpDBManager = new ReadProgressDBManager(activity);
        List<Book> bookList = rpDBManager.queryAllBooks();

        /* 定义标题 */
        Table.TableCell[] titles = new Table.TableCell[11];// 每行5个单元
        for (int i = 0; i < titles.length; i++) {
            titles[i] = new Table.TableCell(mTitle[i],
//                    width + 8 * i,
                    dip2px(activity, dip2px(activity, 7f) * mTitle[i].length()),
                    dip2px(activity, 50),
//                    LayoutParams.MATCH_PARENT,
                    Table.TableCell.STRING);
        }
        tableData.add(new Table.TableRow(titles));//添加标题

        Table.TableCell[] cells;// 每行5个单元
        /* 把表格的行添加到表格 */
        for (Book book : bookList) {
            MainActivity.debug(TAG, book.toString());
            /* 每行的数据 */
            cells = new Table.TableCell[11];// 每行5个单元
            int i = 0; /* 以标题栏的宽度作为每列单元格的宽度 */
            cells[i] = newTableCell(String.valueOf(bookList.indexOf(book)+1), titles[i++].width);
            cells[i] = newTableCell(cutString(book.getBookName(), 11), titles[i++].width);
            cells[i] = newTableCell(cutString(book.getBookAuthor(), 7), titles[i++].width);
            cells[i] = newTableCell(book.getBookType(), titles[i++].width);
            cells[i] = newTableCell(String.valueOf(book.getBookFinishedPage()), titles[i++].width);
            cells[i] = newTableCell(String.valueOf(book.getBookTotalPage()), titles[i++].width);
            cells[i] = newTableCell(book.getReadStateStr(), titles[i++].width);
            cells[i] = newTableCell(book.getCreateTimeStr(), titles[i++].width);
            cells[i] = newTableCell(book.getFinishTimeStr(), titles[i++].width);
            cells[i] = newTableCell(String.valueOf(book.getReadDays()), titles[i++].width);
            cells[i] = newTableCell(book.getBookReadProgress() + "%", titles[i++].width);

            Table.TableRow tableRow = new Table.TableRow(cells);
            tableRow.setBookId(book.getBookId());
            tableData.add(tableRow);
        }
//        cells[cells.length - 1] = new TableCell(R.drawable.fire,
//                titles[cells.length - 1].width,
//                LayoutParams.WRAP_CONTENT,
//                TableCell.IMAGE);
        return tableData;
    }

    private Table.TableCell newTableCell(String content, int width) {
        return new Table.TableCell(content,
                width,
//                titles[i++].width,          /* 以标题栏的宽度作为每列单元格的宽度 */
                dip2px(activity, 40),
                Table.TableCell.STRING);
    }

//    class ItemClickEvent implements AdapterView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                long arg3) {
//            Toast.makeText(getActivity(), "选中第" + String.valueOf(arg2) + "行", Toast.LENGTH_SHORT).show();
//        }
//    }

    public String cutString(String str, int size) {
        if (str.length() > size)
            return str.substring(0, size) + "...";
        else
            return str;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdapterView.OnItemClickListener) {
            mListener = (AdapterView.OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
