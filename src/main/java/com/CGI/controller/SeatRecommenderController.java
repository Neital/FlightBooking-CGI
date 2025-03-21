package com.CGI.controller;

import com.CGI.dto.FlightSeatDTO;
import com.CGI.service.SeatRecommenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.CGI.request.SeatRecommendationRequest;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatRecommenderController {

    @Autowired
    private SeatRecommenderService seatRecommenderService;

    @PostMapping("/recommend")
    public List<List<FlightSeatDTO>> recommendSeats(@RequestBody SeatRecommendationRequest request) {
        return seatRecommenderService.recommendSeats(request.getSeats(), request.getPrefs());
    }

}
