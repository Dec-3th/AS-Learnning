package com.jirdy.greedysnake.framework.implement;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.VolumeProvider;

import com.jirdy.greedysnake.framework.Audio;
import com.jirdy.greedysnake.framework.Music;
import com.jirdy.greedysnake.framework.Sound;

import java.io.IOException;

/**
 * 该类用于创建新的Sound或Music对象，创建成功后返回对象，此时可对对象进行播放暂停等操作。
 * Created by Administrator on 2016/7/2.
 */
public class AudioImpl implements Audio{

    AssetManager assetManager;
    /*
    由于整个游戏中只会有一个SoundPool来村存放所有的声音特效文件，故该参数在Audio中创建和填入sound。
     We do not release the SoundPool in any of the methods. The reason for this is that
there will always be a single Game instance holding a single Audio instance that holds a single
SoundPool instance. The SoundPool instance will, thus, be alive as long as the activity (and with
it our game) is alive. It will be destroyed automatically as soon as the activity ends.
     */
    SoundPool soundPool;

    public AudioImpl(Activity activity){
        //设置音量控制键控制的是该activity的音量。
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        assetManager = activity.getAssets();
        //新建声音流为STREAM_MUSIC类型的声音池，第一次参数表示能同时播放的最多音频数量，第二个参数为音频流的类型，第三个参数目前不用默认为0。
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public Music newMusic(String fileName) throws IOException {
        try {
            //获取音乐文件的文件描述符。
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(fileName);
            //使用文件描述符为参数创建Music对象。
            return new MusicImpl(assetFileDescriptor);
        }catch (IOException e){
            throw new RuntimeException("Couldn't load music '" + fileName + "'");
        }
    }

    /**
     * 打开新的声音文件，并将其载入声音池soundPool中，最后返回封装后的Sound。
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    @Override
    public Sound newSound(String fileName) throws IOException {
        try {
            //获取音乐文件的文件描述符。
            AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
            //通过fileDescriptor将声音特效载入SoundPool中，得到soundId
            int soundId = soundPool.load(fileDescriptor, 0);
            //使用SoundPool和soundId为参数，创建Sound对象。
            return new SoundImpl(soundPool, soundId);
        }catch (IOException e){
            throw new RuntimeException("Couldn't load sound '" + fileName + "'");
        }
    }
}
