package com.jirdy.androidbasics.test;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import java.io.IOException;

public class MediaPlayerTest extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        setContentView(textView);

        //设置音量控制绑定正确的android音频流（即按音量键时，加减的音量是本应用中的音量，不影响系统和其他应用音量）
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mediaPlayer = new MediaPlayer();
        try {
            //获取assets文件夹下音频文件，返回文件描述符。
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("music.ogg");
            //通过文件描述符，设置MediaPlayer要播放的媒体，参数依次为：文件描述，文件offset，大小。
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(), descriptor.getLength());
            //进行播放前准备：确保文件能打开，检查是否可读，是否能在media中播放，
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);//是否循环播放。

/*
            //关于MediaPlayer其他方法：
            mediaPlayer.setVolume(1, 1);//设置左右声道音量（0到1)
            mediaPlayer.isLooping();//是否循环
            //判断是否播放完成的方法：
            mediaPlayer.isPlaying();//1、判断是否在播放
            //2、播放完成后的回调中处理需要处理的事务
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

                @Override
                public void onCompletion(MediaPlayer mediaPlayer){
                    //播放完成后的操作
                }
            });
*/
        } catch (IOException e) {
            textView.setText("Couldn't load music file, " + e);
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            //开始播放，必须在mediaPlayer.prepare();之后调用。
            mediaPlayer.start();
        }
    }

    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            //暂停播放（这个方法只有在MediaPlayer已经准备就绪，同时已经开始播放的时候，调用才有效）。
            mediaPlayer.pause();
            //如果activity被finish则停止播放 释放资源。
            if (isFinishing()) {
                //Note that when we want to start a stopped MediaPlayer, we first have to call the MediaPlayer.prepare() method again.
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }
}


