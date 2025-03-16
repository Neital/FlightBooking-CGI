package com.CGI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SeatDTO {
    private Long id;
    private int seatRow;
    private int seatColumn;
    private String seatClass;
    private List<String> features;
}
