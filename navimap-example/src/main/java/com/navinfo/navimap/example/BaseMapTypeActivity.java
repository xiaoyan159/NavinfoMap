package com.navinfo.navimap.example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.navinfo.mapapi.map.NIMapView;

/**
 * 栅格底图类型Activity
 * */
public class BaseMapTypeActivity extends Activity {
    protected NIMapView niMapView;
    private RadioButton rbtnOSM, rbtnCycle, rbtnTransport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_map_style);

        niMapView = findViewById(R.id.mapView);
        rbtnOSM = findViewById(R.id.map_style_osm);
        rbtnCycle = findViewById(R.id.map_style_cycle);
        rbtnTransport = findViewById(R.id.map_style_transport);

        rbtnOSM.setOnCheckedChangeListener(switchStyleListener);
        rbtnCycle.setOnCheckedChangeListener(switchStyleListener);
        rbtnTransport.setOnCheckedChangeListener(switchStyleListener);
    }

    CompoundButton.OnCheckedChangeListener switchStyleListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                switch (buttonView.getId()){
                    case R.id.map_style_osm:
                        niMapView.switchBaseMapType(NIMapView.BASE_MAP_TYPE.OPEN_STREET_MAP);
                        break;
                    case R.id.map_style_cycle:
                        niMapView.switchBaseMapType(NIMapView.BASE_MAP_TYPE.CYCLE_MAP);
                        break;
                    case R.id.map_style_transport:
                        niMapView.switchBaseMapType(NIMapView.BASE_MAP_TYPE.TRANSPORT_MAP);
                        break;
                }
            }
        }
    };

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
