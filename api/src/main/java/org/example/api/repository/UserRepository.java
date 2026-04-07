package org.example.api.repository;

import org.example.api.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository  extends CrudRepository<User, Long>, PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
}
