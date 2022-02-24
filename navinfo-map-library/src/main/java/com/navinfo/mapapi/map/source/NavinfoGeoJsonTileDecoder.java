/*
 * Copyright 2014 Hannes Janetzek
 *
 * This file is part of the OpenScienceMap project (http://www.opensciencemap.org).
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

import static org.oscim.core.MercatorProjection.latitudeToY;
import static org.oscim.core.MercatorProjection.longitudeToX;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.navinfo.mapapi.utils.ZipUtils;

import org.oscim.core.GeometryBuffer.GeometryType;
import org.oscim.core.MapElement;
import org.oscim.core.Tile;
import org.oscim.tiling.ITileDataSink;
import org.oscim.tiling.source.ITileDecoder;
import org.oscim.utils.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

public class NavinfoGeoJsonTileDecoder implements ITileDecoder {
    static final Logger log = LoggerFactory.getLogger(NavinfoGeoJsonTileDecoder.class);

    private final MapElement mMapElement;
    private final NavinfoGeoJsonEncodeTileSource mTileSource;
    private final LinkedHashMap<String, Object> mTagMap;
    private final JsonFactory mJsonFactory;

    private static final char[] FIELD_FEATURES = "features".toCharArray();
    private static final char[] FIELD_GEOMETRY = "geometry".toCharArray();
    private static final char[] FIELD_PROPERTIES = "properties".toCharArray();
    private static final char[] FIELD_COORDINATES = "coordinates".toCharArray();
    private static final char[] FIELD_TYPE = "type".toCharArray();

    private static final char[] LINETRING = "LineString".toCharArray();
    private static final char[] POLYGON = "Polygon".toCharArray();
    private static final char[] POINT = "Point".toCharArray();
    private static final char[] MULTI_LINESTRING = "MultiLineString".toCharArray();
    private static final char[] MULTI_POLYGON = "MultiPolygon".toCharArray();
    private static final char[] MULTI_POINT = "MultiPoint".toCharArray();

    private ITileDataSink mTileDataSink;

    private double mTileY, mTileX, mTileScale;

    public NavinfoGeoJsonTileDecoder(NavinfoGeoJsonEncodeTileSource tileSource) {
        mTileSource = tileSource;
        mTagMap = new LinkedHashMap<String, Object>();
        mJsonFactory = new JsonFactory();

        mMapElement = new MapElement();
        mMapElement.layer = 5;
    }

    @Override
    public boolean decode(Tile tile, ITileDataSink sink, InputStream is) throws IOException {
        mTileDataSink = sink;
        mTileScale = 1 << tile.zoomLevel;
        mTileX = tile.tileX / mTileScale;
        mTileY = tile.tileY / mTileScale;
        mTileScale *= Tile.SIZE;

        String unzipStr = ZipUtils.unzipInputStream(is);
        JsonParser jp = mJsonFactory.createParser(unzipStr);

        for (JsonToken t; (t = jp.nextToken()) != null; ) {
            if (t == FIELD_NAME) {
                if (match(jp, FIELD_FEATURES)) {
                    if (jp.nextToken() != START_ARRAY)
                        continue;

                    while ((t = jp.nextToken()) != null) {
                        if (t == START_OBJECT)
                            parseFeature(jp);

                        if (t == END_ARRAY)
                            break;
                    }
                }
            }
        }
        return true;
    }

    private void parseFeature(JsonParser jp)
            throws JsonParseException, IOException {

        mMapElement.clear();
        mMapElement.tags.clear();
        mTagMap.clear();

        for (JsonToken t; (t = jp.nextToken()) != null; ) {
            if (t == FIELD_NAME) {
                if (match(jp, FIELD_GEOMETRY)) {
                    if (jp.nextToken() == START_OBJECT)
                        parseGeometry(jp);
                }

                if (match(jp, FIELD_PROPERTIES)) {
                    if (jp.nextToken() == START_OBJECT)
                        parseProperties(jp);
                }
                continue;
            }
            if (t == END_OBJECT)
                break;
        }

        //add tag information
        mTileSource.decodeTags(mMapElement, mTagMap);
        if (mMapElement.tags.size() == 0)
            return;

        mTileSource.postGeomHook(mMapElement);

        if (mMapElement.type == GeometryType.NONE)
            return;

        //process this element
        mTileDataSink.process(mMapElement);
    }

    private void parseProperties(JsonParser jp)
            throws JsonParseException, IOException {
        for (JsonToken t; (t = jp.nextToken()) != null; ) {
            if (t == FIELD_NAME) {
                String text = jp.getCurrentName();

                t = jp.nextToken();
                if (t == VALUE_STRING) {
                    mTagMap.put(text, jp.getText());
                } else if (t == VALUE_NUMBER_INT) {
                    mTagMap.put(text, jp.getNumberValue());
                }
                continue;
            }
            if (t == END_OBJECT)
                break;
        }
    }

    private void parseGeometry(JsonParser jp)
            throws JsonParseException, IOException {

        boolean multi = false;
        GeometryType type = GeometryType.NONE;

        for (JsonToken t; (t = jp.nextToken()) != null; ) {
            if (t == FIELD_NAME) {
                if (match(jp, FIELD_COORDINATES)) {
                    if (jp.nextToken() != START_ARRAY)
                        continue;
                    if (multi) {
                        parseMulti(jp, type);
                    } else {
                        if (type == GeometryType.POLY)
                            parsePolygon(jp);

                        if (type == GeometryType.LINE)
                            parseLineString(jp);

                        if (type == GeometryType.POINT)
                            parseCoordinate(jp);

                    }
                } else if (match(jp, FIELD_TYPE)) {
                    multi = false;

                    jp.nextToken();

                    if (match(jp, LINETRING))
                        type = GeometryType.LINE;
                    else if (match(jp, POLYGON))
                        type = GeometryType.POLY;
                    else if (match(jp, POINT))
                        type = GeometryType.POINT;
                    else if (match(jp, MULTI_LINESTRING)) {
                        type = GeometryType.LINE;
                        multi = true;
                    } else if (match(jp, MULTI_POLYGON)) {
                        type = GeometryType.POLY;
                        multi = true;
                    } else if (match(jp, MULTI_POINT)) {
                        type = GeometryType.POINT;
                        multi = true;
                    }

                    if (type == GeometryType.POINT)
                        mMapElement.startPoints();
                }
                continue;
            }
            if (t == END_OBJECT)
                break;
        }
    }

    private void parseMulti(JsonParser jp, GeometryType type)
            throws JsonParseException, IOException {

        for (JsonToken t; (t = jp.nextToken()) != null; ) {
            if (t == END_ARRAY)
                break;

            if (t == START_ARRAY) {
                if (type == GeometryType.POLY)
                    parsePolygon(jp);

                else if (type == GeometryType.LINE)
                    parseLineString(jp);

                else if (type == GeometryType.POINT)
                    parseCoordinate(jp);
                ;

            } else {
                //....
            }
        }
    }

    private void parsePolygon(JsonParser jp)
            throws JsonParseException, IOException {
        int ring = 0;

        for (JsonToken t; (t = jp.nextToken()) != null; ) {
            if (t == START_ARRAY) {
                if (ring == 0)
                    mMapElement.startPolygon();
                else
                    mMapElement.startHole();

                ring++;
                parseCoordSequence(jp);
                mMapElement.removeLastPoint();
                continue;
            }

            if (t == END_ARRAY)
                break;
        }
    }

    private void parseLineString(JsonParser jp)
            throws JsonParseException, IOException {
        mMapElement.startLine();
        parseCoordSequence(jp);
    }

    private void parseCoordSequence(JsonParser jp)
            throws JsonParseException, IOException {

        for (JsonToken t; (t = jp.nextToken()) != null; ) {

            if (t == START_ARRAY) {
                parseCoordinate(jp);
                continue;
            }

            if (t == END_ARRAY)
                break;

        }
    }

    private void parseCoordinate(JsonParser jp)
            throws JsonParseException, IOException {
        int pos = 0;
        double x = 0, y = 0; //, z = 0;

        for (JsonToken t; (t = jp.nextToken()) != null; ) {
            if (t == VALUE_NUMBER_FLOAT || t == VALUE_NUMBER_INT) {

                // avoid String allocation (by getDouble...)
                char[] val = jp.getTextCharacters();
                int offset = jp.getTextOffset();
                int length = jp.getTextLength();
                double c = ArrayUtils.parseNumber(val, offset, offset + length);

                if (pos == 0)
                    x = c;
                if (pos == 1)
                    y = c;
                //if (pos == 2)
                //z = c;

                pos++;
                continue;
            }

            if (t == END_ARRAY)
                break;
        }

        mMapElement.addPoint((float) ((longitudeToX(x) - mTileX) * mTileScale),
                (float) ((latitudeToY(y) - mTileY) * mTileScale));

    }

    private static final boolean match(JsonParser jp, char[] fieldName)
            throws JsonParseException, IOException {

        int length = jp.getTextLength();
        if (length != fieldName.length)
            return false;

        char[] val = jp.getTextCharacters();
        int offset = jp.getTextOffset();

        for (int i = 0; i < length; i++) {
            if (fieldName[i] != val[i + offset])
                return false;
        }

        return true;
    }
}
