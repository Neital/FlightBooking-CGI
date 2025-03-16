package com.CGI.model.entity;

import com.CGI.model.enums.SeatClass;
import com.CGI.model.enums.SeatFeature;
import com.CGI.model.valueobject.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.generic.FieldGen;

import java.util.List;
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

    @ManyToOne
    @JoinColumn(name = "plane_id", nullable = false)
    private Plane plane;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightSeat> flightSeats;

}