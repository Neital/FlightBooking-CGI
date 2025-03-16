package com.CGI.service;

import com.CGI.model.entity.Seat;
import com.CGI.model.valueobject.Position;
import com.CGI.repository.SeatRepo;
import org.springframework.stereotype.Service;

@Service
public class SeatService {

    private final SeatRepo seatRepo;

    public SeatService(SeatRepo seatRepo) {
        this.seatRepo = seatRepo;
    }

    public void bookSeatOnFlight(Long flightId, Position position) {
        Seat seat = seatRepo.findByFlightIdAndPosition(flightId, position)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found for this flight"));

        if (!seat.isAvailable()) {
            throw new IllegalArgumentException("Seat is already booked");
        }

        seat.setAvailable(false); // Mark the seat as booked
        seatRepo.save(seat); // Save the updated seat to the database
    }
}
