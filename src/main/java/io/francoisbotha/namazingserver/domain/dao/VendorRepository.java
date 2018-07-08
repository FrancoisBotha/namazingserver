package io.francoisbotha.namazingserver.domain.dao;

import io.francoisbotha.namazingserver.domain.model.Vendor;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

import static com.amazonaws.services.alexaforbusiness.model.SortValue.ASC;

@EnableScan
public interface VendorRepository extends PagingAndSortingRepository<Vendor, String> {
    public List<Vendor> findAllByNum(Integer num);
    public List<Vendor> findAllByOrderByNum();

}