package com.heb.togglr.api.repositories;

import com.heb.togglr.api.entities.AdminsEntity;
import com.heb.togglr.api.entities.AdminsEntityPK;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<AdminsEntity, AdminsEntityPK> {
}
