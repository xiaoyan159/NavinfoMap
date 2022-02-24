package com.navinfo.navimap.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.navinfo.mapapi.map.NIMapView;

/**
 * 矢量瓦片在线地图Activity
 * */
public class VectorTileMapActivity extends Activity {
    protected NIMapView niMapView;
    private Switch switchRasterVisiable, switchGridVisiable;
    private Spinner spnVectorTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_tile_map);

        niMapView = findViewById(R.id.mapView);
        niMapView.addDefaultVectorTileLayer(null); // 增加默认的矢量瓦片图层

        switchRasterVisiable = findViewById(R.id.switch_raster_visiable);
        spnVectorTheme = findViewById(R.id.spn_vector_tile_theme);
        switchRasterVisiable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 设置基础栅格底图显隐状态
                niMapView.setBaseRasterVisiable(isChecked);
            }
        });

        switchGridVisiable = findViewById(R.id.switch_grid_visiable);
        switchGridVisiable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                niMapView.setGridLayerVisiable(isChecked);
            }
        });

        spnVectorTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 切换矢量瓦片地图的样式
                niMapView.switchTileVectorLayerTheme(NIMapView.MAP_THEME.DEFAULT.getMapTheme(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        niMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        niMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        niMapView.onDestroy();
    }
}
