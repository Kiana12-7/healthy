package org.example.api.repository;

import org.example.api.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface WorkoutPlanRepository  extends CrudRepository<WorkoutPlan, Long>, PagingAndSortingRepository<WorkoutPlan, Long>, JpaSpecificationExecutor<WorkoutPlan> {

    // 添加自定义查询方法(V003InitWorkoutPlan用的)
    boolean existsByName(String name);

    boolean existsByNameAndFitnessForm_Id(String name, Long fitnessFormId);

    Optional<WorkoutPlan> findFirstByNameAndFitnessForm_IdOrderByIdAsc(String name, Long fitnessFormId);
}
