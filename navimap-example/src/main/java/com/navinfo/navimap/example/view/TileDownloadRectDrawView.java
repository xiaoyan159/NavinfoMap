package com.navinfo.navimap.example.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TileDownloadRectDrawView extends View {
    private Paint paint = null;
    //定义一个内存中图片，将他作为缓冲区
    private Bitmap mBufferBitmap = null;
    //定义缓冲区Cache的Canvas对象
    private Canvas mBufferCanvas = null;
    private Point startPoint;
    private Rect rect;

    public TileDownloadRectDrawView(Context context) {
        this(context, null);
    }

    public TileDownloadRectDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setFlags(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setDither(true);
        rect = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //创建一个与该VIew相同大小的缓冲区
        mBufferBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //创建缓冲区Cache的Canvas对象
        mBufferCanvas = new Canvas();
        //设置cacheCanvas将会绘制到内存的bitmap上
        mBufferCanvas.setBitmap(mBufferBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将cacheBitmap绘制到该View
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(mBufferBitmap, 0, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (startPoint == null) {
                    startPoint = new Point();
                }
                startPoint.set((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mBufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                rect.left = startPoint.x <= event.getX() ? startPoint.x : (int) event.getX();
                rect.top = startPoint.y <= event.getY() ? startPoint.y : (int) event.getY();
                rect.right = startPoint.x >= event.getX() ? startPoint.x : (int) event.getX();
                rect.bottom = startPoint.y >= event.getY() ? startPoint.y : (int) event.getY();
                mBufferCanvas.drawRect(rect, paint);
                invalidate();
                break;
        }
        return true;
    }

    public Rect getRect() {
        if (rect.bottom != rect.top && rect.left != rect.right) {
            return rect;
        }
        return null;
    }
}
