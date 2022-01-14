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
    /**
     * 创建图层
     */
    private NaviLocationLayer mLocationLayer;
    private NIMapView niMapView;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_SECURE_SETTINGS,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.RECORD_AUDIO,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            };

            if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 0);
            }
        }

        setContentView(com.navinfo.mapapi.R.layout.activity_main);
        niMapView = (NIMapView)findViewById(com.navinfo.mapapi.R.id.mapView);
        findViewById(com.navinfo.mapapi.R.id.btn_add).setOnClickListener(this);
        findViewById(com.navinfo.mapapi.R.id.btn_del).setOnClickListener(this);

        MapManager.getInstance().init(MainActivity.this,niMapView);
        MapManager.getInstance().loadMap();
        MapManager.getInstance().changeMapStyle("smap","",5);

        MapManager.getInstance().location();

        niMapView.setZoomControlsPosition(new Point(1400,1400));
        niMapView.showZoomControls(true);
        niMapView.showScaleControl(true);
    }

    @Override
    public void onClick(View view){

//        switch (view.getId()){
//            case com.navinfo.mapapi.R.id.btn_del:
//                if(marker!=null){
//                    List<Overlay> list = new ArrayList<>();
//                    list.add(marker);
//                    niMapView.getMap().removeOverLays(list);
//                }
//                break;
//            case com.navinfo.mapapi.R.id.btn_add:
//                NaviAndroidBitmap mCenterMakerBitmap = new NaviAndroidBitmap(BitmapFactory.decodeResource(getResources(), com.navinfo.mapapi.R.mipmap.marker));
//                //MapManager.getInstance().addMarker(new GeoPoint(40.062304, 116.213801),mCenterMakerBitmap);
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(new LatLng(40.062304, 116.213801)).icon(new BitmapDescriptor(mCenterMakerBitmap));
//                marker = (Marker) niMapView.getMap().addOverlay(markerOptions);
//                break;
//        }
    }
}