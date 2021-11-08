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
    abstract void insert();

    /**
     * 更新
     */
    abstract void update();

    /**
     * 查找
     */
    abstract void find();

    /**
     * 删除
     */
    abstract void delete();
}
