package com.heb.togglr.api.validators;

import com.heb.togglr.api.entities.*;
import com.heb.togglr.api.repositories.ApplicationsRepository;
import com.heb.togglr.api.repositories.FeatureRepository;
import com.heb.togglr.api.repositories.KeysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateConfigsEntityValidator")
public class ConfigsEntityValidator implements Validator {

    @Autowired
    ApplicationsRepository applicationsRepository;

    @Autowired
    FeatureRepository featureRepository;

    @Autowired
    KeysRepository keysRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ConfigsEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ConfigsEntity configsEntity = (ConfigsEntity) target;

        // make sure app is non-null and is a real app
        if (configsEntity.getAppId() == null) {
            errors.rejectValue("appId", "Nonempty appId (Application ID) required");
        } else {
            AppEntity appEntity = applicationsRepository.findById(configsEntity.getAppId()).orElse(null);
            if (appEntity == null) {
                errors.rejectValue("appId", "App for given appId does not exist");
            }
        }

        // make sure key is non-null and is a real key for given app
        if (configsEntity.getKeyName() == null || configsEntity.getKeyName().trim().length() == 0) {
            errors.rejectValue("keyName", "Nonempty keyName (key name) required");
        } else {
            KeysEntityPK pk = new KeysEntityPK();
            pk.setAppId(configsEntity.getAppId());
            pk.setKeyName(configsEntity.getKeyName());
            KeysEntity keysEntity = this.keysRepository.findById(pk).orElse(null);
            if(keysEntity == null){
                errors.rejectValue("keyName", "Could not find specified key for given app");
            }
        }


        // make sure feature is non-null and is a real feature
        if (configsEntity.getFeatureId() == null) {
            errors.rejectValue("featureId", "Nonempty featureId (Feature ID) required");
        } else {
            FeatureEntity featureEntity = featureRepository.findById(configsEntity.getFeatureId()).orElse(null);
            if (featureEntity == null) {
                errors.rejectValue("featureId", "Could not find specified feature for given featureId");
            }
        }

        // make sure the config value is non-null and matches regex pattern
        if (configsEntity.getConfigValue() == null) {
            errors.rejectValue("configValue", "Nonempty configValue (config name) required");
        } else if (badConfigValue(configsEntity.getConfigValue())) {
            errors.rejectValue("configValue", "Letters, numbers, and hyphens only for config value");
        }

    }


    /**
     * Checks that a config name is alphanumeric and
     * that it does not contain any special characters besides
     * hyphens
     * @param input
     * @return
     */
    private boolean badConfigValue(String input) {
        String pattern = "^[-a-zA-Z0-9]+$"; //alphanumeric and hyphens
        return (input.trim().length() == 0 || !input.matches(pattern));
    }


}
