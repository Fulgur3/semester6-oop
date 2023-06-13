package carrental.lab1.repository.connection;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionWrapper implements Closeable {
    private final PreparedStatement beginTransactionStatement;
    private final PreparedStatement commitTransactionStatement;
    private final PreparedStatement rollbackTransactionStatement;

    final Connection connection;
    private boolean isClosed = false;
    private final ConnectionPool pool;

    ConnectionWrapper(Connection connection, ConnectionPool pool) {
        this.connection = connection;
        this.pool = pool;
        try {
            beginTransactionStatement = connection.prepareStatement("BEGIN");
            commitTransactionStatement = connection.prepareStatement("COMMIT");
            rollbackTransactionStatement = connection.prepareStatement("ROLLBACK");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement prepareStatement(String command) {
        if (isClosed)
            throw new IllegalStateException();

        try {
            return connection.prepareStatement(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T doTransaction(Transaction<T> transaction) {
        try {
            connection.setAutoCommit(false);
            beginTransactionStatement.execute();

            T result = transaction.doTransaction();
            commitTransactionStatement.execute();
            connection.setAutoCommit(true);

            return result;
        } catch (Exception e) {
            try {
                rollbackTransactionStatement.execute();
                connection.setAutoCommit(true);
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        synchronized (pool) {
            if (!isClosed) {
                isClosed = true;
                pool.release(this);
            }
        }
    }

    public interface Transaction<T> {
        T doTransaction() throws SQLException;
    }
}
