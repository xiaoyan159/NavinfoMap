package com.navinfo.mapapi;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentActivity;

/**
 * 测试工程
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }

    @Override
    public void onClick(View view){

    }

}