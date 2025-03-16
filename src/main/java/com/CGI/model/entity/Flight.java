package com.CGI.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_airport_id", nullable = false)
    private Airport fromAirport;
    @ManyToOne
    @JoinColumn(name = "to_airport_id", nullable = false)
    private Airport toAirport;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime arrivalDate;

    @ManyToOne
    @JoinColumn(name = "plane_id", nullable = false)
    private Plane plane;

    public Duration getEstimatedFlightTime() {
        return Duration.between(startDate, arrivalDate);
    }

    public int getMinimumPrice() {
        return plane.getMinSeatPrice();
    }

}