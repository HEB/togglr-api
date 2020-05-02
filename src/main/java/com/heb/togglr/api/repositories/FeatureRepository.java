package com.heb.togglr.api.repositories;

import com.heb.togglr.api.entities.FeatureEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeatureRepository extends CrudRepository<FeatureEntity, Integer> {

    List<FeatureEntity> findByAppIdAndActiveIsTrue(int appId);

    List<FeatureEntity> findByAppId(int appId, Sort sort);

}
