package com.tnyoo.savedataapp;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveToFileActivity extends ActionBarActivity {

    public static final String TAG = "SAVE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to_file);
    }

    //保存数据到缓存
    public void saveToCache(View view) {
        getTempFile(getBaseContext(), "http://resource.tnyoo.com/tyImage/www/p_logo.jpg");
    }


    private File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
            Log.i(TAG, "保存缓存Dir: " + context.getCacheDir().toString());
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Error while creating file", Toast.LENGTH_SHORT).show();
        }
        return file;
    }


    /**
     * 保存数据到内部存储
     * getFilesDir() : 返回一个File，代表了你的app的internal目录。
     * getCacheDir() : 返回一个File，代表了你的app的internal缓存目录。
     *
     * @param view
     */
    public void saveToInternalStorage(View view) {
        String filename = "myfile";
        String string = "Hello world!";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
//            Log.i("保存缓存Dir: " , outputStream.);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //私有文件保存到外部存储，这里没有保存文件，只是在外部存储创建文件夹（保存文件参照saveToInternalStorage）。
    public void externalStoragePrivateSave(View view) {
        // Get the directory for the app's private pictures directory.
        File fileDir = getBaseContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        getAlbumStorageDir(fileDir, "jrPics");//实测可以创建文件夹，有文件的话，直接写入
        Log.i(TAG, "保存到私有文件Dir: " + fileDir.getAbsolutePath());
    }

    //共享文件保存到外部存储，这里没有保存文件，只是在外部存储创建文件夹（保存文件参照saveToInternalStorage）。
    public void externalStoragePublicSave(View view) {
        // Get the directory for the app's public pictures directory.
        File fileDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        getAlbumStorageDir(fileDir, "jrPics");//实测可以创建文件夹，有文件的话，直接写入
        Log.i(TAG, "保存到公共文件Dir: " + fileDir.getAbsolutePath());
    }

    public File getAlbumStorageDir(File fileDir, String albumName) {

        File file = new File(fileDir, albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created");
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    //查询剩余空间
    public void querySpace(View view) {
//        可以通过执行getFreeSpace() or getTotalSpace() 来判断是否有足够的空间来保存文件
    }

    //删除文件的两种方式，这里没有文件可删，没有进行测试.
    public void deleteFile(View view) {
        String fileName = "";
        //1 直接调用File.delete进行删除.
        File myFile = new File(fileName);
        boolean isSuccess = myFile.delete();

        //2 若文件保存在internal storage，可以通过Context来访问并通过执行deleteFile()进行删除
        getBaseContext().deleteFile(fileName);
    }

}
