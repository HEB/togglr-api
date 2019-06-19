package com.heb.togglr.api.controllers;

import java.util.Collection;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heb.togglr.api.entities.ConfigsEntity;
import com.heb.togglr.api.entities.FeatureEntity;
import com.heb.togglr.api.repositories.ConfigsRepository;
import com.heb.togglr.api.repositories.FeatureRepository;
import javassist.tools.web.BadHttpRequest;

@RepositoryRestController
public class FeatureEntitiesController {

    private FeatureRepository featureRepository;
    private ConfigsRepository configsRepository;

    public FeatureEntitiesController(FeatureRepository featureRepository, ConfigsRepository configsRepository){
        this.featureRepository = featureRepository;
        this.configsRepository = configsRepository;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/featureEntities/{featureId}")
    @ResponseBody
    public void removeFeature(@PathVariable Integer featureId) throws BadHttpRequest {


        FeatureEntity featureEntity = this.featureRepository.findById(featureId).orElse(null);

        if(featureEntity == null){
            throw new BadHttpRequest(new Exception("Could not fine admin with id " + featureId));
        }

        Collection<ConfigsEntity> configs = featureEntity.getConfigsById();

        this.configsRepository.deleteAll(configs);

        featureEntity.getConfigsById().clear();
        this.featureRepository.delete(featureEntity);
    }
}
