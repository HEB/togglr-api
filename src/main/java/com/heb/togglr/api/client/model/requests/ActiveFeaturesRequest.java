package com.heb.togglr.api.client.model.requests;

import java.util.HashMap;
import java.util.Map;

public class ActiveFeaturesRequest {

    private int appId;
    private Map<String, String> configs;

    public ActiveFeaturesRequest(){
        this.configs = new HashMap<>();
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public Map<String, String> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, String> configs) {
        this.configs = configs;
    }
}
