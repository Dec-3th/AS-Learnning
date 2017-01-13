package com.jreal.sharesimpledata;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReceiveActivity extends AppCompatActivity {

    public final static String TAG = "JR";
    TextView textView;
    ImageView imageView1;
    //    ImageView imageView2;
//    ImageView imageView3;
    LinearLayout containerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        textView = (TextView) findViewById(R.id.receive_text);
        imageView1 = (ImageView) findViewById(R.id.receive_image1);
//        imageView2 = (ImageView) findViewById(R.id.receive_image2);
//        imageView3 = (ImageView) findViewById(R.id.receive_image3);
        containerLayout = (LinearLayout) findViewById(R.id.image_container);

        // Get intent, action and MIME type
//        Intent intent = this.getIntent();
//        String action = intent.getAction();
//        String type = intent.getType();
        handleReceivedData(this.getIntent());

    }


    /**
     * 处理接收到的数据
     * 请注意：
     * 由于无法知道其他程序发送过来的数据内容是文本还是其他类型的数据，若数据量巨大，则需要大量处理时间，因此我们应避免在UI线程里面去处理那些获取到的数据。
     *
     * @param intent
     */
    private void handleReceivedData(Intent intent) {
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
        if (imageUri != null) {
            imageView1.setImageURI(imageUri);
            imageView1.setAdjustViewBounds(true);
        } else
            Toast.makeText(this, "分享的数据不存在：" + imageUri, Toast.LENGTH_SHORT).show();

    }

    private void handleSendMultipleImages(Intent intent) {
        int size;
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        size = imageUris.size();
        Log.i(TAG, "imageUris: " + imageUris);
        if (imageUris.size() > 0) {

            while (size-- > 0) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                imageView.setImageURI(imageUris.get(size));
                imageView.setAdjustViewBounds(true);//设置ImageView布局自适应父View（即在保持长宽比条件下，填满父布局）

                containerLayout.addView(imageView);
            }

        } else
            Toast.makeText(this, "分享的数据集为空 size：" + imageUris.size(), Toast.LENGTH_SHORT).show();


    }
}
