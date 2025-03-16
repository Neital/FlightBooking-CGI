package com.CGI.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Value //Probably should be changed to avoid problems with JPA
@Embeddable
@NoArgsConstructor(force = true)
public class Position implements Serializable {
    int seatRow;
    int seatColumn;
}
