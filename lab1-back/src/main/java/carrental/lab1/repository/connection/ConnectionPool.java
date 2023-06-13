package carrental.lab1.repository.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

public final class ConnectionPool {
    public static ConnectionPool INSTANCE = new ConnectionPool(
            "jdbc:postgresql://localhost:5432/postgres",
            "postgres",
            "5661369",
            "org.postgresql.Driver"
    );
    public static final int MAX_CONNECTIONS = 10;

    private final ArrayList<Connection> allConnections = new ArrayList<>();
    private final LinkedBlockingQueue<Connection> unusedConnections = new LinkedBlockingQueue<>();

    private final String url;
    private final String user;
    private final String password;
    private final String driver;

    public ConnectionPool(String url, String user, String password, String driver) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.driver = driver;
    }

    public ConnectionWrapper getConnection() {
        Connection connection;
        synchronized (allConnections) {
            if (unusedConnections.size() == 0 && allConnections.size() < MAX_CONNECTIONS) {
                connection = createConnection();
                allConnections.add(connection);
            } else {
                connection = Objects.requireNonNull(unusedConnections.poll());
            }
        }
        return new ConnectionWrapper(connection, this);
    }

    public int getUnusedConnectionsCount() {
        return unusedConnections.size();
    }

    void release(ConnectionWrapper connectionWrapper) {
        unusedConnections.add(connectionWrapper.connection);
    }

    private Connection createConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
