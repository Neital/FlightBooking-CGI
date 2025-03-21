package com.CGI.model.entity;

import com.CGI.model.enums.SeatClass;
import com.CGI.model.valueobject.Dimensions;
import com.CGI.model.valueobject.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@Table(name = "planes")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String model; // Unique identifier

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @MapKey(name = "position")
    @NotNull
    private List<Seat> seats = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "seat_prices", joinColumns = @JoinColumn(name = "plane_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "price", nullable = false)
    private Map<SeatClass, Integer> seatPrices;

    @Embedded
    @NotNull
    private Dimensions dimensions;

    public int getMinSeatPrice() {
        return seatPrices.values().stream().min(Integer::compare).orElse(0);
    }
}
