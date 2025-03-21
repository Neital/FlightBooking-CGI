package com.CGI.service;

import com.CGI.dto.FlightSeatDTO;
import com.CGI.dto.PrefsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatRecommenderService {

    public List<List<FlightSeatDTO>> recommendSeats(List<FlightSeatDTO> seats, List<PrefsDTO> prefs) {
        // 1. Filter available seats
        List<FlightSeatDTO> availableSeats = new ArrayList<>(seats.stream()
                .filter(FlightSeatDTO::isAvailable)
                .collect(Collectors.toList()));

        if (prefs.isEmpty()) {
            // Return all available seats for all passengers
            return List.of(availableSeats);
        }

        // 2. Track assigned seats to avoid duplication
        List<FlightSeatDTO> assignedSeats = new ArrayList<>();

        // 3. Map each passenger preference to matching seats
        List<List<FlightSeatDTO>> suitableSeats = prefs.stream()
                .map(pref -> {
                    // Filter matching seats that are not assigned yet
                    List<FlightSeatDTO> matchingSeats = availableSeats.stream()
                            .filter(seat -> seat.getSeat().getSeatClass().equals(pref.getSeatClass()) &&
                                    hasCompatibleFeatures(seat.getSeat().getFeatures(), pref.getPreferredFeatures()) &&
                                    !assignedSeats.contains(seat))  // Ensure seat isn't assigned
                            .collect(Collectors.toList());

                    // Assign the first matching seat to the preference and track it
                    if (!matchingSeats.isEmpty()) {
                        assignedSeats.add(matchingSeats.get(0));  // Mark this seat as assigned
                    }

                    return matchingSeats;
                })
                .collect(Collectors.toList());

        // 4. Return the recommended seats
        return assignSeats(suitableSeats);
    }

    // Check if the seat has compatible features
    private boolean hasCompatibleFeatures(List<String> seatFeatures, List<String> prefFeatures) {
        if (prefFeatures.isEmpty()) {
            return true;
        }
        for (String pref : prefFeatures) {
            if (seatFeatures.contains(pref)) return true;
        }
        return false;
    }

    // Assign the best available seats based on distance and preferences
    private List<List<FlightSeatDTO>> assignSeats(List<List<FlightSeatDTO>> suitableSeats) {
        return suitableSeats.stream()
                .map(this::findBestSeats)
                .collect(Collectors.toList());
    }

    // Find the best available seats from a list of matching seats
    private List<FlightSeatDTO> findBestSeats(List<FlightSeatDTO> matchingSeats) {
        // For simplicity, return the first available seat
        return matchingSeats.isEmpty() ? List.of() : List.of(matchingSeats.get(0));
    }
}
