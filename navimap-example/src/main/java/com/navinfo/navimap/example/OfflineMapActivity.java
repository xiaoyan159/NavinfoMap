package com.navinfo.navimap.example;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.navinfo.mapapi.map.NIMapView;
import com.navinfo.mapapi.utils.CacheTileProgress;
import com.navinfo.mapapi.utils.TileDownloader;
import com.navinfo.navimap.example.view.TileDownloadRectDrawView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * 离线地图Activity
 * */
public class OfflineMapActivity extends Activity {
    protected NIMapView niMapView;
    private CheckBox chkCacheRasterMap;


    private Button bbtn_finish, bbtn_cancle;
    private TileDownloadRectDrawView tileDownloadRectDrawView;
    private ProgressBar tileDownloadProgressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_map);

        niMapView = (NIMapView)findViewById(R.id.mapView);
        chkCacheRasterMap = findViewById(R.id.chk_cache_raster_map);
        tileDownloadRectDrawView = findViewById(R.id.rect_draw_tile_download);
        tileDownloadProgressbar = findViewById(R.id.pb_tile_download);

        chkCacheRasterMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tileDownloadRectDrawView.setVisibility(View.VISIBLE);
                    tileDownloadProgressbar.setVisibility(View.GONE);
                    // 取消下载
                    niMapView.getMap().cancelCacheTileMap();
                } else {
                    if (tileDownloadRectDrawView.isShown()&&tileDownloadRectDrawView.getRect()!=null) {
                        tileDownloadProgressbar.setProgress(0);
                        tileDownloadProgressbar.setVisibility(View.VISIBLE);
                        // 获取用户绘制的缓存矩形
                        Rect rect = tileDownloadRectDrawView.getRect();
                        CacheTileProgress progress = new CacheTileProgress() {
                            @Override
                            public void onProgress(int successCount, int failCount, int maxCount, int layerId, int layerCount) {
                                System.out.println(String.format("下载进度：总下载数-%d，成功-%d，失败-%d", maxCount, successCount, failCount));
                                tileDownloadProgressbar.setSecondaryProgress((int)((double)layerId)/layerCount*maxCount);
                                tileDownloadProgressbar.setMax(maxCount);
                                tileDownloadProgressbar.setProgress(successCount+failCount);
                            }
                        };
                        niMapView.getMap().cacheUrlTileMap(rect, 5, 18, progress);
                    } else {
                        Toast.makeText(OfflineMapActivity.this, "没有绘制缓存区域", Toast.LENGTH_SHORT).show();
                    }
                    tileDownloadRectDrawView.setVisibility(View.GONE);
                }
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
        niMapView.getMap().cancelCacheTileMap();
        niMapView.onDestroy();
    }
}
