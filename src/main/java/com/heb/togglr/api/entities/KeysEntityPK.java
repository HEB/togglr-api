package com.heb.togglr.api.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;

public class KeysEntityPK implements Serializable {
    private Integer appId;
    private String keyName;

    @Column(name = "APP_ID")
    @Id
    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Column(name = "KEY_NAME")
    @Id
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeysEntityPK that = (KeysEntityPK) o;
        return Objects.equals(appId, that.appId) &&
                Objects.equals(keyName, that.keyName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(appId, keyName);
    }
}
