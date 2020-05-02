package com.heb.togglr.api.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.heb.togglr.api.entities.KeysEntity;
import com.heb.togglr.api.entities.KeysEntityPK;

import java.util.List;

public interface KeysRepository extends CrudRepository<KeysEntity, KeysEntityPK> {

    List<KeysEntity> findByAppId(int appId, Sort sort);
}
