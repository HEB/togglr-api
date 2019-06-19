package com.heb.togglr.api.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Id;

public class AdminsEntityPK implements Serializable {
    private String id;
    private Integer appId;

    @Column(name = "ID")
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "APP_ID")
    @Id
    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminsEntityPK that = (AdminsEntityPK) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(appId, that.appId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, appId);
    }
}
