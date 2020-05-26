package com.heb.togglr.api.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestClientException;
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
    @Async
    public void handleFeatureSave(FeatureEntity fe) {
        try {
            AppEntity appEntity = fe.getAppByAppId();

            String webHookUrl = appEntity.getWebhookUrl();
            this.callWebhook(webHookUrl);
        }catch (Exception e){

        }
    }

    @HandleAfterSave
    @Async
    public void handleConfigSave(ConfigsEntity ce) {
        try {
            AppEntity appEntity = ce.getAppByAppId();

            String webhookUrl = appEntity.getWebhookUrl();
            this.callWebhook(webhookUrl);
        }catch (Exception e){

        }
    }

    @HandleAfterCreate
    @Async
    public void handleFeatureCreate(FeatureEntity fe) {
        try {
            AppEntity appEntity = fe.getAppByAppId();

            String webHookUrl = appEntity.getWebhookUrl();
            this.callWebhook(webHookUrl);
        }catch (Exception e){

        }
    }

    @HandleAfterCreate
    @Async
    public void handleConfigCreate(ConfigsEntity ce) {
        try {
            AppEntity appEntity = ce.getAppByAppId();

            String webhookUrl = appEntity.getWebhookUrl();
            this.callWebhook(webhookUrl);
        }catch (Exception e){

        }
    }

    @HandleBeforeDelete
    @Async
    public void handleFeatureDelete(FeatureEntity fe) {
        try {
            AppEntity appEntity = fe.getAppByAppId();

            String webHookUrl = appEntity.getWebhookUrl();
            this.callWebhook(webHookUrl);
        }catch (Exception e){

        }
    }

    @HandleBeforeDelete
    @Async
    public void handleConfigDelete(ConfigsEntity ce) {
        try {
            AppEntity appEntity = ce.getAppByAppId();

            String webhookUrl = appEntity.getWebhookUrl();
            this.callWebhook(webhookUrl);
        }catch (Exception e){

        }
    }

    private void callWebhook(String webhookUrl){
        if(webhookUrl != null) {
            logger.debug("Calling webhook, triggered by update.");
            logger.trace("Webhook URL: " + webhookUrl);
            try {
                WebhookResponse webhookResponse = this.restTemplate.postForObject(webhookUrl, null, WebhookResponse.class);
                if (webhookResponse != null) {
                    logger.debug("Webhook update successful.");
                }
            }catch (RestClientException e){
                logger.debug("An exception happened inside callWebhook. See error log for details.");
                logger.error(e.getMessage());
            }
        }
    }
}