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
 * 地图控件控制Activity
 * */
public class MapWidgetActivity extends Activity {
    protected NIMapView niMapView;
    private Switch switchCompass, switchScale;
    private Spinner spnCompass, spnZoom, spnLogo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_widget);

        niMapView = findViewById(R.id.mapView);
        switchCompass = findViewById(R.id.switch_compass_visiable);
        switchScale = findViewById(R.id.switch_scale_visiable);
        spnCompass = findViewById(R.id.spn_compass_position);
        spnZoom = findViewById(R.id.spn_zoom_controller_position);
        spnLogo = findViewById(R.id.spn_logo_position);

        switchCompass.setOnCheckedChangeListener(checkedChangeListener);
        switchScale.setOnCheckedChangeListener(checkedChangeListener);

        spnCompass.setOnItemSelectedListener(spinnerSelectedListener);
        spnZoom.setOnItemSelectedListener(spinnerSelectedListener);
        spnLogo.setOnItemSelectedListener(spinnerSelectedListener);
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.switch_compass_visiable:
                    niMapView.showCompass(isChecked);
                    break;
                case R.id.switch_scale_visiable:
                    niMapView.showZoomControls(isChecked);
                    break;
            }
        }
    };

    AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            NIMapView.GRAVITY gravity = null;
            if (position == 1) {
                gravity = NIMapView.GRAVITY.LEFT_TOP;
            } else if (position == 2) {
                gravity = NIMapView.GRAVITY.LEFT_BOTTOM;
            } else if (position == 3) {
                gravity = NIMapView.GRAVITY.RIGHT_TOP;
            } else if (position == 4) {
                gravity = NIMapView.GRAVITY.RIGHT_BOTTOM;
            }
            switch (parent.getId()) {
                case R.id.spn_compass_position:
                    if (gravity == null) {
                        niMapView.setCompassPosition(NIMapView.GRAVITY.RIGHT_TOP);
                    } else {
                        niMapView.setCompassPosition(gravity);
                    }
                    break;
                case R.id.spn_logo_position:
                    if (gravity == null) {
                        niMapView.setLogoPosition(NIMapView.GRAVITY.LEFT_BOTTOM);
                    } else {
                        niMapView.setLogoPosition(gravity);
                    }
                    break;
                case R.id.spn_zoom_controller_position:
                    if (gravity == null) {
                        niMapView.setZoomControlPosition(NIMapView.GRAVITY.RIGHT_BOTTOM);
                    } else {
                        niMapView.setZoomControlPosition(gravity);
                    }
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
