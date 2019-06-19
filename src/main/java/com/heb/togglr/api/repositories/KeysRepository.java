package com.heb.togglr.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.heb.togglr.api.entities.KeysEntity;
import com.heb.togglr.api.entities.KeysEntityPK;

public interface KeysRepository extends CrudRepository<KeysEntity, KeysEntityPK> {
}
