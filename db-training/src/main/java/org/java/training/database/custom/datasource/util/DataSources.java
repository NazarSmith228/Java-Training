package org.java.training.database.custom.datasource.util;

import lombok.experimental.UtilityClass;
import org.java.training.database.custom.datasource.CustomDriverAgnosticPooledDataSource;
import org.java.training.database.custom.datasource.PGSimplePooledDataSource;

@UtilityClass
public class DataSources {

    public CustomDriverAgnosticPooledDataSource h2DataSource() {
        return new CustomDriverAgnosticPooledDataSource(
                "org.h2.Driver",
                "jdbc:h2:mem:local;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;DB_CLOSE_ON_EXIT=FALSE",
                "sa",
                "",
                10
        );
    }

    public CustomDriverAgnosticPooledDataSource postgresDataSource() {
        return new CustomDriverAgnosticPooledDataSource(
                "org.postgresql.Driver",
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "postgres",
                10
        );
    }

    public PGSimplePooledDataSource postgresPooledDataSource() {
        return new PGSimplePooledDataSource(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "postgres",
                15
        );
    }
}
