package com.heb.togglr.api.entities.config;

import com.heb.togglr.api.entities.AdminsEntity;
import com.heb.togglr.api.entities.AdminsEntityPK;
import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
class AdminBackendIdConverter implements BackendIdConverter {

    @Override
    public Serializable fromRequestId(String id, Class<?> clazz) {

        // Make sure you validate the input
        if(id == null){
            return null;
        }

        String[] parts = id.split("_");
        AdminsEntityPK pk = new AdminsEntityPK();
        pk.setId(parts[0]);
        pk.setAppId(Integer.parseInt(parts[1]));
        return pk;
    }

    @Override
    public String toRequestId(Serializable source, Class<?> clazz) {

        AdminsEntityPK id = (AdminsEntityPK) source;
        return String.format("%s_%s", id.getId(),id.getAppId());
    }

    @Override
    public boolean supports(Class<?> type) {
        return AdminsEntity.class.equals(type);
    }
}