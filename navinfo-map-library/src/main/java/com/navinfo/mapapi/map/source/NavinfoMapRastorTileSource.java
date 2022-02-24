package com.navinfo.mapapi.map.source;

import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.core.Tile;
import org.oscim.tiling.ITileDataSink;
import org.oscim.tiling.ITileDataSource;
import org.oscim.tiling.source.ITileDecoder;
import org.oscim.tiling.source.LwHttp;
import org.oscim.tiling.source.UrlTileDataSource;
import org.oscim.tiling.source.UrlTileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/*
 *com.nmp.map.source
 *zhjch
 *2021/7/21
 *9:12
 *说明（）
 */
public class NavinfoMapRastorTileSource extends UrlTileSource {

    //    private static final String DEFAULT_URL = NavinfoSourecs.GAOFENMAP.build().getUrl().toString();
    private static final String DEFAULT_PATH = "z={Z}&x={X}&y={Y}";
    private boolean isTMSProtocol = true; // 是否为TMS协议，TMS协议与XYZ协议在Y轴的定义稍有不同

    static final Logger log = LoggerFactory.getLogger(LwHttp.class);

    public static class Builder<T extends Builder<T>> extends UrlTileSource.Builder<T> {
        private boolean isTMSProtocol = true;
        public Builder(String url) {
            super(url, DEFAULT_PATH);
            overZoom(2);
        }

        public Builder isTMSProtocol(boolean tms) {
            this.isTMSProtocol = tms;
            return this;
        }

        @Override
        public NavinfoMapRastorTileSource build() {
            return new NavinfoMapRastorTileSource(this);
        }
    }


    protected NavinfoMapRastorTileSource(Builder<?> builder) {
        super(builder);
        this.isTMSProtocol = builder.isTMSProtocol;
    }

    @SuppressWarnings("rawtypes")
    public static Builder<?> builder(String url) {
        return new Builder(url);
    }


    @Override
    public ITileDataSource getDataSource() {
        return new UrlTileDataSource(this, new TileDecoder(), getHttpEngine());
    }

    public static class TileDecoder implements ITileDecoder {

        @Override
        public boolean decode(Tile tile, ITileDataSink sink, InputStream is)
                throws IOException {

            Bitmap bitmap = CanvasAdapter.decodeBitmap(is);
            if (!bitmap.isValid()) {
                log.debug("{} invalid bitmap", tile);
                return false;
            }
            sink.setTileImage(bitmap);

            return true;
        }
    }

    public String getTileUrl(Tile tile) {
        String url = super.getTileUrl(tile);
        int y = tile.tileY;
        if (!isTMSProtocol) {
            y = (int) Math.pow(2, tile.zoomLevel) - 1 - tile.tileY;
        }
        url = url.replace("{X}", String.valueOf(tile.tileX));
        url = url.replace("{Y}", String.valueOf(y));
        url = url.replace("{Z}", String.valueOf(tile.zoomLevel));
        url = url.replace("{x}", String.valueOf(tile.tileX));
        url = url.replace("{y}", String.valueOf(y));
        url = url.replace("{z}", String.valueOf(tile.zoomLevel));
        return url;
    }

}
