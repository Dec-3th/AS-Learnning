package com.jirdy.smartkm.base;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment基类
 * 1.初始化布局 initHeaderView
 * 2.初始化数据 initData
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    public Activity mActivity;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activity在刚开始就在，所以可以在onCreate中获取
        mActivity = getActivity();
    }

    //Activity创建结束
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(); //页面完全创建结束以后初始化数据（这个时候所有控件都能找到）
    }

    //当Fragment刚被粘贴到Activity时调用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    //初始化Frament布局-必须写
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = initView();

        // Inflate the layout for this fragment
        return view; //展示初始化的布局
    }

    /**
     * 初始化布局 子类必须实现
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据 子类可以不实现
     */
    public void initData(){

    }
}
