package com.CGI.service;

import com.CGI.model.entity.TestEntity;
import com.CGI.repository.TestEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestEntityService {

    @Autowired
    private final TestEntityRepo repository;

    public TestEntityService(TestEntityRepo repository) {
        this.repository = repository;
    }

    public List<TestEntity> getAllTestEntity() {
        return repository.findAll();
    }
/*
    @PostConstruct
    public void init() {
        TestEntity testEntity = new TestEntity();
        testEntity.setName("Test Data");
        repository.save(testEntity);
    }
 */
}