package org.java.training.model.users.util;

import lombok.experimental.UtilityClass;
import org.java.training.model.users.Location;
import org.java.training.model.users.User;

import java.util.List;

@UtilityClass
public class Users {

    public List<User> createUsers() {
        Location l1 = createLocation("Ukraine", "Lviv", "Str. 1");
        Location l2 = createLocation("Ukraine", "Kyiv", "Str. 3");
        Location l3 = createLocation("Poland", "Warsaw", "Str. 67");
        Location l4 = createLocation("Ukraine", "Lviv", "Str. 77");
        Location l5 = createLocation("Ukraine", "Lviv", "Str. 8");
        Location l6 = createLocation("Poland", "Liublin", "Str. 5");

        User u1 = createUser(20, 177, "Nazar", l1);
        User u2 = createUser(23, 59, "Maks", l4);
        User u3 = createUser(18, 290, "Ivan", l3);
        User u4 = createUser(25, 232, "Svyatoslav", l5);
        User u5 = createUser(20, 78, "Oleksii", l2);
        User u6 = createUser(23, 115, "Andrii", l2);
        User u7 = createUser(29, 348, "Yura", l6);
        User u8 = createUser(24, 97, "Fedor", l3);
        User u9 = createUser(19, 156, "Fedor", l1);

        return List.of(u1, u2, u3, u4, u5, u6, u7, u8, u9);
    }

    private Location createLocation(String country, String city, String address) {
        return Location.builder()
                .country(country)
                .city(city)
                .street(address)
                .build();
    }

    private User createUser(int age, int rate, String name, Location location) {
        return User.builder()
                .age(age)
                .rate(rate)
                .name(name)
                .location(location)
                .build();
    }
}
