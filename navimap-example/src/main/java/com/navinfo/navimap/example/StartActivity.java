package com.navinfo.navimap.example;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.navinfo.navimap.example.adapter.StartActivityAdapter;
import com.navinfo.navimap.example.entity.StartActivityEntity;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {
    private ListView lvStart; // 用来展示开始界面的listview
    private List<StartActivityEntity> listData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_start);

        // 请求权限
        requestPermission();

        lvStart = findViewById(R.id.lv_start);
        initListMapData();
        StartActivityAdapter adapter = new StartActivityAdapter(StartActivity.this, listData);
        lvStart.setAdapter(adapter);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_SECURE_SETTINGS,
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.RECORD_AUDIO,
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            };

            if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 0);
            }
        }
    }

    private void initListMapData() {
        listData = new ArrayList<>();

        listData.add(new StartActivityEntity("显示基础地图", BaseMapActivity.class));
        listData.add(new StartActivityEntity("地图元件控制", MapWidgetActivity.class));
        listData.add(new StartActivityEntity("显示用户当前位置", MapLocaitionActivity.class));
        listData.add(new StartActivityEntity("栅格底图显示", BaseMapTypeActivity.class));
        listData.add(new StartActivityEntity("显示TMS在线资源", VectorTileMapActivity.class));
        listData.add(new StartActivityEntity("离线地图", OfflineMapActivity.class));
    }

}
