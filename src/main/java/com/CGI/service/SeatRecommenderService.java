package com.CGI.service;

import com.CGI.dto.FlightSeatDTO;
import com.CGI.dto.PrefsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatRecommenderService {

    // Written by ChatGPT
    public List<List<FlightSeatDTO>> recommendSeats(List<FlightSeatDTO> seats, List<PrefsDTO> prefs) {
        // 1. Filter available seats
        List<FlightSeatDTO> availableSeats = seats.stream()
                .filter(FlightSeatDTO::isAvailable).toList();

        if (prefs.isEmpty()) {
            // Return an empty array
            return new ArrayList<>();
        }

        // 2. Track assigned seats to avoid duplication
        List<FlightSeatDTO> assignedSeats = new ArrayList<>();

        // 3. Sort the seats based on proximity if there are multiple tickets
        List<List<FlightSeatDTO>> suitableSeats = prefs.stream()
                .map(pref -> {
                    // Filter matching seats that are not assigned yet
                    // Ensure seat isn't assigned
                    List<FlightSeatDTO> matchingSeats = availableSeats.stream()
                            .filter(seat -> seat.getSeat().getSeatClass().equals(pref.getSeatClass()) &&
                                    hasCompatibleFeatures(seat.getSeat().getFeatures(), pref.getPreferredFeatures()) &&
                                    !assignedSeats.contains(seat)).sorted((seat1, seat2) -> {
                                double dist1 = calculateDistance(assignedSeats, seat1);
                                double dist2 = calculateDistance(assignedSeats, seat2);
                                return Double.compare(dist1, dist2);
                            }).collect(Collectors.toList());

                    // Sort seats by proximity to the already assigned seats

                    // Assign the first matching seat to the preference and track it
                    if (!matchingSeats.isEmpty()) {
                        assignedSeats.add(matchingSeats.getFirst());  // Mark this seat as assigned
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

    // Calculate the Euclidean distance between a seat and all already assigned seats
    private double calculateDistance(List<FlightSeatDTO> assignedSeats, FlightSeatDTO seat) {
        if (assignedSeats.isEmpty()) {
            return 0;  // If no assigned seats, distance is zero (i.e., no preference yet)
        }
        return assignedSeats.stream()
                .mapToDouble(assignedSeat -> {
                    int rowDiff = assignedSeat.getSeat().getSeatRow() - seat.getSeat().getSeatRow();
                    int colDiff = assignedSeat.getSeat().getSeatColumn() - seat.getSeat().getSeatColumn();
                    return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff); // Euclidean distance
                })
                .min() // Take the minimum distance to the closest assigned seat
                .orElse(Double.MAX_VALUE); // If no assigned seats, return a large number
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
        return matchingSeats.isEmpty() ? List.of() : List.of(matchingSeats.getFirst());
    }
}
