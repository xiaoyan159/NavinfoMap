package com.navinfo.navimap.example;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.navinfo.mapapi.MapManager;
import com.navinfo.mapapi.map.NIMapView;

/**
 * 基础地图Activity
 * */
public class BaseMapActivity extends Activity {
    protected NIMapView niMapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_map);

        niMapView = (NIMapView)findViewById(R.id.mapView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        niMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        niMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        niMapView.onDestroy();
    }
}
