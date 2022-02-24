package com.navinfo.navimap.example;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.navinfo.mapapi.map.NIMapView;

import java.util.List;

public class MapLocaitionActivity extends Activity {
    private NIMapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        mapView = findViewById(R.id.mapview_locaiton);
        mapView.getMap().setMyLocationEnabled(this, true);

        LocationManager m_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = m_locationManager.getProviders(true);
        String m_provider = null;
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            m_provider = LocationManager.NETWORK_PROVIDER;//NETWORK_PROVIDER GPS_PROVIDER
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            m_provider = LocationManager.NETWORK_PROVIDER;
        }
        if (m_provider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MapLocaitionActivity.this, "没有定位权限", Toast.LENGTH_SHORT).show();
                return;
            }
            m_locationManager.requestLocationUpdates(m_provider, 3000, 0, locationListener);
            Location location = m_locationManager.getLastKnownLocation(m_provider);
            if(location!=null){
                //直接获取
                mapView.getMap().setMyLocationData(location);
            } else {
                mapView.getMap().setMyLocationData(40, 100, 30);
            }
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mapView.getMap().setMyLocationData(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
