package com.tnyoo.fragmentsapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class HeadlinesFragment extends ListFragment {

    OnHeadlineSelectedListener mheadlineSelectListener;

    /*
    为了让fragment与activity交互，你可以在Fragment 类中定义一个接口，并且在activity中实现这个接口。Fragment在他们生
    命周期的onAttach()方法中获取接口的实现，然后调用接口的方法来与Activity交互。
    */
    public interface OnHeadlineSelectedListener {
        // Called by HeadlinesFragment when a list item is selected
        public void onArticleSelected(int position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*注意：这里已经使用了android自带的layout作为ListView的布局，不用自己再创建布局。*/
        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        // Create an array adapter for the list view, using the Articles headlines array
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Articles.Headlines));
    }

    @Override
    public void onStart() {
        super.onStart();
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {//called once the fragment is associated with its activity.
        super.onAttach(activity);

        //确保传入的activity已经实现OnHeadlineSelectedListener接口，否则抛出异常。
        try {
            mheadlineSelectListener = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //通知容器activity有条目被选中，与activity进行交互。
        mheadlineSelectListener.onArticleSelected(position);

        //高亮选中条目
        getListView().setItemChecked(position, true);
    }

    /*
    当你创建Fragment的时候，你必须重写onCreateView()回调方法来定义你的布局（这也是唯一一个需要重写的回调方法）
    但在本例中的ListView已经在onCreate中定义了布局，故该方法不需要了。
     */
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_headlines, container, false);
//    }

}
