package com.navinfo.mapapi.map;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.navinfo.mapapi.model.LatLng;

import java.util.List;

/**
 * 创建多边形覆盖物选项类
 */
public final class PolygonOptions extends OverlayOptions {

    /**
     * 设置多边形额外信息
     */
    private Bundle extraInfo;

    /**
     * 设置多边形填充颜色
     */
    private int color;

    /**
     * 获取多边形坐标点列表
     */
    private List<LatLng> points;

    /**
     * 获取多边形 zIndex
     */
    private int zIndex;

    /**
     * 获取多边形可见性
     */
    private boolean visable;

    public PolygonOptions() {
        super();
    }

    /**
     * 设置多边形额外信息
     *
     * @param extraInfo
     * @return
     */
    public PolygonOptions extraInfo(Bundle extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    /**
     * 设置多边形填充颜色
     *
     * @param color
     * @return
     */
    public PolygonOptions fillColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * 获取多边形额外信息
     *
     * @return
     */
    public Bundle getExtraInfo() {
        return this.extraInfo;
    }

    /**
     * 获取多边形填充颜色
     *
     * @return
     */
    public int getFillColor() {
        return this.color;
    }

    /**
     * 获取多边形坐标点列表
     *
     * @return
     */
    public java.util.List<LatLng> getPoints() {
        return this.points;
    }

    /**
     * 获取多边形 zIndex
     *
     * @return
     */
    public int getZIndex() {
        return this.zIndex;
    }

    /**
     * 获取多边形可见性
     *
     * @return
     */
    public boolean isVisible() {
        return this.visable;
    }

    /**
     * 设置多边形坐标点列表
     *
     * @param points
     * @return
     */
    public PolygonOptions points(java.util.List<LatLng> points) {
        this.points = points;
        return this;
    }

    /**
     * 设置多边形可见性
     *
     * @param visible
     * @return
     */
    public PolygonOptions visible(boolean visible) {
        this.visable = visible;
        return this;
    }

    /**
     * 设置多边形 zIndex
     *
     * @param zIndex
     * @return
     */
    public PolygonOptions zIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
