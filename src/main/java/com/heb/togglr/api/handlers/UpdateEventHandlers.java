package com.heb.togglr.api.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.web.client.RestTemplate;

import com.heb.togglr.api.entities.AppEntity;
import com.heb.togglr.api.entities.ConfigsEntity;
import com.heb.togglr.api.entities.FeatureEntity;
import com.heb.togglr.api.models.responses.WebhookResponse;

@RepositoryEventHandler
public class UpdateEventHandlers {

    private static Logger logger = LoggerFactory.getLogger(UpdateEventHandlers.class);

    private RestTemplate restTemplate;

    public UpdateEventHandlers(){
        this.restTemplate = new RestTemplate();
    }

    @HandleAfterSave
    public void handleFeatureSave(FeatureEntity fe) {
        AppEntity appEntity = fe.getAppByAppId();

        String webHookUrl = appEntity.getWebhookUrl();

        if(webHookUrl != null) {
            WebhookResponse webhookResponse = this.restTemplate.postForObject(webHookUrl, null, WebhookResponse.class);
            if(webhookResponse != null){
                logger.debug("Webhook update successful.");
            }
        }
    }

    @HandleAfterSave
    public void handleConfigSave(ConfigsEntity ce) {
        AppEntity appEntity = ce.getAppByAppId();

        String webhookUrl = appEntity.getWebhookUrl();

        if(webhookUrl != null) {
            this.restTemplate.postForObject(webhookUrl, null, WebhookResponse.class);
        }
    }
}