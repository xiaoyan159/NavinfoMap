package com.navinfo.mapapi.canvas;

import android.graphics.Bitmap;

import org.oscim.android.canvas.AndroidBitmap;

import java.io.InputStream;

public class NaviAndroidBitmap extends AndroidBitmap {
    public NaviAndroidBitmap(InputStream inputStream) {
        super(inputStream);
    }

    public NaviAndroidBitmap(InputStream inputStream, int width, int height, int percent) {
        super(inputStream, width, height, percent);
    }

    public NaviAndroidBitmap(int width, int height, int format) {
        super(width, height, format);
    }

    public NaviAndroidBitmap(Bitmap bitmap) {
        super(bitmap);
    }
}
