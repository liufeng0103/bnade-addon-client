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

    public static final String ADDONS_DIR = "/Interface/AddOns";
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
        String version = "";
        String wowDir = ClientProperties.getKeyValue("wowDir");
        File file = new File(wowDir + ADDONS_DIR + "/Bnade/Data.lua");
        if (file.exists()) {
            String content = IOUtils.inputStreamToString(new FileInputStream(file));
            String startStr = "[\"updated\"]=\"";
            String endStr = "\",";
            version = content.substring(content.indexOf(startStr) + startStr.length(), content.indexOf(endStr));
        }
        return version;
    }

    /**
     * 获取远程插件版本
     * @return
     * @throws IOException
     */
    public String getVersion() throws IOException {
        String versionStr = httpClient.get("https://www.bnade.com/wow/addon/version", true);
        Gson gson = new Gson();
        Addon addon = gson.fromJson(versionStr, Addon.class);
        return addon.getVersion();
    }

    public boolean isTradeSkillMasterAppHelperInstalled() {
        return new File(ClientProperties.getKeyValue("wowDir") + ADDONS_DIR + "/TradeSkillMaster_AppHelper").exists();
    }

    /**
     * 获取本地TSM插件数据版本
     * @return
     * @throws IOException
     */
    public String getLocalTSMAppDataVersion() throws IOException {
        String version = "";
        String wowDir = ClientProperties.getKeyValue("wowDir");
        File file = new File(wowDir + ADDONS_DIR + "/TradeSkillMaster_AppHelper/AppData.lua");
        if (file.exists()) {
            String content = IOUtils.inputStreamToString(new FileInputStream(file));
            String startStr = "downloadTime=";
            String endStr = ",fields";
            int start = content.indexOf(startStr);
            int end = content.indexOf(endStr);
            if (start > 0 && end > 0) {
                version = content.substring(start + startStr.length(), end);
            }
        }
        return version;
    }

    /**
     * 获取远程TSM插件数据版本
     * @return
     * @throws IOException
     */
    public String getTSMAppDataVersion(int realmId) throws IOException {
        String versionStr = httpClient.get("https://www.bnade.com/wow/addon/tsm/"+realmId+"/version", true);
        Gson gson = new Gson();
        Addon addon = gson.fromJson(versionStr, Addon.class);
        return addon.getVersion();
    }

    /**
     * 下载并更新本地插件
     * @throws IOException
     */
    public void updateAddon() throws IOException {
        HttpURLConnection con = httpClient.getConnection("https://www.bnade.com/Bnade.zip");
        try (InputStream is = con.getInputStream()) {
            String wowDir = ClientProperties.getKeyValue("wowDir");
            new File(wowDir + ADDONS_DIR).mkdirs();
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
        System.out.println(client.getLocalTSMAppDataVersion());
        System.out.println(client.isTradeSkillMasterAppHelperInstalled());

    }
}
