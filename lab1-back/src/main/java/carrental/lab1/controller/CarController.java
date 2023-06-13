package carrental.lab1.controller;

import carrental.lab1.model.Car;
import carrental.lab1.model.CarStatistic;
import carrental.lab1.model.Payment;
import carrental.lab1.repository.CarDao;
import carrental.lab1.repository.PaymentDao;
import carrental.lab1.repository.connection.ConnectionPool;
import carrental.lab1.repository.connection.ConnectionWrapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class CarController {
    public static final CarController INSTANCE = new CarController();

    private CarController() {}

    public List<Car> getAvailableCars() {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            return CarDao.INSTANCE.getAllFreeCars(connection);
        }
    }

    public List<Car> getAvailableCars(String manufacturer) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            return CarDao.INSTANCE.getFreeCarsForManufacturer(connection, manufacturer);
        }
    }

    public Car getCar(int id) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            return CarDao.INSTANCE.getCar(connection, id);
        }
    }

    public List<String> getAllCarManufacturers() {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            return CarDao.INSTANCE.getAllCarManufacturers(connection);
        }
    }

    public List<CarStatistic.Profit> getMostProfitableCars(LocalDate since) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            return CarDao.INSTANCE.getMostProfitableCars(connection, since);
        }
    }

    public List<CarStatistic.RequestCount> getMostPopularCars(LocalDate since) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            return CarDao.INSTANCE.getMostPopularCars(connection, since);
        }
    }

    public Car addCar(String model, String manufacturer, BigDecimal hrnPerDay, Long currentUserId, String thumbnailUrl, String description, BigDecimal hrnPurchase) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            Car car = CarDao.INSTANCE.insert(connection,
                    model,
                    manufacturer,
                    hrnPerDay,
                    currentUserId,
                    thumbnailUrl,
                    description,
                    hrnPurchase
            );
            PaymentDao.INSTANCE.insertPayment(connection,
                    hrnPurchase.negate(),
                    null,
                    Payment.TYPE_PURCHASE_NEW_CAR,
                    car.getId(),
                    Instant.now()
            );
            return car;
        }
    }
}
