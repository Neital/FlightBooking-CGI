package com.CGI.request;

import com.CGI.dto.FlightSeatDTO;
import com.CGI.dto.PrefsDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SeatRecommendationRequest {
    private List<FlightSeatDTO> seats;
    private List<PrefsDTO> prefs;

}
