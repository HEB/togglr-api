package com.heb.togglr.api.entities.projections;

import com.heb.togglr.api.entities.ConfigsEntity;
import com.heb.togglr.api.entities.FeatureEntity;
import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;
import java.util.Date;

@Projection(name = "includeSubObjects", types = {FeatureEntity.class})
public interface FeatureProjection {

    int getId();

    String getAppId();

    String getDescr();

    Boolean getActive();

    Boolean getNegation();

    Date getLastToggled();

    String getToggledBy();

    Collection<ConfigsEntity> getConfigsById();
}
