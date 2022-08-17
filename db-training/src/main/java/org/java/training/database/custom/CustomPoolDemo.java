package org.java.training.database.custom;

import lombok.Cleanup;
import lombok.SneakyThrows;
import org.java.training.database.custom.datasource.CustomPooledDatasource;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

public class CustomPoolDemo {

    @SneakyThrows
    public static void main(String[] args) {
        DataSource dataSource = initializeDatasource(); // takes approximately 0.2 seconds
//        DataSource dataSource = initializePSQLDatasource(); takes approximately 50 seconds

        System.out.println("Benchmark start");
        long startTime = System.nanoTime();
        for (int i = 0; i < 500; i++) {
            // just measure time taken to get connection and send simple query
            @Cleanup Connection connection = dataSource.getConnection();
            @Cleanup Statement statement = connection.createStatement();
            statement.executeQuery("""
                    SELECT COUNT(*) FROM products
                    """);
        }
        System.out.println("Time taken to get 500 connections: " +
                (System.nanoTime() - startTime) / 1_000_000 + " ms");
    }

    private static DataSource initializeDatasource() {
        return new CustomPooledDatasource(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "postgres",
                10
        );
    }

    private static DataSource initializePSQLDatasource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }
}
