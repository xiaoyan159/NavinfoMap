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

import org.oscim.core.MapElement;
import org.oscim.core.Tag;
import org.oscim.core.Tile;
import org.oscim.tiling.ITileDataSource;
import org.oscim.tiling.source.UrlTileDataSource;
import org.oscim.tiling.source.UrlTileSource;
import org.oscim.tiling.source.geojson.GeojsonTileSource;
import org.oscim.utils.FastMath;

import java.util.LinkedHashMap;
import java.util.Map;

public class NavinfoGeoJsonEncodeTileSource extends GeojsonTileSource {

    private static final String DEFAULT_URL = "http://epohvqjxts85k6u-port.ayiqdpfs.cloud.app.ncloud.navinfo.com/map/online/geo";
    private static final String DEFAULT_PATH = "x={X}&y={Y}&z={Z}";
    private static final UrlTileSource.TileUrlFormatter mTileUrlFormatter = URL_FORMATTER;

    public static class Builder<T extends Builder<T>> extends UrlTileSource.Builder<T> {
        private String locale = "";

        public Builder() {
            super(DEFAULT_URL, DEFAULT_PATH);
            keyName("api_key");
            overZoom(17);
        }

        public T locale(String locale) {
            this.locale = locale;
            return self();
        }

        @Override
        public NavinfoGeoJsonEncodeTileSource build() {
            return new NavinfoGeoJsonEncodeTileSource(this);
        }
    }

    @Override
    public String getTileUrl(Tile tile) {
        StringBuilder sb = new StringBuilder();
        sb.append(DEFAULT_URL).append("?").append(mTileUrlFormatter.formatTilePath(this, tile));
        return sb.toString();
    }

    @Override
    public ITileDataSource getDataSource() {
        return new UrlTileDataSource(this, new NavinfoGeoJsonTileDecoder(this), getHttpEngine());
    }

    @SuppressWarnings("rawtypes")
    public static Builder<?> builder() {
        return new Builder();
    }

    private static Map<String, Tag> mappings = new LinkedHashMap<>();

    private static Tag addMapping(String key, String val) {
        Tag tag = new Tag(key, val);
        mappings.put(key + "=" + val, tag);
        return tag;
    }

    private final String locale;

    public NavinfoGeoJsonEncodeTileSource(Builder<?> builder) {
        super(builder);
        this.locale = builder.locale;
    }

    public NavinfoGeoJsonEncodeTileSource() {
        this(builder());
    }

    public NavinfoGeoJsonEncodeTileSource(String urlString) {
        this(builder().url(urlString));
    }

    @Override
    public void decodeTags(MapElement mapElement, Map<String, Object> properties) {
        boolean hasName = false;
        String fallbackName = null;

        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String val = (value instanceof String) ? (String) value : String.valueOf(value);

            if (key.startsWith(Tag.KEY_NAME)) {
                int len = key.length();
                if (len == 4) {
                    fallbackName = val;
                    continue;
                }
                if (len < 7)
                    continue;
                if (locale.equals(key.substring(5))) {
                    hasName = true;
                    mapElement.tags.add(new Tag(Tag.KEY_NAME, val, false));
                }
                continue;
            }

            Tag tag = mappings.get(key + "=" + val);
            if (tag == null)
                tag = addMapping(key, val);
            mapElement.tags.add(tag);
        }

        if (!hasName && fallbackName != null)
            mapElement.tags.add(new Tag(Tag.KEY_NAME, fallbackName, false));

        // Calculate height of building parts
        if (!properties.containsKey(Tag.KEY_HEIGHT)) {
            if (properties.containsKey(Tag.KEY_VOLUME) && properties.containsKey(Tag.KEY_AREA)) {
                Object volume = properties.get(Tag.KEY_VOLUME);
                String volumeStr = (volume instanceof String) ? (String) volume : String.valueOf(volume);
                Object area = properties.get(Tag.KEY_AREA);
                String areaStr = (area instanceof String) ? (String) area : String.valueOf(area);
                float height = Float.parseFloat(volumeStr) / Float.parseFloat(areaStr);
                String heightStr = String.valueOf(FastMath.round2(height));
                mapElement.tags.add(new Tag(Tag.KEY_HEIGHT, heightStr, false));
            }
        }
    }
}
