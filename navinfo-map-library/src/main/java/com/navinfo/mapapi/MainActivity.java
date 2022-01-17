package com.navinfo.mapapi;

import android.Manifest;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;

import com.navinfo.mapapi.map.BitmapDescriptor;
import com.navinfo.mapapi.map.Marker;
import com.navinfo.mapapi.map.MarkerOptions;
import com.navinfo.mapapi.map.NIMapView;
import com.navinfo.mapapi.map.Overlay;
import com.navinfo.mapapi.map.Polyline;
import com.navinfo.mapapi.map.PolylineOptions;
import com.navinfo.mapapi.model.LatLng;

import android.os.Build;
import android.content.pm.PackageManager;

import org.oscim.android.canvas.AndroidBitmap;
import org.oscim.android.theme.AssetsRenderTheme;
import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.core.GeoPoint;
import org.oscim.layers.LocationTextureLayer;
import org.oscim.renderer.LocationCallback;
import org.oscim.renderer.MapRenderer;
import org.oscim.theme.IRenderTheme;
import org.oscim.theme.ThemeFile;
import org.oscim.theme.ThemeLoader;
import org.oscim.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试工程
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    /**
     * 创建图层
     */
    private LocationTextureLayer mLocationLayer;
    private NIMapView niMapView;
    private Marker marker;
    List<LatLng> list = new ArrayList<>();

    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_SECURE_SETTINGS,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            };

            if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 0);
            }
        }

        setContentView(R.layout.activity_main);
        niMapView = (NIMapView)findViewById(R.id.mapView);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_del).setOnClickListener(this);

        MapManager.getInstance().init(MainActivity.this,niMapView);
        MapManager.getInstance().loadMap();
        MapManager.getInstance().changeMapStyle("smap","",5);

        createLocationLayers();

        MapManager.getInstance().location();

        niMapView.setZoomControlsPosition(new Point(1400,1400));
        niMapView.showZoomControls(true);
        niMapView.showScaleControl(true);
        list.add(new LatLng(40.062304, 116.213801));
        list.add(new LatLng(40.064304, 116.213801));
    }

    @Override
    public void onClick(View view){

        switch (view.getId()){
            case R.id.btn_del:
                if(marker!=null){
                    List<Overlay> list = new ArrayList<>();
                    list.add(marker);
                    niMapView.getMap().removeOverLays(list);
                }
                if(polyline!=null){
                    List<Overlay> list = new ArrayList<>();
                    list.add(polyline);
                    niMapView.getMap().removeOverLays(list);
                }
                break;
            case R.id.btn_add:
                AndroidBitmap mCenterMakerBitmap = new AndroidBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.marker));
                //MapManager.getInstance().addMarker(new GeoPoint(40.062304, 116.213801),mCenterMakerBitmap);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(40.062304, 116.213801)).icon(new BitmapDescriptor(mCenterMakerBitmap));
                marker = (Marker) niMapView.getMap().addOverlay(markerOptions);
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(getColor(R.color.black)).width(5).zIndex(5).points(list);
                polyline = (Polyline) niMapView.getMap().addOverlay(polylineOptions);
                break;
        }
    }

    public void createLocationLayers() {

        InputStream is = null;

        Bitmap bitmapArrow = null;
        try {
            is = getResources().openRawResource(R.drawable.icon_location_arrow);
            bitmapArrow = CanvasAdapter.decodeBitmap(is, (int) (48 * CanvasAdapter.getScale()), (int) (48 * CanvasAdapter.getScale()), 100);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }

        Bitmap bitmapMarker = null;
        try {
            is = getResources().openRawResource(R.drawable.icon_location_arrow);
            bitmapMarker = CanvasAdapter.decodeBitmap(is, (int) (48 * CanvasAdapter.getScale()), (int) (48 * CanvasAdapter.getScale()), 100);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }

        // 显示当前位置图层
        mLocationLayer = new LocationTextureLayer(niMapView.getVtmMap());
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
        niMapView.getVtmMap().layers().add(mLocationLayer);
    }

}