package com.CGI.dto;

import com.CGI.model.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class PlaneDTO {
    private Long id;
    private String model;
    private List<SeatDTO> seats;
    private Map<SeatClass, Integer> seatPrices;

}
