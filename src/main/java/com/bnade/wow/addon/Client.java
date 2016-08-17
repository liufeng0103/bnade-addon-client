package com.bnade.wow.addon;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by liufeng0103 on 8/17/2016.
 */
public class Client {

    public Client() {
    }

    public String getLocalVersion() {
        return "";
    }

    public String getVersion() throws IOException {
        HttpClient httpClient = new HttpClient();
        String versionStr = httpClient.get("http://www.bnade.com/wow/addon/version");
        Gson gson = new Gson();
        Addon addon = gson.fromJson(versionStr, Addon.class);
        return addon.getVersion();
    }

    public void updateAddon() {

    }

}
