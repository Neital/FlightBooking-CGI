package com.CGI.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FlightDTO {
    private Long id;
    private String fromAirportCode;
    private String toAirportCode;
    private String fromAirportName;
    private String toAirportName;
    private String fromAirportCity;
    private String toAirportCity;
    private String fromAirportCountry;
    private String toAirportCountry;
    private LocalDateTime startDate;
    private LocalDateTime arrivalDate;
    private PlaneDTO plane;
    private List<SeatDTO> seats;
}
