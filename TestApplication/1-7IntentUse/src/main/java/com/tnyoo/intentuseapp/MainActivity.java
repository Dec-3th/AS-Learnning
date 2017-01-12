package com.tnyoo.intentuseapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public final static String TAG = "ASTEST";
    public static final int PICK_CONTACT_REQUEST = 1; // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.call:
                call("10086");
                break;
            case R.id.open_map:
//                openMap(getResources().getString(R.string.location));//37.422219,-122.08364?z=14
                openMap("37.422219,-122.08364?z=14");//没有googleMap客户端，不能定位
                break;
            case R.id.open_web_page:
                openWebPage("http://www.tnyoo.com");
                break;
            case R.id.send_email:
                //附件，icon.png必须保存在手机内存的根目录下才能找到，否则找不到。
                File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "icon.png");// 附件

                sendEmail(new String[]{"december.river@yahoo.com"}, "标题：From JR test", "内容：Starry Eyes, Starry Starry Night",
                        file);
                Log.i(TAG, "file: " + file.getAbsolutePath());
                break;
            case R.id.create_calendar_event:
                createCalendarEvent("标题：Android学习", "学校实验室");
                break;
            case R.id.chose_share_app:
                chooseShareApp("Life sucks when your are ordinary");
                break;
            case R.id.start_activity_for_result:
                pickContact();
                break;
            case R.id.show_all_contacts:
                Intent intent = new Intent(this, ContactsListActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "按钮id不存在！", Toast.LENGTH_SHORT).show();
        }
    }

    //拨打指定电话
    public void call(String phoneNumber) {
        Uri number = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, number);//调拨打电话界面
//        Intent intent = new Intent(Intent.ACTION_CALL, number);//直接拨打电话
        safeStartActivity(intent);
    }

    //打开地图
    public void openMap(String location) {
        Uri locat = Uri.parse("geo:" + location);
        Intent intent = new Intent(Intent.ACTION_VIEW, locat);//查看地图
        Log.i(TAG, "openMap:" + locat);
        safeStartActivity(intent);
    }

    //打开网页
    public void openWebPage(String address) {
        Uri webpage = Uri.parse(address);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);//查看地图
        safeStartActivity(intent);
    }

    //发送带附件邮件
    public void sendEmail(String[] recipients, String title, String content, File file) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        //邮件发送类型：带附件的邮件
        emailIntent.setType("application/octet-stream");

        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType(HTTP.PLAIN_TEXT_TYPE);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        // You can also attach multiple items by passing an ArrayList of Uris

        safeStartActivity(emailIntent);

//        emailIntent.putExtra(Intent.EXTRA_STREAM,"[url=]file:///storage/sdcard1/song.mp3[/url]");
//        emailIntent.setType("audio/mp3");
//        startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));

    }

    //创建一个日历事件
    public void createCalendarEvent(String title, String eventLocation) {
        long time = System.currentTimeMillis();//获取当前系统时间

        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(2016, 2, 23, 15, 0);
        beginTime.setTimeInMillis(time);//事件开始时间默认当前时间

        Calendar endTime = Calendar.getInstance();
//        endTime.set(2016, 4, 23, 18, 30);
        endTime.setTimeInMillis(time + 3600 * 1000 * 24);//事件结束时间默认一小时后1000millis=1s, 3600s=1h.再 x24就是一天后.

        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, title);
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation);
        safeStartActivity(calendarIntent);
    }

    /*
    先检查是否存在可以使用的Activity，再打开Activity
    验证有app可以handle这个intent,然后启动它
     */
    private void safeStartActivity(Intent intent) {
        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;
        // Start an activity if it's safe
        if (isIntentSafe) {
            startActivity(intent);
            Log.i(TAG, "打开app成功... o(^.^)o");
        } else {
            Toast.makeText(this, "抱歉，没有app可以打开该资源... o(>.<)o", Toast.LENGTH_SHORT).show();
        }
    }

    //分享文本内容到其他app（通过intent-filter）
    public void chooseShareApp(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        // Always use string resources for UI text.
        // This says something like "Share this photo with"
        String title = getResources().getString(R.string.chooser_title);
        intent.setType(HTTP.PLAIN_TEXT_TYPE);
        intent.putExtra(Intent.EXTRA_TEXT, content);

        /* 显示分享App的选择界面
        为了显示chooser来显示可用于打开的activity，使用createChooser来创建Intent*/
        Intent chooser = Intent.createChooser(intent, title);
//        startActivity(chooser);
        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            Toast.makeText(this, "startActivity(chooser)", Toast.LENGTH_SHORT).show();
            startActivity(chooser);
        }
        Toast.makeText(this, "chooseShareApp", Toast.LENGTH_SHORT).show();
    }


    private void pickContact() {
        //该Intent打开联系人窗口，user选取某个联系人后返回数据：只会返回一个（而不是读取联系人信息
        Intent pickContact = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);// Show user only contacts w/ phone numbers

        /*后一个参数为"request code"，用于标识你的请求。当你接收到result Intent时，可以从回调方法里面的参数去判断这个result是否是你想要的*/
        startActivityForResult(pickContact, PICK_CONTACT_REQUEST);
    }

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
        if (requestCode == PICK_CONTACT_REQUEST) {/*从回调方法里面的requestCode参数去判断这个result是否是你想要的*/
            if (resultCode == RESULT_OK) {//获取成功
                Uri contactUri = data.getData();//被返回的Intent中使用Uri的形式来表示返回的联系人
                String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                if (cursor == null) {
                    Toast.makeText(this, "未获取到联系人!", Toast.LENGTH_SHORT).show();
                    return;
                }
                cursor.moveToFirst();

                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String name = cursor.getString(nameIndex);
                String number = cursor.getString(numberIndex);
                cursor.close();

                Toast.makeText(this, "获取到联系人：" + name + "，电话：" + number, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "获取到联系人：" + name + "，电话：" + number);

            }
        }
    }

}
