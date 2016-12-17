package com.tnyoo.savedataapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "http://www.baiducom/abc.mp3";
        String fileName = Uri.parse(url).getLastPathSegment();
        try {
            File file = File.createTempFile(fileName, null, getBaseContext().getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.main_save_to_preference:
                Intent intent1 = new Intent(this, SaveToPreferenceActivity.class);
                startActivity(intent1);
                break;
            case R.id.main_save_to_file:
                Intent intent2 = new Intent(this, SaveToFileActivity.class);
                startActivity(intent2);
                break;
            case R.id.main_save_to_database:
                Intent intent3 = new Intent(this, SaveToDBActivity.class);
                startActivity(intent3);
                break;
            default:
                Toast.makeText(getBaseContext(), "按钮id不存在！", Toast.LENGTH_SHORT);
        }

    }
}
