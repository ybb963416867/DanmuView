package com.example.pc.danmuview.DanMuView.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.widget.LinearLayout;

import com.example.pc.danmuview.DanMuView.Interface.ICustormAnim;
import com.example.pc.danmuview.DanMuView.DanMuAnimation.DanMuFrameLayout;
import com.example.pc.danmuview.DanMuView.DanMuAnimation.LiveModel;

import java.util.ArrayList;

/**
 * Created by ybb on 2017/9/12.
 */

public class GiftControl implements DanMuFrameLayout.LeftGiftAnimationStatusListener {

    private static final String TAG = "GiftControl";
    protected Context mContext;
    /**
     * 自定义动画
     */
    private ICustormAnim custormAnim;
    /**
     * 点赞队列
     */
    private ArrayList<LiveModel> mGiftQueue;

    /**
     * 直播点赞数量集合
     */
    private SparseArray<DanMuFrameLayout> mGiftLayoutList;
    /**
     * 添加礼物布局的父容器
     */
    private LinearLayout mGiftLayoutParent;

    public GiftControl(Context context) {
        mContext = context;
        mGiftQueue = new ArrayList<>();
    }

    public GiftControl setCustormAnim(CustormAnim anim) {
        custormAnim = anim;

        return this;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                   DanMuFrameLayout danMuFrameLayout = (DanMuFrameLayout) msg.obj;
                    boolean hasGift = danMuFrameLayout.setGift(getGift());
                    if (hasGift) {
                        danMuFrameLayout.startAnimation(custormAnim);
                    }
                    break;
            }
        }
    };

    /**
     * @param isHideMode       是否开启隐藏动画
     * @param giftLayoutParent 父容器
     * @param giftLayoutNums   控件的数量
     * @return
     */
    public GiftControl setGiftLayout(boolean isHideMode, LinearLayout giftLayoutParent, @NonNull int giftLayoutNums) {
        if (giftLayoutNums <= 0) {
            throw new IllegalArgumentException("GiftFrameLayout数量必须大于0");
        }
        if (giftLayoutParent.getChildCount() > 0) {//如果父容器没有子孩子，就进行添加
            return this;
        }
        mGiftLayoutParent = giftLayoutParent;

        final SparseArray<DanMuFrameLayout> giftLayoutList = new SparseArray<>();
        for (int i = 0; i < giftLayoutNums; i++) {
            DanMuFrameLayout danMuFrameLayout = new DanMuFrameLayout(mContext);
            LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            danMuFrameLayout.setLayoutParams(param);
            giftLayoutList.append(i, danMuFrameLayout);
        }

        mGiftLayoutList = giftLayoutList;
        DanMuFrameLayout danMuFrameLayout;
        for (int i = 0; i < mGiftLayoutList.size(); i++) {
            danMuFrameLayout = mGiftLayoutList.get(i);
//            if (i == 0) {
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0, 20, 0, 0);
//                liveFrameLayout.setLayoutParams(params);
//            }
            giftLayoutParent.addView(danMuFrameLayout);
            danMuFrameLayout.setIndex(i);
            danMuFrameLayout.firstHideLayout();
            danMuFrameLayout.setGiftAnimationListener(this);
            danMuFrameLayout.setHideMode(isHideMode);
        }
        return this;
    }

    /**
     * @param gift
     */
    public void loadGift(final LiveModel gift) {
        if (mGiftQueue != null) {
            addGiftQueue(gift);
        }
    }

    private void addGiftQueue(final LiveModel gift) {
        if (mGiftQueue != null) {
            if (mGiftQueue.size() == 0) {
                mGiftQueue.add(gift);
                showGift();
                return;
            }
        }

            if (mGiftQueue.size()>15)
                return;
            mGiftQueue.add(gift);
    }

    /**
     * 清空缓存
     */
    public void clean() {
        mGiftQueue.clear();
    }

    /**
     */
    public synchronized void showGift() {
        if (isEmpty()) {
            return;
        }
        DanMuFrameLayout danMuFrameLayout;
        for (int i = 0; i < mGiftLayoutList.size(); i++) {
            danMuFrameLayout = mGiftLayoutList.get(i);
            if (!danMuFrameLayout.isShowing() && danMuFrameLayout.isEnd()) {
//                Message obtain = Message.obtain();
//                obtain.obj=liveFrameLayout;
//                obtain.what=1;
//                mHandler.sendMessageDelayed(obtain,2000);
                boolean hasGift = danMuFrameLayout.setGift(getGift());
                if (hasGift) {
                    danMuFrameLayout.startAnimation(custormAnim);
                }
            }
        }
    }

    /**
     * @return
     */
    private synchronized LiveModel getGift() {
        LiveModel gift = null;
        if (mGiftQueue.size() != 0) {
            gift = mGiftQueue.get(0);
            mGiftQueue.remove(0);
        }
        return gift;
    }


    @Override
    public void dismiss(int index) {
        DanMuFrameLayout danMuFrameLayout;
        for (int i = 0; i < mGiftLayoutList.size(); i++) {
            danMuFrameLayout = mGiftLayoutList.get(i);
            if (danMuFrameLayout.getIndex() == index) {
                reStartAnimation(danMuFrameLayout, danMuFrameLayout.getIndex());
            }
        }
    }

    private void reStartAnimation(final DanMuFrameLayout danMuFrameLayout, final int index) {
        danMuFrameLayout.setCurrentShowStatus(false);
        AnimatorSet animatorSet = danMuFrameLayout.endAnmation(custormAnim);
        if (animatorSet != null) {
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    danMuFrameLayout.CurrentEndStatus(true);
                    danMuFrameLayout.setGiftViewEndVisibility(isEmpty());
                    showGift();
                }
            });
        }
    }

    public GiftControl reSetGiftLayout(boolean isHideMode, LinearLayout giftLayoutParent, @NonNull int giftLayoutNums) {
        return setGiftLayout(isHideMode, giftLayoutParent, giftLayoutNums);
    }


    /**
     * 是否为空
     *
     * @return
     */
    public synchronized boolean isEmpty() {
        if (mGiftQueue == null || mGiftQueue.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
