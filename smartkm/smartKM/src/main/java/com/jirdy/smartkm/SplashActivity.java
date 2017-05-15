package com.jirdy.smartkm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.jirdy.smartkm.utils.PrefUtils;

public class SplashActivity extends Activity {

    private LinearLayout llRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        llRoot = (LinearLayout) findViewById(R.id.splash_layout);

        //旋转 0-360度，相对自身(RELATIVE_TO_SELF)，旋转点为自身中心(x=0.5f, y=0.5f)
        RotateAnimation rotateAnimation = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);//x,y轴都是相对自身中心开始旋转
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);

        //缩放
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1, //x,y轴都是0到1
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f); //x,y轴都是相对自身中心开始缩放
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);

        //渐变
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);//2s
        alphaAnimation.setFillAfter(true);

        //添加动画集
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);


        //开始播放动画
        llRoot.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //在 SharedPreferences 中保存是否进行过新手引导
                boolean isGuideShow = PrefUtils.getBoolean("is_guide_show", false, getApplicationContext());

                if (isGuideShow){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }else{
                    startActivity(new Intent(getApplicationContext(), GuideActivity.class));
                }

                //结束页面
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
