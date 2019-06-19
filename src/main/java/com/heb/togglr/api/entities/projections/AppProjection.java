package com.heb.togglr.api.entities.projections;

import com.heb.togglr.api.entities.AdminsEntity;
import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.entities.KeysEntity;

import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;

@Projection(name = "includeSubObjects", types = {AppEntity.class})
public interface AppProjection {

    int getId();

    String getName();

    String getDescr();

    String getWebhookUrl();

    Collection<AdminsEntity> getAdminsById();

    Collection<FeatureProjection> getFeaturesById();

    Collection<KeysEntity> getKeysById();
}
