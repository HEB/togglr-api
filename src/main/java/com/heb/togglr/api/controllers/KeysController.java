package com.heb.togglr.api.controllers;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.heb.togglr.api.entities.KeysEntity;
import com.heb.togglr.api.entities.KeysEntityPK;
import com.heb.togglr.api.repositories.KeysRepository;

@RepositoryRestController
public class KeysController {

    private KeysRepository keysRepository;

    public KeysController(KeysRepository keysRepository){
        this.keysRepository = keysRepository;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/keysEntities/{keyId}")
    @ResponseBody
    public void RemoveKey(@PathVariable String keyId){

        String[] parts = keyId.split("_");
        KeysEntityPK pk = new KeysEntityPK();
        pk.setAppId(Integer.parseInt(parts[0]));
        pk.setKeyName(parts[1]);

        KeysEntity keysEntity = this.keysRepository.findById(pk).orElse(null);

        if(keysEntity != null){
            this.keysRepository.delete(keysEntity);
        }
    }

}
