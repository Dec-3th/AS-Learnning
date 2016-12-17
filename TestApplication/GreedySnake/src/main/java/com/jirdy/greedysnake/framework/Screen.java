package com.jirdy.greedysnake.framework;

/**
 * Created by Administrator on 2016/6/18.
 */
public abstract class Screen {
    protected final Game game;

    //Screen接口需要调用Game类里面的模块，故在这里作为一个内部参数，被传递存储。
    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
