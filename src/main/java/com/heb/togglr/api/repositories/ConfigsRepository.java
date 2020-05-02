package com.heb.togglr.api.repositories;

import com.heb.togglr.api.entities.ConfigsEntity;
import com.heb.togglr.api.entities.ConfigsEntityPK;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConfigsRepository extends CrudRepository<ConfigsEntity, ConfigsEntityPK> {

    List<ConfigsEntity> findByAppIdAndFeatureId(int appId, int featureId);

}
