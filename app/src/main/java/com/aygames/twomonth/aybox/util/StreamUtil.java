package com.aygames.twomonth.aybox.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MyPC on 2017/3/1.
 */

public class StreamUtil {
    /**
     * 将输入流转换成字符串
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String stream2string(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len=0;
        byte[] bytes=new byte[1024];
        try {
            while ((len=inputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream.close();
        outputStream.close();
        return outputStream.toString();
    }
}
