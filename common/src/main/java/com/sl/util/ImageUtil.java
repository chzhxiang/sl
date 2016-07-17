/**
 * 
 * 惠购 - 惠购网 - www.huiget.com - 特别会购！
 * Copyright © 2014 惠购 www.huiget.com 版权所有
 */
package com.sl.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * @author yaofang
 * @version $Id: ImageUtil.java, v 0.1 2014年10月7日 下午9:48:02 yaofang Exp $
 */
public class ImageUtil {

    private static final String UPLOAD_DIR      = "/upload/";
    private static final String UPLOAD_TEMP_DIR = "/upload/temp/";

    public static StringBuilder getImageDir(String... locations) {
        StringBuilder dir = new StringBuilder();
        dir.append(UPLOAD_DIR);
        for (String location : locations) {
            dir.append(location);
            dir.append("/");
        }
        return dir;
    }

    public static StringBuilder getTempImageDir(String... locations) {
        StringBuilder dir = new StringBuilder();
        dir.append(UPLOAD_TEMP_DIR);
        for (String location : locations) {
            dir.append(location);
            dir.append("/");
        }
        return dir;
    }

    public static void resize(byte[] bytes, File outputFile, int width, int height, String formatName, Color bgcolor) throws Exception {
        FileOutputStream out = null;
        try {
            Image inputImage = new ImageIcon(bytes).getImage();

            final Image resizedImage = new ImageIcon(inputImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)).getImage();

            // Create the buffered image.  
            BufferedImage bufferedImage = new BufferedImage(resizedImage.getWidth(null), resizedImage.getHeight(null), BufferedImage.TYPE_INT_RGB);

            Graphics graphics = bufferedImage.createGraphics();
            graphics.setColor(bgcolor);
            graphics.fillRect(0, 0, width, height);

            // 等比缩放
            int w = inputImage.getWidth(null);
            int h = inputImage.getHeight(null);

            double widthRate = (double) w / (double) width;
            double heightRate = (double) h / (double) height;

            if (w >= width && h >= height) {
                // 根据缩放比率大的进行缩放控制
                if (widthRate >= heightRate) {
                    h = (int) (h / widthRate);
                    w = width;

                    graphics.drawImage(resizedImage, 0, (height - h) / 2, w, h, Color.white, null);
                } else {
                    w = (int) (w / heightRate);
                    h = height;

                    graphics.drawImage(resizedImage, (width - w) / 2, 0, w, h, Color.white, null);
                }
            } else if (w >= width && h < height) {
                w = width;
                h = (int) (h / widthRate);
                graphics.drawImage(resizedImage, 0, (height - h) / 2, w, h, Color.white, null);
            } else if (w < width && h >= height) {
                h = height;
                w = (int) (w / heightRate);
                graphics.drawImage(resizedImage, (width - w) / 2, 0, w, h, Color.white, null);
            } else {
                graphics.drawImage(resizedImage, (width - w) / 2, (height - h) / 2, w, h, Color.white, null);
            }

            graphics.dispose();

            float softenFactor = 0.05f;
            float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
            Kernel kernel = new Kernel(3, 3, softenArray);
            ConvolveOp convolveOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            bufferedImage = convolveOp.filter(bufferedImage, null);

            out = new FileOutputStream(outputFile);

            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
            param.setQuality(1, true);
            encoder.setJPEGEncodeParam(param);
            encoder.encode(bufferedImage);

        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void resize(byte[] bytes, File outputFile, int width, int height, String formatName) throws Exception {
        resize(bytes, outputFile, width, height, formatName, Color.white);

    }
}
