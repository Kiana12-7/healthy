package org.example.api.repository;

import org.example.api.entity.User;
import org.example.api.entity.WorkoutDurationStat;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkoutDurationStatRepository extends CrudRepository<WorkoutDurationStat, Long>, PagingAndSortingRepository<WorkoutDurationStat, Long>, JpaSpecificationExecutor<WorkoutDurationStat> {
    Optional<WorkoutDurationStat> findByUserAndRecordDate(User user, LocalDate recordDate);

    List<WorkoutDurationStat> findAllByUserAndRecordDateBetweenOrderByRecordDateAsc(User user, LocalDate startDate, LocalDate endDate);

    long countByUserAndTotalDurationSecondsGreaterThan(User user, Integer totalDurationSeconds);
}
