package com.navinfo.mapapi.model;

/**
 * 地理位置
 */
public final class LatLng extends Object{

    /**
     * 纬度
     */
    double 	latitude;

    /**
     * 纬度
     */
    int latitudeE6;
    /**
     * 经度
     */
    double 	longitude;

    /**
     * 经度
     */
    int	longitudeE6;

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

        this.latitudeE6 = (int)(latitude*1000000);
        this.longitudeE6 = (int)(longitude*1000000);
    }

    public LatLng(int latitudeE6, int longitudeE6) {
        this.latitudeE6 = latitudeE6;
        this.longitudeE6 = longitudeE6;

        this.latitude = (double) (latitudeE6/1000000);
        this.longitude = (double) (longitudeE6/1000000);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getLatitudeE6() {
        return latitudeE6;
    }

    public void setLatitudeE6(int latitudeE6) {
        this.latitudeE6 = latitudeE6;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getLongitudeE6() {
        return longitudeE6;
    }

    public void setLongitudeE6(int longitudeE6) {
        this.longitudeE6 = longitudeE6;
    }
}
