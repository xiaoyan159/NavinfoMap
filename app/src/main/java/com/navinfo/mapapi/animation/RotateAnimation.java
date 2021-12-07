package com.navinfo.mapapi.animation;

import android.graphics.Camera;
import android.graphics.Interpolator;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/*
 *com.nmp.widget
 *zhjch
 *2021/6/24
 *22:29
 *说明（图片旋转）
 */
public class RotateAnimation extends Animation {
    private float mFromX;
    private float mToX;
    private float mFromZ;
    private float mToZ;
    private float mCenterX;
    private float mCenterY;
    private final float mDepthZ = 0;
    private final boolean mReverse = true;
    private Camera mCamera;
    private ImageView mImageView;

    private float mPivotX;
    private float mPivotY;

    public RotateAnimation(ImageView imageView) {
        mImageView = imageView;
        // 计算中心点
        mCenterX = imageView.getWidth() / 2.0f;
        mCenterY = imageView.getHeight() / 2.0f;
        setDuration(10);
        setFillAfter(true);

        //匀速旋转
        setInterpolator(new LinearInterpolator());

    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
        mPivotX = resolveSize(Animation.RELATIVE_TO_SELF, 0.5f, width, parentWidth);
        mPivotY = resolveSize(Animation.RELATIVE_TO_SELF, 0.5f, height, parentHeight);
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degrees = mFromZ + ((mToZ - mFromZ) * interpolatedTime);
        float scale = getScaleFactor();
        final Matrix matrix = t.getMatrix();

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;

        //保存一次camera初始状态，用于restore()
        camera.save();
        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }

        final float fromX = mFromX;
        float degreesX = fromX + ((mToX - fromX) * interpolatedTime);
        //围绕Y轴旋转degrees度
        camera.rotateX(degreesX);
//        //行camera中取出矩阵，赋值给matrix
        camera.getMatrix(matrix);
        camera.restore();
        //camera恢复到初始状态，继续用于下次的计算
        if (mPivotX == 0.0f && mPivotY == 0.0f) {
            matrix.preRotate(degrees);//它也是用matrix来实现旋转的,不是matrix的按z轴旋转和它一样, 是它就是这样实现的.
        } else {
            matrix.preRotate(degrees, mPivotX * scale, mPivotY * scale);
        }
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

    public void startRotationX(float start, float end) {

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        //final Rotate3dAnimation rotation =new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        mFromX = start;
        mToX = end;
        mImageView.startAnimation(this);
    }

    public void startRotationZ(float start, float end) {

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        //final Rotate3dAnimation rotation =new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        mFromZ = start;
        mToZ = end;
        mImageView.startAnimation(this);
    }

}
