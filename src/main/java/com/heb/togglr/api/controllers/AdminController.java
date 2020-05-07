package com.heb.togglr.api.controllers;

import com.heb.togglr.api.entities.AdminsEntity;
import com.heb.togglr.api.entities.AdminsEntityPK;
import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.exceptions.AdminNotFoundException;
import com.heb.togglr.api.exceptions.RemovingSelfAdminException;
import com.heb.togglr.api.repositories.AdminRepository;
import com.heb.togglr.api.repositories.ApplicationsRepository;
import javassist.tools.web.BadHttpRequest;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;


@RepositoryRestController
public class AdminController {

    private AdminRepository adminRepository;
    private ApplicationsRepository applicationsRepository;

    public AdminController(AdminRepository adminRepository, ApplicationsRepository applicationsRepository){
        this.adminRepository = adminRepository;
        this.applicationsRepository = applicationsRepository;
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/adminsEntities/{adminId}")
    @ResponseBody
    public void removeAdmin(@PathVariable String adminId, Principal principal) throws BadHttpRequest {

        String[] parts = adminId.split("_");
        if(parts.length != 2){
            throw new BadHttpRequest(new AdminNotFoundException("Could not find admin with id " + adminId));
        }

        AdminsEntityPK pk = new AdminsEntityPK();
        pk.setId(parts[0]);
        pk.setAppId(Integer.parseInt(parts[1]));
        AdminsEntity adminsEntity = this.adminRepository.findById(pk).orElse(null);

        if(adminsEntity == null){
            throw new BadHttpRequest(new AdminNotFoundException("Could not find admin with id " + adminId));
        }

        if (adminsEntity.getId().equalsIgnoreCase(principal.getName())) {
            throw new BadHttpRequest(new RemovingSelfAdminException("Admin being deleted is same as user"));
        }

        if(adminsEntity.getAppByAppId().getAdminsById().size() > 1){
            AppEntity app = adminsEntity.getAppByAppId();
            app.getAdminsById().remove(adminsEntity);
            this.applicationsRepository.save(app);
            this.adminRepository.delete(adminsEntity);
        }
    }
}
