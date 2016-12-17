package com.jirdy.greedysnake.framework.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 本类作用：重用已经创建的对象，即有一个List存放创建过的对象，每次其他程序需要使用新的对象的时候，检查List，如果
 * List中还有已经创建的对象，则从List中取出对象，并将该对象从List中移除，如果List中没有已经创建过对象，则通过接口
 * PoolObjectFactory.createObject()创建新的对象来使用，并将其放入List中。
 *
 *     The Android inputsystem fires many of these events when a key is pressed or a finger touches the screen, so we
 * constantly create new instances that are collected by the garbage collector in short intervals. In
 * order to avoid this, we implement a concept known as instance pooling. Instead of repeatedly
 * creating new instances of a class, we simply reuse previously created instances. The Pool class
 * is a convenient way to implement that behavior.
 *
 * Created by Administrator on 2016/7/5.
 */
public class Pool<T> {

    public interface PoolObjectFactory<T> {
        public T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;//能存储的最大对象数量。

    /**
     * 构造一个Oject池
     * @param factory Object池创建工厂。
     * @param max 最大存储Obj数量。
     */
    public Pool(PoolObjectFactory<T> factory, int max) {
        this.factory = factory;
        freeObjects = new ArrayList<T>(max);
        maxSize = max;
    }

    /**
     * 当需要使用一个新的对象时，先检查List，如果有可重用对象，则从List中取出来使用
     * 否则新建对象来使用。
     * 注意：从这里取出的重用对象，在使用前一定要完全初始化，否则会残留一些旧的参数影响程序使用。
     * The newObject() method is responsible for either handing us a brand-new instance of the type
     * held by the Pool, via the PoolObjectFactory.newObject() method, or returning a pooled instance
     * in case there’s one in the freeObjectsArrayList.
     * @return
     */
    public T newOject(){
        T object = null;

        if(freeObjects.isEmpty())
            object = factory.createObject();
        else
            object = freeObjects.remove(freeObjects.size() -1);
        return object;
    }

    /**
     * 将我们已经使用过不再使用的oject插入freeObjects的列表里面，留给下一次重用，如果满了则不插入，
     * 从而被garbage collector清理掉。
     * The free() method lets us reinsert objects that we no longer use
     * @param object
     */
    public void free(T object){
        if(freeObjects.size()<maxSize){
            freeObjects.add(object);
        }
    }

}
