package com.jirdy.smartkm.utils;

import android.content.Context;

/**
 * 网络缓存工具类
 * 缓存原则：
 * 1.以url+参数为key, 以json为values保存起来
 * 2.保存方法有：sqlite, SharedPreferences, 本地文件
 * 3.当写缓存到本地文件时：以 MD5(url+参数) 为文件名, 以json为文件内容保存
 * 3.当从本地文件读缓存时：以 MD5(url+参数) 为文件名查找缓存文件是否存在, 存在该文件则读取文件内容为缓存数据
 *
 *  注意：url+参数中会包含很多斜杠等不允许作为文件名的字符串；
 *       MD5(url+参数)之后都是32为数字字母组成的字符串，这样作为文件名更合适。
 * Created by december on 17-5-13.
 */

public class CacheUtils {

    /**
     * 向SharedPreferences中写缓存
     * @param key
     * @param data
     * @param context
     */
    public static void setCache(String key, String data, Context context){
        PrefUtils.putString(key, data, context);
    }

    /**
     * 从SharedPreferences中读缓存
     * @param key
     * @param context
     */
    public static String getCache(String key, Context context){
        return PrefUtils.getString(key, null, context); //默认为null（获取不到条目时的默认值）
    }
}
