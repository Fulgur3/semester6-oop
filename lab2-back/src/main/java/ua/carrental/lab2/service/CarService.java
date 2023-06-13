package ua.carrental.lab2.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.carrental.lab2.model.Car;
import ua.carrental.lab2.model.Payment;
import ua.carrental.lab2.model.User;
import ua.carrental.lab2.repository.CarRepository;
import ua.carrental.lab2.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final PaymentRepository paymentRepository;

    public List<Car> getAvailableCars() {
        return carRepository.findByUserIsNull();
    }

    public List<Car> getAvailableCars(String manufacturer) {
        return carRepository.findByUserIsNullAndManufacturer(manufacturer);
    }

    public Car getCar(int id) {
        return carRepository.findById(id).get();
    }

    public List<String> getAllCarManufacturers() {
        return carRepository.findAllCarManufacturers();
    }

    @Transactional
    public Car addCar(String model, String manufacturer, BigDecimal uahPerDay, User user, String thumbnailUrl, String description, BigDecimal uahPurchase) {
        Car car = new Car();
        car.setModel(model);
        car.setManufacturer(manufacturer);
        car.setUahPerDay(uahPerDay);
        car.setThumbnailUrl(thumbnailUrl);
        car.setDescription(description);
        car.setUahPurchase(uahPurchase);
        car.setUser(user);
        car = carRepository.save(car);

        Payment payment = new Payment();
        payment.setUahAmount(uahPurchase.negate());
        payment.setCar(car);
        payment.setRentRequestId(null);
        payment.setType(Payment.TYPE_PURCHASE_NEW_CAR);
        payment.setTime(Instant.now());
        paymentRepository.save(payment);
        return car;
    }
}
