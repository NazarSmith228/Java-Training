package org.java.training.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplexKey {
    String country;
    String city;

    public static ComplexKey of(String country, String city) {
        return new ComplexKey(country, city);
    }
}
