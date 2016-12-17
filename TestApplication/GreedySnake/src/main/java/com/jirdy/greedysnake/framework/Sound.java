package com.jirdy.greedysnake.framework;

/**
 * Created by Administrator on 2016/6/20.
 */
public interface Sound {
    //播放短音频，传入播放音量。
    public void play(float volume);

    //不需要用音频时，释放资源。
    public void dispose();
}
