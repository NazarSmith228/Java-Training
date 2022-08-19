package org.java.training.database.custom.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DataSourceType {

    H2("H2"), POSTGRESQL("PostgreSQL"), MYSQL("MySQL");

    private final String value;

}
