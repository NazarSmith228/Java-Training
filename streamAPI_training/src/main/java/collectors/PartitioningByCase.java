package collectors;

import model.User;
import model.factory.DataFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PartitioningByCase {

    public static void main(String[] args) {
        List<User> testData = DataFactory.createTestData();

        //find out which users are from ukraine
        Map<Boolean, List<User>> partitionedList = testData.stream()
                .collect(
                        Collectors.partitioningBy(
                                user -> user.getLocation().getCountry().equals("Ukraine")
                        )
                );

        List<User> ukrainianUsers = partitionedList.get(true);
        System.out.println(ukrainianUsers);

        //group users per city and filter by age
        Map<String, Map<Boolean, List<User>>> groupedByCityAndPartitionedByAge = testData.stream()
                .collect(
                        Collectors.groupingBy(
                                user -> user.getLocation().getCity(),
                                Collectors.partitioningBy(
                                        user -> user.getAge() > 20
                                )
                        )
                );

        //get all users from a particular city older than 20 years
        Map<String, List<User>> usersPerCityOlderThan = groupedByCityAndPartitionedByAge.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().get(true)
                        )
                );

        System.out.println(usersPerCityOlderThan);
    }
}
