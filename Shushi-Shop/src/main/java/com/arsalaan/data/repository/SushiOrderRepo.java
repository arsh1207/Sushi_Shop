package com.arsalaan.data.repository;

import com.arsalaan.data.entity.SushiOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SushiOrderRepo extends CrudRepository<SushiOrder, Integer> {//PagingAndSortingRepository<SushiOrder, Integer> {
    SushiOrder findById(int sushi_id);
}
