/*
 * Copyright 2018 devemux86
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

public class SMapRastorTileSource extends UrlTileSource {
    // http://smap.navinfo.com:7777/smap-raster-map/raster/basemap/tile?specId=902157&ak=ff0d2a9244c609d1b4115183e&layer=navinfo_world&z=17&x=107918&y=49664
    private static final String DEFAULT_URL = "http://smap.navinfo.com/gateway/smap-raster-map/raster/basemap/tile";
//    private static final String DEFAULT_URL = "http://smap.navinfo.com:7777/smap-raster-map/raster/basemap/tile";

    private static final String DEFAULT_PATH = "z={Z}&x={X}&y={Y}";

    static final Logger log = LoggerFactory.getLogger(LwHttp.class);

    public static class Builder<T extends Builder<T>> extends UrlTileSource.Builder<T> {

        public Builder() {
            super(DEFAULT_URL, DEFAULT_PATH);
            keyName("ak");
            apiKey("ff0d2a9244c609d1b4115183e");
            overZoom(2);
        }

        @Override
        public SMapRastorTileSource build() {
            return new SMapRastorTileSource(this);
        }
    }


    protected SMapRastorTileSource(Builder<?> builder) {
        super(builder);
    }

    @SuppressWarnings("rawtypes")
    public static Builder<?> builder() {
        return new Builder();
    }

    /**
     * Create BitmapTileSource for 'url'
     * <p/>
     * By default path will be formatted as: url/z/x/y.png
     * Use e.g. setExtension(".jpg") to overide ending or
     * implement getUrlString() for custom formatting.
     */
    public SMapRastorTileSource(String url, int zoomMin, int zoomMax) {
        this(DEFAULT_URL, DEFAULT_PATH, zoomMin, zoomMax);
    }

    public SMapRastorTileSource(String url, int zoomMin, int zoomMax, String extension) {
        this(DEFAULT_URL, DEFAULT_PATH + extension, zoomMin, zoomMax);
    }

    public SMapRastorTileSource(String url, String tilePath, int zoomMin, int zoomMax) {
        super(builder()
                .url(url)
                .tilePath(tilePath)
                .zoomMin(zoomMin)
                .zoomMax(zoomMax));
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

    @Override
    public String getTileUrl(Tile tile) {
        StringBuilder sb = new StringBuilder();
        StringBuilder urlSB=sb.append(getUrl()).append("?").append(URL_FORMATTER.formatTilePath(this, tile));
        sb.append("&").append("ak").append("=").append("ff0d2a9244c609d1b4115183e");
        urlSB.append("&").append("specId").append("=").append("902157");
        urlSB.append("&").append("layer").append("=").append("navinfo_world");
        return urlSB.toString();
    }
}
