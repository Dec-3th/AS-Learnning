package com.jreal.sharesimpledata;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReceiveActivity extends AppCompatActivity {

    public final static String TAG = "JR";
    TextView textView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        textView = (TextView) findViewById(R.id.receive_text);
        imageView1 = (ImageView) findViewById(R.id.receive_image1);
        imageView2 = (ImageView) findViewById(R.id.receive_image2);
        imageView3 = (ImageView) findViewById(R.id.receive_image3);
        linearLayout = (LinearLayout) findViewById(R.id.linear_lists);

        // Get intent, action and MIME type
//        Intent intent = this.getIntent();
//        String action = intent.getAction();
//        String type = intent.getType();
        holdReceivedData(this.getIntent());

    }


    private void holdReceivedData(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equalsIgnoreCase(action)) { /* 接收到<action android:name="android.intent.action.SEND"/> 的Intent*/

            if (type.startsWith("text/plain")) { /* <data android:mimeType="text/plain" /> 的Intent*/
                handleSendText(intent);

            } else if (type.startsWith("image/")) { /* <data android:mimeType="image/*" /> 的Intent*/
                handleSendImage(intent);

            } else
                Toast.makeText(this, "未知 Intent Type：" + type, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_SEND_MULTIPLE.equalsIgnoreCase(action)) { /*接收到 <action android:name="android.intent.action.SEND_MULTIPLE" />*/

            if (type.startsWith("image/*")) { /* <data android:mimeType="image/*" /> 的Intent*/
                handleSendMultipleImages(intent);

            } else
                Toast.makeText(this, "未知 Intent Type：" + type, Toast.LENGTH_SHORT).show();

        } else
            Toast.makeText(this, "未知 Intent Action：" + action, Toast.LENGTH_SHORT).show();

        //imageView.setImageURI(uri2); // content://media/external/images/media/63

    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.i(TAG, "sharedText: " + sharedText);
        if (sharedText != null)
            textView.setText(sharedText);
        else
            Toast.makeText(this, "分享数据不存在 sharedText：" + sharedText, Toast.LENGTH_SHORT).show();

    }

    private void handleSendImage(Intent intent) {
        //为文件设置读权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);//被返回的Intent中使用Uri的形式来表示返回的联系人
        Log.i(TAG, "imageUri: " + imageUri);
        if (imageUri != null)
            imageView1.setImageURI(imageUri);
        else
            Toast.makeText(this, "分享数据不存在：" + imageUri.toString(), Toast.LENGTH_SHORT).show();


    }

    private void handleSendMultipleImages(Intent intent) {
        int size;
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        size = imageUris.size();
        Log.i(TAG, "imageUris: " + imageUris);
        if (imageUris.size() > 0) {
//            linearLayout.
            imageView1.setImageURI(imageUris.get(0));
            imageView2.setImageURI(imageUris.get(1));
            imageView3.setImageURI(imageUris.get(2));
        } else
            Toast.makeText(this, "分享数据集为空 size：" + imageUris.size(), Toast.LENGTH_SHORT).show();


    }
}
