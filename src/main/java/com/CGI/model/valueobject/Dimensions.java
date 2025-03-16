package com.CGI.model.valueobject;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Value //Probably should be changed to avoid problems with JPA
@Embeddable
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Dimensions implements Serializable {
    @NotNull int rowCount;
    @NotNull int colCount;
}
