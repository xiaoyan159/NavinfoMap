package com.navinfo.mapapi.map;

/**
 * 地图覆盖物基类
 */
public abstract class Overlay extends java.lang.Object {

    protected Overlay() {
    }

    /**
     * 获取覆盖物额外信息
     *
     * @return
     */
    Bundle getExtraInfo();

    /**
     * 获取覆盖物 zIndex
     *
     * @return
     */
    int getZIndex();

    /**
     * 获取overlay是否被移除的状态
     *
     * @return
     */
    boolean isRemoved();

    /**
     * 设置覆盖物可见性
     *
     * @return
     */
    boolean isVisible();


    /**
     * 删除该覆盖物
     */
    void remove();


    /**
     * 设置覆盖物额外信息
     *
     * @param extraInfo
     */
    void setExtraInfo(Bundle extraInfo);


    /**
     * 获取覆盖物可见性
     *
     * @param visible
     */
    void setVisible(boolean visible);


    /**
     * 设置覆盖物 zIndex
     *
     * @param zIndex
     */
    void setZIndex(int zIndex);

}
