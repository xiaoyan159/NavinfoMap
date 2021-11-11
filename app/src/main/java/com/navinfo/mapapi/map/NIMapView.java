package com.navinfo.mapapi.map;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.navinfo.mapapi.R;

import org.oscim.android.MapView;

/**
 * 一个显示地图的视图（View）。它负责从服务端获取地图数据。它将会捕捉屏幕触控手势事件
 */
public final class NIMapView extends ViewGroup {

    public enum COMPASS_GRAVITY {
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM
    }

    /**
     * VTM地图
     */
    private MapView mapView;
    /**
     * NavinfoMap 地图对象的操作方法与接口
     */
    private NavinfoMap map;

    /**
     *定位图标
     */
    private ImageView compassImage;

    /**
     * 定位图标位置
     */
    private COMPASS_GRAVITY compassGravity = COMPASS_GRAVITY.LEFT_TOP;

    /**
     * 偏移位置
     */
    private int offCompassX = 0, offCompassY = 0;

    /**
     * 根据给定的参数构造一个NIMapView 的新对象。
     *
     * @param context
     */
    public NIMapView(Context context) {
        this(context,null);
    }

    /**
     * 根据给定的参数构造一个NIMapView 的新对象。
     *
     * @param context
     * @param attrs
     */
    public NIMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 根据给定的参数构造一个NIMapView 的新对象。
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public NIMapView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    /**
     * 根据给定的参数构造一个NIMapView 的新对象。
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public NIMapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ViewGroup.LayoutParams layoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mapView = new MapView(context);
        addView(mapView, layoutParams);
        compassImage = new ImageView(context);
        compassImage.setImageResource(R.mipmap.compass);
        ViewGroup.LayoutParams imageParams = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(compassImage, imageParams);
        map = new NavinfoMap(this);

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;
            if (compassImage == childView) {
                switch (compassGravity) {
                    case LEFT_TOP:
                        cl = cParams.leftMargin + offCompassX;
                        ct = cParams.topMargin + offCompassY;
                        break;
                    case RIGHT_TOP:
                        cl = getWidth() - cWidth - cParams.leftMargin
                                - cParams.rightMargin - offCompassX;
                        ct = cParams.topMargin + offCompassY;

                        break;
                    case LEFT_BOTTOM:
                        cl = cParams.leftMargin + offCompassX;
                        ct = getHeight() - cHeight - cParams.bottomMargin - offCompassY;
                        break;
                    case RIGHT_BOTTOM:
                        cl = getWidth() - cWidth - cParams.leftMargin
                                - cParams.rightMargin - offCompassX;
                        ct = getHeight() - cHeight - cParams.bottomMargin - offCompassY;
                        break;
                }
            } else {
                cl = cParams.leftMargin;
                ct = cParams.topMargin;
            }
            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }

    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法 用于NIMapView保存地图状态
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
        if (mapView != null) {
            mapView.onPause();
        }
    }

    /**
     * 当Activity唤醒时调用地图唤醒
     */
    public void onResume() {
        if (mapView != null) {
            mapView.onResume();
        }
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法 用于NIMapView保存地图状态
     *
     * @param bundle
     */
    public void onSaveInstanceState(Bundle bundle) {

    }


    /**
     * 当Activity销毁时调用地图的销毁
     */
    public void onDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

/*    *//**
     * @param child
     * @param params
     *//*
    public void addView(View child, LayoutParams params) {
        super.addView(child,params);
    }*/

    /**
     * 从NIMapView中移除一个子View
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

        return map;
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
    public void setUpViewEventToNIMapView(MotionEvent event) {

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
