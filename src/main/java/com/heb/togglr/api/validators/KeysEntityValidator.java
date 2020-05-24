package com.heb.togglr.api.validators;

import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.entities.KeysEntity;
import com.heb.togglr.api.repositories.ApplicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateKeysEntityValidator")
public class KeysEntityValidator implements Validator {

    @Autowired
    ApplicationsRepository applicationsRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return KeysEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        KeysEntity keysEntity = (KeysEntity) target;

        if (keysEntity.getKeyName() == null || keysEntity.getKeyName().trim().length() == 0) {
            errors.rejectValue("keyName", "Nonempty keyName (key name) required");
        }

        if (keysEntity.getAppId() == null) {
            errors.rejectValue("appId", "Nonempty appId (App ID) required");
        } else {
            AppEntity appEntity = applicationsRepository.findById(keysEntity.getAppId()).orElse(null);
            if (appEntity == null) {
                errors.rejectValue("appId", "An app for given appId does not exist");
            }
        }
    }


}
