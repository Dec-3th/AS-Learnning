package com.jirdy.smartkm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * 新闻详情页面
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "JR.NewsDetailActivity";
    private ImageButton backBtn;
    private ImageButton textSizeBtn;
    private ImageButton shareBtn;
    private WebView mWebView;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        String url = getIntent().getStringExtra("url");

        backBtn = (ImageButton) findViewById(R.id.btn_back);
        textSizeBtn = (ImageButton) findViewById(R.id.btn_textsize);
        shareBtn = (ImageButton) findViewById(R.id.btn_share);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        mWebView = (WebView) findViewById(R.id.wv_news_detail);

        mWebView.loadUrl(url); //加载网页
        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setBuiltInZoomControls(true); //显示放大缩小网页的按钮
        webSettings.setUseWideViewPort(true); //设置双击缩放网页
        webSettings.setJavaScriptEnabled(true); //设置网页允许js功能

        //为WebView设置网页开始加载，结束，跳转的监听
        mWebView.setWebViewClient(new WebViewClient() {

            //网页跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "网页跳转: " + url);
                /**
                 * 这里可以对url进行判断解析，有的url可以直接用本地应用打开，如一个电话或QQ链接
                 * 如 <a href="tel:110">联系我们</a>
                 */

                //强制在当前页面加载网页，不用跳浏览器
                view.loadUrl(url);
                return true;
//                return super.shouldOverrideUrlLoading(view, url);
            }

            //网页开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i(TAG, "网页开始加载");
                pbLoading.setVisibility(View.VISIBLE);
            }

            //网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "网页加载结束");
                pbLoading.clearAnimation();
                pbLoading.setVisibility(View.GONE);
            }
        });

        /**
         * 为WebView设置另外特性：
         * 1.监听网页加载进度
         * 2.获取网页标题、网页图标
         */
        mWebView.setWebChromeClient(new WebChromeClient() {

            //监听网页加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.i(TAG, "网页加载进度: " + newProgress);
            }

            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i(TAG, "获取网页标题：" + title);
            }

            //获取网页图标
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog();
                break;
            case R.id.btn_share:
//                http://wiki.mob.com/android_快速集成指南/

                break;
            default:

                break;
        }
    }

    //点击确定之前，用户选择的字体大小设置
    private int mChooseTextSizeItem;
    //当前的字体大小设置，默认为2，正常大小
    private int mCurrentTextSizeItem = 2;

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");

        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        //设置单选列表框
        builder.setSingleChoiceItems(items, mCurrentTextSizeItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                mChooseTextSizeItem = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //要对网页内容进行设置，需要先获取WebView的Settings对象，才能进行设置
                WebSettings settings = mWebView.getSettings();
                switch (mChooseTextSizeItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    default:

                        break;
                }

                mCurrentTextSizeItem = mChooseTextSizeItem; //保存用户设置
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();//最后，展示对话框
    }
}
