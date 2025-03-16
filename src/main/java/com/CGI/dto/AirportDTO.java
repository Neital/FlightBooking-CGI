package com.CGI.dto;

import lombok.Data;

@Data
public class AirportDTO {
    private Long id;
    private String code;
    private String name;
    private String city;
    private String country;
}
