package com.heb.togglr.api.entities.config;

import java.io.Serializable;

import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import com.heb.togglr.api.entities.KeysEntity;
import com.heb.togglr.api.entities.KeysEntityPK;

@Component
class KeysBackendIdConverter implements BackendIdConverter {

    @Override
    public Serializable fromRequestId(String id, Class<?> clazz) {

        // Make sure you validate the input
        if(id == null){
            return null;
        }

        String[] parts = id.split("_");
        KeysEntityPK pk = new KeysEntityPK();
        pk.setAppId(Integer.parseInt(parts[0]));
        pk.setKeyName(parts[1]);
        return pk;
    }

    @Override
    public String toRequestId(Serializable source, Class<?> clazz) {

        KeysEntityPK id = (KeysEntityPK) source;
        return String.format("%s_%s", id.getAppId(), id.getKeyName());
    }

    @Override
    public boolean supports(Class<?> type) {
        return KeysEntity.class.equals(type);
    }
}