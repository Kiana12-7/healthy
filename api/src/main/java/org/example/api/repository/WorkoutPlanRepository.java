package org.example.api.repository;

import org.example.api.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WorkoutPlanRepository  extends CrudRepository<WorkoutPlan, Long>, PagingAndSortingRepository<WorkoutPlan, Long>, JpaSpecificationExecutor<WorkoutPlan> {
}
