package com.jirdy.filesharing;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 本课将展示一个客户端应用程序应该如何向服务端应用程序请求一个文件，接收服务端应用程序发来的Content URI，然后使用这个Content URI打开这个文件。
 */
public class RequestShareFileActivity extends ActionBarActivity {
    private static final int RequestSingleFileCode = 0;
    private static final int RequestMultiFilesCode = 1;

    public static String TAG = "JR.FileSelect";
    LinearLayout containerLayout;
    private ParcelFileDescriptor mInputPFD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        containerLayout = (LinearLayout) findViewById(R.id.file_container);
    }

    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_request_file:
                /*
                为了向服务端应用程序发送文件请求，在客户端应用程序中，需要调用startActivityForResult)方法，同时传递给这个方法一个Intent参数，
                它包含了客户端应用程序能处理的某个Action，比如ACTION_PICK及一个MIME类型。
                 */
                Intent mRequestFileIntent = new Intent(Intent.ACTION_PICK);
                mRequestFileIntent.setType("image/*");  //请求图片
                mRequestFileIntent.putExtra("requestMultiFiles", false);

                startActivityForResult(mRequestFileIntent, RequestSingleFileCode);
                break;
            case R.id.btn_request_files:
                /* 注意：请求多张图片请使用FileSelectActivity来选择，使用系统内置的相册无法选取多张图片，回调数据和本接口不符
                目前的处理方法是类似FileSelectActivity，列出所有文件夹下图片，自己来处理选取和分享操作。
                 */
                Intent mRequestFilesIntent = new Intent(Intent.ACTION_PICK);
                mRequestFilesIntent.setType("image/*");  //请求图片
                mRequestFilesIntent.putExtra("requestMultiFiles", true);

                startActivityForResult(mRequestFilesIntent, RequestMultiFilesCode);
                break;
            default:
                Toast.makeText(this, "按钮id不存在！", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 安全性问题考虑：
     * 当服务端应用程序向客户端应用程序发回包含Content URI的Intent时，该Intent会传递给客户端应用程序重写的onActivityResult()方法当中。
     * 一旦客户端应用程序拥有了文件的Content URI，它就可以通过获取其FileDescriptor访问文件了。
     * <p/>
     * 这一过程中不用过多担心文件的安全问题，因为客户端应用程序所收到的所有数据只有文件的Content URI而已。
     * 1. 由于URI不包含目录路径信息，客户端应用程序无法查询或打开任何服务端应用程序的其他文件。
     * 2. 客户端应用程序仅仅获取了这个文件的访问渠道以及由服务端应用程序授予的访问权限。
     * 3. 同时访问权限是临时的，一旦这个客户端应用的任务栈结束了，这个文件将无法再被除服务端应用程序之外的其他应用程序访问
     */
    /*
     * When the Activity of the app that hosts files sets a result and calls
     * finish(), this method is invoked. The returned Intent contains the
     * content URI of a selected file. The result code indicates if the
     * selection worked or not.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent returnIntent) {
        // If the selection didn't work
        if (resultCode != RESULT_OK) {
            // Exit without doing anything else
            return;
        } else if(requestCode == RequestSingleFileCode) {
            // 从返回的Intent中获取文件的content Uri
            Uri returnUri = returnIntent.getData();

            //获取文件类型
            String mimeType = getContentResolver().getType(returnUri);

            if (mimeType!=null && mimeType.contains("image/")) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                imageView.setImageURI(returnUri);
                imageView.setAdjustViewBounds(true);//设置ImageView布局自适应父View（即在保持长宽比条件下，填满父布局）

                containerLayout.addView(imageView);
            } else
                Toast.makeText(this, "未知文件类型：" + mimeType, Toast.LENGTH_SHORT).show();

            Cursor returnCursor =
                    getContentResolver().query(returnUri, null, null, null, null);
            if(returnCursor != null) {
            /*
            * Get the column indexes of the data in the Cursor,
            * move to the first row in the Cursor, get the data,
            * and display it.
            */
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                TextView nameView = (TextView) findViewById(R.id.filename_text);
                TextView sizeView = (TextView) findViewById(R.id.filesize_text);
                nameView.setText("文件名：" + returnCursor.getString(nameIndex));
                sizeView.setText("文件大小：" + Long.toString(returnCursor.getLong(sizeIndex)));

                returnCursor.close();
            }
            /*
             * Try to open the file for "read" access using the
             * returned URI. If the file isn't found, write to the
             * error log and return.
             */
            try {
                /* 只读方式尝试打开文件
                 * openFileDescriptor()方法返回一个文件的ParcelFileDescriptor对象。
                 * 客户端应用程序从该对象中获取FileDescriptor对象，然后利用该对象读取这个文件
                 */
                mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "File not found.");
                return;
            }
            // Get a regular file descriptor for the file
            FileDescriptor fd = mInputPFD.getFileDescriptor();
//            FileDescriptor

        }else if(requestCode == RequestMultiFilesCode) {
            if(returnIntent.getData() == null) {
                String type = returnIntent.getType();
                if (type.contains("image/*")) { /* <data android:mimeType="image/*" /> 的Intent*/
                    handleSendMultipleImages(returnIntent);

                } else
                    Toast.makeText(this, "未知文件类型：" + type, Toast.LENGTH_SHORT).show();

            }
        }
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
