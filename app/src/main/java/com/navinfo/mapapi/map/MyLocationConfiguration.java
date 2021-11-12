package com.navinfo.mapapi.map;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 配置定位图层显示方式
 */
public class MyLocationConfiguration extends Object implements Cloneable{

    /**
     * 是否允许显示方向信息
     */
    public boolean enableDirection;

    /**
     * 精度圈边框颜色
     */
    public int accuracyCircleStrokeColor;

    /**
     * 定位图层显示方式
     */
    public LocationMode locationMode;

    /**
     * 用户自定义定位图标
     */
    public final BitmapDescriptor customMarker;

    /**
     * 精度圈填充颜色
     */
    public int accuracyCircleFillColor;

    /**
     * 构造函数
     *
     * @param mode
     * @param enableDirection
     * @param customMarker
     */
    MyLocationConfiguration(LocationMode mode, boolean enableDirection, BitmapDescriptor customMarker) {
        this.locationMode = mode;
        this.enableDirection = enableDirection;
        this.customMarker = customMarker;
    }

    MyLocationConfiguration(LocationMode mode, boolean enableDirection, BitmapDescriptor customMarker, int accuracyCircleFillColor, int accuracyCircleStrokeColor) {
        this.locationMode = mode;
        this.enableDirection = enableDirection;
        this.customMarker = customMarker;
        this.accuracyCircleStrokeColor = accuracyCircleStrokeColor;
        this.accuracyCircleFillColor = accuracyCircleFillColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyLocationConfiguration that = (MyLocationConfiguration) o;
        return enableDirection == that.enableDirection &&
                accuracyCircleStrokeColor == that.accuracyCircleStrokeColor &&
                accuracyCircleFillColor == that.accuracyCircleFillColor &&
                locationMode == that.locationMode &&
                Objects.equals(customMarker, that.customMarker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enableDirection, accuracyCircleStrokeColor, locationMode, customMarker, accuracyCircleFillColor);
    }

    @Override
    public String toString() {
        return "MyLocationConfiguration{" +
                "enableDirection=" + enableDirection +
                ", accuracyCircleStrokeColor=" + accuracyCircleStrokeColor +
                ", locationMode=" + locationMode +
                ", customMarker=" + customMarker +
                ", accuracyCircleFillColor=" + accuracyCircleFillColor +
                '}';
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * 定位图层显示方式
     */
    public static enum LocationMode {
        COMPASS/*罗盘态，显示定位方向圈，保持定位图标在地图中心*/,

        FOLLOWING/*跟随态，保持定位图标在地图中心*/,

        NORMAL/*普通态： 更新定位数据时不对地图做任何操作*/

    }
}


