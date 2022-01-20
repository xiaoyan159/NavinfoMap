package com.navinfo.mapapi.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.navinfo.mapapi.model.LatLng;

import java.util.List;

/**
 * 多边形覆盖物
 */
public final class Polygon extends Overlay{

    /**
     * 设置多边形填充颜色
     */
    private int color;

    /**
     * 获取多边形坐标点列表
     */
    private List<LatLng> points;


    protected Polygon() {
        super();
    }

    @Override
    public Bundle getExtraInfo() {
        return null;
    }

    @Override
    public int getZIndex() {
        return 0;
    }

    @Override
    public boolean isRemoved() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void remove() {

    }

    @Override
    public void setExtraInfo(Bundle extraInfo) {

    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public void setZIndex(int zIndex) {

    }

    /**
     * 获取多边形填充颜色
     * @return
     */
    public int 	getFillColor(){
        return this.color;
    }

    /**
     * 获取多边形坐标点列表
     * @return
     */
    public java.util.List<LatLng> 	getPoints(){
        return this.points;
    }

    /**
     *设置多边形填充颜色
     *  @param color
     */
    public void 	setFillColor(int color){
        this.color = color;
    }

    /**
     * 设置多边形坐标点列表
     * @param points
     */
    public void 	setPoints(java.util.List<LatLng> points){
        this.points = points;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
