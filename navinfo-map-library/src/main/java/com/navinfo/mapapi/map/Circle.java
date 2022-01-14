package com.navinfo.mapapi.map;

import android.os.Bundle;

/**
 *定义在地图上画一个圆的类
 */
public final class Circle extends Overlay{

    protected Circle() {
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
}
