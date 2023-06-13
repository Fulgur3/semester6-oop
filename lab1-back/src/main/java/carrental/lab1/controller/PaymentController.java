package carrental.lab1.controller;

import carrental.lab1.model.PaymentInfo;
import carrental.lab1.repository.PaymentDao;
import carrental.lab1.repository.connection.ConnectionPool;
import carrental.lab1.repository.connection.ConnectionWrapper;

import java.time.LocalDate;
import java.util.List;

public class PaymentController {
    public static final PaymentController INSTANCE = new PaymentController();

    private PaymentController() {}

    public List<PaymentInfo> getAllPaymentInfo(LocalDate since) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            return PaymentDao.INSTANCE.getAllPaymentInfo(connection, since);
        }
    }

    public List<PaymentInfo> getAllPaymentInfo(int carId) {
        try (ConnectionWrapper connection = ConnectionPool.INSTANCE.getConnection()) {
            return PaymentDao.INSTANCE.getAllPaymentInfo(connection, carId);
        }
    }
}
