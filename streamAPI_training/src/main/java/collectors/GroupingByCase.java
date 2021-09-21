package collectors;

import model.ComplexKey;
import model.User;
import model.factory.DataFactory;

import java.util.*;
import java.util.stream.Collectors;

public class GroupingByCase {

    public static void main(String[] args) {
        List<User> testData = DataFactory.createTestData();

        //group users by age
        Map<Integer, List<User>> usersGroupedByAge = testData.stream()
                .collect(
                        Collectors.groupingBy(
                                User::getAge
                        )
                );

        System.out.println(usersGroupedByAge);

        //group users by country and store them in a set
        LinkedHashMap<String, Set<User>> usersGroupedByCountry = testData.stream()
                .collect(
                        Collectors.groupingBy(
                                user -> user.getLocation().getCountry(),
                                LinkedHashMap::new,
                                Collectors.toSet()
                        )
                );
        System.out.println(usersGroupedByCountry);

        //count average rate per country
        Map<String, Double> averageUserRatePerCountry = testData.stream()
                .collect(
                        Collectors.groupingBy(
                                user -> user.getLocation().getCountry(),
                                Collectors.averagingDouble(User::getRate)
                        )
                );
        System.out.println(averageUserRatePerCountry);

        //count rate sum per city
        Map<String, Long> rateSumPerCity = testData.stream()
                .collect(
                        Collectors.groupingBy(
                                user -> user.getLocation().getCity(),
                                Collectors.summingLong(User::getRate)
                        )
                );
        System.out.println(rateSumPerCity);

        //find user with maximum rate
        Map<String, Optional<User>> userWithMaximumRate = testData.stream()
                .collect(
                        Collectors.groupingBy(
                                User::getName,
                                Collectors.maxBy(Comparator.comparingInt(User::getRate))
                        )
                );
        System.out.println(userWithMaximumRate);

        //group users by country and city
        Map<ComplexKey, List<User>> usersGroupedByCountryAndCity = testData.stream()
                .collect(
                        Collectors.groupingBy(
                                user -> ComplexKey.of(user.getLocation().getCountry(), user.getLocation().getCity()),
                                Collectors.toList()
                        )
                );
        System.out.println(usersGroupedByCountryAndCity);

        //group users by country and count amount of users per each city
        Map<String, Map<String, Long>> numberOfUsersPerCityGroupedByCountry = testData.stream()
                .collect(
                        Collectors.groupingBy(
                                user -> user.getLocation().getCountry(),
                                Collectors.groupingBy(
                                        user -> user.getLocation().getCity(),
                                        Collectors.counting()
                                )
                        )
                );
        System.out.println(numberOfUsersPerCityGroupedByCountry);
    }
}
