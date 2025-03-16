package com.CGI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightSeatDTO {
    private Long id;
    private SeatDTO seat;
    private Long flightId;
    private boolean isAvailable;
}
