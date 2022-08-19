package org.java.training.database.custom;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.java.training.database.custom.config.DataSourceType;
import org.java.training.database.custom.datasource.util.DataSources;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomPoolDemo {

    public static void main(String[] args) {
        benchmark(DataSourceType.POSTGRESQL);
        benchmark(DataSourceType.H2);
    }

    @SneakyThrows
    private static void benchmark(DataSourceType dataSourceType) {
        DataSource dataSource = initializeDatasource(dataSourceType);

        System.out.println("Benchmark started for: " + dataSourceType.getValue());
        long startTime = System.nanoTime();
        long randomSum = 0;
        for (int i = 0; i < 500; i++) {
            @Cleanup Connection connection = dataSource.getConnection();
            @Cleanup Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT floor(random() * (10 + 1))");
            resultSet.next();
            randomSum += resultSet.getLong(1);
        }

        System.out.println("Time taken to process 500 requests: " +
                (System.nanoTime() - startTime) / 1_000_000 + " ms");
        System.out.println("Result random sum: " + randomSum);
    }

    private static DataSource initializeDatasource(DataSourceType dataSourceType) {
        return switch (dataSourceType) {
            case H2 -> DataSources.h2DataSource();
            case POSTGRESQL -> DataSources.postgresDataSource();
            default -> throw new UnsupportedOperationException();
        };
    }
}
