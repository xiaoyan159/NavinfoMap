package com.navinfo.mapapi.layers;

import android.annotation.SuppressLint;
import android.content.Context;

import com.navinfo.mapapi.R;
import com.navinfo.mapapi.canvas.NaviAndroidBitmap;
import com.navinfo.mapapi.map.NIMapView;

import org.oscim.android.canvas.AndroidBitmap;
import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.layers.Layer;
import org.oscim.layers.LocationLayer;
import org.oscim.layers.LocationTextureLayer;
import org.oscim.map.Map;
import org.oscim.renderer.LocationCallback;
import org.oscim.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class NaviLocationLayer extends LocationTextureLayer {
    public NaviLocationLayer(Context mContext, Map map) {
        this(mContext, map, CanvasAdapter.getScale());
    }

    public NaviLocationLayer(Context mContext, Map map, float scale) {
        super(map, scale);
        init(mContext);
    }

    private void init(Context mContext) {
        createLocationLayers(mContext, mMap);
    }

    @SuppressLint("ResourceType")
    private Layer createLocationLayers(Context mContext, Map mMap) {

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
        this.locationRenderer.setBitmapArrow(bitmapArrow);
        this.locationRenderer.setBitmapMarker(bitmapMarker);
        this.locationRenderer.setColor(0xffa1dbf5);
        this.locationRenderer.setShowAccuracyZoom(4);
        this.setEnabled(true); // 默认开启当前位置显示
        mMap.layers().add(this, NIMapView.LAYER_GROUPS.ALLWAYS_SHOW_GROUP.getGroupIndex());
        return this;
    }
}
