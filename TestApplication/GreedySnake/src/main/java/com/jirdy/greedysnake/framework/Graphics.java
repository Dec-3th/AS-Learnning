package com.jirdy.greedysnake.framework;

/**
 * Created by Administrator on 2016/6/20.
 */
public interface Graphics {

    /*
    framebuffer：显示屏显示内容的缓冲，理解为在里面填充什么（图片、颜色），什么就会出现在显示屏上。
     */
    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    //load an image given in either JPEG or PNG format.
    public Pixmap newPixmap(String fileName, PixmapFormat format);

    //clears the complete framebuffer with the given color.
    public void clear(int color);

    //set the pixel at (x,y) in the framebuffer to the given color (超出屏幕的点忽略）.
    public void drawPixel(int x, int y, int color);

    //画一条直线
    public void drawLine(int x, int y, int x2, int y2, int color);

    //画一个矩形(x,y)为矩形左上角顶点，矩形宽高为矩形在x和y方向各含有的像素点数目，calor为填充颜色。
    public void drawRect(int x, int y, int width, int height, int color);

    /**
     * 画图像的一部分
     * draws rectangular(矩形) portions of a Pixmap to the framebuffer.
     *
     * @param pixmap
     * @param (x,y)       指定图片左上角在framebuffer中的位置
     *                    coordinates specify the top-left corner’s position of the Pixmap’s target location in the framebuffer
     * @param (srcX,srcY) 指定矩形区域的左上角位置，以图片自带的坐标系为基准计算（即矩形左上角顶点在图片中位置）。
     *                    specify the corresponding top-left corner of the rectangular region that is used from the Pixmap, given in the Pixmap’s own coordinate system.
     * @param srcWidth    矩形宽
     * @param srcHeight   矩形高
     */
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
                           int srcWidth, int srcHeight);

    /**
     * 画整张图
     * @param pixmap
     * @param (x,y) 指定图片左上角在framebuffer中的位置
     */
    public void drawPixmap(Pixmap pixmap, int x, int y);

    /*
     return the width and height of the framebuffer in pixels
     */
    public int getWidth();

    public int getHeight();
}
