package com.CGI.model.entity;

import com.CGI.model.enums.SeatClass;
import com.CGI.model.valueobject.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@Table(name = "planes")
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "plane", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @MapKey(name = "position") // Maps Position to Seat
    @NotNull
    private Map<Position, Seat> seats;

    @ElementCollection
    @CollectionTable(name = "seat_prices", joinColumns = @JoinColumn(name = "plane_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "price", nullable = false)
    private Map<SeatClass, Integer> seatPrices;

    public int getMinSeatPrice() {
        return seatPrices.values().stream().min(Integer::compare).orElse(0);
    }
}
