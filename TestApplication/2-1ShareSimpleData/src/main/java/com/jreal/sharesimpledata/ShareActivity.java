package com.jreal.sharesimpledata;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.tbruyelle.rxpermissions.Permission;
//import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

    public final static String TAG = "JR";
    private ShareActionProvider mShareActionProvider;
    private PermissionsDispatcher permissionsDispatcher;
//    private RxPermissions rxPermissions;
//    private rx.Observable<Boolean> request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Log.i(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
        permissionsDispatcher = new PermissionsDispatcher(this);
//        rxPermissions = new RxPermissions(this);
//        rxPermissions.setLogging(true);
    }

    /**
     * 为 action bar布局菜单条目，onCreateOptionsMenu()回调方法用来inflate菜单资源, 从而获取Menu对象
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);//ActionBar的配置放在res/menu/menu_main.xml

        /*使用menu的findItem方法来找Item，不是this.findViewById，否则找不到*/
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
        Log.i(TAG, " ---onCreateOptionsMenu item: " + menuItem);


//        mShareActionProvider = (ShareActionProvider) menuItem.getActionProvider();
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);//support v7的实现方法
        return true;
    }

    public void onClick(View view) {
        int id = view.getId();

//        // Must be done during an initialization phase like onCreate
//        request = rxPermissions
//                .request(Manifest.permission.CAMERA)
//                .subscribe(new Action<Permission>() {
//                               @Override
//                               public void call(Permission permission) {
//                                   Log.i(TAG, "Permission result " + permission);
//                                   if (permission.granted) {
//
//
//                                   } else if (permission.shouldShowRequestPermissionRationale) {
//                                       // Denied permission without ask never again
//                                       Toast.makeText(getApplicationContext(),
//                                               "Denied permission without ask never again",
//                                               Toast.LENGTH_SHORT).show();
//                                   } else {
//                                       // Denied permission with ask never again
//                                       // Need to go to the settings
//                                       Toast.makeText(getApplicationContext(),
//                                               "Permission denied, can't enable the camera",
//                                               Toast.LENGTH_SHORT).show();
//                                   }
//                               }
//                           },
//                        new Action1<Throwable>() {
//                            @Override
//                            public void call(Throwable t) {
//                                Log.e(TAG, "onError", t);
//                            }
//                        },
//                        new Action0() {
//                            @Override
//                            public void call() {
//                                Log.i(TAG, "OnComplete");
//                            }
//                        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            final List<String> permissionsList = new ArrayList<>();
            permissionsList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionsDispatcher.grantPermissions(permissionsList);

//            permissionsDispatcher.grantPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }

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
                File image1 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "dot11.jpg");
                File image2 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "a.png");
                File image3 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "b.png");

                imageUris.add(Uri.fromFile(image1));//分享网络图片
                imageUris.add(Uri.fromFile(image2)); // Add your image URIs here
                imageUris.add(Uri.fromFile(image1));//分享网络图片
                imageUris.add(Uri.fromFile(image3)); // Add your image URIs here

                shareMultiple(imageUris);

//                ArrayList<CharSequence> charSequences = new ArrayList<CharSequence>();
//                charSequences.add("Because the things you're scared of are usually the most worthwhile.");
//                charSequences.add("因为你所害怕的事情，往往是最值得的。"); // Add your image URIs here
//                charSequences.add("just run for it."); // Add your image URIs here
//
//                shareMultipleContent(charSequences);

                break;
            case R.id.share_with_actionbar:
                File file1 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "dot11.jpg");// 附件
                Log.i(TAG, "使用ActionBar分享 file: " + file1.getAbsolutePath());

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file1));
                shareIntent.setType("image/jpeg");

                setShareIntent(shareIntent);

                break;
            default:
                Toast.makeText(this, "按钮id不存在！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置ActionBar要分享的Intent
     * ActionBar分享不显示分享按钮，无法分享问题解决：
     * https://stackoverflow.com/questions/27887716/exception-this-is-not-supported-use-menuitemcompat-getactionprovider
     * <p/>
     * 我们可以先设置share intent，然后根据UI的变化来对intent进行更新，不同的Intent类型会在ActionBar调用不同的程序。
     *
     * @param shareIntent
     */
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
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

    //activity的onRequestPermissionsResult会被回调来通知结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionsDispatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
