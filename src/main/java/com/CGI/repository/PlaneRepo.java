package com.CGI.repository;

import com.CGI.model.entity.Plane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaneRepo extends JpaRepository<Plane, Long> {
    Optional<Plane> findByModel(String model);
}
