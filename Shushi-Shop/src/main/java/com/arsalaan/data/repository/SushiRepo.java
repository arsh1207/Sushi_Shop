package com.arsalaan.data.repository;

import com.arsalaan.data.entity.Sushi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SushiRepo extends CrudRepository<Sushi, String> {
    Sushi findByName(String sushi_name);
}
