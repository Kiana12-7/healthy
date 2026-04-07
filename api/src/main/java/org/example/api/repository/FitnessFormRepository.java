package org.example.api.repository;

import org.example.api.entity.FitnessForm;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FitnessFormRepository extends CrudRepository<FitnessForm, Long>, PagingAndSortingRepository<FitnessForm, Long>, JpaSpecificationExecutor<FitnessForm> {
}
