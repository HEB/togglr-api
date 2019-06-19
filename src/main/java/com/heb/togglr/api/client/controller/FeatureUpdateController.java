package com.heb.togglr.api.client.controller;


import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heb.togglr.api.client.service.TogglrUpdateNotifier;

@Profile("togglr")
@RestController
@RequestMapping("/togglr")
public class FeatureUpdateController {


    private TogglrUpdateNotifier togglrUpdateNotifier;


    private FeatureUpdateController(TogglrUpdateNotifier togglrUpdateNotifier){
        this.togglrUpdateNotifier = togglrUpdateNotifier;
    }

    @PostMapping(path = "/update")
    public void featuresUpdatedWebhook(){
        this.togglrUpdateNotifier.registerNewUpdate();
    }
}
