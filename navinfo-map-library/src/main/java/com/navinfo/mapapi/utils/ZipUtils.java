package com.navinfo.mapapi.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 压缩、解压缩工具类
 * @author lihegang
 * @date 2021/9/8 15:25
 */
public class ZipUtils {
    
    public static String zipString(String unzip) {
        //使用指定的压缩级别创建一个新的压缩器
        Deflater deflater = new Deflater(9); // 0 ~ 9 压缩等级 低到高
        //设置压缩输入数据
        deflater.setInput(unzip.getBytes(StandardCharsets.UTF_8));
        //当被调用时，表示压缩应该以输入缓冲区的当前内容结束
        deflater.finish();

        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);

        while (!deflater.finished()) {
            int length = deflater.deflate(bytes);
            outputStream.write(bytes, 0, length);
        }

        deflater.end();
        return new BASE64Encoder().encodeBuffer(outputStream.toByteArray());
    }
   
    // 解压缩
    public static String unzipString(String zip) throws IOException {
        byte[] decode = new BASE64Decoder().decodeBuffer(zip);
        Inflater inflater = new Inflater();
        inflater.setInput(decode);

        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);

        try {
            while (!inflater.finished()) {
                int length = inflater.inflate(bytes);
                outputStream.write(bytes, 0, length);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            inflater.end();
        }

        return outputStream.toString("UTF-8");
    }

    // 解压缩
    public static String unzipInputStream(InputStream stream) throws IOException {
        byte[] decode = new BASE64Decoder().decodeBuffer(stream);
        Inflater inflater = new Inflater();
        inflater.setInput(decode);

        final byte[] bytes = new byte[256];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);

        try {
            while (!inflater.finished()) {
                int length = inflater.inflate(bytes);
                outputStream.write(bytes, 0, length);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            inflater.end();
        }

        return outputStream.toString("UTF-8");
    }
}
