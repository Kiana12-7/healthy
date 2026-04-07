package org.example.api.repository;

import org.example.api.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PlanDetailRepository extends CrudRepository<PlanDetail, Long>, PagingAndSortingRepository<PlanDetail, Long>, JpaSpecificationExecutor<PlanDetail> {
}
