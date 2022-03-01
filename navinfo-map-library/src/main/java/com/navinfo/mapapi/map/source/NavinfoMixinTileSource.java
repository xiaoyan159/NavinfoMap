package com.navinfo.mapapi.map.source;

import org.oscim.tiling.ITileDataSource;
import org.oscim.tiling.TileSource;

/**
 * 混合显示在线和离线矢量瓦片数据的tileSource
 * */
public class NavinfoMixinTileSource extends TileSource {
    @Override
    public ITileDataSource getDataSource() {
        return null;
    }

    @Override
    public OpenResult open() {
        return null;
    }

    @Override
    public void close() {

    }
}
