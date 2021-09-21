package model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class ComplexKey {
    private String country;
    private String city;

    public static ComplexKey of(String country, String city) {
        return new ComplexKey(country, city);
    }
}
