package com.bjfu.li.odour.utils;

import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Base64Utils {
    public static String generateImage(String imgStr, String imgDirPath) {
        File imgDir=new File(imgDirPath);
        if(!imgDir.exists())
            imgDir.mkdir();
        //获取图片类型
        String imgExt= imgStr.split(",")[0].split("[/;]")[1];
        imgStr=imgStr.split(",")[1];

        String fileName=UUIDUtils.getUUID()+"."+imgExt;
        String imgFilePath=imgDirPath+"/"+fileName;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return fileName;
        } catch (Exception e) {
            return null;
        }
    }

}
