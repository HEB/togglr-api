package com.heb.togglr.api.controllers;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heb.togglr.api.entities.ConfigsEntity;
import com.heb.togglr.api.entities.ConfigsEntityPK;
import com.heb.togglr.api.repositories.ConfigsRepository;

@RepositoryRestController
public class ConfigsController {

    private ConfigsRepository configsRepository;


    public ConfigsController(ConfigsRepository configsRepository){
        this.configsRepository = configsRepository;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/configsEntities/{configId}")
    @ResponseBody
    public void deleteConfig(@PathVariable String configId) {

        String[] parts = configId.split("_");
        ConfigsEntityPK pk = new ConfigsEntityPK();
        pk.setAppId(Integer.parseInt(parts[0]));
        pk.setKeyName(parts[1]);
        pk.setFeatureId(Integer.parseInt(parts[2]));
        pk.setConfigValue(parts[3]);

        ConfigsEntity configsEntity = this.configsRepository.findById(pk).orElse(null);

        if(configsEntity != null){
            this.configsRepository.delete(configsEntity);
        }
    }

}
