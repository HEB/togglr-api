package com.heb.togglr.api.validators;

import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.entities.FeatureEntity;
import com.heb.togglr.api.repositories.ApplicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateFeatureEntityValidator")
public class FeatureEntityValidator implements Validator {

    @Autowired
    ApplicationsRepository applicationsRepository;

    @Override
    public boolean supports(Class<?> clazz) {
    return FeatureEntity.class.equals(clazz);
}

    @Override
    public void validate(Object target, Errors errors) {
        FeatureEntity featureEntity = (FeatureEntity) target;

        /**
         * Check that descr is present/valid
         */
        if (featureEntity.getDescr() == null || featureEntity.getDescr().trim().length() == 0) {
            errors.rejectValue("descr", "Nonempty descr (description) required");
        }

        /**
         * Check that appId is present/valid
         */
        if (featureEntity.getAppId() == null) {
            errors.rejectValue("appId", "Nonempty appId (App ID) required");
        } else {
            AppEntity appEntity = applicationsRepository.findById(featureEntity.getAppId()).orElse(null);
            if (appEntity == null) {
                errors.rejectValue("appId", "App for appID (App ID) does not exist");
            }
        }

        /**
         * Check that negation is present
         */
        if (featureEntity.getNegation() == null) {
            errors.rejectValue("negation", "negation (true/false) property required");
        }

        /**
         * Check that active is present
         */
        if (featureEntity.getActive() == null) {
            errors.rejectValue("active", "active (true/false) property required");
        }
    }
}
