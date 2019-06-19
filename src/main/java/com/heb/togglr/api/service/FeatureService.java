package com.heb.togglr.api.service;

import com.heb.togglr.api.client.model.response.AvailableFeaturesList;
import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.entities.ConfigsEntity;
import com.heb.togglr.api.entities.FeatureEntity;
import com.heb.togglr.api.repositories.ApplicationsRepository;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FeatureService {

    private ApplicationsRepository applicationsRepository;


    public FeatureService(ApplicationsRepository applicationsRepository) {
        this.applicationsRepository = applicationsRepository;
    }

    /**
     * Returns all active features for an application filtered by CorpId and UserId
     * If a feature is active, and no CorpId or UserId restrictions are configured,
     * the feature will return as active.
     * To completely disable a feature, Active should be set to false.
     *
     * @param appId  Application Id
     * @return List of all active FeatureEntities.
     */
    public AvailableFeaturesList getFeaturesByConfig(int appId, Map<String, String> config) {
        //Create the list we will return
        HashMap<Integer, FeatureEntity> activeFeatures = new HashMap<>();

        AppEntity app = this.applicationsRepository.findById(appId).orElse(null);

        Map<String, String> lowerConfig = new HashMap<>();

        for(String key : config.keySet()){
            String val = config.get(key);
            lowerConfig.put(key.toLowerCase(), val.toLowerCase());
        }


        // If we have a valid app
        if(app != null) {

            // For every feature
            for(FeatureEntity featureEntity : app.getFeaturesById()){

                // If the Feature is Active
                if(featureEntity.getActive()) {

                    //If the feature is negated
                    if (featureEntity.getNegation()) {
                        //If the user config does not match a key
                        if (!keyMatchesConfig(featureEntity.getConfigsById(), lowerConfig)) {
                            //Add the feature to the active list
                            activeFeatures.put(featureEntity.getId(), featureEntity);
                        }
                        //If the feature is not negated
                    } else {
                        //If the user config matches a key
                        if (keyMatchesConfig(featureEntity.getConfigsById(), lowerConfig)) {
                            //Add the feature to the active list
                            activeFeatures.put(featureEntity.getId(), featureEntity);
                        }
                    }
                }
            }
        }

        AvailableFeaturesList availableFeaturesList = new AvailableFeaturesList();
        availableFeaturesList.setAvailableFeatures(FeatureEntity.featureResponsesFromEntities(activeFeatures.values()));

        return availableFeaturesList;
    }

    /**
     * Returns true if one of the user keys matches one of the configs.
     * @param configs
     * @param userConfigs
     * @return
     */
    private boolean keyMatchesConfig(Collection<ConfigsEntity> configs, Map<String, String> userConfigs){

        for(ConfigsEntity configsEntity : configs) {
            String keyValue = userConfigs.get(configsEntity.getKeyName().toLowerCase());

            if(keyValue != null) {
                if (keyValue.toLowerCase().equals(configsEntity.getConfigValue().toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }
}
