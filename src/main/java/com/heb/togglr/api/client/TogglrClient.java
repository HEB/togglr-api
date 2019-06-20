package com.heb.togglr.api.client;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.heb.togglr.api.client.model.requests.ActiveFeaturesRequest;
import com.heb.togglr.api.client.model.response.AvailableFeaturesList;
import com.heb.togglr.api.client.model.response.FeatureResponse;
import com.heb.togglr.api.client.service.TogglrUpdateNotifier;
import redis.clients.jedis.Jedis;

@Service
public class TogglrClient {

    private static Logger logger = LoggerFactory.getLogger(TogglrClient.class);

    @Value("${heb.togglr.client.app-id}")
    private int applicationId;

    @Value("${heb.togglr.client.server-url}")
    private String togglrUrl;

//    @Value("${redis.host}")
//    private String redisHost;
//
//    @Value("${redis.port}")
//    private int redisPort;

    private TogglrUpdateNotifier togglrUpdateNotifier;
    private RestTemplate restTemplate;

    private Map<String, List<FeatureResponse>> cache;

    private Jedis jedis;

    public TogglrClient(TogglrUpdateNotifier togglrUpdateNotifier){
        this.togglrUpdateNotifier = togglrUpdateNotifier;
        this.restTemplate = new RestTemplate();
        this.cache = new HashMap<>();
        this.jedis = new Jedis();
    }

    /**
     * Manually clear the cache.
     */
    public void clearCache(){
        this.togglrUpdateNotifier.clearCache();
    }

    /**
     * Get the list of ActiveFeatures, returned as GrantedAuthorities for a config.
     * @param activeFeaturesRequest List of active Features.
     * @param cacheId Identifier used for caching the Features.
     * @return
     */
    public List<GrantedAuthority> getFeaturesForConfig(ActiveFeaturesRequest activeFeaturesRequest, String cacheId){

        List<FeatureResponse> features = null;

        activeFeaturesRequest.setAppId(this.applicationId);

        if(!this.togglrUpdateNotifier.doesClientNeedUpdate(cacheId)){
            features = cache.get(cacheId);
        }

        if(features == null) {
            try {
                AvailableFeaturesList availableFeaturesList = this.restTemplate.postForObject(this.togglrUrl, activeFeaturesRequest, AvailableFeaturesList.class);

                if (availableFeaturesList != null) {

                    features = availableFeaturesList.getAvailableFeatures();
                    this.cache.put(cacheId, availableFeaturesList.getAvailableFeatures());
                    this.togglrUpdateNotifier.updateUserVersion(cacheId);
                }

            } catch (Exception e) {
                logger.error("Could not update Togglr Configuration");

                features = cache.get(cacheId);

                if(features != null){
                    List<GrantedAuthority> activeFeatures = new ArrayList<>();

                    for (FeatureResponse featureResponse : features) {
                        activeFeatures.add(new SimpleGrantedAuthority(featureResponse.getDescr()));
                    }

                    return activeFeatures;
                }
            }
        }

        if(features == null) {
            return new ArrayList<>();
        }else{
            List<GrantedAuthority> activeFeatures = new ArrayList<>();

            for (FeatureResponse featureResponse : features) {
                activeFeatures.add(new SimpleGrantedAuthority(featureResponse.getDescr()));
            }

            return activeFeatures;
        }
    }
}
