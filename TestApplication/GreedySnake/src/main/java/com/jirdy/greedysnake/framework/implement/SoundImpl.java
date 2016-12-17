package com.jirdy.greedysnake.framework.implement;

import android.media.SoundPool;

import com.jirdy.greedysnake.framework.Sound;

/**
 * Created by Administrator on 2016/7/4.
 */
public class SoundImpl implements Sound {

    //内部成员变量，包括音效池和音效id(音效在音效池中的id)
    private SoundPool soundPool;
    private int soundId;//在音效池中唯一标记一个音效。

    public SoundImpl(SoundPool soundPool, int soundId){
        this.soundPool = soundPool;
        this.soundId = soundId;
    }

    @Override
    public void play(float volume) {
         /*
           第一个参数音频id，第二三个参数分别为左声道和右声道的音量（0到1之间数值），
           第三四个参数分别为优先度（不使用时设为0）和 音频循环频率（不循环设为0），
           最后一个参数为音频播放速率，为1以录制的速率播放，小于1慢进播放，大于1快进播放
         */
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId);
    }
}
