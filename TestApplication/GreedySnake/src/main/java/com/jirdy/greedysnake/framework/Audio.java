package com.jirdy.greedysnake.framework;

import java.io.IOException;

/**
 * Audio is responsible for creating sound and Music instances from asset files
 * Created by Administrator on 2016/6/20.
 */
public interface Audio {
    // A Music instance represents a streamed audio file.
    public Music newMusic(String fileName) throws IOException;

    //A Sound instance represents a short sound effect that we keep entirely in memory.
    public Sound newSound(String fileName) throws IOException;
}
