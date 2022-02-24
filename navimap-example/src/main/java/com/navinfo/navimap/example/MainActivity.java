package com.navinfo.navimap.example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.navinfo.mapapi.MapManager;
import com.navinfo.mapapi.layers.NaviLocationLayer;
import com.navinfo.mapapi.map.Marker;
import com.navinfo.mapapi.map.NIMapView;

/**
 * 测试工程
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
//    /**
//     * 创建图层
//     */
//    private NaviLocationLayer mLocationLayer;
//    private NIMapView niMapView;
//    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(com.navinfo.mapapi.R.layout.activity_main);
//        niMapView = (NIMapView)findViewById(com.navinfo.mapapi.R.id.mapView);
//        findViewById(com.navinfo.mapapi.R.id.btn_add).setOnClickListener(this);
//        findViewById(com.navinfo.mapapi.R.id.btn_del).setOnClickListener(this);
//
//        MapManager.getInstance().init(MainActivity.this,niMapView);
//        MapManager.getInstance().loadMap();
//        MapManager.getInstance().changeMapStyle("smap","",5);
//
//        MapManager.getInstance().location();
//
////        niMapView.setZoomControlsPosition(new Point(1400,1400));
//        niMapView.showZoomControls(true);
//        niMapView.setZoomControlPosition(NIMapView.GRAVITY.RIGHT_TOP);
//        niMapView.setCompassPosition(NIMapView.GRAVITY.RIGHT_BOTTOM);
//        niMapView.setLogoPosition(NIMapView.GRAVITY.LEFT_TOP);
    }

    @Override
    public void onClick(View view){

    }
}