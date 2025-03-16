package com.CGI.repository;

import com.CGI.model.entity.FlightSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightSeatRepo extends JpaRepository<FlightSeat, Long> {
}
