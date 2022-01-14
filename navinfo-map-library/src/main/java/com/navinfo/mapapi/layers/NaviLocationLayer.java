package com.navinfo.mapapi.layers;

import android.annotation.SuppressLint;
import android.content.Context;

import com.navinfo.mapapi.R;
import com.navinfo.mapapi.canvas.NaviAndroidBitmap;

import org.oscim.android.canvas.AndroidBitmap;
import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.layers.LocationTextureLayer;
import org.oscim.map.Map;
import org.oscim.renderer.LocationCallback;
import org.oscim.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class NaviLocationLayer extends LocationTextureLayer {
    public NaviLocationLayer(Map map) {
        this(map, CanvasAdapter.getScale());
    }

    public NaviLocationLayer(Map map, float scale) {
        super(map, scale);
    }

    public void init(Context mContext) {
    }

    @SuppressLint("ResourceType")
    private void createLocationLayers(Context mContext, Map mMap) {

        InputStream is = null;

        Bitmap bitmapArrow = null;
        try {
            is = mContext.getResources().openRawResource(R.drawable.icon_location_arrow);
            bitmapArrow = CanvasAdapter.decodeBitmap(is, (int) (48 * CanvasAdapter.getScale()), (int) (48 * CanvasAdapter.getScale()), 100);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }

        Bitmap bitmapMarker = null;
        try {
            is = mContext.getResources().openRawResource(com.navinfo.mapapi.R.drawable.icon_location_arrow);
            bitmapMarker = CanvasAdapter.decodeBitmap(is, (int) (48 * CanvasAdapter.getScale()), (int) (48 * CanvasAdapter.getScale()), 100);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }

        // 显示当前位置图层
        LocationTextureLayer mLocationLayer = new LocationTextureLayer(mMap);
        mLocationLayer.locationRenderer.setBitmapArrow(bitmapArrow);
        mLocationLayer.locationRenderer.setBitmapMarker(bitmapMarker);
        mLocationLayer.locationRenderer.setColor(0xffa1dbf5);
        mLocationLayer.locationRenderer.setShowAccuracyZoom(4);
        mLocationLayer.locationRenderer.setCallback(new LocationCallback() {
            @Override
            public boolean hasRotation() {
                return true;
            }

            @Override
            public float getRotation() {
                return 0;
            }
        });
        mLocationLayer.setEnabled(false); // 默认开启当前位置显示
        mMap.layers().add(mLocationLayer);
    }
}
