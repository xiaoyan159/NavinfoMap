package com.navinfo.mapapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.navinfo.mapapi.layers.NaviLocationLayer;
import com.navinfo.mapapi.model.LatLng;
import com.navinfo.mapapi.model.LatLngBounds;
import com.navinfo.mapapi.utils.CacheTileProgress;
import com.navinfo.mapapi.utils.TileDownloader;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.download.Download;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.oscim.backend.canvas.Paint;
import org.oscim.core.GeoPoint;
import org.oscim.core.MapPosition;
import org.oscim.core.MercatorProjection;
import org.oscim.layers.Layer;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerInterface;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.vector.VectorLayer;
import org.oscim.layers.vector.geometries.Drawable;
import org.oscim.layers.vector.geometries.LineDrawable;
import org.oscim.layers.vector.geometries.PointDrawable;
import org.oscim.layers.vector.geometries.PolygonDrawable;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.map.Map;
import org.oscim.tiling.source.UrlTileSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 定义 NavinfoMap 地图对象的操作方法与接口
 */
public class NavinfoMap {
    /**
     *
     */
    private Map map;
    /**
     * 地图控件
     */
    private NIMapView mMapView;
    /**
     * 指北针显隐
     */
    private boolean enableCompassImage = true;

    /**
     * marker图层
     */
    private ItemizedLayer markerLayer;

    /**
     * 几何图层
     */
    private VectorLayer vectorLayer;

    /**
     * Marker事件监听
     */
    private OnMarkerClickListener onMarkerClickListener;

    /**
     * 缓存记录
     */
    private java.util.Map<String, Overlay> mapCache;

    /**
     * 缓存记录
     */
    private java.util.Map<String, Drawable> mapDrawableCache;

    /**
     * 用户位置显示图层
     * */
    private NaviLocationLayer locationLayer;
    /**
     * 构造函数
     */
    public NavinfoMap(NIMapView niMapView) {
        this.mMapView = niMapView;
        this.map = mMapView.getVtmMap();
        this.mapCache = new HashMap<>();
        this.mapDrawableCache = new HashMap<>();
    }

    /**
     * 获取地图的当前状态
     *
     * @return
     */
    private Map getVtmMap() {
        return map;
    }

    /**
     * 向地图添加一个 Overlay
     *
     * @param options
     * @return
     */
    public Overlay addOverlay(OverlayOptions options) {
        if (options != null) {
            if (options instanceof MarkerOptions) {
                Marker marker = new Marker();
                marker.setDraggable(((MarkerOptions) options).isDraggable());
                marker.setExtraInfo(((MarkerOptions) options).getExtraInfo());
                marker.setIcon(((MarkerOptions) options).getIcon());
                marker.setPosition(((MarkerOptions) options).getPosition());
                marker.setVisible(((MarkerOptions) options).isVisible());
                marker.setZIndex(((MarkerOptions) options).getZIndex());

                MarkerItem markerItem = new MarkerItem(marker.getId(), marker.getTitle(), new GeoPoint(marker.getPosition().getLatitude(), marker.getPosition().getLongitude()));
                MarkerSymbol markerSymbol = new MarkerSymbol(marker.getIcon().getBitmap(), MarkerSymbol.HotspotPlace.BOTTOM_CENTER);

                if (markerLayer == null) {
                    markerLayer = new ItemizedLayer(getVtmMap(), markerSymbol);
                    getVtmMap().layers().add(markerLayer, marker.getZIndex());
                }
                markerItem.setMarker(markerSymbol);
                markerLayer.addItem(markerItem);
                markerLayer.populate();
                this.mapCache.put(marker.getId(), marker);
                return marker;
            } else if (options instanceof PolylineOptions) {
                PolylineOptions polylineOptions = (PolylineOptions) options;
                Polyline polyline = new Polyline();
                polyline.setClickable(polylineOptions.isClickable());
                polyline.setPoints(polylineOptions.getPoints());
                polyline.setWidth(polylineOptions.getWidth());
                polyline.setColor(polylineOptions.getColor());
                polyline.setExtraInfo(polylineOptions.getExtraInfo());
                polyline.setZIndex(polylineOptions.getZIndex());
                Geometry geometry = getLineString(polyline.getPoints());
                Style vectorStyle = Style.builder().scaleZoomLevel(20).buffer(1).strokeColor(polyline.getColor()).stippleColor(polyline.getColor()).fillColor(polyline.getColor()).strokeWidth(polyline.getWidth()).stippleWidth(polyline.getWidth()).build();
                Drawable drawable = convertGeometry2Drawable(geometry, vectorStyle);
                if (vectorLayer == null) {
                    vectorLayer = new VectorLayer(getVtmMap());
                    getVtmMap().layers().add(vectorLayer, polyline.getZIndex());
                }
                vectorLayer.add(drawable);
                vectorLayer.update();
                this.mapCache.put(polyline.hashCode() + "", polyline);
                this.mapDrawableCache.put(polyline.hashCode() + "", drawable);
                return polyline;
            } else if (options instanceof PolygonOptions) {
                PolygonOptions polygonOptions = (PolygonOptions) options;
                Polygon polygon = new Polygon();
                polygon.setExtraInfo(polygonOptions.getExtraInfo());
                polygon.setFillColor(polygonOptions.getFillColor());
                polygon.setPoints(polygonOptions.getPoints());
                polygon.setVisible(polygonOptions.isVisible());
                polygon.setZIndex(polygonOptions.getZIndex());
                List<GeoPoint> list = new ArrayList<>();
                if (polygon.getPoints() != null) {
                    for (LatLng latlng : polygon.getPoints()) {
                        list.add(new GeoPoint(latlng.getLatitude(), latlng.getLongitude()));
                    }
                    if (vectorLayer == null) {
                        vectorLayer = new VectorLayer(getVtmMap());
                        getVtmMap().layers().add(vectorLayer, polygon.getZIndex());
                    }
                    Style.Builder styleBuilder = Style.builder().scaleZoomLevel(12);
                    Style style = styleBuilder.fillColor(polygon.getFillColor()).build();
                    PolygonDrawable drawable = new PolygonDrawable(list, style);
                    vectorLayer.add(drawable);
                    vectorLayer.update();
                    this.mapCache.put(polygon.hashCode() + "", polygon);
                    this.mapDrawableCache.put(polygon.hashCode() + "", drawable);
                    return polygon;
                }
            }
        }
        return null;
    }


    /**
     * 数据转换
     *
     * @param geometry
     * @param vectorLayerStyle
     * @return
     */
    private Drawable convertGeometry2Drawable(Geometry geometry, Style vectorLayerStyle) {

        if (geometry == null) {
            return null;
        }

        Drawable resultDrawable = null;

        if ("POINT".equals(geometry.getGeometryType().toUpperCase())) {
            GeoPoint geoPoint = new GeoPoint(geometry.getCoordinate().y, geometry.getCoordinate().x);
            if (geoPoint != null) {
                resultDrawable = new PointDrawable(geoPoint, vectorLayerStyle);
            }
        } else if ("LINESTRING".equals(geometry.getGeometryType().toUpperCase())) {
            if (geometry != null) {
                resultDrawable = new LineDrawable((LineString) geometry, vectorLayerStyle);
            } else {
                System.out.println("convertGeometry2Drawable=复杂===" + geometry.toString());
            }
        } else if ("POLYGON".equals(geometry.getGeometryType().toUpperCase())) {
/*            org.locationtech.jts.geom.Geometry polygon = GeometryTools.getInstance(MapManager.getInstance().getmMapView().mapView()).createGeometry(geometry.ExportToWkt());
            if (polygon != null) {
                resultDrawable = new PolygonDrawable((Polygon) polygon, vectorLayerStyle);
            }*/
        }

        return resultDrawable;
    }

    /**
     * 创建线几何
     *
     * @param list
     * @return
     */
    private Geometry getLineString(List<LatLng> list) {
        if (list != null && list.size() > 1) {
            GeometryFactory factory = new GeometryFactory();
            Coordinate[] coordinates = new Coordinate[list.size()];
            for (int i = 0; i < list.size(); i++) {
                coordinates[i] = new Coordinate(list.get(i).getLongitude(), list.get(i).getLatitude());
            }
            Geometry geometry = factory.createLineString(coordinates);
            if (geometry != null)
                return geometry;
        }
        return null;

    }


    /**
     * 向地图添加多个 Overlay
     *
     * @param options
     * @return
     */
    public java.util.List<Overlay> addOverlays(java.util.List<OverlayOptions> options) {

        return null;
    }


    /**
     * 向地图添加一个TileOverlay覆盖物
     *
     * @param overlayOptions
     * @return
     */
    public TileOverlay addTileLayer(TileOverlayOptions overlayOptions) {

        return null;
    }


    /**
     * 以动画方式更新地图状态，默认动画耗时 300 ms
     *
     * @param update
     */
    public void animateMapStatus(MapStatusUpdate update) {

    }

    /**
     * 以动画方式更新地图状态
     *
     * @param update
     * @param durationMs
     */
    public void animateMapStatus(MapStatusUpdate update, int durationMs) {

    }


    /**
     * 调整定位图层相对于Overlay图层的顺序 enable = true : 定位图层在Overlay图层之下; enable = false : 定位图层在Overlay图层之上; 默认false，即定位图层在Overlay图层之上
     *
     * @param enable
     */
    public void changeLocationLayerOrder(boolean enable) {

    }

    /**
     * 清除地图缓存数据，支持清除普通地图和卫星图缓存  MAP_TYPE_NORMAL 普通图； MAP_TYPE_SATELLITE 卫星图；
     *
     * @param mapType
     */
    public void cleanCache(int mapType) {

    }

    /**
     * 清空地图所有的 Overlay 覆盖物以及 InfoWindow
     */
    public void clear() {

    }

    /**
     * 获取已添加的所有InfoWindow对象
     *
     * @return
     */
    public java.util.List<InfoWindow> getAllInfoWindows() {
        return null;
    }

    /**
     * 获取地图显示大小等级
     *
     * @return
     */
    public int getFontSizeLevel() {

        return 0;
    }

    /**
     * 获取定位数据
     *
     * @return
     */
    public MyLocationData getLocationData() {
        return null;
    }

    /**
     * 获取地图的当前状态
     *
     * @return
     */
    public MapStatus getMapStatus() {

        return null;
    }

    /**
     * 获取地图当前的模式，空白地图、普通地图或者卫星图
     *
     * @return
     */
    public int getMapType() {
        return 0;
    }

    /**
     * 获取指定区域内所有的Marker点
     *
     * @param bounds
     * @return
     */
    public java.util.List<Marker> getMarkersInBounds(LatLngBounds bounds) {

        return null;
    }

    /**
     * 获取地图最大缩放级别
     *
     * @return
     */
    public float getMaxZoomLevel() {
        return map.viewport().getMaxZoomLevel();
    }

    /**
     * 获取地图最小缩放级别
     *
     * @return
     */
    public float getMinZoomLevel() {

        return map.viewport().getMinZoomLevel();
    }

    /**
     * 获取地图投影坐标转换器, 当地图初始化完成之前返回 null，在 OnMapLoadedCallback.onMapLoaded() 之后才能正常
     *
     * @return
     */
    public Projection getProjection() {

        return null;
    }

    /**
     * 获取地图ui控制器
     *
     * @return
     */
    public UiSettings getUiSettings() {
        return null;
    }


    /**
     * 隐藏地图上的所有InfoWindow
     */
    public void hideInfoWindow() {

    }

    /**
     * 清除特定的InfoWindow
     *
     * @param infoWindow
     */
    public void hideInfoWindow(InfoWindow infoWindow) {

    }

    /**
     * 获取是否允许定位图层
     *
     * @return
     */
    public boolean isMyLocationEnabled() {

        return false;
    }

    /**
     * 获取是否显示底图默认标注
     *
     * @return
     */
    public boolean isShowMapPoi() {
        return false;
    }

    /**
     * 移除一个地图 Marker 覆盖物点击事件监听者
     *
     * @param listener
     */
    public void removeMarkerClickListener(OnMarkerClickListener listener) {
        onMarkerClickListener = null;
    }

    /**
     * 批量删除添加的多个 Overlay
     *
     * @param overlays
     */
    public synchronized void removeOverLays(java.util.List<Overlay> overlays) {
        synchronized (this) {
            if (overlays != null) {
                //遍历图层
                for (Overlay overlay : overlays) {
                    if (getVtmMap().layers() != null) {
                        b:
                        for (int i = 0; i < getVtmMap().layers().size(); i++) {
                            Layer layer = getVtmMap().layers().get(i);
                            if (overlay instanceof Marker) {
                                Marker marker = (Marker) overlay;
                                if (layer instanceof ItemizedLayer) {
                                    List<MarkerInterface> list = ((ItemizedLayer) layer).getItemList();
                                    if (list != null) {
                                        for (MarkerInterface markerInterface : list) {
                                            MarkerItem markerItem = (MarkerItem) markerInterface;
                                            if (markerItem.getTitle().equalsIgnoreCase(marker.getId())) {
                                                this.mapCache.remove(marker.getId() + "");
                                                ((ItemizedLayer) layer).removeItem(markerInterface);
                                                ((ItemizedLayer) layer).populate();
                                                break b;
                                            }
                                        }
                                    }
                                }
                            } else if (overlay instanceof Polyline) {
                                if (layer instanceof VectorLayer) {
                                    if (this.mapDrawableCache != null && this.mapDrawableCache.containsKey(overlay.hashCode() + "")) {
                                        ((VectorLayer) layer).remove(this.mapDrawableCache.get(overlay.hashCode() + ""));
                                        this.mapDrawableCache.remove(overlay.hashCode() + "");
                                        this.mapCache.remove(overlay.hashCode() + "");
                                        ((VectorLayer) layer).update();
                                        break b;
                                    }
                                }
                            } else if (overlay instanceof Polygon) {
                                if (layer instanceof VectorLayer) {
                                    if (this.mapDrawableCache != null && this.mapDrawableCache.containsKey(overlay.hashCode() + "")) {
                                        ((VectorLayer) layer).remove(this.mapDrawableCache.get(overlay.hashCode() + ""));
                                        this.mapDrawableCache.remove(overlay.hashCode() + "");
                                        this.mapCache.remove(overlay.hashCode() + "");
                                        ((VectorLayer) layer).update();
                                        break b;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }


    /**
     * 设置指南针是否显示
     *
     * @param enable
     */
    public void setCompassEnable(boolean enable) {
        this.enableCompassImage = enable;
        if (mMapView != null && mMapView.getCompassImage() != null) {
            mMapView.getCompassImage().setVisibility(enable ? View.VISIBLE : View.GONE);
            mMapView.getCompassImage().setEnabled(enable);
        }
    }

    /**
     * 获取指北针显隐控制
     *
     * @return true 显示 false 隐藏
     */
    public boolean isEnableCompassImage() {
        return enableCompassImage;
    }

    /**
     * 设置指南针自定义图标
     *
     * @param icon
     */
    public void setCompassIcon(Bitmap icon) {
        if (mMapView != null && mMapView.getCompassImage() != null) {
            mMapView.getCompassImage().setImageBitmap(icon);
        }
    }


    /**
     * 设置地图显示大小等级
     *
     * @param level
     */
    public void setFontSizeLevel(int level) {

    }

    /**
     * 设置指定的图层是否可以点击.
     *
     * @param mapLayer
     * @param isClickable
     */
    public void setLayerClickable(MapLayer mapLayer, boolean isClickable) {

    }

    /**
     * 改变地图状态
     *
     * @param update
     */
    public void setMapStatus(MapStatusUpdate update) {

    }

    /**
     * 设置地图类型 MAP_TYPE_NORMAL 普通图； MAP_TYPE_SATELLITE 卫星图； MAP_TYPE_NONE 空白地图
     *
     * @param type
     */
    public void setMapType(int type) {

    }

    /**
     * 设置地图最大以及最小缩放级别
     *
     * @param max
     * @param min
     */
    public void setMaxAndMinZoomLevel(int max, int min) {
        map.viewport().setMaxZoomLevel(max);
        map.viewport().setMinZoomLevel(min);
    }

    /**
     * 放大
     *
     * @param animate 是否动画过渡
     */
    public void zoomIn(boolean animate) {
        MapPosition mapPosition = map.getMapPosition();
        mapPosition.setZoom(mapPosition.getZoom() + 1);
        if (animate) {
//            map.animator().animateZoom(300, 2, 0.5f, 0.5f);
            map.animator().animateTo(mapPosition);
        } else {
            map.setMapPosition(mapPosition);
        }
    }

    /**
     * 缩小地图
     *
     * @param animate 是否动画过渡
     */
    public void zoomOut(boolean animate) {
        MapPosition mapPosition = map.getMapPosition();
        mapPosition.setZoom(mapPosition.getZoom() - 1);
        if (animate) {
//            map.animator().animateZoom(300, 0.5, 0.5f, 0.5f);
            map.animator().animateTo(mapPosition);
        } else {
            map.setMapPosition(mapPosition);
        }
    }

    /**
     * 设置定位数据, 只有先允许定位图层后设置数据才会生效，参见 setMyLocationEnabled(boolean)
     *
     * @param data
     */
    public void setMyLocationData(Location data) {
        if (locationLayer == null) {
            return;
        }
        locationLayer.setPosition(data.getLatitude(), data.getLongitude(), data.getAccuracy());
    }

    public void setMyLocationData(double lat, double lon, float accuracy) {
        if (locationLayer == null) {
            return;
        }
        locationLayer.setPosition(lat, lon, accuracy);
    }

    /**
     * 设置是否允许定位图层
     *
     * @param enabled
     */
    public void setMyLocationEnabled(Context mContext, boolean enabled) {
        initLocaitonLayer(mContext);
        locationLayer.setEnabled(enabled);
    }

    private void initLocaitonLayer(Context mContext) {
        if (map == null) {
            throw new IllegalStateException("map不可用，无法显示当前位置！");
        }
        if (locationLayer == null) {
            locationLayer = new NaviLocationLayer(mContext, map);
        }
    }

    /**
     * 设置地图单击事件监听者
     *
     * @param listener
     */
    public void setOnMapClickListener(OnMapClickListener listener) {
        mMapView.setOnMapClickListener(listener);
    }

    /**
     * 设置地图双击事件监听者
     *
     * @param listener
     */
    public void setOnMapDoubleClickListener(OnMapDoubleClickListener listener) {
        mMapView.setOnMapDoubleClickListener(listener);
    }


    /**
     * 设置地图在每一帧绘制时的回调接口，该接口在绘制线程中调用
     *
     * @param callback
     */
    public void setOnMapDrawFrameCallback(OnMapDrawFrameCallback callback) {

    }

    /**
     * 设置地图加载完成回调，该接口需要在地图加载到页面之前调用，否则不会触发回调。
     *
     * @param callback
     */
    public void setOnMapLoadedCallback(OnMapLoadedCallback callback) {

    }

    /**
     * 设置地图长按事件监听者
     *
     * @param listener
     */
    public void setOnMapLongClickListener(OnMapLongClickListener listener) {
        mMapView.setOnMapLongClickListener(listener);
    }

    /**
     * 设置地图渲染完成回调
     *
     * @param callback
     */
    public void setOnMapRenderCallbadk(OnMapRenderCallback callback) {

    }

    /**
     * 设置地图状态监听者
     *
     * @param listener
     */
    public void setOnMapStatusChangeListener(OnMapStatusChangeListener listener) {

    }


    /**
     * @param listener
     */
    public void setOnMapTouchListener(OnMapTouchListener listener) {
        mMapView.setOnMapTouchListener(listener);
    }

    /**
     * 设置地图 Marker 覆盖物点击事件监听者,可设置多个监听对象，停止监听时调用removeMarkerClickListener移除监听对象
     *
     * @param listener
     */
    public void setOnMarkerClickListener(OnMarkerClickListener listener) {
        this.onMarkerClickListener = listener;
    }

    /**
     * 设置 Marker 拖拽事件监听者
     *
     * @param listener
     */
    public void setOnMarkerDragListener(OnMarkerDragListener listener) {

    }

    /**
     * 设置地图 MultiPoint 覆盖物点击事件监听者
     *
     * @param listener
     */
    public void setOnMultiPointClickListener(OnMultiPointClickListener listener) {

    }

    /**
     * 设置定位图标点击事件监听者
     *
     * @param listener
     */
    public void setOnMyLocationClickListener(OnMyLocationClickListener listener) {

    }


    /**
     * 设置地图 Polyline 覆盖物点击事件监听者
     *
     * @param listener
     */
    public void setOnPolylineClickListener(OnPolylineClickListener listener) {

    }


    /**
     * 设置地图上控件与地图边界的距离，包含比例尺、缩放控件、logo、指南针的位置 只有在 OnMapLoadedCallback.onMapLoaded() 之后设置才生效
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setViewPadding(int left, int top, int right, int bottom) {

    }

    /**
     * 显示 InfoWindow, 该接口会先隐藏其他已添加的InfoWindow, 再添加新的InfoWindow
     *
     * @param infoWindow
     */
    public void showInfoWindow(InfoWindow infoWindow) {

    }

    /**
     * 显示 InfoWindow, 该接口可以设置是否在添加InfoWindow之前，先隐藏其他已经添加的InfoWindow.
     *
     * @param infoWindow
     * @param isHideOthers
     */
    public void showInfoWindow(InfoWindow infoWindow, boolean isHideOthers) {

    }

    /**
     * @param infoWindowList
     */
    public void showInfoWindows(java.util.List<InfoWindow> infoWindowList) {

    }

    /**
     * 控制是否显示底图默认标注, 默认显示
     *
     * @param isShow
     */
    public void showMapPoi(boolean isShow) {

    }

    /**
     * @param callback
     */
    public void snapshot(SnapshotReadyCallback callback) {

    }

    /**
     * 切换指定图层的顺序
     *
     * @param srcLayer
     * @param destLayer
     */
    public void switchLayerOrder(MapLayer srcLayer, MapLayer destLayer) {

    }


    /**
     * 地图单击事件监听接口
     */
    public static interface OnMapClickListener {
        /**
         * 地图单击事件回调函数
         *
         * @param point
         */
        void onMapClick(LatLng point);

        /**
         * 地图内 Poi 单击事件回调函数
         *
         * @param poi
         */
        void onMapPoiClick(MapPoi poi);
    }

    /**
     * 地图双击事件监听接口
     */
    public static interface OnMapDoubleClickListener {

        /**
         * 地图双击事件监听回调函数
         *
         * @param point
         */
        void onMapDoubleClick(LatLng point);

    }

    /**
     * 地图绘制回调接口
     */
    public static interface OnMapDrawFrameCallback {
        /**
         * 地图每一帧绘制结束后回调接口，在此你可以绘制自己的内容
         *
         * @param drawingMapStatus
         */
        void onMapDrawFrame(MapStatus drawingMapStatus);
    }

    /**
     * 地图加载完成回调接口
     */
    public static interface OnMapLoadedCallback {
        /**
         * 地图加载完成回调函数
         */
        void onMapLoaded();
    }

    /**
     * 地图长按事件监听接口
     */
    public static interface OnMapLongClickListener {
        /**
         * 地图长按事件监听回调函数
         *
         * @param point
         */
        void onMapLongClick(LatLng point);
    }

    /**
     * 地图渲染完成回调接口
     */
    public static interface OnMapRenderCallback {
        /**
         * 地图渲染完成回调函数
         */
        void onMapRenderFinished();

    }

    /**
     * 地图状态改变相关接口
     */
    public static interface OnMapStatusChangeListener {
        /**
         * 地图状态变化中
         *
         * @param status
         */
        void onMapStatusChange(MapStatus status);


        /**
         * 地图状态改变结束
         *
         * @param status
         */
        void onMapStatusChangeFinish(MapStatus status);


        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         *
         * @param status
         */
        void onMapStatusChangeStart(MapStatus status);

        /**
         * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
         *
         * @param status
         * @param reason
         */
        void onMapStatusChangeStart(MapStatus status, int reason);
    }

    /**
     * 用户触摸地图时回调接口
     */
    public static interface OnMapTouchListener {
        /**
         * 当用户触摸地图时回调函数
         *
         * @param event
         */
        void onTouch(MotionEvent event);
    }

    /**
     * 地图 Marker 覆盖物点击事件监听接口
     */
    public static interface OnMarkerClickListener {
        /**
         * 地图 Marker 覆盖物点击事件监听函数,开发者注意根据参数Marker来判断响应某个对象的点击事件
         *
         * @param marker
         * @return
         */
        boolean onMarkerClick(Marker marker);

        /**
         * 地图 Marker 覆盖物长按事件监听函数,开发者注意根据参数Marker来判断响应某个对象的点击事件
         *
         * @param marker
         * @return
         */
        boolean onMarkerLongClick(Marker marker);
    }

    /**
     * 地图 Marker 覆盖物拖拽事件监听接口
     */
    public static interface OnMarkerDragListener {

        /**
         * Marker 被拖拽的过程中。
         *
         * @param marker
         */
        void onMarkerDrag(Marker marker);

        /**
         * Marker 拖拽结束
         *
         * @param marker
         */
        void onMarkerDragEnd(Marker marker);

        /**
         * 开始拖拽 Marker
         *
         * @param marker
         */
        void onMarkerDragStart(Marker marker);

    }

    /**
     * 地图MultiPoint覆盖物点击事件监听接口
     */
    public static interface OnMultiPointClickListener {

        /**
         * 地图 MultiPoint 覆盖物点击事件监听函数
         *
         * @param multiPoint
         * @param multiPointItem
         * @return
         */
        boolean onMultiPointClick(MultiPoint multiPoint, MultiPointItem multiPointItem);
    }

    /**
     * 地图定位图标点击事件监听接口
     */
    public static interface OnMyLocationClickListener {

        /**
         * 地图定位图标点击事件监听函数
         *
         * @return
         */
        boolean onMyLocationClick();

    }

    /**
     * 地图polyline覆盖物点击事件监听接口
     */
    public static interface OnPolylineClickListener {

        /**
         * 地图 Polyline 覆盖物点击事件监听函数
         *
         * @param polyline
         * @return
         */
        boolean onPolylineClick(Polyline polyline);
    }

    /**
     * 地图截屏回调接口
     */
    public static interface SnapshotReadyCallback {
        /**
         * 地图截屏回调接口
         *
         * @param snapshot
         */
        void onSnapshotReady(Bitmap snapshot);

    }

    public void updateMap() {
        getVtmMap().updateMap();
    }

    public void updateMap(boolean redraw) {
        getVtmMap().updateMap(redraw);
    }

    public void render() {
        getVtmMap().render();
    }

    public boolean post(Runnable action) {
        return getVtmMap().post(action);
    }

    public boolean postDelayed(Runnable action, long delay) {
        return getVtmMap().postDelayed(action, delay);
    }

    public int getWidth() {
        return getVtmMap().getWidth();
    }

    public int getHeight() {
        return getVtmMap().getHeight();
    }

    public int getScreenWidth() {
        return getVtmMap().getScreenWidth();
    }

    public int getScreenHeight() {
        return getVtmMap().getScreenHeight();
    }

    public void beginFrame() {
        getVtmMap().beginFrame();
    }

    public void doneFrame(boolean needsRedraw) {
        getVtmMap().doneFrame(needsRedraw);
    }

    public int getMapZoomLevel() {
        return getVtmMap().getMapPosition().getZoomLevel();
    }

    /**
     * 设置地图
     * */
    public void setMapPosition(double lat, double lon, int zoomLevel) {
        double scale = 1 << zoomLevel;
        getVtmMap().setMapPosition(lat, lon, scale);
    }

    public void animateMapPosition(double lat, double lon, int zoomLevel, int duration) {
        if (duration < 0) {
            duration = 500;
        }
        if (zoomLevel <= 0) {
            zoomLevel = getVtmMap().getMapPosition().zoomLevel;
        }
        double scale = 1 << zoomLevel;
        MapPosition mapPosition = new MapPosition(lat, lon, scale);
        getVtmMap().animator().animateTo(duration, mapPosition);
    }

    /**
     * 下载离线矢量地图
     * */
    public void downloadVectorMap(String url, DownloadProgress downloadProgress) {
        Kalle.Download.get(url).directory(NavinfoLayerManager.defaultDir).onProgress(new Download.ProgressBar() {
            @Override
            public void onProgress(int progress, long byteCount, long speed) {
                downloadProgress.onProgress(progress, byteCount, speed);
            }
        });
    }

    /**
     * 缓存urlTilesource对应的数据
     * */
    public List<FutureTask> cacheUrlTileMap(Rect rect, int minZoomLevel, int maxZoomLevel, CacheTileProgress progress) {
        List<Layer> layerList = getVtmMap().layers();
        List<UrlTileSource> urlTileSourceList = new ArrayList<>();
        if (layerList!=null&&!layerList.isEmpty()) {
            for (int i = 0; i < layerList.size(); i++) {
                Layer layer = layerList.get(i);
                if (layer instanceof BitmapTileLayer && ((BitmapTileLayer) layer).getTileSource() instanceof UrlTileSource) {
                    UrlTileSource urlTileSource = (UrlTileSource) ((BitmapTileLayer) layer).getTileSource();
                    urlTileSourceList.add(urlTileSource);
                }
            }
        }
        // 根据rect获取对应的地理坐标
        GeoPoint leftTopGeoPoint = map.viewport().fromScreenPoint(rect.left, rect.top);
        GeoPoint rightBottomGeoPoint = map.viewport().fromScreenPoint(rect.right, rect.bottom);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        List<FutureTask> futureTaskList = new ArrayList<>();
        progress.setLayerCount(urlTileSourceList.size());
        for (int i = 0; i < urlTileSourceList.size(); i++) {
            UrlTileSource urlTileSource = urlTileSourceList.get(i);
            int finalI = i;
            progress.setLayerId(i);
            Callable callable = TileDownloader.getInstance().downloadRasterTile(urlTileSource, leftTopGeoPoint, rightBottomGeoPoint, (byte) minZoomLevel, (byte) maxZoomLevel, progress);
            FutureTask futureTask = new FutureTask(callable);
            futureTaskList.add(futureTask);
        }

        if (futureTaskList!=null&&!futureTaskList.isEmpty()){
            for (int i = 0; i < futureTaskList.size(); i++) {
                scheduledExecutorService.submit(futureTaskList.get(i));
            }
            scheduledExecutorService.shutdown();
        }
        return futureTaskList;
    }

    public void cancelCacheTileMap() {
        TileDownloader.getInstance().setCanDownloadRasterTile(false);
    }

    public interface DownloadProgress {

        Download.ProgressBar DEFAULT = new Download.ProgressBar() {
            @Override
            public void onProgress(int progress, long byteCount, long speed) {
            }
        };

        /**
         * Download onProgress changes.
         */
        void onProgress(int progress, long byteCount, long speed);
    }
}
