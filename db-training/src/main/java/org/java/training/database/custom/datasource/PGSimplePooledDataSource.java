package org.java.training.database.custom.datasource;

import lombok.SneakyThrows;
import org.java.training.database.custom.connection.ConnectionProxy;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;

import java.sql.Connection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PGSimplePooledDataSource extends PGSimpleDataSource {

    private final Queue<Connection> connectionPool;

    private final int poolSize;

    public PGSimplePooledDataSource(String url, String user, String password, int poolSize) {
        setUrl(url);
        setUser(user);
        setPassword(password);

        Preconditions.checkArgument(poolSize > 0, "poolSize");
        this.poolSize = poolSize;

        this.connectionPool = new ConcurrentLinkedQueue<>();
        initializePool();
    }

    @SneakyThrows
    private void initializePool() {
        for (int i = 0; i < this.poolSize; i++) {
            Connection physicalConnection = super.getConnection();
            ConnectionProxy logicalConnection = new ConnectionProxy(physicalConnection, this.connectionPool);
            this.connectionPool.offer(logicalConnection);
        }
    }

    @Override
    public Connection getConnection() {
        return this.connectionPool.poll();
    }
}
