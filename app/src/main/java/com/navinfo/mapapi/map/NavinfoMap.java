package com.navinfo.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.navinfo.mapapi.model.LatLng;
import com.navinfo.mapapi.model.LatLngBounds;

import org.oscim.core.GeoPoint;
import org.oscim.core.MapPosition;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.map.Map;

/**
 * 定义 NavinfoMap 地图对象的操作方法与接口
 */
public class NavinfoMap extends Object {

    /**
     *
     */
    Map map;
    /**
     * 地图控件
     */
    NIMapView mMapView;
    /**
     * 指北针显隐
     */
    private boolean enableCompassImage = true;

    /**
     * 指北针位置
     */
    private Point compassPoint;

    /**
     * marker图层
     */
    private ItemizedLayer markerLayer;

    /**
     * 构造函数
     */
    public NavinfoMap(NIMapView niMapView) {
        this.mMapView = niMapView;
        this.map = niMapView.getVtmMap();
    }

    /**
     * 获取地图的当前状态
     *
     * @return
     */
    public Map getVtmMap() {

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

                MarkerItem markerItem = new MarkerItem(marker.getId(),marker.getTitle(),new GeoPoint(marker.getPosition().getLatitude(),marker.getPosition().getLongitude()));
                MarkerSymbol markerSymbol = new MarkerSymbol(marker.getIcon().getBitmap(),MarkerSymbol.HotspotPlace.BOTTOM_CENTER);

                if(markerLayer==null){
                    markerLayer = new ItemizedLayer(getVtmMap(),markerSymbol);
                    getVtmMap().layers().add(markerLayer,marker.getZIndex());
                }
                markerItem.setMarker(markerSymbol);
                markerLayer.addItem(markerItem);
            }
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
     * 获取屏幕坐标系下指南针位置
     *
     * @return
     */
    public android.graphics.Point getCompassPosition() {

        return compassPoint;
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

    }

    /**
     * 批量删除添加的多个 Overlay
     *
     * @param overlays
     */
    public void removeOverLays(java.util.List<Overlay> overlays) {

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
     * 设置指南针的位置
     *
     * @param p
     */
    public void setCompassPosition(android.graphics.Point p) {
        this.compassPoint = p;
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
    public void setMyLocationData(MyLocationData data) {

    }


    /**
     * 设置是否允许定位图层
     *
     * @param enabled
     */
    public void setMyLocationEnabled(boolean enabled) {

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
}
