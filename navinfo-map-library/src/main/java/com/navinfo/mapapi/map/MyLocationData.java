package com.navinfo.mapapi.map;

import android.location.Location;

/**
 * 定位数据
 */
public class MyLocationData extends Location {
    private double lat, lon;

    public MyLocationData(String provider) {
        super(provider);
    }

    public MyLocationData(Location l) {
        super(l);
    }
}
