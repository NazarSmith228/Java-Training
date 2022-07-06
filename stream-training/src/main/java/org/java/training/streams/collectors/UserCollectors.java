package org.java.training.streams.collectors;

import lombok.experimental.UtilityClass;
import org.java.training.model.ComplexKey;
import org.java.training.model.users.User;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

@UtilityClass
public class UserCollectors {

    public <T> Map<T, List<User>> groupBy(List<User> users, Function<User, T> groupingFunction) {
        return users.stream()
                .collect(groupingBy(groupingFunction));
    }

    public Map<String, Double> countAverageRatePerCountry(List<User> users) {
        return users.stream()
                .collect(groupingBy(user -> user.location().country(), averagingDouble(User::rate)));
    }

    public Map<String, Long> findAmountOfAdultUsersPerCity(List<User> users) {
        return users.stream()
                .collect(groupingBy(user -> user.location().city(),
                        filtering(user -> user.age() >= 18, counting())));
    }

    public Map<String, User> findAdultUserWithMaxRatePerCountry(List<User> users) {
        return users.stream()
                .collect(collectingAndThen(
                        groupingBy(user -> user.location().country(),
                                filtering(user -> user.age() >= 18, maxBy(comparingInt(User::rate)))
                        ),
                        opMap -> opMap.entrySet().stream()
                                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().orElseThrow()))
                ));
    }

    public Map<String, Map<String, Long>> countAmountOfUsersPerCityInEachCountry(List<User> users) {
        return users.stream()
                .collect(groupingBy(user -> user.location().country(),
                        groupingBy(user -> user.location().city(), counting()))
                );
    }


    public Map<String, List<User>> groupUsersByCountryAndCityAndSortAscendingByAge(List<User> users) {
        return users.stream()
                .collect(collectingAndThen(
                                groupingBy(user -> ComplexKey.of(user.location().country(), user.location().city())),
                                userMap -> userMap.entrySet().stream()
                                        .map(entry -> Map.entry(
                                                entry.getKey().toString(),
                                                entry.getValue().stream().sorted(comparingInt(User::age)).toList())
                                        )
                                        .collect(toMap(Map.Entry::getKey, Map.Entry::getValue))
                        )
                );
    }
}
