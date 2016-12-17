package com.jirdy.flashlight.ads;

import android.view.ViewGroup;

import com.adsmogo.adapters.AdsMogoCustomEventPlatformEnum;
import com.adsmogo.controller.listener.AdsMogoListener;

/**
 * 芒果广告监听.
 */
class MyAdsMogoListener implements AdsMogoListener {

    @Override
    public void onInitFinish() {
        MogoAds.debug(MogoAds.TAG, "onInitFinish");
    }

    @Override
    public void onRequestAd(String s) {
        MogoAds.debug(MogoAds.TAG, "onRequestAd: " + s);
    }

    @Override
    public void onRealClickAd() {
        MogoAds.debug(MogoAds.TAG, "onRealClickAd");
    }

    @Override
    public void onReceiveAd(ViewGroup viewGroup, String s) {
        MogoAds.debug(MogoAds.TAG, "onReceiveAd: " + s);
    }

    @Override
    public void onFailedReceiveAd() {
        MogoAds.debug(MogoAds.TAG, "onFailedReceiveAd");
    }

    @Override
    public void onClickAd(String s) {
        MogoAds.debug(MogoAds.TAG, "onClickAd: " + s);
    }

    @Override
    public boolean onCloseAd() {
        MogoAds.debug(MogoAds.TAG, "onCloseAd");
        return false;
    }

    @Override
    public void onCloseMogoDialog() {
        MogoAds.debug(MogoAds.TAG, "onCloseMogoDialog");
    }

    @Override
    public Class getCustomEvemtPlatformAdapterClass(AdsMogoCustomEventPlatformEnum adsMogoCustomEventPlatformEnum) {
        return null;
    }
}