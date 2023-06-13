package carrental.lab1.controller;

import carrental.lab1.model.User;
import carrental.lab1.repository.UserDao;
import carrental.lab1.repository.connection.ConnectionPool;
import carrental.lab1.repository.connection.ConnectionWrapper;

import java.util.HashMap;
import java.util.Random;

public class UserController {
    public static final UserController INSTANCE = new UserController();

    private static final Random RNG = new Random();
    private final HashMap<Long, Long> userIdsToTokens = new HashMap<>();
    private final HashMap<Long, Long> tokensToUserIds = new HashMap<>();

    private UserController() {}

    public Long getUserIdFromToken(long token) {
        return tokensToUserIds.get(token);
    }

    public boolean isAdminToken(long token) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            Long userId = tokensToUserIds.get(token);
            if (userId == null)
                return false;
            User user = UserDao.INSTANCE.getUser(connection, userId);
            if (user == null)
                return false;
            return user.isAdmin();
        }
    }

    public Long registerAndLogIn(long passportId, String username, String password) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {

            return connection.doTransaction(() -> {
                User user = UserDao.INSTANCE.getUserByUsername(connection, username);

                if (user == null) {
                    UserDao.INSTANCE.insertUser(connection, passportId, username, password);
                }
                return logIn(username, password);
            });
        }
    }

    public Long logIn(String username, String password) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {

            User user = UserDao.INSTANCE.getUserByUsername(connection, username);

            if (user == null)
                return null;
            if (!password.equals(user.getPassword()))
                return null;

            synchronized (userIdsToTokens) {
                Long token = userIdsToTokens.getOrDefault(user.getPassportId(), null);

                if (token == null) {
                    token = RNG.nextLong();
                    System.err.println("Passport ID: " + user.getPassportId() + ", token: " + token);
                    userIdsToTokens.put(user.getPassportId(), token);
                    tokensToUserIds.put(token, user.getPassportId());
                }

                return token;
            }
        }
    }
}
