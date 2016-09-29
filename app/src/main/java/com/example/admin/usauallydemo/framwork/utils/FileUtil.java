package com.example.admin.usauallydemo.framwork.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lizhangfeng on 16/3/29.
 * description:
 */
public class FileUtil {

    public static File getFileFromBytes(byte[] b, File ret) {

        BufferedOutputStream stream = null;
        try {

            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

}
