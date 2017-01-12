package com.tnyoo.fragmentsapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {

    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;

    public ArticleFragment() {
        // Required empty public constructor
    }

    //该fragment没有自己的布局，实现onCreateView添加布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            //当activity重新创建时，使用Key:ARG_POSITION，从savedInstanceState中取出前面看的文章位置。
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onStart() {//旋转屏幕时，会调用该方法，要根据position重新显示文章
        super.onStart();
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();

        if (args != null) {//从参数args中获取选定文章的位置，显示文章.

            updateArticleView(args.getInt(ARG_POSITION));

        } else if (mCurrentPosition != -1) {//保存过的文章位置
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
    }

    /**
     * 刷新/显示 文章
     *
     * @param position 文章位置
     */
    public void updateArticleView(int position) {
        TextView article = (TextView) getActivity().findViewById(R.id.article);
        article.setText(Articles.Articles[position]);
        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
         当activity重新创建时（如旋转屏幕时），需要使用onSaveInstanceState保存
         之前看的文章位置。其中ARG_POSITION 为 Key，mCurrentPosition为 Value.
          */
        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
}
