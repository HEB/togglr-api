package com.heb.togglr.api.entities.config;

import com.heb.togglr.api.entities.*;
import com.heb.togglr.api.repositories.AdminRepository;
import com.heb.togglr.api.repositories.ConfigsRepository;
import com.heb.togglr.api.repositories.KeysRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        config.setBasePath("/");
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);
        config.exposeIdsFor(AppEntity.class, AdminsEntity.class, ConfigsEntity.class, FeatureEntity.class, KeysEntity.class);

        config.withEntityLookup().forRepository(AdminRepository.class, (AdminsEntity ad) -> {
            AdminsEntityPK pk = new AdminsEntityPK();
            pk.setId(ad.getId());
            pk.setAppId(ad.getAppId());
            return pk;
        }, AdminRepository::findById);

        config.withEntityLookup().forRepository(KeysRepository.class, (KeysEntity ke) -> {
            KeysEntityPK pk = new KeysEntityPK();
            pk.setKeyName(ke.getKeyName());
            pk.setAppId(ke.getAppId());
            return pk;
        }, KeysRepository::findById);

        config.withEntityLookup().forRepository(ConfigsRepository.class, (ConfigsEntity ce) -> {
            ConfigsEntityPK pk = new ConfigsEntityPK();
            pk.setKeyName(ce.getKeyName());
            pk.setAppId(ce.getAppId());
            pk.setFeatureId(ce.getFeatureId());
            pk.setConfigValue(ce.getConfigValue());
            return pk;
        }, ConfigsRepository::findById);
    }
}
