package com.heb.togglr.api.repositories;

import org.springframework.data.repository.CrudRepository;

import com.heb.togglr.api.entities.SuperAdminsEntity;

public interface SuperAdminRepository extends CrudRepository<SuperAdminsEntity, String> {
}
