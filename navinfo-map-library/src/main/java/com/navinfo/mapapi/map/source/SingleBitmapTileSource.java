package com.navinfo.mapapi.map.source;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.geom.util.AffineTransformationBuilder;
import org.oscim.android.canvas.AndroidBitmap;
import org.oscim.backend.CanvasAdapter;
import org.oscim.core.BoundingBox;
import org.oscim.core.GeoPoint;
import org.oscim.core.Tile;
import org.oscim.layers.tile.MapTile;
import org.oscim.tiling.ITileDataSink;
import org.oscim.tiling.ITileDataSource;
import org.oscim.tiling.QueryResult;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.ITileDecoder;
import org.oscim.tiling.source.UrlTileSource;
import org.oscim.tiling.source.bitmap.BitmapTileSource;
import org.oscim.utils.geom.GeomBuilder;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import static org.oscim.tiling.QueryResult.FAILED;
import static org.oscim.tiling.QueryResult.SUCCESS;

public class SingleBitmapTileSource extends TileSource {
    private Bitmap mBitmap;
    private Coordinate bitmapPoint0, bitmapPoint1, bitmapPoint2, geoPoint0, geoPoint1, geoPoint2;
    private AffineTransformation geoAffineTransformation, bitmapAffineTransformation;
    private Bitmap.Config bitmapConfig;

    private Coordinate bitmapStartCoor, bitmapEndCoor;

    public static class Builder<T extends SingleBitmapTileSource.Builder<T>> extends UrlTileSource.Builder<T> {
        private Bitmap mBitmap;
        private Coordinate bitmapPoint0, bitmapPoint1, bitmapPoint2, geoPoint0, geoPoint1, geoPoint2;

        public Builder(Bitmap mBitmap, Coordinate bitmapPoint0, Coordinate bitmapPoint1, Coordinate bitmapPoint2,
                       Coordinate geoPoint0, Coordinate geoPoint1, Coordinate geoPoint2) {
            super();
            this.mBitmap = mBitmap;
            this.bitmapPoint0 = bitmapPoint0;
            this.bitmapPoint1 = bitmapPoint1;
            this.bitmapPoint2 = bitmapPoint2;
            this.geoPoint0 = geoPoint0;
            this.geoPoint1 = geoPoint1;
            this.geoPoint2 = geoPoint2;
        }

        @Override
        public SingleBitmapTileSource build() {
            return new SingleBitmapTileSource(this);
        }
    }

    protected SingleBitmapTileSource(SingleBitmapTileSource.Builder<?> builder) {
        super(builder);
        this.mBitmap = builder.mBitmap;
        this.bitmapPoint0 = builder.bitmapPoint0;
        this.bitmapPoint1 = builder.bitmapPoint1;
        this.bitmapPoint2 = builder.bitmapPoint2;
        this.geoPoint0 = builder.geoPoint0;
        this.geoPoint1 = builder.geoPoint1;
        this.geoPoint2 = builder.geoPoint2;
        AffineTransformationBuilder geoTransBuilder = new AffineTransformationBuilder(bitmapPoint0, bitmapPoint1, bitmapPoint2, geoPoint0, geoPoint1, geoPoint2);
        this.geoAffineTransformation = geoTransBuilder.getTransformation();

        AffineTransformationBuilder bitmapTransBuilder = new AffineTransformationBuilder(geoPoint0, geoPoint1, geoPoint2, bitmapPoint0, bitmapPoint1, bitmapPoint2);
        this.bitmapAffineTransformation = bitmapTransBuilder.getTransformation();

        bitmapStartCoor = new Coordinate();
        bitmapStartCoor = geoAffineTransformation.transform(new Coordinate(0, 0), bitmapStartCoor);
        bitmapEndCoor = new Coordinate();
        bitmapEndCoor = geoAffineTransformation.transform(new Coordinate(mBitmap.getWidth(), mBitmap.getHeight()), bitmapEndCoor);
        bitmapConfig = Bitmap.Config.ARGB_4444;
    }

    @SuppressWarnings("rawtypes")
    public static BitmapTileSource.Builder<?> builder() {
        return new BitmapTileSource.Builder();
    }

    @Override
    public ITileDataSource getDataSource() {

        return new SingleBitmapDataSource(this.mBitmap, new GeoPoint(bitmapStartCoor.y, bitmapStartCoor.x),
                new GeoPoint(bitmapEndCoor.y, bitmapEndCoor.x), new TileDecoder());
    }

    @Override
    public OpenResult open() {
        return OpenResult.SUCCESS;
    }

    @Override
    public void close() {

    }

    class SingleBitmapDataSource implements ITileDataSource {
        private Bitmap bitmap;
//        private GeoPoint leftTopPoint, rightBottomPoint;
        private ITileDecoder tileDecoder;

        private Polygon bitmapRect;
        private Paint defaultPaint;

        public SingleBitmapDataSource(Bitmap bitmap, GeoPoint leftTopPoint, GeoPoint rightBottomPoint, ITileDecoder tileDecoder) {
            this.bitmap = bitmap;
//            this.leftTopPoint = leftTopPoint;
//            this.rightBottomPoint = rightBottomPoint;
            this.bitmapRect = createRectPolygon(leftTopPoint.getLongitude(), leftTopPoint.getLatitude(),
                    rightBottomPoint.getLongitude(), rightBottomPoint.getLatitude());
            this.tileDecoder = tileDecoder;
            this.defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.defaultPaint.setFilterBitmap(true);
        }

        @Override
        public void query(MapTile tile, ITileDataSink mapDataSink) {
            QueryResult res = FAILED;
            // 使用tile的rect获取与Bitmap对应区域的相交面
            BoundingBox tileBoundingBox = tile.getBoundingBox();
            Polygon tilePolygon = createRectPolygon(tileBoundingBox.getMinLongitude(), tileBoundingBox.getMinLatitude(),
                    tileBoundingBox.getMaxLongitude(), tileBoundingBox.getMaxLatitude());
            Bitmap tileBitmap = Bitmap.createBitmap(256, 256, bitmapConfig);
            tileBitmap.setHasAlpha(true);
            if (bitmapRect.intersects(tilePolygon)&&tile.zoomLevel>=mZoomMin&&tile.zoomLevel<=mZoomMax) {
                Polygon intersectionPolygon = (Polygon) bitmapRect.intersection(tilePolygon);
                // 使用相交的polygon获取bitmap上对应的坐标
                if (intersectionPolygon.getNumPoints()>3) {
                    // 获取tile坐标和图片的仿射变换参数
                    Coordinate leftTop = new Coordinate(tileBoundingBox.getMinLongitude(), tileBoundingBox.getMaxLatitude());
                    Coordinate leftBottom = new Coordinate(tileBoundingBox.getMinLongitude(), tileBoundingBox.getMinLatitude());
                    Coordinate rightBottom = new Coordinate(tileBoundingBox.getMaxLongitude(), tileBoundingBox.getMinLatitude());
                    AffineTransformationBuilder builder = new AffineTransformationBuilder(leftTop, leftBottom, rightBottom, new Coordinate(0, 0), new Coordinate(0, 256), new Coordinate(256, 256));
                    Polygon screenPolygon = (Polygon) builder.getTransformation().transform(intersectionPolygon);

                    Coordinate[] screenCoorRect = getPolygonWrapRect(screenPolygon);

                    Polygon clipPolygon = (Polygon) bitmapAffineTransformation.transform(intersectionPolygon);
                    Coordinate[] clipCoorRect = getPolygonWrapRect(clipPolygon);

                    // 计算出相交面对应的bitmap坐标，裁切图片
                    Bitmap clipBitmap = Bitmap.createBitmap(bitmap, (int)clipCoorRect[0].x, (int)clipCoorRect[0].y, Math.abs((int)clipCoorRect[1].x - (int)clipCoorRect[0].x), Math.abs((int)clipCoorRect[1].y - (int)clipCoorRect[0].y));
                    // 根据相交面，求取bitmap的大小和位置

                    Canvas canvas = new Canvas(tileBitmap);
                    canvas.drawBitmap(clipBitmap, new Rect(0, 0, clipBitmap.getWidth(), clipBitmap.getHeight()),
                            new RectF((float) screenCoorRect[0].x, (float)screenCoorRect[0].y, (float)screenCoorRect[1].x, (float)screenCoorRect[1].y), defaultPaint);
                }
            }
            if (tileBitmap!=null) {
                AndroidBitmap tileAndroidBitmap = new AndroidBitmap(tileBitmap);
                tileAndroidBitmap.scaleTo(256, 256);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(tileAndroidBitmap.getPngEncodedData());
                try {
                    if (tileDecoder.decode(tile, mapDataSink, inputStream)) {
                        res = SUCCESS;
                        tileAndroidBitmap.recycle();
                        tileBitmap.recycle();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mapDataSink.completed(res);
                }
            }
            return;
        }

        @Override
        public void dispose() {

        }

        @Override
        public void cancel() {

        }
    }

    public static class TileDecoder implements ITileDecoder {

        @Override
        public boolean decode(Tile tile, ITileDataSink sink, InputStream is)
                throws IOException {

            org.oscim.backend.canvas.Bitmap bitmap = CanvasAdapter.decodeBitmap(is);
            if (!bitmap.isValid()) {
                Log.d("{} invalid bitmap", tile.toString());
                return false;
            }
            sink.setTileImage(bitmap);

            return true;
        }
    }

    private Polygon createRectPolygon(double minX, double minY, double maxX, double maxY) {
        GeomBuilder tileGeomBuilder = new GeomBuilder();
        tileGeomBuilder.point(minX, minY);
        tileGeomBuilder.point(minX, maxY);
        tileGeomBuilder.point(maxX, maxY);
        tileGeomBuilder.point(maxX, minY);
        tileGeomBuilder.point(minX, minY);
        return tileGeomBuilder.toPolygon();
    }

    private Coordinate[] getPolygonWrapRect(Polygon polygon) {
        // 获取最小的x，y值，即为Bitmap的起始坐标点,最大x、y值，为Bitmap的显示范围
        Coordinate screenLeftTop = null,screenRightBottom = null;
        for (Coordinate coordinate:polygon.getCoordinates()) {
            if (screenLeftTop == null) {
                screenLeftTop = coordinate;
            }
            if (screenRightBottom == null) {
                screenRightBottom = coordinate;
            }
            if (coordinate.x<=screenLeftTop.x&&coordinate.y<=screenLeftTop.y){
                screenLeftTop = coordinate;
            }
            if (coordinate.x>=screenRightBottom.x&&coordinate.y>=screenRightBottom.y) {
                screenRightBottom = coordinate;
            }
        }
        Coordinate[] resultCoordinate = new Coordinate[2];
        resultCoordinate[0] = screenLeftTop;
        resultCoordinate[1] = screenRightBottom;
        return resultCoordinate;
    }
}
