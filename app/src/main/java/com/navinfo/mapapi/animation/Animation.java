package com.navinfo.mapapi.animation;

import android.graphics.Interpolator;

/**
 * Marker 动画接口类 适用于
 */
public abstract class Animation extends java.lang.Object {

    /**
     * 取消 Marker 动画
     */
    abstract void cancel();

    /**
     * 设置 Marker 动画监听
     *
     * @param animationListener
     */
    abstract void setAnimationListener(Animation.AnimationListener animationListener);

    /**
     * 设置 Marker 动画执行时间
     *
     * @param duration
     */
    abstract void setDuration(long duration);

    /**
     * 设置 Marker 动画插值器
     * @param interpolator
     */
    abstract void setInterpolator(Interpolator interpolator);


    /**
     * Marker 动画监听接口
     */
    public static interface AnimationListener {
        void onAnimationCancel();

        void onAnimationEnd();

        void onAnimationRepeat();

        void onAnimationStart();
    }


    /**
     * Marker 动画重复模式枚举类
     */
    public static enum RepeatMode {
        RESTART,
        REVERSE
    }
}
