package com.jirdy.greedysnake.framework.implement;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;

import com.jirdy.greedysnake.framework.Input;
import com.jirdy.greedysnake.framework.utils.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class KeyboardHandler implements View.OnKeyListener {

    /*
    将所有128个按键的状态(按下或没按)存在一个数组里，以每个按键的keycode为索引(所有keycode值在0~127之间)
    */
    boolean[] pressedKeys = new boolean[128];

    /*
    将创建过的KeyEvent都存入keyEventPool中复用，we recycle all the KeyEvent objects we create
    */
    Pool<Input.KeyEvent> keyEventPool;

    /*
    stores the KeyEvent instances hat have not yet been consumed by our game.
    */
    List<Input.KeyEvent> keyEventsBuffer = new ArrayList<>();

    /*
    stores the KeyEvents that we return by calling the KeyboardHandler.getKeyEvents()
    */
    List<Input.KeyEvent> keyEvents = new ArrayList<>();



    public KeyboardHandler(View view) {

        //实现Pool.PoolObjectFactory接口，创建一个KeyEvent对象池的工厂，用来创建KeyEvent对象。
        Pool.PoolObjectFactory<Input.KeyEvent> factory = new Pool.PoolObjectFactory<Input.KeyEvent>() {
            @Override
            public Input.KeyEvent createObject() {
                return new Input.KeyEvent();
            }
        };

        //创建一个KeyEvent池，存放对象上限为100个。
        keyEventPool = new Pool<>(factory, 100);

        view.setOnKeyListener(this); // 登记KeyboardHandler为view的按键监听
        view.setFocusableInTouchMode(true); // 设置view能通过触摸获取焦点
        view.requestFocus(); // 请求焦点
    }

    /**
     * 当有按键事件到达时，通过pressedKeys记录按下和释放的按键，将事件缓存到keyEventsBuffer中
     * @param view 发出事件的组件
     * @param keyCode 对应按下按键的唯一代码
     * @param keyEvent 按键事件对象
     * @return
     */
    @Override
    public boolean onKey(View view, int keyCode, android.view.KeyEvent keyEvent) {

        if(keyEvent.getAction() == KeyEvent.ACTION_MULTIPLE)//多个按键情况这里不处理
            return false;

        /*线程同步模式下运行，即对该类加锁，同一时刻只能有一个线程访问该类中synchronized (this)方法（类中所有这样的方法）*/
        //创建新的Input.KeyEvent，存储android.view.KeyEvent的主要参数到Input.KeyEvent
        synchronized (this){
            Input.KeyEvent inputKeyEvent = keyEventPool.newOject();//有可重用对象就使用，没有就新建。
            //存储keyEvent参数
            inputKeyEvent.keyCode = keyCode;
            inputKeyEvent.keyChar = (char)keyEvent.getUnicodeChar();

            //记录按下或松开的按键
            if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                //存储keyType
                inputKeyEvent.keyType = KeyEvent.ACTION_DOWN;
                if(keyCode>0 && keyCode<127)
                    pressedKeys[keyCode] = true;
            }
            if(keyEvent.getAction() == KeyEvent.ACTION_UP){

                inputKeyEvent.keyType = KeyEvent.ACTION_UP;
                if(keyCode>0 && keyCode<127)
                    pressedKeys[keyCode] = false;
            }
            keyEventsBuffer.add(inputKeyEvent);
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > 127)
            return false;
        return pressedKeys[keyCode];
    }

    public List <Input.KeyEvent> getKeyEvents() {

        synchronized (this) {
            //将keyEvents中所有的Event，都通过free方法插入keyEventPool的freeObjects中，留待下次使用。
            int len = keyEvents.size();
            for (int i = 0; i < len; i++) {
                keyEventPool.free(keyEvents.get(i));
            }
            //之后清空所有使用过的Events，从Events缓存中获取所有新捕捉到的Events
            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            //清空缓存
            keyEventsBuffer.clear();

            //返回新捕捉到的Events。
            return keyEvents;
        }
    }

}
