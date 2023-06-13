package carrental.lab1.repository;

import carrental.lab1.model.RentRequest;
import carrental.lab1.model.RequestInfo;
import carrental.lab1.repository.connection.ConnectionWrapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentRequestDao {
    public static final RentRequestDao INSTANCE = new RentRequestDao();

    private RentRequestDao() {}

    private static RentRequest resultToRentRequest(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int status = resultSet.getInt("status");
        String statusMessage = resultSet.getString("message");
        long userId = resultSet.getLong("user_id");
        int carId = resultSet.getInt("car_id");
        int days = resultSet.getInt("days");
        LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
        BigDecimal paymentCost = resultSet.getBigDecimal("payment_cost");

        return new RentRequest(id, status, statusMessage, userId, days, carId, startDate, paymentCost);
    }

    private static RequestInfo resultToRequestInfo(ResultSet resultSet) throws SQLException {
        int requestId = resultSet.getInt(1);
        int status = resultSet.getInt("status");
        long passportId = resultSet.getLong("user_id");
        int days = resultSet.getInt("days");
        int carId = resultSet.getInt("car_id");
        String startDate = resultSet.getDate("start_date").toLocalDate().toString();
        String carManufacturer = resultSet.getString("manufacturer");
        String carModel = resultSet.getString("model");
        BigDecimal hrnPerDay = resultSet.getBigDecimal("hrn_per_day");
        String message = resultSet.getString("message");
        BigDecimal repairCost = resultSet.getBigDecimal("payment_cost");

        return new RequestInfo(requestId, status, message, passportId, days, carId, startDate, carManufacturer, carModel, hrnPerDay, repairCost);
    }

    public RentRequest getRequest(ConnectionWrapper connection, int id) {
        String sql = "SELECT * FROM requests WHERE id = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return resultToRentRequest(resultSet);
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update(ConnectionWrapper connection, RentRequest request) {
        String sql = "UPDATE requests" +
                " SET status = ?," +
                "     message = ?," +
                "     user_id = ?," +
                "     car_id = ?," +
                "     days = ?," +
                "     start_date = ?," +
                "     payment_cost = ?" +
                " WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, request.getStatus());
            statement.setString(2, request.getStatusMessage());
            statement.setLong(3, request.getUserId());
            statement.setInt(4, request.getCarId());
            statement.setInt(5, request.getDays());
            statement.setDate(6, Date.valueOf(request.getStartDate()));
            statement.setBigDecimal(7, request.getRepairCost());
            statement.setInt(8, request.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteALlPendingForCarId(ConnectionWrapper connection, int carId) {
        String sql = "DELETE FROM requests" +
                " WHERE car_id = ? AND STATUS = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, carId);
            statement.setInt(2, RentRequest.STATUS_PENDING);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public RentRequest insert(ConnectionWrapper connection, int status, String message, long userId,
                              int carId, int days, LocalDate startDate, BigDecimal paymentCost) {
        String sql = "INSERT INTO requests" +
                " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)" +
                " RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, status);
            statement.setString(2, message);
            statement.setLong(3, userId);
            statement.setInt(4, carId);
            statement.setInt(5, days);
            statement.setDate(6, Date.valueOf(startDate));
            statement.setBigDecimal(7, paymentCost);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                return new RentRequest(
                        id,
                        status,
                        message,
                        userId,
                        days,
                        carId,
                        startDate,
                        paymentCost
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<RequestInfo> getAllRequestsWithStatus(ConnectionWrapper connection, int status) {
        String sql = "SELECT * FROM requests INNER JOIN cars c on c.id = requests.car_id WHERE status = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, status);

            ArrayList<RequestInfo> requests = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(resultToRequestInfo(resultSet));
            }

            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<RequestInfo> getUserRequestsWithStatus(ConnectionWrapper connection, int status, long userId) {
        String sql = "SELECT * FROM requests INNER JOIN cars c on c.id = requests.car_id" +
                " WHERE status = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, status);
            statement.setLong(2, userId);

            ArrayList<RequestInfo> requests = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(resultToRequestInfo(resultSet));
            }

            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public List<RequestInfo> getOutdatedActiveRequests(ConnectionWrapper connection) {
        String sql = "SELECT * FROM requests INNER JOIN cars c on c.id = requests.car_id" +
                " WHERE status = ? AND ? > (start_date + days)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, RentRequest.STATUS_ACTIVE);
            statement.setDate(2, Date.valueOf(LocalDate.now()));

            ArrayList<RequestInfo> requests = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(resultToRequestInfo(resultSet));
            }

            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<RequestInfo> getOutdatedActiveRequests(ConnectionWrapper connection, long userId) {
        String sql = "SELECT * FROM requests INNER JOIN cars c on c.id = requests.car_id" +
                " WHERE status = ? AND ? > (start_date + days) AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, RentRequest.STATUS_ACTIVE);
            statement.setDate(2, Date.valueOf(LocalDate.now()));
            statement.setLong(3, userId);

            ArrayList<RequestInfo> requests = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requests.add(resultToRequestInfo(resultSet));
            }

            return requests;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
