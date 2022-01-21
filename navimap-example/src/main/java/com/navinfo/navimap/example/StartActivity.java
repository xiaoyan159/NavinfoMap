package com.navinfo.navimap.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;

import com.navinfo.navimap.example.adapter.StartActivityAdapter;
import com.navinfo.navimap.example.entity.StartActivityEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartActivity extends Activity {
    private ListView lvStart; // 用来展示开始界面的listview
    private List<StartActivityEntity> listData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_start);

        lvStart = findViewById(R.id.lv_start);
        initListMapData();
        StartActivityAdapter adapter = new StartActivityAdapter(StartActivity.this, listData);
        lvStart.setAdapter(adapter);
    }

    private void initListMapData() {
        listData = new ArrayList<>();

        listData.add(new StartActivityEntity("基础地图显示", MainActivity.class));
    }

}
