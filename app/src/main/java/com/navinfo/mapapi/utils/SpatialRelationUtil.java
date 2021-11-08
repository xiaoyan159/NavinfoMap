package com.navinfo.mapapi.utils;

import com.navinfo.mapapi.model.LatLng;

/**
 * 判断空间关系工具
 */
public class SpatialRelationUtil extends Object{

    /**
     * 构造函数
     */
    public SpatialRelationUtil() {
    }

    /**返回某点距线上最近距离的点
     * @param mPoints
     * @param point
     * @return
     */
    static LatLng getNearestDistancePointFromLine(java.util.List<LatLng> mPoints, LatLng point){

        return null;
    }

    /**返回某点距线上最近的点
     * @param mPoints
     * @param point
     * @return
     */
    static LatLng 	getNearestPointFromLine(java.util.List<LatLng> mPoints, LatLng point){
        return null
    }

    /**
     *     判断圆形是否包含传入的经纬度点
     * @param center
     * @param radius
     * @param point
     * @return
     */
    static boolean 	isCircleContainsPoint(LatLng center, int radius, LatLng point){
        return false;
    }

    /**
     * 返回一个点是否在一个多边形区域内
     * @param mPoints
     * @param point
     * @return
     */
    static boolean 	isPolygonContainsPoint(java.util.List<LatLng> mPoints, LatLng point){
        return false;
    }

}
