package com.CGI.model.entity;

import com.CGI.model.enums.SeatClass;
import com.CGI.model.enums.SeatFeature;
import com.CGI.model.valueobject.Position;
import jakarta.persistence.*;
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
    private Position position;

    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<SeatFeature> features;

    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

}