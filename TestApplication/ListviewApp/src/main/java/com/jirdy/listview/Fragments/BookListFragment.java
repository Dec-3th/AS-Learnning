package com.jirdy.listview.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jirdy.listview.MainActivity;
import com.jirdy.listview.R;
import com.jirdy.listview.dbUtils.ReadProgressDBManager;
import com.jirdy.listview.model.Book;
import com.jirdy.listview.utils.BookListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * fragment 的生命周期函数：
 * 1.onAttach(Activity) called once the fragment is associated with its activity.
 * 2.onCreate(Bundle) called to do initial creation of the fragment.
 * 3.onCreateView(LayoutInflater, ViewGroup, Bundle) creates and returns the view hierarchy associated with the fragment.
 * 4.onActivityCreated(Bundle) tells the fragment that its activity has completed its own Activity.onCreate().
 * 5.onViewStateRestored(Bundle) tells the fragment that all of the saved state of its view hierarchy has been restored.
 * 6.onStart() makes the fragment visible to the user (based on its containing activity being started).
 * 7.onResume() makes the fragment begin interacting with the user (based on its containing activity being resumed).
 * <p/>
 * As a fragment is no longer being used, it goes through a reverse series of callbacks:
 * <p/>
 * 1.onPause() fragment is no longer interacting with the user either because its activity is being paused or a fragment operation is modifying it in the activity.
 * 2.onStop() fragment is no longer visible to the user either because its activity is being stopped or a fragment operation is modifying it in the activity.
 * 3.onDestroyView() allows the fragment to clean up resources associated with its View.
 * 4.onDestroy() called to do final cleanup of the fragment's state.
 * 5.onDetach() called immediately prior to the fragment no longer being associated with its activity.
 */
public class BookListFragment extends Fragment implements MainActivity.OnDBDataChanged {

    public static String TAG = "Jirdy.Read.ListFG";
    private AdapterView.OnItemClickListener mBookListClickListener;
    private BookListAdapter adapter;
    private List<Book> bookList;
    private List<Book> AllList;
    private Context context;
    private TextView allbooks;//总共
    private TextView readingbooks;//在读
    private TextView finishedbooks;//已读

    /*
    当你创建Fragment的时候，你必须重写onCreateView()回调方法来定义你的布局（这也是唯一一个需要重写的回调方法）
    但在本例中的ListView已经在onCreate中定义了布局，故该方法不需要了。
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_booklists, container, false);
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        refreshList();

        // (We do this during onStart because at the point the listview is available.)
        MainActivity.debug(TAG, "onViewCreated bookList size:" + bookList.size());
        if (getActivity().findViewById(R.id.book_list) != null && bookList != null) {

            ListView listView = (ListView) getActivity().findViewById(R.id.book_list);
            listView.setOnItemClickListener(mBookListClickListener);
//            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(AbsListView absListView, int i) {
//                    ImageView imageView = (ImageView)getActivity().findViewById(R.id.booklist_image_view);
//                    imageView.setVisibility(View.INVISIBLE);
//                }
//
//                @Override
//                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                }
//            });

            adapter = new BookListAdapter(getActivity(), bookList);
            listView.setAdapter(adapter);
        }

//        allbooks = (TextView)getActivity().findViewById(R.id.booklist_all_list);
//        allbooks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                refreshList();
//            }
//        });
//
//        readingbooks = (TextView)getActivity().findViewById(R.id.booklist_read_list);
//        readingbooks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showSubList(0);
//            }
//        });
//
//        finishedbooks = (TextView)getActivity().findViewById(R.id.booklist_finish_list);
//        finishedbooks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showSubList(1);
//            }
//        });

    }


    @Override
    public void refreshList() {

        if (bookList == null) {
            bookList = new ArrayList<Book>();//新建数据源，为了能正常更新listview，该数据源不能变（即bookList不能指向新的List，new出来的，只能拷贝到bookList中）
            MainActivity.debug(TAG, "create bookList");
        }

        ReadProgressDBManager rpDBManager = new ReadProgressDBManager(context);
        AllList = rpDBManager.queryAllBooks();
        bookList.clear();//清空bookList内的内容，但其指针指向不变。
        if (AllList.size() != 0)
            bookList.addAll(AllList);


        if (adapter != null) {
            adapter.notifyDataSetChanged();//数据源更新，通知adapter更新ListView.
            MainActivity.debug(TAG, "notifyDataSetChanged");
        }
        MainActivity.debug(TAG, "refreshList bookList: " + bookList);

    }

    public void showSubList(int readState) {
        List<Book> tempList = new ArrayList<>();
        for (Book b : AllList) {
            if (b.getReadState() == readState)
                tempList.add(b);
        }

        bookList.clear();//清空bookList内的内容，但其指针指向不变。
        bookList.addAll(tempList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();//数据源更新，通知adapter更新ListView.
            MainActivity.debug(TAG, "notifyDataSetChanged");
        }
        MainActivity.debug(TAG, "refreshList bookList: " + bookList);
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //确保传入的activity已经实现OnBookListSelectedListener接口，否则抛出异常。
        try {
            this.context = context;
//            MainActivity.debug(TAG, "onAttach bookList:" + bookList);
            mBookListClickListener = (AdapterView.OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnBookListSelectedListener");

        }
    }
}
