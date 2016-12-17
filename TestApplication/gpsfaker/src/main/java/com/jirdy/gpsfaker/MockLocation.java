package com.jirdy.gpsfaker;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.jirdy.gpsfaker.activity.MainActivity;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/21.
 */
public class MockLocation implements LocationListener {

    private LocationManager locationManager;
    private boolean hasAddTestProvider = false;
    private boolean canMockPosition;
    private MainActivity mainActivity;
    private double oldLat = 0;
    private double oldLng = 0;

//    public MockLocation(LocationManager locationManager) {
//        this.locationManager = locationManager;
//    }

    public void initMock(MainActivity activity){
        mainActivity = activity;
        locationManager = (LocationManager) mainActivity
                .getSystemService(Context.LOCATION_SERVICE);

        canMockPosition = (Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0)
                || Build.VERSION.SDK_INT > 22;

        if (canMockPosition && hasAddTestProvider == false) {
            try {
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = locationManager.getProvider(providerStr);
                if (provider != null) {
                    locationManager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    locationManager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                locationManager.setTestProviderEnabled(providerStr, true);
                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
                locationManager.requestLocationUpdates(providerStr, 0, 0, this);

                // 模拟位置可用
                hasAddTestProvider = true;
                canMockPosition = true;
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }

    }

    /**
     * 停止模拟位置，以免启用模拟数据后无法还原使用系统位置
     * 若模拟位置未开启，则removeTestProvider将会抛出异常；
     * 若已addTestProvider后，关闭模拟位置，未removeTestProvider将导致系统GPS无数据更新；
     */
    public void stopMockLocation() {
        if (hasAddTestProvider) {
            try {
                locationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                // 若未成功addTestProvider，或者系统模拟位置已关闭则必然会出错
            }
            hasAddTestProvider = false;
        }
    }

    public boolean hasAddTestProvider(){
        return hasAddTestProvider;
    }

    /**
     * setLocation 设置GPS的位置
     *
     */
    public void pushMockLocation(double longitude, double latitude) {
//		Location location = new Location(mMockProviderName);
//		location.setTime(System.currentTimeMillis());
//		location.setLatitude(latitude);
//		location.setLongitude(longitude);
//		location.setAltitude(2.0f);
//		location.setAccuracy(3.0f);
//        /*
//        在android4.2之后的版本里对Location这个类做个细微的改动，增加了setElapsedRealtimeNanos和
//        getElapsedRealtimeNanos两个方法。而上面的错误就是没有为location的实例设置ElapsedRealtimeNanos
//         */
//        if(Build.VERSION.SDK_INT > 16){
//            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
//        }
//        locationManager.setTestProviderLocation(mMockProviderName, location);

        try {
            // 模拟位置（addTestProvider成功的前提下）
            String providerStr = LocationManager.GPS_PROVIDER;
            Location mockLocation = new Location(providerStr);
            mockLocation.setLatitude(latitude);  // 维度（度）
            mockLocation.setLongitude(longitude); // 经度（度）
            mockLocation.setAltitude(30);  // 高度（米）
            mockLocation.setBearing(180);  // 方向（度）
            mockLocation.setSpeed(10);  //速度（米/秒）
            mockLocation.setAccuracy(0.1f);  // 精度（米）
            mockLocation.setTime(new Date().getTime());  // 本地时间
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
            }
            locationManager.setTestProviderLocation(providerStr, mockLocation);
        } catch (Exception e) {
            // 防止用户在软件运行过程中关闭模拟位置或选择其他应用
            stopMockLocation();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        if(Math.abs(lat-oldLat)>0.1 || Math.abs(lng-oldLng)>0.1) {
            Log.i("jirdy", String.format("location: x=%s y=%s", lat, lng));
            mainActivity.setMockResult(lat, lng);
            oldLat = lat;
            oldLng = lng;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
