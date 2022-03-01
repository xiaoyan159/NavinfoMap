package com.navinfo.mapapi.utils;

public abstract class CacheTileProgress {
    private int layerId; // layerId 当前下载的图层数
    private int layerCount; // layerCount 总的图层个数

    public int getLayerId() {
        return layerId;
    }

    public void setLayerId(int layerId) {
        this.layerId = layerId;
    }

    public int getLayerCount() {
        return layerCount;
    }

    public void setLayerCount(int layerCount) {
        this.layerCount = layerCount;
    }

    /**
     * 缓存栅格数据的回调
     * @param successCount 当前图层缓存成功的瓦片个数
     * @param failCount 当前图层缓存失败的瓦片个数
     * @param  maxCount 当前图层总的瓦片个数
     * @param layerId 当前下载的图层数
     * @param layerCount 总的图层个数
     * */
    public abstract void onProgress(int successCount, int failCount, int maxCount, int layerId, int layerCount);
}
