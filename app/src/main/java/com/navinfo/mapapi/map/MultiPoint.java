package com.navinfo.mapapi.map;

import android.os.Bundle;

/**
 * 定义地图 MultiPoint 图层
 */
public final class MultiPoint extends Overlay {

    protected MultiPoint() {
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
