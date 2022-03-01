package com.navinfo.mapapi.utils;

import org.oscim.backend.CanvasAdapter;
import org.oscim.core.GeoPoint;
import org.oscim.core.MercatorProjection;
import org.oscim.core.Tile;
import org.oscim.tiling.ITileCache;
import org.oscim.tiling.source.HttpEngine;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.UrlTileSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TileDownloader {
    private static TileDownloader instance;
    private boolean canDownloadRasterTile; // 是否可下载Tile数据
    public static TileDownloader getInstance() {
        if (instance == null) {
            instance = new TileDownloader();
        }
        return instance;
    }

    /**
     * 下载指定的tile文件
     */
    private boolean download(Tile mTile, UrlTileSource mUrlTileSource, HttpEngine mHttpEngine) {
        if (mUrlTileSource != null) {
            ITileCache mTileCache = mUrlTileSource.tileCache;
            ITileCache.TileReader c = mTileCache.getTile(mTile);
            if (c != null) {
                return true;
            }
            if (mUrlTileSource.getTilePath()[mUrlTileSource.getTilePath().length - 1].endsWith(".pbf") || mUrlTileSource.getTilePath()[mUrlTileSource.getTilePath().length - 1].endsWith("json")) {
                return false;
            }
            ITileCache.TileWriter cacheWriter = null;
            try {
                mHttpEngine.sendRequest(mTile);
                InputStream is = mHttpEngine.read();
                if (is != null && is.available() > 0) {
                    cacheWriter = mTileCache.writeTile(mTile);
                    mHttpEngine.setCache(cacheWriter.getOutputStream());
                    CanvasAdapter.decodeBitmap(is);
                } else {
                    return false;
                }
            } catch (SocketException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (cacheWriter != null) {
                    cacheWriter.complete(true);
                }
                mHttpEngine.requestCompleted(true);
                return true;
            }
        }
        return false;
    }

    /**
     * 根据屏幕中的rect坐标计算需要下载的tile的集合
     */
    private List<Tile> getTileArrayFromRect(GeoPoint leftTopGeoPoint, GeoPoint rightBottomGeoPoint, byte startZoomLevel, byte endZoomLevel) {
        List<Tile> tileList = new ArrayList<>();

        for (byte i = startZoomLevel; i <= endZoomLevel; i++) {
            int tileNumLeft = MercatorProjection.longitudeToTileX(leftTopGeoPoint.getLongitude(), i);
            int tileNumRight = MercatorProjection.longitudeToTileX(rightBottomGeoPoint.getLongitude(), i);
            int tileNumTop = MercatorProjection.latitudeToTileY(leftTopGeoPoint.getLatitude(), i);
            int tileNumBottom = MercatorProjection.latitudeToTileY(rightBottomGeoPoint.getLatitude(), i);

            int currentMaxXY = 2 << i;
            if (tileNumRight < tileNumLeft) {
                tileNumRight += tileNumRight + currentMaxXY;
            }
            if (tileNumBottom < tileNumTop) {
                tileNumBottom += tileNumBottom + currentMaxXY;
            }

            for (int tileX = tileNumLeft; tileX <= tileNumRight; tileX++) {
                for (int tileY = tileNumTop; tileY <= tileNumBottom; tileY++) {
                    tileList.add(new Tile(tileX % currentMaxXY, tileY % currentMaxXY, i));
                }
            }
        }
        return tileList;
    }

    public Callable downloadRasterTile(UrlTileSource urlTileSource, GeoPoint leftTopGeoPoint, GeoPoint rightBottomGeoPoint, byte startZoomLevel, byte endZoomLevel, CacheTileProgress progress) {
        canDownloadRasterTile = true;
        return new Callable() {
            @Override
            public Object call(){
                List<Tile> tileList = getTileArrayFromRect(leftTopGeoPoint, rightBottomGeoPoint, startZoomLevel, endZoomLevel);
                HttpEngine httpEngine = new OkHttpEngine.OkHttpFactory().create(urlTileSource);
                int successCount = 0;
                int failCount = 0;
                int maxCount = tileList.size();
                for (int i = 0; i < tileList.size(); i++) {
                    if (!canDownloadRasterTile) { // 如果不需要下载tile，则直接关闭循环
                        break;
                    }
                    if (download(tileList.get(i), urlTileSource, httpEngine)) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                    if (progress!=null) {
                        progress.onProgress(successCount, failCount, maxCount, progress.getLayerId(), progress.getLayerCount());
                    }
                }
                return null;
            }
        };
    }

    public void setCanDownloadRasterTile(boolean canDownloadRasterTile) {
        this.canDownloadRasterTile = canDownloadRasterTile;
    }
}
