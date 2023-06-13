package carrental.lab1.repository;

import carrental.lab1.model.Payment;
import carrental.lab1.model.PaymentInfo;
import carrental.lab1.repository.connection.ConnectionWrapper;

import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao {
    public static final PaymentDao INSTANCE = new PaymentDao();

    private PaymentDao() {}

    private static Payment resultToPayment(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        BigDecimal hrnAmount = resultSet.getBigDecimal("hrn_amount");
        Integer requestId = resultSet.getInt("request_id");
        if (resultSet.wasNull())
            requestId = null;
        Integer carId = resultSet.getInt("car_id");
        if (resultSet.wasNull())
            carId = null;
        int type = resultSet.getInt("type");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant();

        return new Payment(id, hrnAmount, requestId, type, carId, instant);
    }

    private static PaymentInfo resultToPaymentInfo(ResultSet resultSet) throws SQLException {
        BigDecimal hrnAmount = resultSet.getBigDecimal("hrn_amount");
        String model = resultSet.getString("model");
        String manufacturer = resultSet.getString("manufacturer");
        int type = resultSet.getInt("type");
        String date = resultSet.getDate("date").toString();

        return new PaymentInfo(date, model, manufacturer, type, hrnAmount);
    }

    public Payment getPayment(ConnectionWrapper connection, int id) {
        String sql = "SELECT * FROM payments WHERE id = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultToPayment(resultSet);
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<PaymentInfo> getAllPaymentInfo(ConnectionWrapper connection, LocalDate since) {
        String sql = "SELECT type, hrn_amount, date, model, manufacturer FROM payments" +
                " INNER JOIN cars ON payments.car_id = cars.id ";
        if (since != null)
            sql += " WHERE date >= ? ";
        sql += " ORDER BY date DESC";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (since != null)
                statement.setDate(1, Date.valueOf(since));

            ArrayList<PaymentInfo> payments = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                payments.add(resultToPaymentInfo(resultSet));
            }

            return payments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<PaymentInfo> getAllPaymentInfo(ConnectionWrapper connection, int carId) {
        String sql = "SELECT type, hrn_amount, date, model, manufacturer FROM payments" +
                " INNER JOIN cars ON payments.car_id = cars.id " +
                " WHERE cars.id = ?" +
                " ORDER BY date DESC";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, carId);

            ArrayList<PaymentInfo> payments = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                payments.add(resultToPaymentInfo(resultSet));
            }

            return payments;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Payment getInitialPaymentForRequest(ConnectionWrapper connection, int requestId) {
        String sql = "SELECT * FROM payments WHERE request_id = ? AND TYPE = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, requestId);
            statement.setInt(2, Payment.TYPE_REVENUE);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                return resultToPayment(resultSet);
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Payment insertPayment(ConnectionWrapper connection, BigDecimal hrnAmount, Integer requestId, int type, Integer carId, Instant date) {
        String sql = "INSERT INTO payments" +
                " VALUES (DEFAULT, ?, ?, ?, ?, ?)" +
                " RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, hrnAmount);
            statement.setObject(2, requestId);
            statement.setInt(3, type);
            statement.setObject(4, carId);
            statement.setObject(5, LocalDateTime.ofInstant(date, ZoneId.systemDefault()));

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Payment(result.getInt("id"), hrnAmount, requestId, type, carId, date);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
