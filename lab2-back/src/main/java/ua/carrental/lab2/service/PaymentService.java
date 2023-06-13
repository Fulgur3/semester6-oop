package ua.carrental.lab2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.carrental.lab2.model.Payment;
import ua.carrental.lab2.repository.CarRepository;
import ua.carrental.lab2.repository.PaymentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CarRepository carRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsForCar(int carId) {
        return paymentRepository.findAllByCarOrderByTimeDesc(carRepository.findById(carId).get());
    }
}
