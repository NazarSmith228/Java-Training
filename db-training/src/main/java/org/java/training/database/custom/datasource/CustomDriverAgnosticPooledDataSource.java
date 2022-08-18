package org.java.training.database.custom.datasource;

import org.java.training.database.custom.connection.ConnectionProxy;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class CustomDriverAgnosticPooledDataSource implements DataSource {

    private final Queue<Connection> connectionPool;
    private final String url;
    private final String username;
    private final String password;
    private final int poolSize;

    public CustomDriverAgnosticPooledDataSource(String driverClassName, String url, String username, String password,
                                                int poolSize) {
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(
                    "Datasource cannot be initialized: unable to find driver %s on the classpath"
                            .formatted(driverClassName));
        }
        this.url = url;
        this.username = username;
        this.password = password;

        Preconditions.checkArgument(poolSize > 0, "poolSize");
        this.poolSize = poolSize;

        this.connectionPool = new ConcurrentLinkedQueue<>();
        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < poolSize; i++) {
            try {
                Connection physicalConnection = DriverManager.getConnection(url, username, password);
                ConnectionProxy logicalConnection = new ConnectionProxy(physicalConnection, connectionPool);
                connectionPool.offer(logicalConnection);
            } catch (SQLException e) {
                throw new IllegalStateException("Wasn't able to create a physical connection with database", e);
            }
        }
    }

    @Override
    public Connection getConnection() {
        return this.connectionPool.poll();
    }

    @Override
    public Connection getConnection(String username, String password) {
        // no need to use user and pass since Connection is retrieved from pool
        return this.connectionPool.poll();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void setLoginTimeout(int seconds) {
        // NOOP
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 300; // hard-coded value
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(getClass())) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap %s to %s".formatted(getClass(), iface));
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isAssignableFrom(getClass());
    }
}
