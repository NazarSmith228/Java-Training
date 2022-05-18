package org.java.training.streams.impl.collectors;

import lombok.experimental.UtilityClass;
import org.java.training.model.ComplexKey;
import org.java.training.model.User;
import org.java.training.model.util.Users;

import java.util.*;

import static java.util.stream.Collectors.*;

@UtilityClass
public class CollectorsUtils {

    public void grouping() {
        List<User> testData = Users.createUsers();

        //group users by age
        Map<Integer, List<User>> usersGroupedByAge = testData.stream()
                .collect(groupingBy(User::age));

        System.out.println("Users grouped by age:");
        System.out.println(usersGroupedByAge);

        //group users by country and store them in a set
        LinkedHashMap<String, Set<User>> usersGroupedByCountry = testData.stream()
                .collect(
                        groupingBy(user -> user.location().country(), LinkedHashMap::new, toSet())
                );

        System.out.println("Users grouped by country:");
        System.out.println(usersGroupedByCountry);

        //count average rate per country
        Map<String, Double> averageUserRatePerCountry = testData.stream()
                .collect(
                        groupingBy(user -> user.location().country(), averagingDouble(User::rate))
                );

        System.out.println("Average user rate per country:");
        System.out.println(averageUserRatePerCountry);

        //count rate sum per city
        Map<String, Long> rateSumPerCity = testData.stream()
                .collect(
                        groupingBy(user -> user.location().city(), summingLong(User::rate))
                );

        System.out.println("User rate sum per city:");
        System.out.println(rateSumPerCity);

        //find user with maximum rate
        Map<String, Optional<User>> usersWithMaximumRate = testData.stream()
                .collect(
                        groupingBy(User::name, maxBy(Comparator.comparingInt(User::rate)))
                );

        System.out.println("Users with max rate:");
        System.out.println(usersWithMaximumRate);

        //group users by country and city
        Map<ComplexKey, List<User>> usersGroupedByCountryAndCity = testData.stream()
                .collect(
                        groupingBy(
                                user -> ComplexKey.of(user.location().country(), user.location().city()),
                                toList()
                        )
                );

        System.out.println("Users grouped by county and city:");
        System.out.println(usersGroupedByCountryAndCity);

        //group users by country and count amount of users per each city
        Map<String, Map<String, Long>> numberOfUsersPerCityGroupedByCountry = testData.stream()
                .collect(
                        groupingBy(user -> user.location().country(),
                                groupingBy(user -> user.location().city(), counting())
                        )
                );

        System.out.println("Users grouped by county, counted amount of users per city:");
        System.out.println(numberOfUsersPerCityGroupedByCountry);
    }

    public void partitioning() {
        List<User> testData = Users.createUsers();

        //find out which users are from Ukraine
        Map<Boolean, List<User>> partitionedList = testData.stream()
                .collect(partitioningBy(user -> user.location().country().equals("Ukraine")));

        List<User> ukrainianUsers = partitionedList.get(true);

        System.out.println("Users from Ukraine:");
        System.out.println(ukrainianUsers);

        //group users per city and filter by age
        Map<String, Map<Boolean, List<User>>> groupedByCityAndPartitionedByAge = testData.stream()
                .collect(
                        groupingBy(user -> user.location().city(), partitioningBy(user -> user.age() > 20))
                );

        //get all users from a particular city older than 20 years
        Map<String, List<User>> usersPerCityOlderThan = groupedByCityAndPartitionedByAge.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().get(true)));

        System.out.println("Users older than 20, grouped per city:");
        System.out.println(usersPerCityOlderThan);
    }
}
