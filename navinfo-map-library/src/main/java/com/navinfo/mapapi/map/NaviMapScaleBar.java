/*
 * NaviMap的scalebar
 */
package com.navinfo.mapapi.map;

import com.navinfo.mapapi.MapManager;

import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Canvas;
import org.oscim.backend.canvas.Color;
import org.oscim.backend.canvas.Paint;
import org.oscim.map.Map;
import org.oscim.renderer.BitmapRenderer;
import org.oscim.renderer.GLViewport;
import org.oscim.scalebar.DistanceUnitAdapter;
import org.oscim.scalebar.MapScaleBar;
import org.oscim.scalebar.MapScaleBarLayer;
import org.oscim.scalebar.MetricUnitAdapter;

/**
 * Displays the default MapScaleBar.
 */
public class NaviMapScaleBar extends MapScaleBar {
    private static final int BITMAP_HEIGHT = 40;
    private static final int BITMAP_WIDTH = 120;
    private static final int DEFAULT_HORIZONTAL_MARGIN = 5;
    private static final int DEFAULT_VERTICAL_MARGIN = 0;
    private static final int SCALE_BAR_MARGIN = 10;
    private static final float STROKE_EXTERNAL = 4;
    private static final float STROKE_INTERNAL = 2;
    private static final int TEXT_MARGIN = 1;

    public enum ScaleBarMode {BOTH, SINGLE}

    private final float scale;
    private ScaleBarMode scaleBarMode;
    private DistanceUnitAdapter secondaryDistanceUnitAdapter;

    private final Paint paintScaleBar;
    private final Paint paintScaleBarStroke;
    private final Paint paintScaleText;
    private final Paint paintScaleTextStroke;

    private final String levelPri = "(";
    private final String levelNxt = "级)";
    private StringBuilder levelBuilder;
    private float mapLevel;

    public NaviMapScaleBar(Map map) {
        this(map, CanvasAdapter.getScale());
    }

    public NaviMapScaleBar(Map map, float scale) {
        super(map, (int) (BITMAP_WIDTH * scale), (int) (BITMAP_HEIGHT * scale), scale);

        setMarginHorizontal((int) (DEFAULT_HORIZONTAL_MARGIN * scale));
        setMarginVertical((int) (DEFAULT_VERTICAL_MARGIN * scale));

        this.scale = scale;
        this.scaleBarMode = ScaleBarMode.SINGLE;
        this.secondaryDistanceUnitAdapter = MetricUnitAdapter.INSTANCE;
        this.distanceUnitAdapter = MetricUnitAdapter.INSTANCE;

        this.paintScaleBar = createScaleBarPaint(Color.BLACK, STROKE_INTERNAL, Paint.Style.FILL);
        this.paintScaleBarStroke = createScaleBarPaint(Color.WHITE, STROKE_EXTERNAL, Paint.Style.STROKE);
        this.paintScaleText = createTextPaint(Color.BLACK, 0, Paint.Style.FILL);
        this.paintScaleTextStroke = createTextPaint(Color.WHITE, 2, Paint.Style.STROKE);
        this.levelBuilder = new StringBuilder();
    }

    public MapScaleBarLayer initScaleBarLayer(GLViewport.Position position, int xOffset, int yOffset) {
        MapScaleBarLayer mapScaleBarLayer = new MapScaleBarLayer(this.map, this);
        BitmapRenderer renderer = mapScaleBarLayer.getRenderer();
        renderer.setPosition(position); // 设置scaleBar在地图上的位置
        renderer.setOffset(xOffset * CanvasAdapter.getScale(), yOffset* CanvasAdapter.getScale());
        this.map.layers().add(mapScaleBarLayer, NIMapView.LAYER_GROUPS.ALLWAYS_SHOW_GROUP.ordinal());
        return mapScaleBarLayer;
    }

    /**
     * @return the secondary {@link DistanceUnitAdapter} in use by this MapScaleBar
     */
    public DistanceUnitAdapter getSecondaryDistanceUnitAdapter() {
        return this.secondaryDistanceUnitAdapter;
    }

    /**
     * Set the secondary {@link DistanceUnitAdapter} for the MapScaleBar
     *
     * @param distanceUnitAdapter The secondary {@link DistanceUnitAdapter} to be used by this {@link MapScaleBar}
     */
    public void setSecondaryDistanceUnitAdapter(DistanceUnitAdapter distanceUnitAdapter) {
        if (distanceUnitAdapter == null) {
            throw new IllegalArgumentException("adapter must not be null");
        }
        this.secondaryDistanceUnitAdapter = distanceUnitAdapter;
        this.redrawNeeded = true;
    }

    public ScaleBarMode getScaleBarMode() {
        return this.scaleBarMode;
    }

    public void setScaleBarMode(ScaleBarMode scaleBarMode) {
        this.scaleBarMode = scaleBarMode;
        this.redrawNeeded = true;
    }

    private Paint createScaleBarPaint(int color, float strokeWidth, Paint.Style style) {
        Paint paint = CanvasAdapter.newPaint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth * this.scale);
        paint.setStyle(style);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        return paint;
    }

    private Paint createTextPaint(int color, float strokeWidth, Paint.Style style) {
        Paint paint = CanvasAdapter.newPaint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth * this.scale);
        paint.setStyle(style);
        paint.setTypeface(Paint.FontFamily.DEFAULT, Paint.FontStyle.BOLD);
        paint.setTextSize(12 * this.scale);
        return paint;
    }

    @Override
    protected void redraw(Canvas canvas) {
        canvas.fillColor(Color.TRANSPARENT);

        ScaleBarLengthAndValue lengthAndValue = this.calculateScaleBarLengthAndValue();
        ScaleBarLengthAndValue lengthAndValue2;

        if (this.scaleBarMode == ScaleBarMode.BOTH) {
            lengthAndValue2 = this.calculateScaleBarLengthAndValue(this.secondaryDistanceUnitAdapter);
        } else {
            lengthAndValue2 = new ScaleBarLengthAndValue(0, 0);
        }

        drawScaleBar(canvas, lengthAndValue.scaleBarLength, lengthAndValue2.scaleBarLength, this.paintScaleBarStroke, this.scale);
        drawScaleBar(canvas, lengthAndValue.scaleBarLength, lengthAndValue2.scaleBarLength, this.paintScaleBar, this.scale);

        String scaleText1 = this.distanceUnitAdapter.getScaleText(lengthAndValue.scaleBarValue);
        String scaleText2 = this.scaleBarMode == ScaleBarMode.BOTH ? this.secondaryDistanceUnitAdapter.getScaleText(lengthAndValue2.scaleBarValue) : "";

        levelBuilder.delete(0, levelBuilder.length());
        String level = levelBuilder.append(levelPri).append(map.getMapPosition().getZoomLevel()).append(levelNxt).toString();
        //获取当前的地图级别
//        mapLevel = map.getMapPosition().getZoomLevel();
//        for (int i = 0; i < this.distanceUnitAdapter.getScaleBarValues().length; i++) {
//            if (this.distanceUnitAdapter.getScaleBarValues()[i] == lengthAndValue.scaleBarValue) {
//                level = levelPri + (i + 1) + levelNxt;
//                mapLevel = i + 1;
//                break;
//            }
//        }

        drawScaleText(canvas, scaleText1, scaleText2, level, this.paintScaleTextStroke, this.scale);
        drawScaleText(canvas, scaleText1, scaleText2, level, this.paintScaleText, this.scale);

    }

    private void drawScaleBar(Canvas canvas, int scaleBarLength1, int scaleBarLength2, Paint paint, float scale) {
        int maxScaleBarLength = Math.max(scaleBarLength1, scaleBarLength2);

        switch (scaleBarPosition) {
            case BOTTOM_CENTER:
                if (scaleBarLength2 == 0) {
                    canvas.drawLine(Math.round((canvas.getWidth() - maxScaleBarLength) * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale),
                            Math.round((canvas.getWidth() + maxScaleBarLength) * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round((canvas.getWidth() - maxScaleBarLength) * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round((canvas.getWidth() - maxScaleBarLength) * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round((canvas.getWidth() + maxScaleBarLength) * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round((canvas.getWidth() + maxScaleBarLength) * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                } else {
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength1), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength1), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength2), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength2), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                }
                break;
            case BOTTOM_LEFT:
                if (scaleBarLength2 == 0) {
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                } else {
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength1), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength1), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength2), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength2), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                }
                break;
            case BOTTOM_RIGHT:
                if (scaleBarLength2 == 0) {
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - maxScaleBarLength), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - maxScaleBarLength), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                } else {
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - scaleBarLength1), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - scaleBarLength1), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - scaleBarLength2), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - scaleBarLength2), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                }
                break;
            case TOP_CENTER:
                if (scaleBarLength2 == 0) {
                    canvas.drawLine(Math.round((canvas.getWidth() - maxScaleBarLength) * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round((canvas.getWidth() + maxScaleBarLength) * 0.5f), Math.round(SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round((canvas.getWidth() - maxScaleBarLength) * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round((canvas.getWidth() - maxScaleBarLength) * 0.5f), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round((canvas.getWidth() + maxScaleBarLength) * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round((canvas.getWidth() + maxScaleBarLength) * 0.5f), Math.round(canvas.getHeight() * 0.5f), paint);
                } else {
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength1), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength1), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength2), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength2), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                }
                break;
            case TOP_LEFT:
                if (scaleBarLength2 == 0) {
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f), paint);
                } else {
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength1), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength1), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength2), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(STROKE_EXTERNAL * scale * 0.5f + scaleBarLength2), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                }
                break;
            case TOP_RIGHT:
                if (scaleBarLength2 == 0) {
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - maxScaleBarLength), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - maxScaleBarLength), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f), paint);
                } else {
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - maxScaleBarLength), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - scaleBarLength1), Math.round(SCALE_BAR_MARGIN * scale),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - scaleBarLength1), Math.round(canvas.getHeight() * 0.5f), paint);
                    canvas.drawLine(Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - scaleBarLength2), Math.round(canvas.getHeight() * 0.5f),
                            Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale * 0.5f - scaleBarLength2), Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale), paint);
                }
                break;
        }
    }

    private void drawScaleText(Canvas canvas, String scaleText1, String scaleText2, String levelText, Paint paint, float scale) {
        switch (scaleBarPosition) {
            case BOTTOM_CENTER:
                if (scaleText2.length() == 0) {
                    canvas.drawText(scaleText1, Math.round((canvas.getWidth() - this.paintScaleTextStroke.getTextWidth(scaleText1)) * 0.5f),
                            Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round((canvas.getWidth() - this.paintScaleTextStroke.getTextWidth(scaleText1)) * 0.5f + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                } else {
                    canvas.drawText(scaleText1, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    canvas.drawText(scaleText2, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                }
                break;
            case BOTTOM_LEFT:
                if (scaleText2.length() == 0) {
                    canvas.drawText(scaleText1, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                } else {
                    canvas.drawText(scaleText1, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    canvas.drawText(scaleText2, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);

                    // 绘制中心点坐标
//                    canvas.drawText(map.getMapPosition().getLatitude()+"", Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
//                            Math.round(canvas.getHeight() * 0.3f + STROKE_EXTERNAL * scale * 0.3f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)), paint);
//                    canvas.drawText(map.getMapPosition().getLongitude()+"", Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
//                            Math.round(canvas.getHeight() * 0.3f + STROKE_EXTERNAL * scale * 0.3f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)+ TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)), paint);
                }
                break;
            case BOTTOM_RIGHT:
                if (scaleText2.length() == 0) {
                    canvas.drawText(scaleText1, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText1)),
                            Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText1) + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() - SCALE_BAR_MARGIN * scale - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                } else {
                    canvas.drawText(scaleText1, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText1)),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    canvas.drawText(scaleText2, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText2)),
                            Math.round(canvas.getHeight() * 0.5f + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText1) + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                }
                break;
            case TOP_CENTER:
                if (scaleText2.length() == 0) {
                    canvas.drawText(scaleText1, Math.round((canvas.getWidth() - this.paintScaleTextStroke.getTextWidth(scaleText1)) * 0.5f),
                            Math.round(SCALE_BAR_MARGIN * scale + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText1)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round((canvas.getWidth() - this.paintScaleTextStroke.getTextWidth(scaleText1) + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale) * 0.5f),
                            Math.round(SCALE_BAR_MARGIN * scale + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText1)), paint);
                } else {
                    canvas.drawText(scaleText1, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    canvas.drawText(scaleText2, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                }
                break;
            case TOP_LEFT:
                if (scaleText2.length() == 0) {
                    canvas.drawText(scaleText1, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(SCALE_BAR_MARGIN * scale + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText1)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(SCALE_BAR_MARGIN * scale + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText1)), paint);
                } else {
                    canvas.drawText(scaleText1, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    canvas.drawText(scaleText2, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(STROKE_EXTERNAL * scale + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                }
                break;
            case TOP_RIGHT:
                if (scaleText2.length() == 0) {
                    canvas.drawText(scaleText1, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText1)),
                            Math.round(SCALE_BAR_MARGIN * scale + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText1)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText1) + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(SCALE_BAR_MARGIN * scale + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText1)), paint);
                } else {
                    canvas.drawText(scaleText1, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText1)),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                    canvas.drawText(scaleText2, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText2)),
                            Math.round(canvas.getHeight() * 0.5f + STROKE_EXTERNAL * scale * 0.5f + TEXT_MARGIN * scale + this.paintScaleTextStroke.getTextHeight(scaleText2)), paint);
                    //绘制等级
                    canvas.drawText(levelText, Math.round(canvas.getWidth() - STROKE_EXTERNAL * scale - TEXT_MARGIN * scale - this.paintScaleTextStroke.getTextWidth(scaleText1) + this.paintScaleTextStroke.getTextWidth(scaleText1) + TEXT_MARGIN * scale),
                            Math.round(canvas.getHeight() * 0.5f - STROKE_EXTERNAL * scale * 0.5f - TEXT_MARGIN * scale), paint);
                }
                break;
        }
    }

    public float getMapLevel() {
        return mapLevel;
    }
}
