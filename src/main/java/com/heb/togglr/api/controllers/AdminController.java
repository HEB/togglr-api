package com.heb.togglr.api.controllers;

import com.heb.togglr.api.entities.AdminsEntity;
import com.heb.togglr.api.entities.AdminsEntityPK;
import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.repositories.AdminRepository;
import com.heb.togglr.api.repositories.ApplicationsRepository;
import javassist.tools.web.BadHttpRequest;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;


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
    public void removeAdmin(@PathVariable String adminId) throws BadHttpRequest {

        String[] parts = adminId.split("_");
        if(parts.length != 2){
            throw new BadHttpRequest(new Exception("Could not fine admin with id " + adminId));
        }

        AdminsEntityPK pk = new AdminsEntityPK();
        pk.setId(parts[0]);
        pk.setAppId(Integer.parseInt(parts[1]));
        AdminsEntity adminsEntity = this.adminRepository.findById(pk).orElse(null);

        if(adminsEntity == null){
            throw new BadHttpRequest(new Exception("Could not fine admin with id " + adminId));
        }

        if(adminsEntity.getAppByAppId().getAdminsById().size() > 1){
            AppEntity app = adminsEntity.getAppByAppId();
            app.getAdminsById().remove(adminsEntity);
            this.applicationsRepository.save(app);
            this.adminRepository.delete(adminsEntity);
        }
    }
}
