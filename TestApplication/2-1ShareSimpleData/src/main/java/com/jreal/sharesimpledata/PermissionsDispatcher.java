package com.jreal.sharesimpledata;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/13.
 */
public class PermissionsDispatcher {

    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private Activity mActivity;

    public PermissionsDispatcher(Activity activity){
        this.mActivity = activity;
    }

    public void grantPermissions(String permission) {

        final List<String> permissionsList = new ArrayList<>();
        permissionsList.add(permission);

        grantPermissions(permissionsList);
    }

    public void grantPermissions(final List<String> permissionsList) {

        /*必须先检查权限是否已经获取到了
        ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        */

//        final List<String> permissionsList = new ArrayList<String>();
        List<String> permissionsNeeded = new ArrayList<String>();

//        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
//            permissionsNeeded.add("GPS");
//        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
//            permissionsNeeded.add("Read Contacts");
//        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
//            permissionsNeeded.add("Write Contacts");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read External Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale , message可以写上需要权限的原因
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /*
                                requestPermissions 执行 弹出请求授权对话框
                                目前问题：在点击不再询问之后，在请求权限，这里能弹出对话框，但不会请求权限。。。
                                */
                                ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                Log.i(ShareActivity.TAG, " ---ActivityCompat.requestPermissions again.");

                            }
                        });
                return;
            }

            ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

//        insertDummyContact();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {

        if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            // Check for Rationale Option
            /*求requestPermissions前，我们通过activity的shouldShowRequestPermissionRationale方法来检查是否需要弹出请求权限的提示对话框*/
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
//                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // All Permissions Granted
//                    insertDummyContact();
                } else {
                    // Permission Denied
                    Toast.makeText(mActivity, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();

                    showMessageOKCancel("请求权限被拒绝，无法体验应用完整功能，是否打开设置窗口进行授权？",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //简单粗暴，权限拒绝后直接跳转设置界面
                                    Intent localIntent = new Intent();
                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    localIntent.setData(Uri.fromParts("package", mActivity.getPackageName(), null));
                                    mActivity.startActivity(localIntent);

                                }
                            });
                }
            }
            break;
            default:
                mActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
