package com.CGI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.CGI.model.entity.TestEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepo extends JpaRepository<TestEntity, Long> {
}






