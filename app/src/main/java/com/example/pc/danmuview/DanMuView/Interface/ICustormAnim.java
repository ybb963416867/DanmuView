package com.example.pc.danmuview.DanMuView.Interface;

import android.animation.AnimatorSet;
import android.view.View;

import com.example.pc.danmuview.DanMuView.DanMuAnimation.DanMuFrameLayout;


/**
 * Created by ybb on 2017/9/12.
 */

public interface ICustormAnim {
    AnimatorSet startAnim(DanMuFrameLayout danMuFrameLayout, View rootView);
    AnimatorSet comboAnim(DanMuFrameLayout danMuFrameLayout, View rootView);
    AnimatorSet endAnim(DanMuFrameLayout danMuFrameLayout, View rootView);
}
