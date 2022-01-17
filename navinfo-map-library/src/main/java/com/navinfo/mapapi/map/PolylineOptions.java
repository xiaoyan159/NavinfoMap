package com.navinfo.mapapi.map;

import android.graphics.Point;
import android.os.Bundle;

import com.navinfo.mapapi.model.LatLng;

import java.util.List;

/**
 * 创建折线覆盖物选项类
 */
public final class PolylineOptions extends OverlayOptions {

    /**
     * 点击
     */
    private boolean clickable;

    /**
     * 折线颜色
     */
    private int color;

    /**
     * 设置自定义纹理
     */
    private BitmapDescriptor bitmapDescriptor;


    /**
     * marker 覆盖物的额外信息
     */
    private Bundle extraInfo;

    /**
     *
     */
    private boolean focus;

    /**
     * Marker 覆盖物屏幕位置点
     */
    private Point point;

    /**
     * marker 覆盖物的 zIndex
     */
    private int zIndex;

    /**
     * 可见性
     */
    private boolean visible;

    /**
     * 折线坐标点列表
     */
    private List<LatLng> points;

    /**
     * 折线线宽
     */
    private int width;


    public PolylineOptions() {
        super();
    }

    /**
     * 设置Marker是否可点击
     *
     * @param isClickable
     * @return
     */
    public PolylineOptions clickable(boolean isClickable) {
        this.clickable = isClickable;
        return this;
    }

    /**
     * 设置折线颜色
     *
     * @param color
     * @return
     */
    public PolylineOptions color(int color) {
        this.color = color;
        return this;
    }

    /**
     * 设置自定义纹理
     *
     * @param customTexture
     * @return
     */
    public PolylineOptions customTexture(BitmapDescriptor customTexture) {
        this.bitmapDescriptor = customTexture;
        return this;
    }


    /**
     * 设置 marker 覆盖物的额外信息
     *
     * @param extraInfo
     * @return
     */
    public PolylineOptions extraInfo(Bundle extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    /**
     * @param focus
     * @return
     */
    public PolylineOptions focus(boolean focus) {
        this.extraInfo = extraInfo;
        return this;
    }

    /**
     * 获取折线颜色
     *
     * @return
     */
    public int getColor() {
        return this.color;
    }

    /**
     * 获取marker覆盖物的额外信息
     *
     * @return
     */
    Bundle getExtraInfo() {
        return this.extraInfo;
    }

    /**
     * 获取 获取自定义纹理对象
     *
     * @return
     */
    BitmapDescriptor getCustomTexture() {
        return this.bitmapDescriptor;
    }


    /**
     * 获取 marker 覆盖物的 zIndex
     *
     * @return
     */
    int getZIndex() {

        return zIndex;
    }

    /**
     * 获取折线坐标点列表
     *
     * @return
     */
    public java.util.List<LatLng> getPoints() {
        return this.points;
    }


    /**
     * 获取折线线宽 需要注意的是：Polyline的宽度适配地图当前缩放级别下的像素与地理范围的对应关系
     *
     * @return
     */
    public int getWidth() {

        return this.width;
    }

    /**
     * @return
     */
    public boolean isFocus() {
        return this.focus;
    }


    /**
     * 获取 marker 覆盖物的可见性
     *
     * @return
     */
    boolean isVisible() {

        return this.visible;
    }

    /**
     * 是否点击
     * @return
     */
    public boolean isClickable() {
        return this.clickable;
    }

    /**
     *设置折线坐标点列表
     * @param points
     * @return
     */
    public PolylineOptions points(java.util.List<LatLng> points) {
        this.points = points;
        return this;
    }

    /**
     * 设置折线线宽， 默认为 5
     * @param width
     * @return
     */
    public PolylineOptions width(int width) {
        this.width = width;
        return this;
    }


    /**
     * 设置 marker 覆盖物的可见性
     *
     * @param visible
     * @return
     */
    public PolylineOptions visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    /**
     * 设置 marker 覆盖物的 zIndex
     *
     * @param zIndex
     * @return
     */
    public PolylineOptions zIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }
}
