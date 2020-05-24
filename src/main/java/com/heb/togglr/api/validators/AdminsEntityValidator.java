package com.heb.togglr.api.validators;

import com.heb.togglr.api.entities.AdminsEntity;
import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.repositories.ApplicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("beforeCreateAdminsEntityValidator")
public class AdminsEntityValidator implements Validator {

    @Autowired
    ApplicationsRepository applicationsRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return AdminsEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AdminsEntity adminsEntity = (AdminsEntity) target;

        if (adminsEntity.getId() == null) {
            errors.rejectValue("id", "Nonempty id (Admin ID) required");
        }

        if (adminsEntity.getAppId() == null) {
            errors.rejectValue("appId", "Nonempty appId (Application ID) required");
        } else {
            AppEntity appEntity = applicationsRepository.findById(adminsEntity.getAppId()).orElse(null);
            if (appEntity == null) {
                errors.rejectValue("appId", "App for appID (App ID) does not exist");
            }
        }
    }



}
