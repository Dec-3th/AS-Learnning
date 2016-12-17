package com.jirdy.greedysnake.framework.implement;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.jirdy.greedysnake.framework.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/7/2.
 */
public class FileIOImpl implements FileIO {

    Context context;
    AssetManager assetManager;
    String externalStoragePath;

    public FileIOImpl(Context context){
        this.context = context;
        this.assetManager = context.getAssets();
        this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator;
    }

    /**
     * 从assets文件夹读取给定文件名文件的输入流。
     * @param fileName 文件名
     * @return 给定文件名的输入流
     * @throws IOException
     */
    @Override
    public InputStream readAsset(String fileName) throws IOException {
        return assetManager.open(fileName);
    }

    /**
     * 从内部存储中获取给定文件名的输入流。
     * @param fileName 文件名
     * @return 给定文件名的输入流
     * @throws IOException
     */
    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    /**
     * 从内部存储中获取给定文件名的输出流。
     * @param fileName
     * @return 给定文件名的输出流
     * @throws IOException
     */
    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath+fileName);
    }

    /**
     * 获取该上下文的默认SharedPreferences
     * @return
     */
    public SharedPreferences getPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}

