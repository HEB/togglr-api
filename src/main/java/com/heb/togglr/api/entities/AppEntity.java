package com.heb.togglr.api.entities;

import java.util.Collection;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name ="APP", schema = "togglr")
public class AppEntity {
    private Integer id;
    private String name;
    private String descr;
    private String webhookUrl;
    private Collection<AdminsEntity> adminsById;
    private Collection<ConfigsEntity> configsById;
    private Collection<FeatureEntity> featuresById;
    private Collection<KeysEntity> keysById;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "APP_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "DESCR")
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Basic
    @Column(name = "WEBHOOK_URL")
    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppEntity appEntity = (AppEntity) o;
        return Objects.equals(id, appEntity.id) &&
                Objects.equals(name, appEntity.name) &&
                Objects.equals(descr, appEntity.descr);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, descr);
    }

    @OneToMany(mappedBy = "appByAppId", cascade = CascadeType.REMOVE)
    public Collection<AdminsEntity> getAdminsById() {
        return adminsById;
    }

    public void setAdminsById(Collection<AdminsEntity> adminsById) {
        this.adminsById = adminsById;
    }

    @OneToMany(mappedBy = "appByAppId", cascade = CascadeType.REMOVE)
    public Collection<ConfigsEntity> getConfigsById() {
        return configsById;
    }

    public void setConfigsById(Collection<ConfigsEntity> configsById) {
        this.configsById = configsById;
    }

    @OneToMany(mappedBy = "appByAppId", cascade = CascadeType.REMOVE)
    public Collection<FeatureEntity> getFeaturesById() {
        return featuresById;
    }

    public void setFeaturesById(Collection<FeatureEntity> featuresById) {
        this.featuresById = featuresById;
    }

    @OneToMany(mappedBy = "appByAppId", cascade = CascadeType.REMOVE)
    public Collection<KeysEntity> getKeysById() {
        return keysById;
    }

    public void setKeysById(Collection<KeysEntity> keysById) {
        this.keysById = keysById;
    }
}
