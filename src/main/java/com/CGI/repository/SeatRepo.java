package com.CGI.repository;

import com.CGI.model.entity.Seat;
import com.CGI.model.valueobject.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepo extends JpaRepository<Seat, Long> {
    Optional<Seat> findByFlightIdAndPosition(Long flightId, Position position);
}
