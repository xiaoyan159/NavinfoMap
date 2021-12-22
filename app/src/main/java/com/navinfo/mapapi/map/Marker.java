package com.navinfo.mapapi.map;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.navinfo.mapapi.model.LatLng;

/**
 * 定义地图 Marker 覆盖物
 */
public final class Marker extends Overlay {

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



    /**
     * 获取 Marker 的屏幕位置
     *
     * @return
     */
    Point getFixedPosition() {

        return this.point;
    }

    /**
     * 获取 Marker 覆盖物的图标
     *
     * @return
     */
    BitmapDescriptor getIcon() {

        return bitmapDescriptor;
    }

    /**
     * 获取 Marker 覆盖物的ID
     *
     * @return
     */
    String getId() {
        return "";
    }

    /**
     * 获取 Marker 绑定的InfoWindow
     *
     * @return
     */
    InfoWindow getInfoWindow() {
        return this.infoWindow;
    }

    /**
     * 获取 Marker 覆盖物的位置坐标
     *
     * @return
     */
    LatLng getPosition() {

        return this.position;
    }

    /**
     * 获取 Marker 覆盖物的标题
     *
     * @return
     */
    String getTitle() {
        return "";
    }

    /**
     * 获取 Marker 覆盖物的Y方向的偏移量
     *
     * @return
     */
    int getYOffset() {

        return 0;
    }

    /**
     * 移除与 Marker
     * 绑定的InfoWindow
     */
    void hideInfoWindow() {

    }


    /**
     * 获取Marker是否可点击
     *
     * @return
     */
    boolean isClickable() {
        return clickable;
    }

    /**
     * 获取 marker 覆盖物是否可以拖拽
     *
     * @return
     */
    boolean isDraggable() {

        return draggable;
    }

    /**
     * 获取 Marker
     * 是否跟随地图移动
     *
     * @return
     */
    boolean isFixed() {
        return false;
    }

    /**
     * 判断是否显示InfoWindow
     *
     * @return
     */
    boolean isInfoWindowEnabled() {
        return false;
    }

    /**
     * 设置 Marker 图标的透明度
     *
     * @param alpha
     */
    void setAlpha(float alpha) {

    }

    /**
     * 设置Marker是否可点击
     *
     * @param isClickable
     */
    void setClickable(boolean isClickable) {
        this.clickable = isClickable;
    }

    /**
     * 设置 marker 是否允许拖拽，默认不可拖拽
     *
     * @param draggable
     */
    void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * 设置 Marker 覆盖物的图标，相同图案的 icon 的 Marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
     *
     * @param icon
     */
    void setIcon(BitmapDescriptor icon) {
        this.bitmapDescriptor = icon;
    }

    /**
     * 设置 Marker 覆盖物的位置坐标
     *
     * @param position
     */
    void setPosition(LatLng position) {
        this.position = position;
    }

    /**
     * 设置 Marker 覆盖物的Y方向的偏移量
     *
     * @param mYOffset
     */
    void setYOffset(int mYOffset) {
        this.yOffset = mYOffset;
    }

    /**
     * 添加 Marker 关联的InfoWindow,两者的更新是相互独立的。
     *
     * @param mInfoWindow
     */
    void showInfoWindow(InfoWindow mInfoWindow) {
        this.infoWindow = mInfoWindow;
    }

    /**
     * 更新与Marker绑定的InfoWindow对应的位置
     *
     * @param position
     */
    void updateInfoWindowPosition(LatLng position) {

    }

    /**
     * 更新与Marker绑定的InfoWindow对应的View
     *
     * @param view
     */
    void updateInfoWindowView(View view) {
        this.view = view;
    }


    /**
     * 更新与Marker绑定的InfoWindow对应的yOffset
     *
     * @param yOffset
     */
    void updateInfoWindowYOffset(int yOffset) {
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
}
