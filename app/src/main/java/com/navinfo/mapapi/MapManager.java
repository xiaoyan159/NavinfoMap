package com.navinfo.mapapi;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Environment;
import android.view.View;

import com.navinfo.mapapi.map.NIMapView;
import com.navinfo.mapapi.map.NavinfoMap;

import org.oscim.core.MapPosition;
import org.oscim.layers.GroupLayer;
import okhttp3.OkHttpClient;


/**
 *
 */
public class MapManager {

    //地图view
    private NIMapView mMapView;
    //地图类
    private NavinfoMap mMap;
    //单例对象
    private static volatile MapManager mInstance;
    private Activity mActivity;

    /**
     * 创建图层
     */
    private final boolean USE_CACHE = false;
    private String filePath = Environment.getExternalStorageDirectory() + "/" + "EditorMark"+ "/maps/";
    protected GroupLayer baseGroupLayer; // 用于盛放所有基础底图的图层组，便于统一管理

    /**
     * 单例方法
     */
    public static MapManager getInstance() {

        if (mInstance == null) {
            synchronized (MapManager.class) {
                if (mInstance == null) {
                    mInstance = new MapManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     *
     * @param mapView 地图对象
     */
    public void init(Activity activity, NIMapView mapView) {
        mActivity = activity;
        mMapView = mapView;
        if(mMapView!=null){
            mMap = mMapView.getMap();
            mMap.setCompassIcon(R.drawable.icon_map_rotate);
        }

        loadMap();
    }



    public void loadMap() {
        for (MapGroupEnum group : MapGroupEnum.values()) {
            mMap.layers().addGroup(group.ordinal());
        }

        initBaseMap();
    }

    /**
     * 初始化基础底图，自动遍历map文件夹下所有的.map数据加载并显示
     */
    public void initBaseMap() {
        if (baseGroupLayer == null) {

        }
    }


    private void loadTheme(final int styleId) {

    }

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
        MapPosition mapPosition = mMapView.oldMap().getMapPosition();
        mMap.zoomIn();
    }

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
        mMap.zoomOut();
    }


    public void onResume() {
        mMapView.onResume();
    }

    public void onPause() {
        mMapView.onPause();
    }


    private ItemizedLayer.OnItemGestureListener itemGestureListener = new ItemizedLayer.OnItemGestureListener<MarkerInterface>();

    public enum MapGroupEnum {
        BASE_GROUP/*基础底图*/, RASTER_GROUP/*栅格图层组*/,
        VECTOR_GROUP/*矢量图层组*/, TAB_GROUP/*tab图层组*/,
        TRACK_GROUP/*轨迹图层组*/, STATION_GROUP/*基站图层组*/,
        DRAW_GROUP/*用户绘制图层组*/, OTHER_GROUP /*其他图层组*/,
        DATA_GROUP/*数据图层组*/, MARKER_GROUP/*Marker图层组*/,
    }

}

