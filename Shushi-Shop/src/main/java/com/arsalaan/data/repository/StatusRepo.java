package com.arsalaan.data.repository;

import com.arsalaan.data.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepo extends CrudRepository<Status, String> {
    Status findByName(String name);
}
