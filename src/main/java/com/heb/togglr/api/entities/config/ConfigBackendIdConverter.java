package com.heb.togglr.api.entities.config;

import java.io.Serializable;

import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import com.heb.togglr.api.entities.ConfigsEntity;
import com.heb.togglr.api.entities.ConfigsEntityPK;

@Component
class ConfigBackendIdConverter implements BackendIdConverter {

    @Override
    public Serializable fromRequestId(String id, Class<?> clazz) {

        // Make sure you validate the input
        if(id == null){
            return null;
        }

        String[] parts = id.split("_");
        ConfigsEntityPK pk = new ConfigsEntityPK();
        pk.setAppId(Integer.parseInt(parts[0]));
        pk.setKeyName(parts[1]);
        pk.setFeatureId(Integer.parseInt(parts[2]));
        pk.setConfigValue(parts[3]);
        return pk;
    }

    @Override
    public String toRequestId(Serializable source, Class<?> clazz) {

        ConfigsEntityPK id = (ConfigsEntityPK) source;
        return String.format("%s_%s_%s_%s", id.getAppId(), id.getKeyName(), id.getFeatureId(), id.getConfigValue());
    }

    @Override
    public boolean supports(Class<?> type) {
        return ConfigsEntity.class.equals(type);
    }
}