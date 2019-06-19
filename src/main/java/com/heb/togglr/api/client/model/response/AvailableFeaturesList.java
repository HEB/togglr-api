package com.heb.togglr.api.client.model.response;

import java.util.List;

public class AvailableFeaturesList {

    private List<FeatureResponse> availableFeatures;

    public List<FeatureResponse> getAvailableFeatures() {
        return availableFeatures;
    }

    public void setAvailableFeatures(List<FeatureResponse> availableFeatures) {
        this.availableFeatures = availableFeatures;
    }
}
