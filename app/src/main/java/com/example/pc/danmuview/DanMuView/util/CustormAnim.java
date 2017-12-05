package com.example.pc.danmuview.DanMuView.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.pc.danmuview.DanMuView.DanMuAnimation.DanMuFrameLayout;
import com.example.pc.danmuview.DanMuView.Interface.ICustormAnim;


/**
 * Created by ybb on 2017/9/12.
 */

public class CustormAnim implements ICustormAnim {

    @Override
    public AnimatorSet startAnim(final DanMuFrameLayout danMuFrameLayout, View rootView) {
        //礼物飞入LinearInterpolator
        ObjectAnimator flyFromLtoR2 = DanMuAnimationUtil.createFlyFromLtoR(danMuFrameLayout,rootView.getHeight(), -rootView.getHeight(), 6000, new LinearInterpolator());
        flyFromLtoR2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                danMuFrameLayout.initLayoutState();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                danMuFrameLayout.comboAnimation();
            }
        });
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(flyFromLtoR2);
        animSet.start();
        return animSet;
    }

    @Override
    public AnimatorSet comboAnim(final DanMuFrameLayout danMuFrameLayout, View rootView) {
        danMuFrameLayout.comboEndAnim();
        return null;
    }

    @Override
    public AnimatorSet endAnim(final DanMuFrameLayout danMuFrameLayout, View rootView) {
        //向上渐变消失
        ObjectAnimator fadeAnimator = DanMuAnimationUtil.createFadeAnimator(danMuFrameLayout, 0, -100, 0, 0);
        // 复原
        ObjectAnimator fadeAnimator2 = DanMuAnimationUtil.createFadeAnimator(danMuFrameLayout, 100, 0, 0, 0);

        AnimatorSet animatorSet = DanMuAnimationUtil.startAnimation(fadeAnimator, fadeAnimator2);
//        AnimatorSet animatorSet = DanMuAnimationUtil.startAnimation(null,fadeAnimator2);
        return animatorSet;
//        return testAnim(liveFrameLayout);
    }

    @NonNull
    private AnimatorSet testAnim(DanMuFrameLayout danMuFrameLayout) {
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", 0, -50);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.5f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(danMuFrameLayout, translationY, alpha);
        animator.setStartDelay(0);
        animator.setDuration(1000);

        translationY = PropertyValuesHolder.ofFloat("translationY", -50, -100);
        alpha = PropertyValuesHolder.ofFloat("alpha", 0.5f, 0f);
        ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(danMuFrameLayout, translationY, alpha);
        animator1.setStartDelay(0);
        animator1.setDuration(1000);

        // 复原
        ObjectAnimator fadeAnimator2 = DanMuAnimationUtil.createFadeAnimator(danMuFrameLayout, 0, 0, 0, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animator1).after(animator);
        animatorSet.play(fadeAnimator2).after(animator1);
        animatorSet.start();
        return animatorSet;
    }
}
