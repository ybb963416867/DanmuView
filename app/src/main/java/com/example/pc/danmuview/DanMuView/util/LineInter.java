package com.example.pc.danmuview.DanMuView.util;

import android.annotation.SuppressLint;
import android.view.animation.BaseInterpolator;

/**
 * Created by pc on 2017/12/4 0004.
 */

@SuppressLint("NewApi")
public class LineInter extends BaseInterpolator {
    @Override
    public float getInterpolation(float input) {
        return ((4*input-2)*(4*input-2)*(4*input-2))/16f + 0.5f;
    }
}
