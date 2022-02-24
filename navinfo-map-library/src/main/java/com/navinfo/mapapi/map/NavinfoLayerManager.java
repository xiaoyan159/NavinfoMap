package com.navinfo.mapapi.map;

import android.os.Environment;

import com.navinfo.mapapi.map.source.NavinfoGeoJsonTileSource;
import com.navinfo.mapapi.map.source.NavinfoMapRastorTileSource;

import org.oscim.layers.GroupLayer;
import org.oscim.layers.Layer;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Map;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.UrlTileSource;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class NavinfoLayerManager {
    private Map vtmMap;
    private String defaultDir = Environment.getExternalStorageDirectory() + "/" + "EditorMark";
    private String defaultLocalMapPath = defaultDir + "/maps/";

    public NavinfoLayerManager(Map vtmMap) {
        this.vtmMap = vtmMap;
    }

    public Layer getRasterTileLayer(String url, String tilePath, boolean useCache) {
        if (this.vtmMap == null) {
            throw new IllegalStateException("无法获取到map对象");
        }
        NavinfoMapRastorTileSource mTileSource = NavinfoMapRastorTileSource.builder(url).tilePath(tilePath).build();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 如果使用缓存
        if (useCache) {
            File cacheDirectory = new File(defaultDir, "tiles-raster");
            int cacheSize = 300 * 1024 * 1024; // 300 MB
            Cache cache = new Cache(cacheDirectory, cacheSize);
            builder.cache(cache);
        }

        mTileSource.setHttpEngine(new OkHttpEngine.OkHttpFactory(builder));
        mTileSource.setHttpRequestHeaders(Collections.singletonMap("User-Agent", "vtm-android-example"));

        BitmapTileLayer rasterLayer = new BitmapTileLayer(this.vtmMap, mTileSource);
        return rasterLayer;
    }

    // 初始化请求在线底图数据
    public Layer getDefaultVectorLayer(boolean useCache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (useCache) {
            // Cache the tiles into file system
            File cacheDirectory = new File(defaultDir, "tiles-vector");
            int cacheSize = 200 * 1024 * 1024; // 200 MB
            Cache cache = new Cache(cacheDirectory, cacheSize);
            builder.cache(cache);
        }

        UrlTileSource tileSource = NavinfoGeoJsonTileSource.builder()
                .apiKey("4wTLZyXcQym31pxC_HGy7Q") // Put a proper API key
                .httpFactory(new OkHttpEngine.OkHttpFactory(builder))
                //.locale("en")
                .build();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJjbGllbnRJZCI6MTAzLCJ1c2VyTmFtZSI6ImFkbWluIiwidXNlcklkIjoxLCJ1c2VyR3JvdXAiOiLnoJTlj5Hpg6giLCJvcmdJZCI6MSwiaWF0IjoxNjMwOTk4MzI5LCJleHAiOjE2MzEwODQ3Mjl9.0wFm8mAA9dCC2FmZj-u1dhxTFDRYx8AqVnh2C88hitk");
        tileSource.setHttpRequestHeaders(headerMap);
        VectorTileLayer l = new VectorTileLayer(this.vtmMap, tileSource);
        return l;
    }
}
