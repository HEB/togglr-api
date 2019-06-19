package com.heb.togglr.api.client.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TogglrUpdateNotifier {

    @Value("${heb.togglr.cache-time:#{0}}")
    private long cacheTime;

    private long updateCount = 0;
    private Map<String, Long> lastUpdateReceivedd;
    private Map<String, Long> lastUpdateTime;

    public TogglrUpdateNotifier(){
        this.lastUpdateReceivedd = new HashMap<>();
        this.lastUpdateTime = new HashMap<>();
    }

    public void registerNewUpdate(){
        this.updateCount++;
        this.clearCache();
    }

    /**
     * Returns True if the user needs to have their togglr items refreshed.
     * @param cacheId
     * @return
     */
    public boolean doesClientNeedUpdate(String cacheId){
        Long lastUpdate = this.lastUpdateReceivedd.get(cacheId);
        Long lastUpdateTime = this.lastUpdateTime.get(cacheId);


        if(lastUpdate == null || lastUpdate < updateCount || Instant.now().toEpochMilli() - lastUpdateTime > cacheTime){
            return true;
        }

        return false;
    }

    /**
     * Stores the current update version for the user.
     * @param cacheId
     */
    public void updateUserVersion(String cacheId){
        this.lastUpdateReceivedd.put(cacheId, this.updateCount);
        this.lastUpdateTime.put(cacheId, Instant.now().toEpochMilli());
    }

    /**
     * Reset the maps used for caching.
     */
    public void clearCache(){
        this.lastUpdateReceivedd.clear();
        this.lastUpdateTime.clear();
    }
}