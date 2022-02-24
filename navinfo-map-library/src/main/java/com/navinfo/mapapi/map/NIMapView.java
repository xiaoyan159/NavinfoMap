package com.navinfo.mapapi.map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.navinfo.mapapi.MapManager;
import com.navinfo.mapapi.R;
import com.navinfo.mapapi.animation.RotateAnimation;
import com.navinfo.mapapi.map.source.NavinfoGeoJsonTileSource;
import com.navinfo.mapapi.model.LatLng;

import org.oscim.android.MapView;
import org.oscim.core.GeoPoint;
import org.oscim.event.Gesture;
import org.oscim.event.GestureListener;
import org.oscim.layers.Layer;
import org.oscim.layers.TileGridLayer;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Map;
import org.oscim.core.MapPosition;
import org.oscim.event.Event;
import org.oscim.renderer.GLViewport;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.UrlTileSource;

import java.io.File;
import java.util.HashMap;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * 一个显示地图的视图（View）。它负责从服务端获取地图数据。它将会捕捉屏幕触控手势事件
 */
public final class NIMapView extends RelativeLayout {
    /**
     * VTM地图
     */
    private MapView mapView;
    /**
     * NavinfoMap 地图对象的操作方法与接口
     */
    private NavinfoMap map;

    /**
     * 定位图标
     */
    protected ImageView compassImage;

    /**
     * logo图标
     */
    protected ImageView logoImage;

    /**
     * 图片旋转
     */
    private RotateAnimation mRotateAnimation;

    /**
     * 之前的旋转角度
     */
    private float mLastRotateZ = 0;

    /**
     * 缩放按钮
     */
    private ImageView zoomInImage, zoomOutImage;
    private View zoomLayout;

    /**
     * 地图的单击事件监听
     */
    private NavinfoMap.OnMapClickListener mapClickListener;

    /**
     * 地图的双击事件监听
     */
    private NavinfoMap.OnMapDoubleClickListener mapDoubleClickListener;
    /**
     * 地图的长按事件监听
     */
    private NavinfoMap.OnMapLongClickListener mapLongClickListener;

    /**
     * 地图的触摸事件
     */
    private NavinfoMap.OnMapTouchListener touchListener;
    /**
     * 地图图层管理器
     * */
    private NavinfoLayerManager mLayerManager;
    private Layer baseRasterLayer, defaultVectorTileLayer, defaultVectorLabelLayer, gridLayer;

    public NIMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NIMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public NIMapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.base_map_layout, this, true);

        mapView = rootView.findViewById(R.id.nimapview);
        map = new NavinfoMap(this);
        compassImage = rootView.findViewById(R.id.navinfo_map_compass);
        mLayerManager = new NavinfoLayerManager(getVtmMap());

        initMapGroup(); // 初始化图层组
        logoImage = rootView.findViewById(R.id.navinfo_map_logo);
        mRotateAnimation = new RotateAnimation(compassImage);
        getVtmMap().events.bind(new Map.UpdateListener() {
            @Override
            public void onMapEvent(Event e, MapPosition mapPosition) {
                //旋转
                if (mLastRotateZ != mapPosition.bearing) {
                    mRotateAnimation.startRotationZ(mLastRotateZ, mapPosition.bearing);
                    mLastRotateZ = mapPosition.bearing;
                }

                //增加控制联动效果
                if (map != null && map.isEnableCompassImage()) {
                    //2D,正北隐藏
                    if (compassImage.getVisibility() != View.VISIBLE && (mapPosition.tilt != 0 || mapPosition.bearing != 0)) {
                        compassImage.setVisibility(View.VISIBLE);
                    } else if (compassImage.getVisibility() == View.VISIBLE && mapPosition.tilt == 0 && mapPosition.bearing == 0) {
                        compassImage.clearAnimation();
                        compassImage.setVisibility(View.GONE);
                    }
                } else {
                    compassImage.clearAnimation();
                    compassImage.setVisibility(View.GONE);
                }
            }
        });


        // 增加事件图层
        NIMapView.MapEventsReceiver mapEventsReceiver = new NIMapView.MapEventsReceiver(getVtmMap());
        getVtmMap().layers().add(mapEventsReceiver, LAYER_GROUPS.OPERATE.groupIndex);

        // 增加比例尺图层
        NaviMapScaleBar naviMapScaleBar = new NaviMapScaleBar(getVtmMap());
        naviMapScaleBar.initScaleBarLayer(GLViewport.Position.BOTTOM_LEFT, 25, 60);

        if (gridLayer == null) {
            gridLayer = new TileGridLayer(getVtmMap());
            getVtmMap().layers().add(gridLayer, LAYER_GROUPS.ALLWAYS_SHOW_GROUP.groupIndex);
            gridLayer.setEnabled(false);
        }

        mapView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touchListener != null) {
                    touchListener.onTouch(event);
                }
                return false;
            }
        });

        compassImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPosition mapPosition = getVtmMap().getMapPosition();
                mapPosition.setBearing(0);
                mapPosition.setTilt(0);
                getVtmMap().animator().animateTo(300, mapPosition);
            }
        });

        zoomInImage = rootView.findViewById(R.id.navinfo_map_zoom_in);
        zoomInImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                zoomIn(arg0);
            }
        });

        zoomOutImage = rootView.findViewById(R.id.navinfo_map_zoom_out);
        zoomOutImage.setImageResource(R.drawable.icon_map_zoom_out);
        zoomOutImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                zoomOut(arg0);
            }
        });

        zoomLayout = rootView.findViewById(R.id.navinfo_map_zoom_layer);

        initMap();
    }

    /**
     * 初始化地图
     * */
    private void initMap(){
        switchBaseMapType(BASE_MAP_TYPE.OPEN_STREET_MAP);
        switchTileVectorLayerTheme(MAP_THEME.DEFAULT);
    }

    private void initMapGroup() {
        for (LAYER_GROUPS groups: LAYER_GROUPS.values()) {
            getVtmMap().layers().addGroup(groups.groupIndex);
        }
    }


    /**
     * 切换基础底图样式
     * */
    public void switchBaseMapType(BASE_MAP_TYPE type) {
        if (baseRasterLayer!=null) {
            getVtmMap().layers().remove(baseRasterLayer);
            baseRasterLayer = null;
            getVtmMap().updateMap();
        }
        baseRasterLayer = mLayerManager.getRasterTileLayer(type.url, type.tilePath, true);
        getVtmMap().layers().add(baseRasterLayer, LAYER_GROUPS.BASE_RASTER.groupIndex);
        getVtmMap().clearMap();
    }

    public void addDefaultVectorTileLayer(MAP_THEME theme) {
        if (defaultVectorTileLayer!=null) {
            getVtmMap().layers().remove(defaultVectorTileLayer);
            defaultVectorTileLayer = null;
            getVtmMap().updateMap();
        }
        defaultVectorTileLayer = mLayerManager.getDefaultVectorLayer(true);
        getVtmMap().layers().add(defaultVectorTileLayer, LAYER_GROUPS.VECTOR_TILE.groupIndex);
        defaultVectorLabelLayer = new LabelLayer(getVtmMap(), (VectorTileLayer) defaultVectorTileLayer);
        getVtmMap().layers().add(defaultVectorLabelLayer, LAYER_GROUPS.VECTOR_TILE.groupIndex);
        if (theme!=null) {
            switchTileVectorLayerTheme(theme);
        }
        getVtmMap().updateMap();
    }

    public void setBaseRasterVisiable(boolean visiable) {
        if (baseRasterLayer!=null) {
            baseRasterLayer.setEnabled(visiable);
            getVtmMap().updateMap();
        }
    }

    /**
     * 基础
     * */
    public enum BASE_MAP_TYPE {
        OPEN_STREET_MAP("http://a.tile.openstreetmap.org", "/{Z}}/{X}/{Y}.png"), // openStreetMap底图
        CYCLE_MAP("http://c.tile.opencyclemap.org/cycle", "/{Z}}/{X}/{Y}.png"), // cyclemap底图
        TRANSPORT_MAP("http://b.tile2.opencyclemap.org/transport", "/{Z}}/{X}/{Y}.png"); // TransportMap底图
        String url;
        String tilePath;

        BASE_MAP_TYPE(String url, String tilePath) {
            this.url = url;
            this.tilePath = tilePath;
        }
    }

    /**
     * 网格图层是否显示
     * */
    public void setGridLayerVisiable(boolean visiable) {
        if (gridLayer!=null) {
            gridLayer.setEnabled(visiable);
            getVtmMap().updateMap();
        }
    }

    /**
     * 图层组定义
     *
     * */
    public enum LAYER_GROUPS {
        BASE_RASTER(0)/*栅格底图*/, BASE_VECTOR(1)/*矢量底图*/,
        RASTER_TILE(2)/*栅格网格*/, VECTOR_TILE(3)/*矢量网格*/,
        VECTOR(4)/*矢量图层组*/, OTHER(5)/*其他图层*/,
        OPERATE(7)/*操作图层组*/,ALLWAYS_SHOW_GROUP(6);
        int groupIndex;

        LAYER_GROUPS(int groupIndex) {
            this.groupIndex = groupIndex;
        }

        public int getGroupIndex() {
            return groupIndex;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public enum GRAVITY {
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM
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
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

    /**
     * 从NIMapView中移除一个子View
     *
     * @param view
     */
    public void removeView(View view) {
        super.removeView(view);
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
    protected Map getVtmMap() {
        if (mapView != null)
            return mapView.map();
        return null;
    }

    /**
     * 获取当前地图级别对应比例尺大小
     *
     * @return
     */
    public int getMapLevel() {

        if (mapView != null && mapView.map() != null)
            return mapView.map().getMapPosition().getZoomLevel();

        return 0;
    }

    /**
     * @param view
     */
    public void zoomIn(View view) {
        if (view != null) {
            if (view.isEnabled()) {
                map.zoomIn(true);
            }
            view.setEnabled(false);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                }
            }, 300);
        }
    }

    /**
     * @param view
     */
    public void zoomOut(View view) {
        if (view != null) {
            if (view.isEnabled()) {
                map.zoomOut(true);
            }
            view.setEnabled(false);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setEnabled(true);
                }
            }, 300);
        }
    }

    /**
     * 设置地图的点击事件
     */
    public void setOnMapClickListener(NavinfoMap.OnMapClickListener listener) {
        this.mapClickListener = listener;
    }

    /**
     * 设置地图的双击事件
     * 注：默认情况下，双击会自动放大地图
     */
    public void setOnMapDoubleClickListener(NavinfoMap.OnMapDoubleClickListener listener) {
        this.mapDoubleClickListener = listener;
    }

    /**
     * 设置地图长按事件
     *
     * @param listener
     */
    public void setOnMapLongClickListener(NavinfoMap.OnMapLongClickListener listener) {
        this.mapLongClickListener = listener;
    }

    /**
     * 设置地图的触摸事件
     *
     * @param listener
     */
    public void setOnMapTouchListener(NavinfoMap.OnMapTouchListener listener) {
        this.touchListener = listener;
    }

    /**
     * 设置MotionEvent
     *
     * @param event
     */
    public void setUpViewEventToNIMapView(MotionEvent event) {

    }

    /**
     * 设置缩放按钮位置
     * @param position 按钮位置
     * */
    public void setZoomControlPosition(GRAVITY position) {
        if (zoomLayout!=null) {
            setLayoutParams(position, zoomLayout);
        }
    }

    /**
     * 设置缩放按钮位置
     * @param position 按钮位置
     * */
    public void setLogoPosition(GRAVITY position) {
        if (logoImage!=null) {
            setLayoutParams(position, logoImage);
        }
    }

    /**
     * 设置缩放按钮位置
     * @param position 按钮位置
     * */
    public void setCompassPosition(GRAVITY position) {
        if (compassImage!=null) {
            setLayoutParams(position, compassImage);
        }
    }


    /**
     * 根据GRAVITY生成对应的layoutParams
     * @param position 按钮相对于父布局的位置
     * @param view 被设置显示位置的view
     * */
    private void setLayoutParams(GRAVITY position, View view) {
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (layoutParams.getRules()!=null) {
            for (int i=0; i<layoutParams.getRules().length; i++) {
                layoutParams.removeRule(i);
            }
        }
        switch (position) {
            case LEFT_TOP:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case RIGHT_TOP:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case LEFT_BOTTOM:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case RIGHT_BOTTOM:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 设置是否显示缩放控件
     *
     * @param show
     */
    public void showZoomControls(boolean show) {
        if (zoomLayout!=null) {
            if (show) {
                zoomLayout.setVisibility(VISIBLE);
            } else {
                zoomLayout.setVisibility(GONE);
            }
        }
    }

    /**
     * 设置是否显示缩放控件
     *
     * @param show
     */
    public void showCompass(boolean show) {
        if (map!=null) {
            map.setCompassEnable(show);
        }
    }

    /**
     * 获取指北针
     */
    protected ImageView getCompassImage() {
        return compassImage;
    }


    // 地图点击事件对应的图层
    private class MapEventsReceiver extends Layer implements GestureListener {

        public MapEventsReceiver(Map map) {
            super(map);
        }

        @Override
        public boolean onGesture(Gesture g, org.oscim.event.MotionEvent e) {
            GeoPoint geoPoint = mMap.viewport().fromScreenPoint(e.getX(), e.getY());
            if (g instanceof Gesture.Tap) { // 单击事件
                if (mapClickListener != null) {
                    mapClickListener.onMapClick(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
                }
            } else if (g instanceof Gesture.DoubleTap) { // 双击
                if (mapDoubleClickListener != null) {
                    mapDoubleClickListener.onMapDoubleClick(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
                }
            } else if (g instanceof Gesture.LongPress) { // 长按
                if (mapLongClickListener != null) {
                    mapLongClickListener.onMapLongClick(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));
                }
            }
            return false;
        }
    }

    public enum MAP_THEME {
        DEFAULT(0),  NEWTRON(1), OSMAGRAY(2),
        OSMARENDER(3),TRONRENDER(4);
        int themeId;

        MAP_THEME(int themeId) {
            this.themeId = themeId;
        }

        public MAP_THEME getMapTheme(int themeId) {
            MAP_THEME theme = DEFAULT;
            switch (themeId) {
                case 1:
                    theme = NEWTRON;
                    break;
                case 2:
                    theme = OSMAGRAY;
                    break;
                case 3:
                    theme = OSMARENDER;
                    break;
                case 4:
                    theme = TRONRENDER;
                    break;
            }
            return theme;
        }
    }

    public void switchTileVectorLayerTheme(MAP_THEME styleId) {
        // 如果不包含vectorlayer，则设置theme无效
        boolean bHis = false;
        for (Layer layer : getVtmMap().layers()) {
            if (layer instanceof VectorTileLayer) {
                bHis = true;
                break;
            }
        }
        if (!bHis) {
            getVtmMap().updateMap(true);
            return;
        }
        switch (styleId) {
            case NEWTRON:
                getVtmMap().setTheme(VtmThemes.NEWTRON, true);
                break;
            case OSMAGRAY:
                getVtmMap().setTheme(VtmThemes.OSMAGRAY, true);
                break;
            case OSMARENDER:
                getVtmMap().setTheme(VtmThemes.OSMARENDER, true);
                break;
            case TRONRENDER:
                getVtmMap().setTheme(VtmThemes.TRONRENDER, true);
                break;
            default:
                getVtmMap().setTheme(VtmThemes.DEFAULT, true);
                break;
        }
        getVtmMap().updateMap();
    }
}
