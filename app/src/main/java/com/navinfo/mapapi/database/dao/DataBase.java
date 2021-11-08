package com.navinfo.mapapi.database.dao;

import android.graphics.Interpolator;

import com.navinfo.mapapi.animation.Animation;

/**
 * 数据库操作
 */
public abstract class DataBase extends java.lang.Object {

    /**
     * 新增
     */
    public void insert();

    /**
     * 更新
     */
    public void update();

    /**
     * 查找
     */
    public void find();

    /**
     * 删除
     */
    public void delete();
}
