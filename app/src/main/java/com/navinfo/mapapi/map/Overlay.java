package com.navinfo.mapapi.map;

import android.graphics.Point;
import android.os.Bundle;

/**
 * 地图覆盖物基类
 */
public abstract class Overlay extends java.lang.Object {

    /**
     * 点击
     */
    public boolean clickable;

    /**
     * marker 是否允许拖拽，默认不可拖拽
     */
    public boolean draggable;

    /**
     * marker 覆盖物的额外信息
     */
    public Bundle extraInfo;

    /**
     * Marker 覆盖物屏幕位置点
     */
    public Point point;

    /**
     * marker 覆盖物的 zIndex
     */
    public int zIndex;

    protected Overlay() {
    }

    /**
     * 获取覆盖物额外信息
     *
     * @return
     */
    public abstract Bundle getExtraInfo();

    /**
     * 获取覆盖物 zIndex
     *
     * @return
     */
    public abstract int getZIndex();

    /**
     * 获取overlay是否被移除的状态
     *
     * @return
     */
    public abstract boolean isRemoved();

    /**
     * 设置覆盖物可见性
     *
     * @return
     */
    public abstract boolean isVisible();


    /**
     * 删除该覆盖物
     */
    public abstract void remove();


    /**
     * 设置覆盖物额外信息
     *
     * @param extraInfo
     */
    public abstract void setExtraInfo(Bundle extraInfo);


    /**
     * 获取覆盖物可见性
     *
     * @param visible
     */
    public abstract void setVisible(boolean visible);


    /**
     * 设置覆盖物 zIndex
     *
     * @param zIndex
     */
    public abstract void setZIndex(int zIndex);

}
