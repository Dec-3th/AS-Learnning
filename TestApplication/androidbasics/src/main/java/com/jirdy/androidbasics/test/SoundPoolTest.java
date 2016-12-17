package com.jirdy.androidbasics.test;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

/**
 * 注意：SoundPool is known to have problems with MP3 files and long sound files
 */
public class SoundPoolTest extends AppCompatActivity implements View.OnTouchListener {
    SoundPool soundPool;
    int explosionId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setOnTouchListener(this);
        setContentView(textView);

        //设置音量控制绑定正确的android音频流（即按音量键时，加减的音量是本应用中的音量，不影响系统和其他应用音量）
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //获取声音池，第一次参数表示能同时播放的最多音频数量，第二个参数为音频流的类型，第三个参数目前不用默认为0。
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = getAssets();
            //打开assets文件夹下指定文件，返回文件的描述符(descriptor)
            AssetFileDescriptor descriptor = assetManager
                    .openFd("explosion.ogg");

            //通过文件描述符(descriptor)，获取资源id，用于播放。
            /*the second argument specifies the priority of the sound effect. This is currently not used, and should be set to 1 for future compatibility.*/
            explosionId = soundPool.load(descriptor, 1);//第二个参数优先度

            textView.setText("触摸屏幕播放爆炸音效");
        } catch (IOException e) {
            textView.setText("Couldn't load sound effect from asset, " + e);
        }
    }

    //触屏之后播放音频
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (explosionId != -1) {
                /*
                第一个参数音频id，第二三个参数分别为左声道和右声道的音量（0到1之间数值），
                第三四个参数分别为优先度（不使用时设为0）和 音频循环频率（不循环设为0），
                最后一个参数为音频播放速率，为1以录制的速率播放，小于1慢进播放，大于1快进播放
                 */
                soundPool.play(explosionId, 1, 1, 0, 0, 1);
            }
        }
        return true;
    }
}