package com.navinfo.mapapi.map;

import android.graphics.Point;
import android.os.Bundle;

import com.navinfo.mapapi.model.LatLng;

import java.util.List;

/**
 * 线覆盖物
 */
public final class Polyline extends Overlay {

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


    protected Polyline() {
        super();
    }

    @Override
    public Bundle getExtraInfo() {
        return null;
    }

    @Override
    public int getZIndex() {
        return 0;
    }

    @Override
    public boolean isRemoved() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void remove() {

    }

    @Override
    public void setExtraInfo(Bundle extraInfo) {

    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public void setZIndex(int zIndex) {

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
     * 获取折线坐标点列表
     *
     * @return
     */
    public java.util.List<LatLng> getPoints() {
        return this.points;
    }


    /**
     * 获取折线纹理图片
     *
     * @return
     */
    public BitmapDescriptor getTexture() {
        return this.bitmapDescriptor;
    }

    /**
     * 获取折线线宽
     *
     * @return
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * 获取Polyline是否可点击
     *
     * @return
     */
    public boolean isClickable() {
        return this.clickable;
    }

    /**
     * 获取是否被选中，获得焦点
     *
     * @return
     */
    public boolean isFocus() {
        return this.focus;
    }

    /**
     * 设置Polyline是否可点击
     *
     * @param isClickable
     */
    public void setClickable(boolean isClickable) {
        this.clickable = isClickable;
    }

    /**
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * 设置是否可以被选中，获得焦点
     *
     * @param focus
     */
    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    /**
     * 设置折线坐标点列表
     *
     * @param points
     */
    public void setPoints(java.util.List<LatLng> points) {
        this.points = points;
    }

    /**
     * 设置折线纹理图片
     *
     * @param mTexture
     */
    public void setTexture(BitmapDescriptor mTexture) {
        this.bitmapDescriptor = mTexture;
    }

    /**
     * 设置折线线宽 默认为5
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
