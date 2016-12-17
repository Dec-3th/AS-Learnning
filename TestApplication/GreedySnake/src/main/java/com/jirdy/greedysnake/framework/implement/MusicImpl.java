package com.jirdy.greedysnake.framework.implement;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.jirdy.greedysnake.framework.Music;

import java.io.IOException;

/**
 * Created by Administrator on 2016/7/4.
 */
public class MusicImpl implements Music, MediaPlayer.OnCompletionListener {

    //Remember, we can only call MediaPlayer.start()/stop()/pause() when the MediaPlayer is prepared.
    private MediaPlayer mediaPlayer;
    private float volume;
    /*用于保存mediaPlayer的状态，是否已经prepare，
    当一个音乐文件播放完毕，或者调用了MediaPlayer.stop()方法后，需要重新调用MediaPlayer.prepare()才能再次播放*/
    private boolean isPrepared = false;

    public MusicImpl(AssetFileDescriptor fileDescriptor) {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            //准备
            mediaPlayer.prepare();
            isPrepared = true;
            //设置MusicImpl类为音乐播放完成之后的监听（已实现MediaPlayer.OnCompletionListener的接口）
            mediaPlayer.setOnCompletionListener(this);

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music:" + e);
        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        /*
        播放完毕后修改isPrepared状态为false，synchronized关键字作用是对isPrepared加锁再修改，
        即此时此刻只能有一个线程修改isPrepared的值，修改前后对isPrepared进行了加锁解锁。
        （下面在同步块中修改isPrepared，保证isPrepared全局的值一致）
         */
        synchronized (this) {
            isPrepared = false;//播放complete以后需要重新prepare()才能再次播放。
        }
    }

    @Override
    public void play() {

        if (mediaPlayer.isPlaying())
            return;
        try {
            /*在 play()方法中重新进行了prepare()，保证每次调用play()都会播放*/
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        mediaPlayer.stop();
        //stop以后需要重新prepare()才能再次播放（同步块中修改isPrepared，保证isPrepared全局的值一致）。
        synchronized (this) {
            isPrepared = false;
        }
    }


    @Override
    public void pause() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    @Override
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    /**
     * 当音乐播放暂停(pause)和播放停止(stop)的时候，该方法都返回false，故不能用用来判断音乐是否停止播放
     *
     * @return
     */
    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public boolean isStoping() {
        return !isPrepared;
    }

    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    @Override
    public void dispose() {
        if (mediaPlayer.isPlaying())//如果正在播放则关闭。
            mediaPlayer.stop();

        mediaPlayer.release();//释放资源（一定要在音乐播放stop以后才能调用该方法，否则会抛出异常）
    }
}
