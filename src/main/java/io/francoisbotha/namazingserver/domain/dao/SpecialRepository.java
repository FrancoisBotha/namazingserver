package io.francoisbotha.namazingserver.domain.dao;

import io.francoisbotha.namazingserver.domain.model.Special;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface SpecialRepository extends CrudRepository<Special, String> {
    List<Special> findAll();
}