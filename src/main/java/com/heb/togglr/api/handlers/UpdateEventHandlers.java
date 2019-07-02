package com.heb.togglr.api.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
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
    public void handleFeatureSave(FeatureEntity fe) {
        AppEntity appEntity = fe.getAppByAppId();

        String webHookUrl = appEntity.getWebhookUrl();
        this.callWebhook(webHookUrl);
    }

    @HandleAfterSave
    public void handleConfigSave(ConfigsEntity ce) {
        AppEntity appEntity = ce.getAppByAppId();

        String webhookUrl = appEntity.getWebhookUrl();
        this.callWebhook(webhookUrl);
    }

    @HandleAfterCreate
    public void handleFeatureCreate(FeatureEntity fe) {
        AppEntity appEntity = fe.getAppByAppId();

        String webHookUrl = appEntity.getWebhookUrl();
        this.callWebhook(webHookUrl);
    }

    @HandleAfterCreate
    public void handleConfigCreate(ConfigsEntity ce) {
        AppEntity appEntity = ce.getAppByAppId();

        String webhookUrl = appEntity.getWebhookUrl();
        this.callWebhook(webhookUrl);
    }

    @HandleAfterDelete
    public void handleFeaturDelete(FeatureEntity fe) {
        AppEntity appEntity = fe.getAppByAppId();

        String webHookUrl = appEntity.getWebhookUrl();
        this.callWebhook(webHookUrl);
    }

    @HandleAfterDelete
    public void handleConfigDelete(ConfigsEntity ce) {
        AppEntity appEntity = ce.getAppByAppId();

        String webhookUrl = appEntity.getWebhookUrl();
        this.callWebhook(webhookUrl);
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
                logger.error(e.getMessage());
            }
        }
    }
}