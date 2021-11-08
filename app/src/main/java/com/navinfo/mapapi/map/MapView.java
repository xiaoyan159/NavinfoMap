package com.navinfo.mapapi.map;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 一个显示地图的视图（View）。它负责从服务端获取地图数据。它将会捕捉屏幕触控手势事件
 */
public final class MapView extends ViewGroup {

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param context
     */
    MapView(Context context) {

    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param context
     * @param attrs
     */
    MapView(Context context, AttributeSet attrs) {

    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    MapView(Context context, AttributeSet attrs, int defStyle) {

    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param context
     * @param options
     */
    MapView(Context context, NavinfoMapOptions options) {
    }


    /**
     * 用户重载这个方法时必须调用父类的这个方法 用于MapView保存地图状态
     *
     * @param context
     * @param bundle
     */
    public void onCreate(Context context, Bundle bundle) {

    }

    /**
     * 当Activity暂停的时候调用地图暂停
     */
    public void onPause() {

    }

    /**
     * 当Activity唤醒时调用地图唤醒
     */
    public void onResume() {

    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法 用于MapView保存地图状态
     *
     * @param bundle
     */
    public void onSaveInstanceState(Bundle bundle) {

    }


    /**
     * 当Activity销毁时调用地图的销毁
     */
    public void onDestroy() {

    }

    /**
     * @param child
     * @param params
     */
    public void addView(View child, LayoutParams params) {

    }

    /**
     * 从MapView中移除一个子View
     *
     * @param view
     */
    public void removeView(View view) {

    }

    /**
     * 获取Logo位置
     *
     * @return
     */
    public LogoPosition getLogoPosition() {

        return null;
    }

    /**
     * 设置Logo位置
     *
     * @param position
     */
    public void setLogoPosition(LogoPosition position) {

    }


    /**
     * 获取地图控制器
     *
     * @return
     */
    public NavinfoMap getMap() {

        return null;
    }

    /**
     * 获取当前地图级别对应比例尺大小
     *
     * @return
     */
    public int getMapLevel() {

        return 0;
    }

    /**
     * 获取比例尺控件对应的屏幕位置
     *
     * @return
     */
    public Point getScaleControlPosition() {
        return null;
    }

    /**
     * 获取比例尺控件的高度
     *
     * @return
     */
    public int getScaleControlViewHeight() {
        return 0;
    }


    /**
     * 获取比例尺控件的宽度
     *
     * @return
     */
    public int getScaleControlViewWidth() {

        return 0;
    }


    /**
     * 设置比例尺控件的位置，在 onMapLoadFinish 后生效
     *
     * @param p
     */
    public void setScaleControlPosition(Point p) {

    }


    /**
     * 获取缩放控件的屏幕位置
     *
     * @return
     */
    public Point getZoomControlsPosition() {

        return null;
    }


    /**
     * 获取比例尺控件是否显示
     *
     * @return
     */
    public boolean isShowScaleControl() {

        return false;
    }


    /**
     * 设置MotionEvent
     *
     * @param event
     */
    public void setUpViewEventToMapView(MotionEvent event) {

    }

    /**
     * 设置缩放控件的位置，在 onMapLoadFinish 后生效
     *
     * @param p
     */
    public void setZoomControlsPosition(Point p) {

    }

    /**
     * 设置是否显示比例尺控件
     *
     * @param show
     */
    public void showScaleControl(boolean show) {

    }


    /**
     * 设置是否显示缩放控件
     * @param show
     */
    public void showZoomControls(boolean show) {

    }

}
