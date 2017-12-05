package com.example.pc.danmuview.DanMuView.DanMuAnimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pc.danmuview.R;
import com.example.pc.danmuview.DanMuView.Interface.ICustormAnim;
import com.example.pc.danmuview.DanMuView.util.DanMuAnimationUtil;


/**
 * Created by ybb on 2017/9/12.
 */
public class DanMuFrameLayout extends FrameLayout implements Handler.Callback {
    private LayoutInflater mInflater;
    private Context mContext;
    private Handler mHandler = new Handler(this);//控制动画消失
    /**
     * 展示时间
     */
    public static final int GIFT_DISMISS_TIME = 0;
    /**
     * 当前动画runnable
     */
    private Runnable mCurrentAnimRunnable;

//    LinearLayout anim_rl;
    ImageView anim_gift;
    TextView anim_nickname;
    private LiveModel mGift;
    /**
     * item 显示位置
     */
    private int mIndex = 1;
    /**
     * 动画正在显示，
     */
    private boolean isShowing = false;
    /**
     * 动画结束
     */
    private boolean isEnd = true;
    /**
     * 自定义动画的接口
     */
    private ICustormAnim anim;
    /**
     * 是否开启礼物动画隐藏模式（如果两个礼物动画同时显示，并且第一个优先结束，第二个礼物的位置会移动到第一个位置上去）
     */
    private boolean isHideMode = false;

    private LeftGiftAnimationStatusListener mGiftAnimationListener;
    private View rootView;

    public DanMuFrameLayout(Context context) {
        this(context, null);
    }

    public DanMuFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        initView();
    }

    private void initView() {
        rootView = mInflater.inflate(R.layout.item_gift, null);
//        anim_rl = (LinearLayout) rootView.findViewById(R.id.infoRl);
//        anim_gift = (ImageView) rootView.findViewById(R.id.giftIv);
        anim_nickname = (TextView) rootView.findViewById(R.id.nickNameTv);
//        anim_sign = (TextView) rootView.findViewById(R.id.infoTv);
        this.addView(rootView);
    }

//

    public void firstHideLayout() {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(DanMuFrameLayout.this, alpha);
        animator.setStartDelay(0);
        animator.setDuration(0);
        animator.start();
    }

    public void setHideMode(boolean isHideMode) {
        this.isHideMode = isHideMode;
    }

    public void hideView() {
        rootView.setVisibility(INVISIBLE);
    }

    public void setGiftViewEndVisibility(boolean hasGift) {

        if (isHideMode && hasGift) {
            DanMuFrameLayout.this.setVisibility(View.GONE);
        } else {
            DanMuFrameLayout.this.setVisibility(View.INVISIBLE);
        }
    }

    public boolean setGift(LiveModel gift) {
        if (gift == null) {
            return false;
        }
        mGift = gift;


        if (!TextUtils.isEmpty(gift.getSendUserName())) {
            anim_nickname.setText(gift.getSendUserName());
        }
        return true;
    }

    public LiveModel getGift() {
        return mGift;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return true;
    }

    /**
     * 关闭此Item Layout,并通知外部隐藏自身(供内部调用)
     */
    private void dismissGiftLayout() {
        removeDismissGiftCallback();
        if (mGiftAnimationListener != null) {
            mGiftAnimationListener.dismiss(mIndex);
        }
    }

    private void removeDismissGiftCallback() {
        if (mCurrentAnimRunnable != null) {
            mHandler.removeCallbacks(mCurrentAnimRunnable);
            mCurrentAnimRunnable = null;
        }
    }

    private class GiftNumAnimaRunnable implements Runnable {

        @Override
        public void run() {
            dismissGiftLayout();
        }
    }

    /**
     * 设置item显示位置
     *
     * @param mIndex
     */
    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    /**
     * 获取ite显示位置
     *
     * @return
     */
    public int getIndex() {
        return mIndex;
    }

    public interface LeftGiftAnimationStatusListener {
        void dismiss(int index);
    }

    public void setGiftAnimationListener(LeftGiftAnimationStatusListener giftAnimationListener) {
        this.mGiftAnimationListener = giftAnimationListener;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setCurrentShowStatus(boolean status) {
        isShowing = status;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void CurrentEndStatus(boolean isEnd) {
        this.isEnd = isEnd;
    }

    /**
     * 获取发送人id
     *
     * @return
     */
    public String getCurrentSendUserId() {
        if (mGift != null) {
            return mGift.getSendUserId();
        }
        return null;
    }

    public void clearHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;//这里要置位null，否则当前页面销毁时，正在执行的礼物动画会造成内存泄漏
        }

        mGiftAnimationListener = null;
        resetGift();
    }

    public void resetGift() {
        mCurrentAnimRunnable = null;
        mGift = null;
        mIndex = -1;
        isShowing = false;
        isEnd = true;
        isHideMode = false;
    }

    /**
     * 动画开始时回调，使用方法借鉴{@link #startAnimation}
     */
    public void initLayoutState() {
        this.setVisibility(View.VISIBLE);
        this.setAlpha(1f);
        isShowing = true;
        isEnd = false;
    }

    /**
     * 连击结束时回调
     */
    public void comboEndAnim() {
        if (mHandler != null) {
            mCurrentAnimRunnable = new GiftNumAnimaRunnable();
            mHandler.postDelayed(mCurrentAnimRunnable, GIFT_DISMISS_TIME);
        }
    }

    public AnimatorSet startAnimation(ICustormAnim anim) {
        this.anim = anim;
        if (anim == null) {
            hideView();
            //布局飞入
            ObjectAnimator flyFromLtoR = DanMuAnimationUtil.createFlyFromLtoR(rootView, -getWidth(), 0, 400, new OvershootInterpolator());
            flyFromLtoR.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    initLayoutState();
                }
            });

            //礼物飞入
            ObjectAnimator flyFromLtoR2 = DanMuAnimationUtil.createFlyFromLtoR(anim_gift, -getWidth(), 0, 400, new DecelerateInterpolator());
            flyFromLtoR2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    anim_gift.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    comboAnimation();
                }
            });
            AnimatorSet animatorSet = DanMuAnimationUtil.startAnimation(flyFromLtoR, flyFromLtoR2);

            return animatorSet;
        } else {
            return anim.startAnim(this, rootView.getRootView());
        }
    }

    /**
     * 向上消失的动画
     */

    public void comboAnimation() {
        if (anim != null) {
            anim.comboAnim(this, rootView);
        }
    }

    public AnimatorSet endAnmation(ICustormAnim anim) {
        if (anim == null) {
            //向上渐变消失
            ObjectAnimator fadeAnimator = DanMuAnimationUtil.createFadeAnimator(DanMuFrameLayout.this, 0, -100, 500, 0);
            fadeAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                }
            });
            // 复原
            ObjectAnimator fadeAnimator2 = DanMuAnimationUtil.createFadeAnimator(DanMuFrameLayout.this, 100, 0, 0, 0);
            AnimatorSet animatorSet = DanMuAnimationUtil.startAnimation(fadeAnimator, fadeAnimator2);
            return animatorSet;
        } else {
            return anim.endAnim(this, rootView);
        }
    }
}
