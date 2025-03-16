package com.CGI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.CGI.model.entity.TestEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TestEntityRepo extends JpaRepository<TestEntity, Long> {
}






