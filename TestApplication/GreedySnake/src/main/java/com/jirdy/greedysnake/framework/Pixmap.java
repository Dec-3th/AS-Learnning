package com.jirdy.greedysnake.framework;

/**
 * Created by Administrator on 2016/6/20.
 */
public interface Pixmap {

    public int getWidth();

    public int getHeight();

    public Graphics.PixmapFormat getFormat();

    //清除图片占用资源
    public void dispose();
}
