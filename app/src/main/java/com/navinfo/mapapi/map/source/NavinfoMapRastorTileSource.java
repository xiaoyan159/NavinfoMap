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

    static final Logger log = LoggerFactory.getLogger(LwHttp.class);

    public static class Builder<T extends Builder<T>> extends UrlTileSource.Builder<T> {

        public Builder(String url) {
            super(url, DEFAULT_PATH);
            overZoom(2);
        }

        @Override
        public NavinfoMapRastorTileSource build() {
            return new NavinfoMapRastorTileSource(this);
        }
    }


    protected NavinfoMapRastorTileSource(Builder<?> builder) {
        super(builder);
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
        String url = getUrl().toString();
        int y = tile.tileY;
        if (url.contains("map.gtimg.com")) {
            y = (int) Math.pow(2, tile.zoomLevel) - 1 - tile.tileY;
        }
        url = url.replace("{X}", String.valueOf(tile.tileX));
        url = url.replace("{Y}", String.valueOf(y));
        url = url.replace("{Z}", String.valueOf(tile.zoomLevel));
        url = url.replace("{x}", String.valueOf(tile.tileX));
        url = url.replace("{y}", String.valueOf(y));
        url = url.replace("{z}", String.valueOf(tile.zoomLevel));
//        url = url.replace("{z}",   String.valueOf(tile.zoomLevel));
//        StringBuilder sb = new StringBuilder();
//        String fuhao = "?";
//        if (getUrl().toString().contains("?")) {
//            fuhao = "&";
//        }
//        StringBuilder urlSB = sb.append(getUrl()).append(fuhao).append(URL_FORMATTER.formatTilePath(this, tile));
//        Log.e("jingo",urlSB.toString());
        return url;
    }

}
