package com.bnade.wow.addon;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.util.Properties;

/**
 * Created by liufe on 2016/8/18.
 */
public class ClientProperties {

    private static final String PROPERTY_FILE = "client.properties";
    private static Properties props = new Properties();

    static {
        try {
            InputStream is = ClientProperties.class.getClassLoader().getResourceAsStream(PROPERTY_FILE);
            if (is == null) {
                is = new FileInputStream(PROPERTY_FILE);
            }
            props.load(is);
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

    public static void setKeyValue(String key, String value) throws IOException {
        OutputStream fos = new FileOutputStream(PROPERTY_FILE);
        props.setProperty(key, value);
        props.store(fos, "Update " + key + " value to " + value);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(ClientProperties.getKeyValue("wowDir"));
        System.out.println(ClientProperties.getKeyValue("dd"));
        ClientProperties.setKeyValue("test", "test");
    }
}
