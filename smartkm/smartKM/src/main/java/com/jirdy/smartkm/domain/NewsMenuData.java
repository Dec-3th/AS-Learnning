package com.jirdy.smartkm.domain;

import java.util.ArrayList;

/**
 * 新闻中心数据解析
 *
 * Gson对象封装原则：
 * 1.json中遇到{}就是一个对象
 * 2.json中遇到[]就是一个ArrayList
 * 3.对象中所有字段的命名必须和json中的字段名一致，但新建的类名可以自己命名
 *
 * Created by december on 17-5-13.
 */

public class NewsMenuData {

    public int retcode;
    public ArrayList<NewsData> data;
    public ArrayList<String> extend;

    public class NewsData{
        public String id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    public class NewsTabData{
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsMenuData{" +
                "data=" + data +
                '}';
    }
}
