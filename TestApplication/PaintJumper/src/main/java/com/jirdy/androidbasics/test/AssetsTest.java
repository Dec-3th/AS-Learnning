package com.jirdy.androidbasics.test;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssetsTest extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        setContentView(textView);

        //获取AssetManager，通过AssetManager能够获取assets/ 文件夹下文件。
        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;
        try {
            Log.i("ANDBASIC", "Start Open: " + assetManager.toString());

            //This method will return a plain-old Java InputStream, which we can use to read in any sort of file.
            inputStream = assetManager.open("texts/myawesometext.txt");
            //下面方式同样可以获取到text的数据，只不过只能返回InputStream流
//            inputStream=this.getClass().getClassLoader().getResourceAsStream("assets/texts/myawesometext.txt");

            Log.i("ANDBASIC", "Finish Open");
            String text = loadTextFile(inputStream);
            textView.setText(text);
        } catch (IOException e) {
            textView.setText("Couldn't load file: " + e.toString());
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    textView.setText("Couldn't close file");
                }
        }
    }

    public String loadTextFile(InputStream inputStream) throws IOException {
        Log.i("ANDBASIC", "loadTextFile");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[inputStream.available()];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0)
            byteStream.write(bytes, 0, len);
        return new String(byteStream.toByteArray(), "UTF8");//格式化为utf-8编码字符串
    }
}
