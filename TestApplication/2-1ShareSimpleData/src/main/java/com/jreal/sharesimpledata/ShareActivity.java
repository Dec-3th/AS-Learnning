package com.jreal.sharesimpledata;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    public final static String TAG = "JR";
    public ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        imageView1 = (ImageView) findViewById(R.id.share_image1);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "dot11.jpg");// 附件
        Log.i(TAG, "file: " + file.getAbsolutePath());

        imageView1.setImageURI(Uri.fromFile(file));
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.share_text:
                shareContent("Because the things you're scared of are usually the most worthwhile. " +
                        "---因为你所害怕的事情，往往是最值得的。");
                break;
            case R.id.share_binary:
                //icon.png必须保存在手机内存的根目录下才能找到，否则找不到。
                File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "dot11.jpg");// 附件
                Log.i(TAG, "file: " + file.getAbsolutePath());

                shareBinary(Uri.fromFile(file));
                break;
            case R.id.share_multiple:
                ArrayList<Uri> imageUris = new ArrayList<Uri>();
//                File image1 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "dot11.jpg");
                File image2 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "a.png");
                File image3 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "b.png");

//                imageUris.add(Uri.parse("http://www.baidu.com/img/bdlogo.png"));//分享网络图片
                imageUris.add(Uri.fromFile(image2)); // Add your image URIs here
                imageUris.add(Uri.fromFile(image3)); // Add your image URIs here

                shareMultiple(imageUris);

//                ArrayList<CharSequence> charSequences = new ArrayList<CharSequence>();
//                charSequences.add("Because the things you're scared of are usually the most worthwhile.");
//                charSequences.add("因为你所害怕的事情，往往是最值得的。"); // Add your image URIs here
//                charSequences.add("just run for it."); // Add your image URIs here
//
//                shareMultipleContent(charSequences);

                break;
//            case R.id.send_email:
//                Log.i(TAG, "file: " + file.getAbsolutePath());
//                break;
            default:
                Toast.makeText(this, "按钮id不存在！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 分享文本
     *
     * @param content
     */
    private void shareContent(String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to_title)));
    }

    /**
     * 分享二进制文件(Uri资源)
     *
     * @param urlToImage
     */
    private void shareBinary(Uri urlToImage) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, urlToImage);
        intent.setType("image/jpeg");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to_title)));
    }

    /**
     * 发送多块内容(Send Multiple Pieces of Content)
     *
     * @param imageUris
     */
    private void shareMultiple(ArrayList<Uri> imageUris) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to_title)));
    }

//    /**
//     * 发送多个文本内容(Send Multiple Pieces of Content)
//     *
//     * @param charSequences
//     */
//    private void shareMultipleContent(ArrayList<CharSequence> charSequences) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//        intent.putCharSequenceArrayListExtra(Intent.EXTRA_TEXT, charSequences);
//        intent.setType("text/plain");
//        startActivity(intent.createChooser(intent, getResources().getText(R.string.send_to_title)));
//    }

    /**
     * 接收Activity返回的结果
     *
     * @param requestCode 你通过startActivityForResult()传递的request	code。
     * @param resultCode  activity指定的result code。如果操作成功则是RESULT_OK，如果用户没有操作成功，而是直接点击回退或者其什么原因，那么则是RESULT_CANCELED
     * @param data        intent,它包含了返回的result数据部分（可以自己指定要返回的数据在里面）。
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult " + resultCode);

            if (resultCode == RESULT_OK) {//获取成功

                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();

            }

    }
}
