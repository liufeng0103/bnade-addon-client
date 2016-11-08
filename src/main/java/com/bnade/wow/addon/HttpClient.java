package com.bnade.wow.addon;

import com.google.gson.JsonObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

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
        HttpURLConnection con = getConnection(url);
        try (InputStream is = con.getInputStream())  {
            content = IOUtils.inputStreamToString(is);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return content;
    }

    public String get(String url, boolean isSsl) throws IOException {
        if (isSsl) {
            try {
                trustAllHttpsCertificates();
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            }
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    System.out.println("Warning: URL Host: " + urlHostName + " vs. "
                            + session.getPeerHost());
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        }
        return get(url);
    }

    public HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        con.setConnectTimeout(connectionTimeout);
        con.setReadTimeout(readTimeout);
        return con;
    }

    private static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        String url = "http://www.baidu.com";
        System.out.println(client.get(url));
    }
}