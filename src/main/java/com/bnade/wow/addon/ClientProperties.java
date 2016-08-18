package com.bnade.wow.addon;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by liufe on 2016/8/18.
 */
public class ClientProperties {

    private static final String PROPERTY_FILE = "client.properties";
    private static Properties props = new Properties();
    static {
        try {
            props.load(new FileInputStream(PROPERTY_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getKeyValue(String key) {
        return props.getProperty(key);
    }

    public static String getKeyValue(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public static void setKeyValue(String key, String value) {
        OutputStream fos = new FileOutputStream(profilepath);
        props.setProperty(keyname, keyvalue);
        // 以适合使用 load 方法加载到 Properties 表中的格式，
        // 将此 Properties 表中的属性列表（键和元素对）写入输出流
        props.store(fos, "Update '" + keyname + "' value");

    }
}
