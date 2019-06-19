package com.heb.togglr.api.controllers;

import com.heb.togglr.api.client.model.requests.ActiveFeaturesRequest;
import com.heb.togglr.api.client.model.response.AvailableFeaturesList;
import com.heb.togglr.api.client.model.response.FeatureResponse;
import com.heb.togglr.api.service.FeatureService;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RepositoryRestController
@RequestMapping("/features")
public class FeaturesController {

    private FeatureService featureService;

    public FeaturesController(FeatureService featureService){
        this.featureService = featureService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/active")
    @ResponseBody
    public AvailableFeaturesList getAllActiveFeatures(@RequestBody ActiveFeaturesRequest activeFeaturesRequest){
        return this.featureService.getFeaturesByConfig(
                activeFeaturesRequest.getAppId(),
                activeFeaturesRequest.getConfigs());
    }
}
