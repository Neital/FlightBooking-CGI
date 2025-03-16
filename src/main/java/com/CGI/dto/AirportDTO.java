package com.CGI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AirportDTO {
    private Long id;
    private String code;
    private String name;
    private String city;
    private String country;
}
