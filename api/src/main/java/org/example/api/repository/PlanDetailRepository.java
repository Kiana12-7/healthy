package org.example.api.repository;

import org.example.api.entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PlanDetailRepository extends CrudRepository<PlanDetail, Long>, PagingAndSortingRepository<PlanDetail, Long>, JpaSpecificationExecutor<PlanDetail> {
    long countByWorkoutPlan_Id(Long workoutPlanId);

    void deleteByWorkoutPlan_Id(Long workoutPlanId);

    List<PlanDetail> findAllByWorkoutPlan_IdOrderByDayNumberAscOrderInDayAsc(Long workoutPlanId);

    List<PlanDetail> findAllByWorkoutPlan_IdAndDayNumberOrderByOrderInDayAsc(Long workoutPlanId, Integer dayNumber);
}
