package com.CGI.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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
    @NotNull
    private Airport fromAirport;
    @ManyToOne
    @JoinColumn(name = "to_airport_id", nullable = false)
    @NotNull
    private Airport toAirport;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime startDate;
    @Column(nullable = false)
    @NotNull
    private LocalDateTime arrivalDate;

    @ManyToOne
    @JoinColumn(name = "plane_id", nullable = false)
    @NotNull
    private Plane plane;

    @OneToMany(mappedBy = "flight")
    private List<Seat> seats;  // A list of seats specific to this flight

    public Duration getEstimatedFlightTime() {
        return Duration.between(startDate, arrivalDate);
    }

    public int getMinimumPrice() {
        return plane.getMinSeatPrice();
    }

}