package org.java.training.streams.collectors;

import org.java.training.model.User;
import org.java.training.model.factory.DataFactory;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

public class PartitioningByCase {

    public static void main(String[] args) {
        List<User> testData = DataFactory.createTestData();

        //find out which users are from Ukraine
        Map<Boolean, List<User>> partitionedList = testData.stream()
                .collect(partitioningBy(user -> user.getLocation().getCountry().equals("Ukraine")));

        List<User> ukrainianUsers = partitionedList.get(true);
        System.out.println(ukrainianUsers);

        //group users per city and filter by age
        Map<String, Map<Boolean, List<User>>> groupedByCityAndPartitionedByAge = testData.stream()
                .collect(
                        groupingBy(user -> user.getLocation().getCity(), partitioningBy(user -> user.getAge() > 20))
                );

        //get all users from a particular city older than 20 years
        Map<String, List<User>> usersPerCityOlderThan = groupedByCityAndPartitionedByAge.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().get(true)));

        System.out.println(usersPerCityOlderThan);
    }
}
