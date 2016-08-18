package com.bnade.wow.addon;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by liufeng0103 on 8/17/2016.
 */
public class IOUtils {

    // 参考http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string, 这种方式转化inputStream到String效率最高
    public static String inputStreamToString(InputStream is) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }
    }

    /**
     * 解压zip文件到目标目录
     * @param is
     * @param outputFolder
     * @throws IOException
     */
    public static void extractAllFromInputStream(InputStream is, String outputFolder) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry ze = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
//                System.out.println("file unzip : "+ newFile.getAbsoluteFile());
                new File(newFile.getParent()).mkdirs();
                if (ze.isDirectory()) {
                    newFile.mkdir();
                } else {
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                ze = zis.getNextEntry();
            }
        }
    }

}
