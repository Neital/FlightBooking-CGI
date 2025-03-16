package com.CGI.model.entity;

import com.CGI.model.enums.SeatClass;
import com.CGI.model.enums.SeatFeature;
import com.CGI.model.valueobject.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    @NotNull
    private Position position;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private SeatClass seatClass;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Set<SeatFeature> features;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "plane_id", nullable = false)
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "flight_id")  // Add a flight reference to track seat availability per flight
    private Flight flight;

}