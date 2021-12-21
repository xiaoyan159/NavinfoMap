package com.navinfo.mapapi.map;

import org.oscim.backend.canvas.Bitmap;

/**
 * bitmap 描述信息
 */
public class BitmapDescriptor extends java.lang.Object {

    final Bitmap mBitmap;

    public BitmapDescriptor(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    /**
     * 获取对应的Bitmap对象
     *
     * @return
     */
    Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * 回收 bitmap 资源，请确保在不再使用该 bitmap descriptor 时再调用该函数。
     */
    void recycle() {

        if(mBitmap!=null&&mBitmap.isValid()){
            mBitmap.recycle();
        }

        mBitmap = null;
    }

}
