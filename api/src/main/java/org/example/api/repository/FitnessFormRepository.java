package org.example.api.repository;

import org.example.api.entity.FitnessForm;
import org.example.api.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface FitnessFormRepository extends CrudRepository<FitnessForm, Long>, PagingAndSortingRepository<FitnessForm, Long>, JpaSpecificationExecutor<FitnessForm> {
    Optional<FitnessForm> findBy(Specification<FitnessForm> spec);

    Optional<FitnessForm> findByUser(User user);
}
