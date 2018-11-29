package com.cn.readPic;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageCutUtil {
	/**
     * 图片剪裁
     * @param x 距离左上角的x轴距离
     * @param y 距离左上角的y轴距离
     * @param width 宽度
     * @param height 高度
     * @param sourcePath 图片源
     * @param descpath 目标位置
     */
    public static void imageCut(int x, int y, int width, int height, String sourcePath, String descpath) {  
        FileInputStream is = null;  
        ImageInputStream iis = null;  
        try {  
            is = new FileInputStream(sourcePath);  
            String fileSuffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);  
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix);  
            ImageReader reader = it.next();  
            iis = ImageIO.createImageInputStream(is);  
            reader.setInput(iis, true);  
            ImageReadParam param = reader.getDefaultReadParam();  
            Rectangle rect = new Rectangle(x, y, width, height);  
            param.setSourceRegion(rect);  
            BufferedImage bi = reader.read(0, param);  
            ImageIO.write(bi, fileSuffix, new File(descpath));  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } finally {  
            if (is != null) {  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                is = null;  
            }  
            if (iis != null) {  
                try {  
                    iis.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
                iis = null;  
            }  
        }  
    }
    
    /**
     * 判断是否为纯色
     * @param imgPath 图片源
     * @param percent 纯色百分比，即大于此百分比为同一种颜色则判定为纯色,范围[0-1]
     * @return
     * @throws IOException
     */
    public static boolean isSimpleColorImg(String imgPath,float percent) throws IOException{
        BufferedImage src=ImageIO.read(new File(imgPath));
        int height=src.getHeight();
        int width=src.getWidth();
        int count=0,pixTemp=0,pixel=0;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                pixel=src.getRGB(i, j);
                if(pixel==pixTemp) //如果上一个像素点和这个像素点颜色一样的话，就判定为同一种颜色
                    count++;
                else
                    count=0;
                if((float)count/(height*width)>=percent) //如果连续相同的像素点大于设定的百分比的话，就判定为是纯色的图片 
                    return true;
                pixTemp=pixel;
            }
        }
        return false;
    }
    
    public static void main(String[] args) throws IOException {
    	File f1 = new File("D:/Tesseract-OCR/readpic/3.png");
    	System.out.println(isSimpleColorImg("D:/Tesseract-OCR/readpic/4.png", 0.01f));
    	imageCut(1, 1, 60, 60, "D:/Tesseract-OCR/readpic/3.png", "D:/Tesseract-OCR/readpic/4.png");
	}
}
