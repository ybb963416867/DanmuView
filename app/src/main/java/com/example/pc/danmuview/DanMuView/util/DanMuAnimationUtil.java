package com.example.pc.danmuview.DanMuView.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by ybb on 2017/9/12.
 */
public class DanMuAnimationUtil {


    /**
     * @param target
     * @param star     动画起始坐标
     * @param end      动画终止坐标
     * @param duration 持续时间
     * @return 创建一个从左到右的飞入动画
     * 礼物飞入动画
     */
    public static ObjectAnimator createFlyFromLtoR(final View target, float star, float end, int duration, TimeInterpolator interpolator) {
        //1.个人信息先飞出来
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(target, "translationY",
                star, end);
        anim1.setInterpolator(interpolator);
        anim1.setDuration(duration);
        return anim1;
    }


    /**
     * @param target
     * @return 播放帧动画
     */
    public static AnimationDrawable startAnimationDrawable(ImageView target) {
        AnimationDrawable animationDrawable = (AnimationDrawable) target.getDrawable();
        if (animationDrawable != null) {
            target.setVisibility(View.VISIBLE);
            animationDrawable.start();
        }
        return animationDrawable;
    }


    /**
     * @param target
     * @param drawable 设置帧动画
     */
//    public static void setAnimationDrawable(ImageView target, AnimationDrawable drawable) {
//
//        target.setBackground(drawable);
//    }

    /**
     * @param target
     * @param star
     * @param end
     * @param duration
     * @param startDelay
     * @return 向上飞 淡出
     */
    public static ObjectAnimator createFadeAnimator(final View target, float star, float end, int duration, int startDelay) {

//        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", star, end);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f);
//        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, translationY, alpha);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, alpha);
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        return animator;
    }

    /**
     * @param animator1
     * @param animator2
     * @return 按顺序播放动画
     */
    public static AnimatorSet startAnimation(ObjectAnimator animator1, ObjectAnimator animator2) {
        AnimatorSet animSet = new AnimatorSet();
//        animSet.playSequentially(animators);
        animSet.play(animator1).before(animator2);
        animSet.start();
        return animSet;
    }
    /**
     * @param animator1
     * @param animator2
     * @param animator3
     * @param animator4
     * @param animator5
     * @return 按顺序播放动画
     */
    public static AnimatorSet startAnimation(ObjectAnimator animator1, ObjectAnimator animator2, ObjectAnimator animator3, ObjectAnimator animator4, ObjectAnimator animator5) {
        AnimatorSet animSet = new AnimatorSet();
//        animSet.playSequentially(animators);
        animSet.play(animator1).before(animator2);
        animSet.play(animator3).after(animator2);
        animSet.play(animator4).after(animator3);
        animSet.play(animator5).after(animator4);
        animSet.start();
        return animSet;
    }

}
