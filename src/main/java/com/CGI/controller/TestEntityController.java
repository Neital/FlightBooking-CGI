package com.CGI.controller;

import com.CGI.model.entity.TestEntity;
import com.CGI.service.TestEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestEntityController {

    @Autowired
    private TestEntityService testEntityService;

    @GetMapping
    public List<TestEntity> getAllTestEntity() {
        return testEntityService.getAllTestEntity();
    }
}
