package org.example.api.repository;

import org.example.api.entity.Video;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VideoRepository  extends CrudRepository<Video, Long>, PagingAndSortingRepository<Video, Long>, JpaSpecificationExecutor<Video> {
}
