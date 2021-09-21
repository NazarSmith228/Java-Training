package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int age;
    private int rate;
    private String name;
    private Location location;
}
