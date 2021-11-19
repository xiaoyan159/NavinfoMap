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
import org.oscim.map.Map;

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
     *缩放按钮
     */
    private ImageView zoomInImage,zoomOutImage;

    /**
     * 定位图标位置
     */
    private COMPASS_GRAVITY compassGravity = COMPASS_GRAVITY.LEFT_TOP;

    /**
     * 偏移位置
     */
    private int offCompassX = 0, offCompassY = 0;

    /**
     * 缩放按钮位置
     */
    private Point zoomPoint = new Point(1300,1650);

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
        ViewGroup.LayoutParams layoutParams = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mapView = new MapView(context);
        addView(mapView, layoutParams);
        compassImage = new ImageView(context);
        compassImage.setImageResource(R.mipmap.compass);
        ViewGroup.LayoutParams imageParams = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(compassImage, imageParams);

        zoomInImage = new ImageView(context);
        zoomInImage.setImageResource(R.drawable.icon_map_zoom_in);
        zoomInImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                zoomIn(arg0);
            }
        });

        zoomOutImage = new ImageView(context);
        zoomOutImage.setImageResource(R.drawable.icon_map_zoom_out);
        zoomOutImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                zoomOut(arg0);
            }
        });


        map = new NavinfoMap(this);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;

        // 用于计算左边两个childView的高度
        int lHeight = 0;
        // 用于计算右边两个childView的高度，最终高度取二者之间大值
        int rHeight = 0;

        // 用于计算上边两个childView的宽度
        int tWidth = 0;
        // 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        int bWidth = 0;

        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            // 上面两个childView
            tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;

        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight, rHeight);

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
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
                        ct = getHeight() - cHeight - cParams.bottomMargin - offCompassY ;
                        break;
                }
            }else if(zoomInImage==childView){
                cl = zoomPoint.x - cParams.leftMargin - cParams.rightMargin;
                ct = zoomPoint.y - cParams.bottomMargin;
            }else if(zoomOutImage==childView){
                cl = zoomPoint.x - cParams.leftMargin - cParams.rightMargin;
                ct = zoomPoint.y - cParams.bottomMargin + 80 + 24;
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


    /**
     * @param child
     * @param params
     */
    public void addView(View child, LayoutParams params) {
        super.addView(child,params);
    }

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
     * 获取VTM-Map
     *
     * @return
     */
    public Map getVtmMap() {
        if(mapView!=null)
            return mapView.map();
        return null;
    }

    /**
     * 获取当前地图级别对应比例尺大小
     *
     * @return
     */
    public int getMapLevel() {

        if(mapView!=null&&mapView.map()!=null)
            return mapView.map().getMapPosition().getZoomLevel();

        return 0;
    }

    /**
     * @param view
     */
    public void zoomIn(View view) {
        if (view != null) {
            view.setEnabled(false);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                }
            }, 300);
        }
        map.zoomIn();
    }

    /**
     * @param view
     */
    public void zoomOut(View view) {
        if (view != null) {
            view.setEnabled(false);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                }
            }, 300);
        }
        map.zoomOut();
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
     * 设置缩放控件的位置，在 onMapLoadFinish 后生效
     *
     * @param p
     */
    public void setZoomControlsPosition(Point p) {
        this.zoomPoint = p;
    }

    /**
     * 获取缩放控件的屏幕位置
     *
     * @return
     */
    public Point getZoomControlsPosition() {

        return zoomPoint;
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
        if(show){
            ViewGroup.LayoutParams imageParams = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            addView(zoomOutImage, imageParams);
            addView(zoomInImage, imageParams);
        }else{
            removeView(zoomInImage);
            removeView(zoomOutImage);
        }
    }

}
