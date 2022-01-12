package com.navinfo.mapapi.map;

import android.graphics.Point;
import android.os.Bundle;

import com.navinfo.mapapi.model.LatLng;

/**
 *
 */
public final class MarkerOptions extends OverlayOptions {

    /**
     * 点击
     */
    private boolean clickable;

    /**
     * marker 是否允许拖拽，默认不可拖拽
     */
    private boolean draggable;

    /**
     * marker 覆盖物的额外信息
     */
    private Bundle extraInfo;

    /**
     * Marker 覆盖物屏幕位置点
     */
    private Point point;

    /**
     * marker 覆盖物的 zIndex
     */
    private int zIndex;

    /**
     * Marker 覆盖物的图标
     */
    private BitmapDescriptor bitmapDescriptor;

    /**
     * Marker 绑定的InfoWindow
     */
    private InfoWindow infoWindow;

    /**
     * 覆盖物的位置坐标
     */
    private LatLng position;

    /**
     * 可见性
     */
    private boolean visible;

    public MarkerOptions() {
        super();
    }

    /**
     * 设置Marker是否可点击
     *
     * @param isClickable
     * @return
     */
    public MarkerOptions  clickable(boolean isClickable) {
        this.clickable = isClickable;
        return this;
    }

    /**
     * 设置 marker 是否允许拖拽，默认不可拖拽
     *
     * @param draggable
     * @return
     */
    public MarkerOptions draggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    /**
     * 设置 marker 覆盖物的额外信息
     *
     * @param extraInfo
     * @return
     */
    public MarkerOptions extraInfo(Bundle extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    /**
     * 设置 Marker覆盖物屏幕位置点
     *
     * @param point
     * @return
     */
    public MarkerOptions fixedScreenPosition(Point point) {
        this.point = point;
        return this;
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
     * 获取 marker 覆盖物的位置坐标
     *
     * @return
     */
    LatLng getPosition() {

        return this.position;
    }

    /**
     * 获取 Marker 覆盖物的图标
     *
     * @return
     */
    BitmapDescriptor getIcon() {
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
     * 设置 Marker 覆盖物的图标，相同图案的 icon 的 marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
     *
     * @param icon
     * @return
     */
    public MarkerOptions icon(BitmapDescriptor icon) {
        this.bitmapDescriptor = icon;
        return this;
    }

    /**
     * 设置 Marker 绑定的InfoWindow
     *
     * @param infoWindow
     * @return
     */
    public MarkerOptions infoWindow(InfoWindow infoWindow) {
        this.infoWindow = infoWindow;
        return this;
    }

    /**
     * 获取 marker 覆盖物是否可以拖拽
     *
     * @return
     */
    boolean isDraggable() {

        return this.draggable;
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
     * 设置 marker 覆盖物的位置坐标
     *
     * @param position
     * @return
     */
    public MarkerOptions position(LatLng position) {
        this.position = position;
        return this;
    }

    /**
     * 设置 marker 覆盖物的可见性
     *
     * @param visible
     * @return
     */
    public MarkerOptions visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    /**
     * 设置 marker 覆盖物的 zIndex
     *
     * @param zIndex
     * @return
     */
    public MarkerOptions zIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }
}
