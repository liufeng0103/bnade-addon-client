package com.bnade.wow.addon;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by liufeng0103 on 8/17/2016.
 */
public class Client {

    private String wowDir = "K:\\WOWClient";
    private static final String ADDONS_DIR = "/Interface/AddOns";
    private HttpClient httpClient;

    public Client() {
        httpClient = new HttpClient();
    }

    /**
     * 获取插件版本
     * @return
     * @throws IOException
     */
    public String getLocalVersion() throws IOException {
        String content = IOUtils.inputStreamToString(new FileInputStream(wowDir + ADDONS_DIR + "/Bnade/Data.lua"));
        String startStr = "[\"updated\"]=\"";
        String endStr = "\",";
        return content.substring(content.indexOf(startStr) + startStr.length(), content.indexOf(endStr));
    }

    /**
     * 获取全程插件版本
     * @return
     * @throws IOException
     */
    public String getVersion() throws IOException {
        String versionStr = httpClient.get("http://www.bnade.com/wow/addon/version");
        Gson gson = new Gson();
        Addon addon = gson.fromJson(versionStr, Addon.class);
        return addon.getVersion();
    }

    /**
     * 下载并更新本地插件
     * @throws IOException
     */
    public void updateAddon() throws IOException {
        HttpURLConnection con = httpClient.getConnection("http://www.bnade.com/Bnade.zip");
        try (InputStream is = con.getInputStream()) {
            IOUtils.extractAllFromInputStream(is, wowDir + ADDONS_DIR);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    /**
     * 判断是否为正确的魔兽世界安装目录
     * @param wowDirPath
     * @return
     */
    public boolean isCorrectWowDir(String wowDirPath) {
        File wowDir = new File(wowDirPath);
        for (File file : wowDir.listFiles()) {
//            System.out.println(file.getName());
            if ("World of Warcraft Launcher.exe".equals(file.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
//        client.updateAddon();
        System.out.println(client.getLocalVersion());
    }
}
