package io.francoisbotha.namazingserver.domain.dao;

import io.francoisbotha.namazingserver.domain.model.Menu;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface MenuRepository extends CrudRepository<Menu, String> {
    List<Menu> findAll();
}