package com.jirdy.greedysnake.framework;

/**
 * Created by Administrator on 2016/6/20.
 */
public interface Music {
    public void play();

    public void pause();

    public void setLooping(boolean looping);

    //设置音频播放音量。
    public void setVolume(float volume);

    public boolean isPlaying();

    public boolean isStoping();

    //we can set the volume as a float in the range of 0 (silent) to 1 (maximum volume).
    public boolean isLooping();

    //Once we no longer need the Music instance, we have to dispose of it，关闭音频流文件，不再播放.
    public void dispose();
}
