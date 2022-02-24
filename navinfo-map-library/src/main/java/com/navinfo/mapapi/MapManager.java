package com.navinfo.mapapi;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Interpolator;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.navinfo.mapapi.map.NIMapView;
import com.navinfo.mapapi.map.NaviMapScaleBar;
import com.navinfo.mapapi.map.NavinfoMap;
import com.navinfo.mapapi.map.source.NavinfoMapRastorTileSource;
import com.navinfo.mapapi.map.source.NavinfoMultiMapFileTileSource;
import com.navinfo.mapapi.map.source.SMapRastorTileSource;

import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.core.GeoPoint;
import org.oscim.core.MapPosition;
import org.oscim.layers.GroupLayer;
import org.oscim.layers.Layer;
import org.oscim.layers.marker.ItemizedLayer;
import org.oscim.layers.marker.MarkerInterface;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.marker.MarkerSymbol;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.renderer.BitmapRenderer;
import org.oscim.renderer.GLViewport;
import org.oscim.scalebar.MapScaleBar;
import org.oscim.scalebar.MapScaleBarLayer;
import org.oscim.scalebar.MetricUnitAdapter;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.source.mapfile.MapFileTileSource;

import okhttp3.Interceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

import org.oscim.tiling.source.OkHttpEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class MapManager {

//    //地图view
//    private NIMapView mMapView;
//    //地图类
//    private NavinfoMap mMap;
//    //单例对象
//    private static volatile MapManager mInstance;
//    private Activity mActivity;
//
//    /**
//     * 创建图层
//     */
//    private final boolean USE_CACHE = false;
//    private String dir = Environment.getExternalStorageDirectory() + "/" + "EditorMark";
//    private String filePath = dir + "/maps/";
//    protected GroupLayer baseGroupLayer; // 用于盛放所有基础底图的图层组，便于统一管理
//    private Layer otherBaseLayer;
//    private NaviMapScaleBar naviMapScaleBar; // 地图比例尺控件
//
//    /**
//     * 单例方法
//     */
//    public static MapManager getInstance() {
//
//        if (mInstance == null) {
//            synchronized (MapManager.class) {
//                if (mInstance == null) {
//                    mInstance = new MapManager();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    /**
//     * 初始化
//     *
//     * @param activity
//     * @param mapView  地图对象
//     */
//    public void init(Activity activity, NIMapView mapView) {
//        mActivity = activity;
//        mMapView = mapView;
//        if (mMapView != null) {
//            mMap = mMapView.getMap();
//        }
//    }
//
//
//    public void loadMap() {
//        for (MapGroupEnum group : MapGroupEnum.values()) {
//            mMapView.getVtmMap().layers().addGroup(group.ordinal());
//        }
//        initBaseMap();
//    }
//
//    /**
//     * 初始化基础底图，自动遍历map文件夹下所有的.map数据加载并显示
//     */
//    public void initBaseMap() {
//        // 显示地图比例尺
//        if (naviMapScaleBar == null) {
//            naviMapScaleBar = new NaviMapScaleBar(mMapView.getVtmMap());
//            naviMapScaleBar.initScaleBarLayer(GLViewport.Position.BOTTOM_LEFT, 25, 60);
//        }
//
//        if (baseGroupLayer == null) {
//            baseGroupLayer = new GroupLayer(mMapView.getVtmMap());
//            mMapView.getVtmMap().layers().add(baseGroupLayer, MapGroupEnum.BASE_GROUP.ordinal());
//        }
//        for (Layer layer : baseGroupLayer.layers) {
//            mMapView.getVtmMap().layers().remove(layer);
//        }
//        baseGroupLayer.layers.clear();
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.MILLISECONDS)
//                .addInterceptor(new HttpInterceptor())
//                .readTimeout(5, TimeUnit.MINUTES);
//
//        if (true) {
//            // Cache the tiles into file system
//            File cacheDirectory = new File(filePath + "cache", "tiles");
//            int cacheSize = 200 * 1024 * 1024; // 10 MB
//            Cache cache = new Cache(cacheDirectory, cacheSize);
//            builder.cache(cache);
//        }
//
//        NavinfoMultiMapFileTileSource urlTileSource = NavinfoMultiMapFileTileSource.builder()
//                .httpFactory(new OkHttpEngine.OkHttpFactory(builder)).build();
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("token", "");
//        urlTileSource.setHttpRequestHeaders(headerMap);
//        VectorTileLayer baseMapLayer = new VectorTileLayer(mMapView.getVtmMap(), urlTileSource);
//
//        File baseMapFolder = new File(filePath);
//        if (!baseMapFolder.exists()) {
//            return;
//        }
//        File[] dirFileList = baseMapFolder.listFiles();
//        if (dirFileList != null && dirFileList.length > 0) {
//            for (File dirFile : dirFileList) {
//                if (dirFile.isDirectory() && !dirFile.getName().endsWith("download")) {
//                    File[] mapFileList = dirFile.listFiles();
//                    for (File mapFile : mapFileList) {
//                        if (!mapFile.exists() || !mapFile.getName().endsWith(".map")) {
//                            continue;
//                        }
//                        MapFileTileSource mTileSource = new MapFileTileSource();
//                        mTileSource.setPreferredLanguage("zh");
//                        if (mTileSource.setMapFile(mapFile.getAbsolutePath())) {
//                            urlTileSource.add(mTileSource);
//                        }
//                    }
//                }
//            }
//        }
//
//        baseMapLayer.setTileSource(urlTileSource);
//        baseGroupLayer.layers.add(baseMapLayer);
//        baseGroupLayer.layers.add(new BuildingLayer(mMapView.getVtmMap(), baseMapLayer));
//        baseGroupLayer.layers.add(new LabelLayer(mMapView.getVtmMap(), baseMapLayer));
//
//        for (Layer layer : baseGroupLayer.layers) {
//            mMapView.getVtmMap().layers().add(layer, MapGroupEnum.BASE_GROUP.ordinal());
//        }
//        loadTheme(1);
//    }
//
//    /**
//     * 获取地图的比例尺控件，可以修改比例尺在地图上的显示位置，文字显示位置，是否为单行显示等
//     * */
//    public NaviMapScaleBar getNaviMapScaleBar() {
//        return naviMapScaleBar;
//    }
//
//    private void loadTheme(final int styleId) {
//        boolean bHis = false;
//        for (Layer layer : mMapView.getVtmMap().layers()) {
//            if (layer instanceof VectorTileLayer) {
//                bHis = true;
//                break;
//            }
//        }
//        if (!bHis) {
//            mMapView.getVtmMap().updateMap(true);
//            return;
//        }
//        switch (styleId) {
//            case 2:
//                mMapView.getVtmMap().setTheme(VtmThemes.OSMAGRAY, true);
//                break;
//            case 3:
//                mMapView.getVtmMap().setTheme(VtmThemes.MAPZEN, true);
//                break;
//            case 4:
//                mMapView.getVtmMap().setTheme(VtmThemes.TRONRENDER, true);
//                break;
//            default:
//                mMapView.getVtmMap().setTheme(VtmThemes.DEFAULT, true);
//                break;
//        }
//        mMapView.getVtmMap().updateMap(true);
//    }
//
//    private void showBaseMap() {
//        if (otherBaseLayer != null && mMap.getVtmMap().layers().contains(otherBaseLayer)) {
//            mMap.getVtmMap().layers().remove(otherBaseLayer);
//            otherBaseLayer = null;
//        }
//        for (Layer layer : baseGroupLayer.layers) {
//            layer.setEnabled(true);
//        }
//
//        mMap.getVtmMap().updateMap(true);
//        loadTheme(1);
//    }
//
//    public void showOtherMap(String url) {
//        if (otherBaseLayer != null && mMap.getVtmMap().layers().contains(otherBaseLayer)) {
//            mMap.getVtmMap().layers().remove(otherBaseLayer);
//            otherBaseLayer = null;
//        }
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.MILLISECONDS)
//                .addInterceptor(new HttpInterceptor())
//                .readTimeout(5, TimeUnit.MINUTES);
//        NavinfoMapRastorTileSource sMapRasterTileSource = NavinfoMapRastorTileSource.builder(url).httpFactory(new OkHttpEngine.OkHttpFactory(builder)).build();
//        if (USE_CACHE) {
//            // Cache the tiles into file system
//            File cacheDirectory = new File(dir, "tiles");
//            int cacheSize = 10 * 1024 * 1024; // 10 MB
//
//            Cache cache = new Cache(cacheDirectory, cacheSize);
//            builder.cache(cache);
//        }
//        otherBaseLayer = new BitmapTileLayer(mMapView.getVtmMap(), sMapRasterTileSource);
//        mMap.getVtmMap().layers().add(0, otherBaseLayer);
//        if (baseGroupLayer != null) {
//            for (Layer layer : baseGroupLayer.layers) {
//                layer.setEnabled(false);
//            }
//        }
//    }
//
//    private void showSMap() {
//        if (otherBaseLayer != null && mMap.getVtmMap().layers().contains(otherBaseLayer)) {
//            mMap.getVtmMap().layers().remove(otherBaseLayer);
//            otherBaseLayer = null;
//        }
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .connectTimeout(60, TimeUnit.MILLISECONDS)
//                .readTimeout(5, TimeUnit.MINUTES);
//        SMapRastorTileSource sMapRasterTileSource = SMapRastorTileSource.builder().httpFactory(new OkHttpEngine.OkHttpFactory(builder)).build();
//        if (USE_CACHE) {
//            // Cache the tiles into file system
//            File cacheDirectory = new File(dir, "tiles");
//            int cacheSize = 10 * 1024 * 1024; // 10 MB
//
//            Cache cache = new Cache(cacheDirectory, cacheSize);
//            builder.cache(cache);
//        }
//        otherBaseLayer = new BitmapTileLayer(mMapView.getVtmMap(), sMapRasterTileSource);
//        mMap.getVtmMap().layers().add(0, otherBaseLayer);
//        if (baseGroupLayer != null) {
//            for (Layer layer : baseGroupLayer.layers) {
//                layer.setEnabled(false);
//            }
//        }
//    }
//
//    public void changeMapStyle(String type, String url, int style) {
//        if (type.equals("smap")) {
//            switch (style) {
//                case 2:
//                    showSMap();
//                    loadTheme(1);
//                    break;
//                case 3:
//                    showSMap();
//                    loadTheme(4);
//                    break;
//                case 4:
//                    showSMap();
//                    loadTheme(2);
//                    break;
//                case 5:
//                    showOtherMap("http://10.130.10.206/service/yingxiang/map/rest/satellite/?z={z}&x={x}&y={y}");
//                    loadTheme(1);
//                    break;
//                default:
//                    showBaseMap();
//                    break;
//
//            }
//        } else {
//            showOtherMap(url);
//            loadTheme(style);
//        }
//
//    }
//
//
//    public void zoomIn(View view) {
//        if (view != null) {
//            view.setEnabled(false);
//            view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    view.setEnabled(true);
//                }
//            }, 300);
//        }
//        MapPosition mapPosition = mMap.getVtmMap().getMapPosition();
//
//    }
//
//    public void location() {
//
//        MapPosition mapPosition = mMapView.getVtmMap().getMapPosition();
//        if (mapPosition.getZoomLevel() < 16) {
//            mapPosition.setZoomLevel(16);
//        }
//        mapPosition.setPosition(40.062304, 116.213801);
//        mMapView.getVtmMap().animator().animateTo(300, mapPosition);
//    }
//
//    //增加marker
//    public MarkerItem addMarker(GeoPoint geopoint, Bitmap bitmap) {
//
//        if (geopoint != null && bitmap != null) {
//
//            MarkerItem markerItem = new MarkerItem("", "", geopoint.getLatitude() + "," + geopoint.getLongitude(), geopoint);
//
//            MarkerSymbol markerSymbol = new MarkerSymbol(bitmap, MarkerSymbol.HotspotPlace.BOTTOM_CENTER);
//
//            markerItem.setMarker(markerSymbol);
//
//            addMarker2MarkerLayer(markerItem, bitmap, MapGroupEnum.DRAW_GROUP.ordinal());
//
//            return markerItem;
//        }
//
//        return null;
//    }
//
//    private void addMarker2MarkerLayer(MarkerInterface markerItem, Bitmap defaultBitmap, int layerGroup) {
//        //要确保线在marker下面，所以先把线的图层加好
//        if (markerItem == null) {
//            return;
//        }
//
//        MarkerSymbol symbol = new MarkerSymbol(defaultBitmap, MarkerSymbol.HotspotPlace.BOTTOM_CENTER);
//        ItemizedLayer itemizedLayer = new ItemizedLayer(mMapView.getVtmMap(), symbol);
//        itemizedLayer.addItem(markerItem);
//        if (!mMap.getVtmMap().layers().contains(itemizedLayer)) {
//            mMap.getVtmMap().layers().add(itemizedLayer, layerGroup);
//        }
//        itemizedLayer.update();
//
//    }
//
//    public void zoomOut(View view) {
//        if (view != null) {
//            view.setEnabled(false);
//            view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    view.setEnabled(true);
//                }
//            }, 300);
//        }
//    }
//
//
//    public void onResume() {
//        mMapView.onResume();
//    }
//
//    public void onPause() {
//        mMapView.onPause();
//    }
//
//    /**
//     * @author zhangwei on 2020/12/23.
//     * 用于拦截重复请求
//     */
//    class HttpInterceptor implements Interceptor {
////    private LinkedHashMap<String, Long> requestMap = new LinkedHashMap();//实现拦截重复请求
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//
//            String key = chain.request().url().toString();//请求url
//            Log.e("jingo", "OKHttp Url = " + key);
//            try {
//                Request.Builder builder = chain.request().newBuilder();
//                return chain.proceed(builder.build());
//            } catch (IOException e) {
//                Log.e("jingo", " HttpInterceptor " + e.toString());
//                throw e;
//            } finally {
//            }
//        }
//    }
//
//    public enum MapGroupEnum {
//        BASE_GROUP/*基础底图*/, RASTER_GROUP/*栅格图层组*/,
//        VECTOR_GROUP/*矢量图层组*/, TAB_GROUP/*tab数据图层组*/,
//        TRACK_GROUP/*轨迹图层组*/, STATION_GROUP/*基站图层组*/,
//        DRAW_GROUP/*用户绘制图层组*/, OTHER_GROUP /*其他图层组*/,
//        DATA_GROUP/*数据图层组*/, MARKER_GROUP/*Marker图层组*/,
//        OPERATE_GROUP/*操作图层组*/, ALLWAYS_SHOW_GROUP/*常显图层组*/,
//    }

}

