package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private String country;
    private String city;
    private String street;
}
