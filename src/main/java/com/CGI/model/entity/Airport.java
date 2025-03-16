package com.CGI.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "airports")
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;  // e.g., "JFK", "LHR"
    private String name;  // e.g., "John F. Kennedy International Airport"
    private String city;  // e.g., "New York"
    private String country;  // e.g., "USA"
}