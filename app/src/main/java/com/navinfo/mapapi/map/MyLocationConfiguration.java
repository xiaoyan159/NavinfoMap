package com.navinfo.mapapi.map;

/**
 * 配置定位图层显示方式
 */
public class MyLocationConfiguration {

    /**
     * 是否允许显示方向信息
     */
    public boolean enableDirection;

    /**
     * 精度圈边框颜色
     */
    public int accuracyCircleStrokeColor;

    /**
     *定位图层显示方式
     */
    public LocationMode locationMode;

    /**
     * 用户自定义定位图标
     */
    public final BitmapDescriptor customMarker;

    /**
     * 构造函数
     *
     * @param mode
     * @param enableDirection
     * @param customMarker
     */
    MyLocationConfiguration(LocationMode mode, final boolean enableDirection, BitmapDescriptor customMarker) {
        this.enableDirection = enableDirection;
        this.locationMode = mode;
        this.customMarker = customMarker;
    }

    public static enum LocationMode {
        COMPASS/*罗盘态，显示定位方向圈，保持定位图标在地图中心*/,

        FOLLOWING/*跟随态，保持定位图标在地图中心*/,

        NORMAL/*普通态： 更新定位数据时不对地图做任何操作*/

    }
}


