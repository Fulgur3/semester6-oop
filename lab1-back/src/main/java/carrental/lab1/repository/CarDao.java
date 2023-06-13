package carrental.lab1.repository;

import carrental.lab1.model.Car;
import carrental.lab1.model.CarStatistic;
import carrental.lab1.repository.connection.ConnectionWrapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    public static final CarDao INSTANCE = new CarDao();

    private CarDao() {}

    private static Car resultToCar(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String model = resultSet.getString("model");
        String manufacturer = resultSet.getString("manufacturer");
        BigDecimal hrnPerDay = resultSet.getBigDecimal("hrn_per_day");
        Long currentUserId = resultSet.getLong("current_user_id");
        if (resultSet.wasNull())
            currentUserId = null;
        String thumbnailUrl = resultSet.getString("thumbnail_url");
        String description = resultSet.getString("description");
        BigDecimal hrnPurchase = resultSet.getBigDecimal("hrn_purchase");

        return new Car(id, model, manufacturer, hrnPerDay, currentUserId, thumbnailUrl, description, hrnPurchase);
    }

    private static CarStatistic.Profit resultToProfitStatistic(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String model = resultSet.getString("model");
        String manufacturer = resultSet.getString("manufacturer");
        BigDecimal hrnProfit = resultSet.getBigDecimal("sum");

        return new CarStatistic.Profit(id, manufacturer, model, hrnProfit);
    }

    private static CarStatistic.RequestCount resultToRequestCountStatistic(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String model = resultSet.getString("model");
        String manufacturer = resultSet.getString("manufacturer");
        int requestCount = resultSet.getInt("requests");

        return new CarStatistic.RequestCount(id, manufacturer, model, requestCount);
    }

    public Car getCar(ConnectionWrapper connection, int id) {
        String sql = "SELECT * FROM cars WHERE id = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return resultToCar(resultSet);
            else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void update(ConnectionWrapper connection, Car car) {
        String sql = "UPDATE cars" +
                " SET model = ?," +
                "     manufacturer = ?," +
                "     hrn_per_day = ?," +
                "     current_user_id = ?," +
                "     thumbnail_url = ?," +
                "     description = ?," +
                "     hrn_purchase = ?" +
                " WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, car.getModel());
            statement.setString(2, car.getManufacturer());
            statement.setBigDecimal(3, car.getHrnPerDay());
            statement.setObject(4, car.getCurrentUserId());
            statement.setString(5, car.getThumbnailUrl());
            statement.setString(6, car.getDescription());
            statement.setBigDecimal(7, car.getHrnPurchase());

            statement.setInt(8, car.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Car insert(ConnectionWrapper connection, String model, String manufacturer, BigDecimal hrnPerDay, Long currentUserId, String thumbnailUrl, String description, BigDecimal hrnPurchase) {
        String sql = "INSERT INTO cars" +
                " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?)" +
                " RETURNING id";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, model);
            statement.setString(2, manufacturer);
            statement.setBigDecimal(3, hrnPerDay);
            statement.setString(4, thumbnailUrl);
            statement.setObject(5, currentUserId);
            statement.setString(6, description);
            statement.setBigDecimal(7, hrnPurchase);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new Car(result.getInt("id"), model, manufacturer, hrnPerDay, currentUserId, thumbnailUrl, description, hrnPurchase);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Car> getAllFreeCars(ConnectionWrapper connection) {
        String sql = "SELECT * FROM cars WHERE current_user_id IS NULL";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ArrayList<Car> cars = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(resultToCar(resultSet));
            }

            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Car> getFreeCarsForManufacturer(ConnectionWrapper connection, String manufacturer) {
        String sql = "SELECT * FROM cars WHERE current_user_id IS NULL AND manufacturer = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, manufacturer);
            ArrayList<Car> cars = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(resultToCar(resultSet));
            }

            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllCarManufacturers(ConnectionWrapper connection) {
        String sql = "SELECT DISTINCT manufacturer FROM cars";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ArrayList<String> manufacturers = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                manufacturers.add(resultSet.getString("manufacturer"));
            }

            return manufacturers;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public List<CarStatistic.Profit> getMostProfitableCars(ConnectionWrapper connection, LocalDate since) {
        String sql = """
                SELECT cars.id, model, manufacturer, SUM (hrn_amount) FROM cars
                INNER JOIN payments ON (
                    payments.car_id = cars.id""";
        if (since != null)
            sql += " AND date >= ?";
        sql += """
                    
                )
                GROUP BY cars.id
                ORDER BY SUM(hrn_amount) DESC""";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (since != null)
                statement.setDate(1, Date.valueOf(since));
            ArrayList<CarStatistic.Profit> cars = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(resultToProfitStatistic(resultSet));
            }

            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public List<CarStatistic.RequestCount> getMostPopularCars(ConnectionWrapper connection, LocalDate since) {
        String sql = """
            SELECT cars.id, model, manufacturer, COUNT (DISTINCT r.id) AS requests
            FROM cars INNER JOIN requests r on cars.id = r.car_id""";
        if (since != null) {
            sql += " AND start_date >= ?";
        }
        sql += """
            
            GROUP BY cars.id;""";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (since != null)
                statement.setDate(1, Date.valueOf(since));
            ArrayList<CarStatistic.RequestCount> cars = new ArrayList<>();

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(resultToRequestCountStatistic(resultSet));
            }

            return cars;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
