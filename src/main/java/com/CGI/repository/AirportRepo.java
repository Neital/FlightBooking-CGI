package com.CGI.repository;

import com.CGI.model.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepo extends JpaRepository<Airport, Long> {
    Optional<Airport> findByCode(String code);
}
