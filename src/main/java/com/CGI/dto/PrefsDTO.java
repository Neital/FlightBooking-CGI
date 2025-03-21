package com.CGI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PrefsDTO {
    private String seatClass;
    private List<String> preferredFeatures;
}
