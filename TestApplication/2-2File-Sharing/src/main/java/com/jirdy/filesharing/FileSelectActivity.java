package com.jirdy.filesharing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 该Activity用于显示在内部存储的“files/images/”目录下可以获得的文件，然后允许用户选择期望的文件，选择之后点击完成，将文件分享给请求的应用。
 * <p/>
 * 为了将文件安全地从我们的应用程序共享给其它应用程序，我们需要对自己的应用进行配置来提供安全的文件句柄（Content URI的形式）。
 * Android的FileProvider组件会基于在XML文件中的具体配置为文件创建Content URI
 * <p/>
 * 步骤：
 * 1. 为了给应用程序定义一个FileProvider，需要在Manifest清单文件中定义一个entry，该entry指明了需要使用的创建Content URI的Authority。
 * 2. 此外，还需要一个XML文件的文件名，该XML文件指定了我们的应用可以共享的目录路径。
 * <p/>
 * 注意：Note: XML文件是我们定义共享目录的唯一方式，不可以用代码的形式添加目录。
 */
public class FileSelectActivity extends ActionBarActivity {

    public static String TAG = "JR.FileSelect";

    // Array of files in the images subdirectory（images目录下的所有文件数组）
    File[] mImageFiles;
    // Array of filenames corresponding to mImageFiles（images目录下的所有文件的文件名数组）
    List<String> mImageFileNameList;

    // The path to the root of this app's internal storage（软件安装目录位置）
    private File mPrivateRootDir;
    // The path to the "images" subdirectory（file/images 目录位置）
    private File mImagesDir;

    private Intent mResultIntent;//返回的Intent，包含所选文件Uri

    private Uri fileUri; //所选文件Uri

    private ArrayList<Uri> selectedFilesUri; //所选文件的Uri

    private boolean selectMultiFiles;

    private AdapterView.OnItemClickListener mListClickListener = new AdapterView.OnItemClickListener() {

        /**
         * 在onItemClick()中，根据被选中文件的文件名获取一个File对象，然后将其作为参数传递给getUriForFile()，
         */
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {

//            File requestFile = mImageFiles[position];
            File requestFile = new File(mImagesDir, mImageFileNameList.get(position));
            Log.i(TAG, "onItemClick file path: " + requestFile.getPath() + ", exists? " + requestFile.exists());

            /*
            现在我们有一个完整的FileProvider声明，它在应用程序的内部存储中“files/”目录或其子目录下创建文件的Content URI。
            当我们的应用为一个文件创建了Content URI，该Content URI将会包含下列信息：
                1.<provider>标签中指定的Authority（“com.example.myapp.fileprovider”）；
                2.路径“myimages/”；
                3.文件的名字。
            例如，如果本课的例子定义了一个FileProvider，然后我们需要一个文件“default_image.jpg”的Content URI，FileProvider会返回如下URI：
            content://com.example.myapp.fileprovider/myimages/default_image.jpg
            */
            //使用FileProvider来获取文件的content Uri,我们能生成的那些Content URI所对应的文件，必须是那些在meta-data文件中包含<paths>标签的目录内的文件
            try {
                //需传入的参数是：上下文Context，在<provider>标签中为FileProvider所指定的Authority，文件名
                fileUri = FileProvider.getUriForFile(FileSelectActivity.this,
                        "com.jirdy.filesharing.fileprovider",
                        requestFile);

                Log.i(TAG, "The selected file: " + fileUri);

            } catch (IllegalArgumentException e) {
                Log.e(TAG,
                        "The selected file can't be shared: " +
                                requestFile.getName());
                e.printStackTrace();
            }


            if (selectMultiFiles) { //选择多个文件分享

                if (fileUri != null) {

                    /*将选择的文件添加到分享列表，双击取消分享*/
                    if (selectedFilesUri.contains(fileUri)) {
                        selectedFilesUri.remove(fileUri);
                        view.setBackgroundColor(Color.WHITE); //双击取消选择，恢复背景为白色
                    } else {
                        selectedFilesUri.add(fileUri);
                        view.setBackgroundColor(Color.LTGRAY); //选中的背景为灰色
                    }
                }

            } else { //选择单个文件分享

                if (fileUri != null) {
                    /**
                     * 为Content Uri授权临时的读取权限，授予的权限是临时的，并且当接收文件的应用程序的任务栈终止后，会自动过期。
                     *
                     * 注意：调用setFlags()来为文件授予临时被访问权限是唯一的安全的方法。尽量避免对文件的Content URI调用Context.grantUriPermission()，
                     * 因为通过该方法授予的权限，只能通过调用Context.revokeUriPermission()来撤销。
                     */
                    mResultIntent.addFlags(
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Put the Uri and MIME type in the result Intent（将Uri和文件类型放入返回的Intent）
                    mResultIntent.setDataAndType(
                            fileUri,//返回数据
                            getContentResolver().getType(fileUri));//返回数据类型

                    setResult(Activity.RESULT_OK,
                            mResultIntent);

                } else {
                    mResultIntent.setDataAndType(null, "");
                    setResult(RESULT_CANCELED,
                            mResultIntent);
                    Log.e(TAG,
                            "share file failed: " +
                                    requestFile.getName());
                }

                finish(); //选择好就返回
            }

            ImageView imageView = (ImageView) findViewById(R.id.image_selected);
            imageView.setAdjustViewBounds(true);
            imageView.setImageURI(Uri.fromFile(requestFile));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        // （建立一个Intent，用于返回数据给请求文件的Activity）
        mResultIntent =
                new Intent("com.jirdy.filesharing.ACTION_RETURN_FILE");

        //（获取软件安装目录下的 files/ 目录）
        mPrivateRootDir = getFilesDir();
        // （获取软件安装目录下的 files/images 目录）
        mImagesDir = new File(mPrivateRootDir, "images");
        //（获取files/images文件夹下所有文件）
        mImageFiles = mImagesDir.listFiles();

        mImageFileNameList = new ArrayList<>();
        selectedFilesUri = new ArrayList<>();

        // Set the Activity's result to null to begin with（刚开始设置返回数据为null)
        setResult(Activity.RESULT_CANCELED, null);

        if (mImageFiles != null) {
            //获取images文件夹下所有文件名称
            for (File f: mImageFiles) {
                mImageFileNameList.add(f.getName());
            }

            /*
            使用简单的List列出images文件夹下文件的文件名
            */
            int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

            Log.i(TAG, "mImageFileNameList:" + mImageFileNameList);
            ListView listView = (ListView) this.findViewById(R.id.image_list);
            listView.setOnItemClickListener(mListClickListener);
            listView.setBackgroundColor(Color.WHITE); //设置默认背景色

            // Create an array adapter for the list view, using the Articles headlines array
            listView.setAdapter(new ArrayAdapter<String>(this, layout, mImageFileNameList));

        } else {
            Log.i(TAG, "没有要展示的文件！mImageFiles: " + mImageFiles);
            Toast.makeText(getBaseContext(), "没有要展示的文件！mImageFiles: " + mImageFiles, Toast.LENGTH_SHORT).show();
        }

        Intent requestIntent = getIntent();
        selectMultiFiles = requestIntent.getBooleanExtra("requestMultiFiles", false); //是否选择多个文件分享
        Log.i(TAG, "selectMultiFiles: " + selectMultiFiles);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fille_select, menu);
//        if(selectMultiFiles == 0){
//            MenuItem finishBtn = (MenuItem)menu.findItem(R.id.action_finish);
//            finishBtn.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_finish:
                if (!selectedFilesUri.isEmpty()) {
                    mResultIntent.addFlags(
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Put the Uri and MIME type in the result Intent（将Uri和文件类型放入返回的Intent）
                /* 设置返回数据和类型 到Intent中
                 * 为了向请求文件的应用程序提供其需要的文件，我们将包含了Content URI和相应权限的Intent传递给setResult()。
                 * 当定义的Activity结束后，系统会把这个包含了Content URI的Intent传递给客户端应用程序。
                 */
//                    mResultIntent.setDataAndType(
//                            fileUri,//返回数据
//                            getContentResolver().getType(fileUri));//返回数据类型
                    mResultIntent.setType("image/*");
                    mResultIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, selectedFilesUri);
                    setResult(Activity.RESULT_OK,
                            mResultIntent);
                } else {
                    mResultIntent.setDataAndType(null, "");
                    setResult(RESULT_CANCELED,
                            mResultIntent);
                    Log.e(TAG,
                            "share file failed");
                }
                finish();
                return true;
            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "设置", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}

