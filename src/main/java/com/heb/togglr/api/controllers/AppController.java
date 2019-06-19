package com.heb.togglr.api.controllers;

import com.heb.togglr.api.entities.AdminsEntity;
import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.repositories.AdminRepository;
import com.heb.togglr.api.repositories.ApplicationsRepository;
import com.heb.togglr.api.repositories.SuperAdminRepository;

import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.Resources;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RepositoryRestController
public class AppController {

    private ApplicationsRepository applicationsRepository;
    private AdminRepository adminRepository;
    private SuperAdminRepository superAdminRepository;

    public AppController(ApplicationsRepository applicationsRepository, AdminRepository adminRepository, SuperAdminRepository superAdminRepository){
        this.applicationsRepository = applicationsRepository;
        this.adminRepository = adminRepository;
        this.superAdminRepository = superAdminRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/appEntities")
    @ResponseBody
    public ResponseEntity<?> createApp(@RequestBody AppEntity appEntity){
        //Clone the list of admins
        List<AdminsEntity> admins = new ArrayList<>();

        if(appEntity.getAdminsById() != null) {
            for (AdminsEntity ae : appEntity.getAdminsById()) {
                AdminsEntity adminsEntity = new AdminsEntity();
                adminsEntity.setId(ae.getId());
                admins.add(adminsEntity);
            }
            //Clear before we save
            appEntity.getAdminsById().clear();
        }

        appEntity = this.applicationsRepository.save(appEntity);

        for(AdminsEntity ae : admins){
            ae.setAppId(appEntity.getId());
            appEntity.getAdminsById().add(ae);
        }

        this.adminRepository.saveAll(admins);
        appEntity = this.applicationsRepository.save(appEntity);

        return ResponseEntity.ok(appEntity);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/appEntities/{appId}")
    @ResponseBody
    public void updateApplication(@PathVariable int appId, @RequestBody AppEntity appEntity){
        AppEntity repoVersion = this.applicationsRepository.findById(appId).orElse(null);

        if(repoVersion != null){
            repoVersion.setWebhookUrl(appEntity.getWebhookUrl());
        }

        repoVersion = this.applicationsRepository.save(repoVersion);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/appEntities")
    @ResponseBody
    public Resources getApplicationsForUser(PersistentEntityResourceAssembler resourceAssembler){
        String userId = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getUsername();

        List<AppEntity> appEntities;

        if(this.superAdminRepository.findById(userId).orElse(null) != null){
            appEntities = new ArrayList<>();
            applicationsRepository.findAll().forEach(appEntities::add);
        }else{
            appEntities = this.applicationsRepository.findAllByAdminsById_Id(userId);
        }

        List<Resource<AppEntity>> resourcelist = new ArrayList<Resource<AppEntity>>();

        for(AppEntity app : appEntities){
            Resource resource = resourceAssembler.toFullResource(app);
            resourcelist.add(resource);
        }

        Resources<AppEntity> resources = new Resources(resourcelist);

        return resources;
    }
}
