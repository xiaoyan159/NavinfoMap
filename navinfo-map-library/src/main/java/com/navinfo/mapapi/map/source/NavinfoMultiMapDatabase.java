package com.navinfo.mapapi.map.source;

import org.oscim.core.Tile;
import org.oscim.layers.tile.MapTile;
import org.oscim.tiling.ITileDataSink;
import org.oscim.tiling.QueryResult;
import org.oscim.tiling.TileDataSink;
import org.oscim.tiling.source.HttpEngine;
import org.oscim.tiling.source.ITileDecoder;
import org.oscim.tiling.source.UrlTileDataSource;
import org.oscim.tiling.source.UrlTileSource;
import org.oscim.tiling.source.mapfile.MapDatabase;
import org.oscim.tiling.source.mapfile.MapReadResult;

import java.util.ArrayList;
import java.util.List;

/*
 *com.navinfo.map.source
 *zhjch
 *2021/9/17
 *10:12
 *说明（）
 */
class NavinfoMultiMapDatabase extends UrlTileDataSource {
    private final List<MapDatabase> mapDatabases = new ArrayList<>();

    public NavinfoMultiMapDatabase(UrlTileSource tileSource, ITileDecoder tileDecoder, HttpEngine conn) {
        super(tileSource, tileDecoder, conn);
    }

    public boolean add(MapDatabase mapDatabase) {
        if (mapDatabases.contains(mapDatabase)) {
            throw new IllegalArgumentException("Duplicate map database");
        }
        return mapDatabases.add(mapDatabase);
    }

    @Override
    public void query(MapTile tile, ITileDataSink sink) {
        TileDataSink dataSink = new TileDataSink(sink);
        for (MapDatabase mapDatabase : mapDatabases) {
            if (mapDatabase.supportsTile(tile))
                mapDatabase.query(tile, dataSink);
        }
        if (dataSink.getResult() == null || dataSink.getResult() != QueryResult.SUCCESS) {
            super.query(tile, sink);
        }else{
            sink.completed(dataSink.getResult());
        }
    }

    @Override
    public void dispose() {
        for (MapDatabase mapDatabase : mapDatabases) {
            mapDatabase.dispose();
        }
        super.dispose();
    }

    @Override
    public void cancel() {
        for (MapDatabase mapDatabase : mapDatabases) {
            mapDatabase.cancel();
        }
        super.cancel();
    }

    public MapReadResult readLabels(Tile tile) {
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDatabase mdb : mapDatabases) {
            if (mdb.supportsTile(tile)) {
                MapReadResult result = mdb.readLabels(tile);
                if (result == null) {
                    continue;
                }
                boolean isWater = mapReadResult.isWater & result.isWater;
                mapReadResult.isWater = isWater;
                mapReadResult.add(result, false);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readLabels(Tile upperLeft, Tile lowerRight) {
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDatabase mdb : mapDatabases) {
            if (mdb.supportsTile(upperLeft)) {
                MapReadResult result = mdb.readLabels(upperLeft, lowerRight);
                if (result == null) {
                    continue;
                }
                boolean isWater = mapReadResult.isWater & result.isWater;
                mapReadResult.isWater = isWater;
                mapReadResult.add(result, false);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readMapData(Tile tile) {
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDatabase mdb : mapDatabases) {
            if (mdb.supportsTile(tile)) {
                MapReadResult result = mdb.readMapData(tile);
                if (result == null) {
                    continue;
                }
                boolean isWater = mapReadResult.isWater & result.isWater;
                mapReadResult.isWater = isWater;
                mapReadResult.add(result, false);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readMapData(Tile upperLeft, Tile lowerRight) {
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDatabase mdb : mapDatabases) {
            if (mdb.supportsTile(upperLeft)) {
                MapReadResult result = mdb.readMapData(upperLeft, lowerRight);
                if (result == null) {
                    continue;
                }
                boolean isWater = mapReadResult.isWater & result.isWater;
                mapReadResult.isWater = isWater;
                mapReadResult.add(result, false);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readPoiData(Tile tile) {
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDatabase mdb : mapDatabases) {
            if (mdb.supportsTile(tile)) {
                MapReadResult result = mdb.readPoiData(tile);
                if (result == null) {
                    continue;
                }
                boolean isWater = mapReadResult.isWater & result.isWater;
                mapReadResult.isWater = isWater;
                mapReadResult.add(result, false);
            }
        }
        return mapReadResult;
    }

    public MapReadResult readPoiData(Tile upperLeft, Tile lowerRight) {
        MapReadResult mapReadResult = new MapReadResult();
        for (MapDatabase mdb : mapDatabases) {
            if (mdb.supportsTile(upperLeft)) {
                MapReadResult result = mdb.readPoiData(upperLeft, lowerRight);
                if (result == null) {
                    continue;
                }
                boolean isWater = mapReadResult.isWater & result.isWater;
                mapReadResult.isWater = isWater;
                mapReadResult.add(result, false);
            }
        }
        return mapReadResult;
    }

    public boolean supportsTile(Tile tile) {
        for (MapDatabase mdb : mapDatabases) {
            if (mdb.supportsTile(tile)) {
                return true;
            }
        }
        return false;
    }
}
