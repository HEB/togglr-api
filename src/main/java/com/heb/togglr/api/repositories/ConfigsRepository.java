package com.heb.togglr.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.heb.togglr.api.entities.ConfigsEntity;
import com.heb.togglr.api.entities.ConfigsEntityPK;

public interface ConfigsRepository extends CrudRepository<ConfigsEntity, ConfigsEntityPK> {
}
