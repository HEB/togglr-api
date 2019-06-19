package com.heb.togglr.api.repositories;

import com.heb.togglr.api.entities.AppEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationsRepository extends CrudRepository<AppEntity, Integer> {

    public List<AppEntity> findAllByAdminsById_Id(String id);
}
