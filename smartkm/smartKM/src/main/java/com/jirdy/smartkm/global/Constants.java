package com.jirdy.smartkm.global;

/**
 * Created by jinrui on 2017/5/12.
 */

public class Constants {

//    public static final String SERVER_IP = "192.168.16.159"; //my-pc
    public static final String SERVER_IP = "192.168.21.116"; //com-pc
    //服务器根链接 http://192.168.21.116:8080/zhbj
    public static final String SERVER_URL = "http://" + SERVER_IP + ":8080/zhbj";

    //获取分类信息链接
    public static final String CATEGORIES_URL = SERVER_URL + "/categories.json";
}
