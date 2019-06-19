package com.heb.togglr.api.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;

public class ConfigsEntityPK implements Serializable {
    private Integer appId;
    private Integer featureId;
    private String keyName;
    private String configValue;

    @Column(name = "APP_ID")
    @Id
    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Column(name = "FEATURE_ID")
    @Id
    public Integer getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Integer featureId) {
        this.featureId = featureId;
    }

    @Column(name = "KEY_NAME")
    @Id
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Column(name = "CONFIG_VALUE")
    @Id
    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigsEntityPK that = (ConfigsEntityPK) o;
        return Objects.equals(appId, that.appId) &&
                Objects.equals(featureId, that.featureId) &&
                Objects.equals(keyName, that.keyName) &&
                Objects.equals(configValue, that.configValue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(appId, featureId, keyName, configValue);
    }
}
