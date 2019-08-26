package com.heb.togglr.api.client.model.response;

import java.util.ArrayList;
import java.util.List;

public class AvailableFeaturesList {

    private List<FeatureResponse> availableFeatures;

    public AvailableFeaturesList(){
        this.availableFeatures = new ArrayList<>();
    }

    public List<FeatureResponse> getAvailableFeatures() {
        return availableFeatures;
    }

    public void setAvailableFeatures(List<FeatureResponse> availableFeatures) {
        this.availableFeatures = availableFeatures;
    }

    @Override
    public String toString() {
        return "AvailableFeaturesList{" +
                "availableFeatures=" + availableFeatures.toString() +
                '}';
    }
}
