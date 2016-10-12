package com.traciing.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Carl_yang on 2016/6/13.
 */
public class SystemUtil {

    /**
     * 流转换成BYTE数组
     * @param is 输入流
     * @return
     * @throws Exception
     */
    public static byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {

            baos.write(buffer, 0, len);

        }
        is.close();
        baos.flush();
        return baos.toByteArray();
    }
}
