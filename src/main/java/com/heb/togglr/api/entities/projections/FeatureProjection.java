package com.heb.togglr.api.entities.projections;

import com.heb.togglr.api.entities.*;
import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;

@Projection(name = "includeSubObjects", types = {FeatureEntity.class})
public interface FeatureProjection {

    int getId();

    String getAppId();

    String getDescr();

    Boolean getActive();

    Boolean getNegation();

    Collection<ConfigsEntity> getConfigsById();
}
