package ua.carrental.lab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.carrental.lab2.model.Car;
import ua.carrental.lab2.model.Payment;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment getFirstByRentRequestIdAndType(int requestId, int type);

    List<Payment> findAllByCarOrderByTimeDesc(Car car);
}
