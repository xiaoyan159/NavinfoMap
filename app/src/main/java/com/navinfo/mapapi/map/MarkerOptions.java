package com.navinfo.mapapi.map;

/**
 *
 */
public final class MarkerOptions extends OverlayOptions {

    /**
     * 层级
     */
    private int zIndex;

    /**
     * 设置Marker是否可点击
     *
     * @param isClickable
     * @return
     */
    MarkerOptions clickable(boolean isClickable) {
        return null;

    }

    /**
     * 设置 marker 是否允许拖拽，默认不可拖拽
     *
     * @param draggable
     * @return
     */
    MarkerOptions draggable(boolean draggable) {
        return null;

    }

    /**
     * 设置 marker 覆盖物的额外信息
     *
     * @param extraInfo
     * @return
     */
    MarkerOptions extraInfo(Bundle extraInfo) {
        return null;

    }

    /**
     * 设置 Marker覆盖物屏幕位置点
     *
     * @param point
     * @return
     */
    MarkerOptions fixedScreenPosition(Point point) {
        return null;

    }


    /**
     * 获取marker覆盖物的额外信息
     *
     * @return
     */
    Bundle getExtraInfo() {
        return null;

    }

    /**
     * 获取 marker 覆盖物的位置坐标
     *
     * @return
     */
    LatLng getPosition() {

        return null;
    }

    /**
     * 获取 Marker 覆盖物的图标
     *
     * @return
     */
    BitmapDescriptor getIcon() {
        return null;

    }

    /**
     * 获取 marker 覆盖物的位置坐标
     *
     * @return
     */
    LatLng getPosition() {
        return null;

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
    MarkerOptions icon(BitmapDescriptor icon) {

        return null;
    }

    /**
     * 设置 Marker 绑定的InfoWindow
     *
     * @param infoWindow
     * @return
     */
    MarkerOptions infoWindow(InfoWindow infoWindow) {

        return null;
    }

    /**
     * 获取 marker 覆盖物是否可以拖拽
     *
     * @return
     */
    boolean isDraggable() {

    }

    /**
     * 获取 marker 覆盖物的可见性
     *
     * @return
     */
    boolean isVisible() {

    }

    /**
     * 设置 marker 覆盖物的位置坐标
     *
     * @param position
     * @return
     */
    MarkerOptions position(LatLng position) {

        return null;
    }

    /**
     * 设置 marker 覆盖物的可见性
     *
     * @param visible
     * @return
     */
    MarkerOptions visible(boolean visible) {

        return null;
    }

    /**
     * 设置 marker 覆盖物的 zIndex
     *
     * @param zIndex
     * @return
     */
    MarkerOptions zIndex(int zIndex) {

        return null;
    }

}
