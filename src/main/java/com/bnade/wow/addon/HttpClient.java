package com.bnade.wow.addon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * htpp客户端程序，完成一些简单的http操作
 *
 * @author liufeng0103
 */
public class HttpClient {

    // http连接超时时间，单位是毫秒
    private int connectionTimeout = 5000;
    // http读取超时时间，单位是毫秒
    private int readTimeout = 5000;

    /**
     * 根据url参数，通过http的get方法获取url的内容
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String get(String url) throws IOException {
        String content = "";
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            con.setConnectTimeout(connectionTimeout);
            con.setReadTimeout(readTimeout);
            // 参考http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string, 这种方式转化inputStream到String效率最高
            try (InputStream is = con.getInputStream(); ) {
                content = IOUtils.inputStreamToString(is);
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return content;
    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        String url = "http://www.baidu.com";
        System.out.println(client.get(url));
    }
}