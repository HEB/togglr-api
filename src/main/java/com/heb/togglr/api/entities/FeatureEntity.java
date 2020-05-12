package com.heb.togglr.api.entities;

import com.heb.togglr.api.client.model.response.FeatureResponse;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "FEATURE", schema = "togglr")
public class FeatureEntity {
    private Integer id;
    private String descr;
    private Integer appId;
    private Boolean active;
    private Boolean negation;
    private Date lastToggled;
    private String toggledBy;
    private Collection<ConfigsEntity> configsById;
    private AppEntity appByAppId;

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
    @Column(name = "DESCR")
    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Basic
    @Column(name = "APP_ID")
    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Basic
    @Column(name = "ACTIVE")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "NEGATION")
    public Boolean getNegation() {
        return negation;
    }

    public void setNegation(Boolean negation) {
        this.negation = negation;
    }

    @Basic
    @Column(name = "LAST_TOGGLED")
    public Date getLastToggled() {
        return lastToggled;
    }

    public void setLastToggled(Date lastToggled) {
        this.lastToggled = lastToggled;
    }

    @Basic
    @Column(name = "TOGGLED_BY")
    public String getToggledBy() {
        return toggledBy;
    }

    public void setToggledBy(String toggledBy) {
        this.toggledBy = toggledBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureEntity entity = (FeatureEntity) o;
        return Objects.equals(id, entity.id) &&
                Objects.equals(descr, entity.descr) &&
                Objects.equals(appId, entity.appId) &&
                Objects.equals(active, entity.active) &&
                Objects.equals(negation, entity.negation) &&
                Objects.equals(lastToggled, entity.lastToggled) &&
                Objects.equals(toggledBy, entity.toggledBy);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, descr, appId, active, negation, lastToggled, toggledBy);
    }

    @OneToMany(mappedBy = "featureByFeatureId", cascade = CascadeType.REMOVE)
    public Collection<ConfigsEntity> getConfigsById() {
        return configsById;
    }

    public void setConfigsById(Collection<ConfigsEntity> configsById) {
        this.configsById = configsById;
    }

    @ManyToOne
    @JoinColumn(name = "APP_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    public AppEntity getAppByAppId() {
        return appByAppId;
    }

    public void setAppByAppId(AppEntity appByAppId) {
        this.appByAppId = appByAppId;
    }


    public static FeatureResponse featureResponseFromEntity(FeatureEntity featureEntity) {

        FeatureResponse response = new FeatureResponse();
        response.setAppId(featureEntity.getAppId());
        response.setDescr(featureEntity.getDescr());
        response.setId(featureEntity.getId());
        response.setLastToggled(featureEntity.getLastToggled());
        response.setToggledBy(featureEntity.getToggledBy());

        return response;
    }


    public static List<FeatureResponse> featureResponsesFromEntities(Collection<FeatureEntity> entities){

        List<FeatureResponse> featureResponses = new ArrayList<>();

        for(FeatureEntity entity : entities){
            featureResponses.add(featureResponseFromEntity(entity));
        }

        return featureResponses;
    }

}
