package com.navinfo.mapapi.map;

/**
 * 定义地图 Marker 覆盖物
 */
public final class Marker extends Overlay {

    /**
     * 资源图标
     */
    private BitmapDescriptor bitmapDescriptor;

    /**
     * 点击
     */
    private boolean clickable;

    /**
     * 拖拽
     */
    private boolean draggable;

    /**
     * 获取 Marker 的屏幕位置
     *
     * @return
     */
    Point getFixedPosition() {

        return null;
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
        return null;
    }

    /**
     * 获取 Marker 覆盖物的位置坐标
     *
     * @return
     */
    LatLng getPosition() {

        return null;
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

    }

    /**
     * 添加 Marker 关联的InfoWindow,两者的更新是相互独立的。
     *
     * @param mInfoWindow
     */
    void showInfoWindow(InfoWindow mInfoWindow) {

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

    }


    /**
     * 更新与Marker绑定的InfoWindow对应的yOffset
     *
     * @param yOffset
     */
    void updateInfoWindowYOffset(int yOffset) {

    }
}
