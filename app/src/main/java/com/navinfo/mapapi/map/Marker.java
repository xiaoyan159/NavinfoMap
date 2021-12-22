package com.navinfo.mapapi.map;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.navinfo.mapapi.model.LatLng;

import java.util.UUID;

/**
 * 定义地图 Marker 覆盖物
 */
public final class Marker extends Overlay {

    /**
     * 位移id
     */
    private String id;
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
     * 视图View
     */
    private View view;

    /**
     *
     */
    private int yOffset;

    protected Marker() {
        super();
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取 Marker 的屏幕位置
     *
     * @return
     */
    public Point getFixedPosition() {

        return this.point;
    }

    /**
     * 获取 Marker 覆盖物的图标
     *
     * @return
     */
    public BitmapDescriptor getIcon() {

        return bitmapDescriptor;
    }

    /**
     * 获取 Marker 覆盖物的ID
     *
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     * 获取 Marker 绑定的InfoWindow
     *
     * @return
     */
    public InfoWindow getInfoWindow() {
        return this.infoWindow;
    }

    /**
     * 获取 Marker 覆盖物的位置坐标
     *
     * @return
     */
    public LatLng getPosition() {

        return this.position;
    }

    /**
     * 获取 Marker 覆盖物的标题
     *
     * @return
     */
    public String getTitle() {
        return "";
    }

    /**
     * 获取 Marker 覆盖物的Y方向的偏移量
     *
     * @return
     */
    public int getYOffset() {

        return 0;
    }

    /**
     * 移除与 Marker
     * 绑定的InfoWindow
     */
    public void hideInfoWindow() {

    }


    /**
     * 获取Marker是否可点击
     *
     * @return
     */
    public boolean isClickable() {
        return clickable;
    }

    /**
     * 获取 marker 覆盖物是否可以拖拽
     *
     * @return
     */
    public boolean isDraggable() {

        return draggable;
    }

    /**
     * 获取 Marker
     * 是否跟随地图移动
     *
     * @return
     */
    public boolean isFixed() {
        return false;
    }

    /**
     * 判断是否显示InfoWindow
     *
     * @return
     */
    public boolean isInfoWindowEnabled() {
        return false;
    }

    /**
     * 设置 Marker 图标的透明度
     *
     * @param alpha
     */
    public void setAlpha(float alpha) {

    }

    /**
     * 设置Marker是否可点击
     *
     * @param isClickable
     */
    public void setClickable(boolean isClickable) {
        this.clickable = isClickable;
    }

    /**
     * 设置 marker 是否允许拖拽，默认不可拖拽
     *
     * @param draggable
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * 设置 Marker 覆盖物的图标，相同图案的 icon 的 Marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
     *
     * @param icon
     */
    public void setIcon(BitmapDescriptor icon) {
        this.bitmapDescriptor = icon;
    }

    /**
     * 设置 Marker 覆盖物的位置坐标
     *
     * @param position
     */
    public void setPosition(LatLng position) {
        this.position = position;
    }

    /**
     * 设置 Marker 覆盖物的Y方向的偏移量
     *
     * @param mYOffset
     */
    public void setYOffset(int mYOffset) {
        this.yOffset = mYOffset;
    }

    /**
     * 添加 Marker 关联的InfoWindow,两者的更新是相互独立的。
     *
     * @param mInfoWindow
     */
    public void showInfoWindow(InfoWindow mInfoWindow) {
        this.infoWindow = mInfoWindow;
    }

    /**
     * 更新与Marker绑定的InfoWindow对应的位置
     *
     * @param position
     */
    public void updateInfoWindowPosition(LatLng position) {

    }

    /**
     * 更新与Marker绑定的InfoWindow对应的View
     *
     * @param view
     */
    public void updateInfoWindowView(View view) {
        this.view = view;
    }


    /**
     * 更新与Marker绑定的InfoWindow对应的yOffset
     *
     * @param yOffset
     */
    public void updateInfoWindowYOffset(int yOffset) {
        this.yOffset = yOffset;
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
        return this.remove;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void remove() {
        this.remove = true;
    }

    @Override
    public void setExtraInfo(Bundle extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
