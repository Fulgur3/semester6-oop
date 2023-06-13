package ua.carrental.lab2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.carrental.lab2.model.Car;
import ua.carrental.lab2.model.Payment;
import ua.carrental.lab2.model.RentRequest;
import ua.carrental.lab2.model.User;
import ua.carrental.lab2.repository.CarRepository;
import ua.carrental.lab2.repository.PaymentRepository;
import ua.carrental.lab2.repository.RentRequestRepository;
import ua.carrental.lab2.repository.UserRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentRequestService {
    private final RentRequestRepository rentRequestRepository;
    private final CarRepository carRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;


    public List<RentRequest> getRequestsWithStatus(int status) {
        return rentRequestRepository.findAllByStatus(status);
    }

    public List<RentRequest> getRequestsWithStatusForUser(int status, long userId) {
        return rentRequestRepository.findAllByStatusAndUserId(status, userId);
    }

    public List<RentRequest> getActiveOutdatedRequests() {
        LocalDate now = LocalDate.now();
        return getRequestsWithStatus(RentRequest.STATUS_ACTIVE)
                .stream().filter(rentRequest -> rentRequest.getEndDate().compareTo(now) < 0)
                .collect(Collectors.toList());
    }

    public List<RentRequest> getActiveOutdatedRequests(long userId) {
        LocalDate now = LocalDate.now();
        return getRequestsWithStatusForUser(RentRequest.STATUS_ACTIVE, userId)
                .stream().filter(rentRequest -> rentRequest.getEndDate().compareTo(now) < 0)
                .collect(Collectors.toList());
    }

    @Transactional
    public RentRequest addNewPending(long userId, int carId, int days, LocalDate startDate, BigDecimal uahAmount) {
        Car car = carRepository.getById(carId);

        if (uahAmount.compareTo(car.getUahPerDay().multiply(BigDecimal.valueOf(days))) < 0)
            throw new IllegalArgumentException("Payment amount is not high enough");

        RentRequest request = new RentRequest();
        request.setUserId(userId);
        request.setDays(days);
        request.setCar(car);
        request.setStartDate(startDate);
        request.setStatus(RentRequest.STATUS_PENDING);
        request = rentRequestRepository.save(request);

        Payment newPayment = new Payment();
        newPayment.setTime(Instant.now());
        newPayment.setRentRequestId(request.getId());
        newPayment.setType(Payment.TYPE_REVENUE);
        newPayment.setCar(car);
        newPayment.setUahAmount(uahAmount);
        paymentRepository.save(newPayment);

        return request;
    }

    @Transactional
    public RentRequest approve(int id) {
        RentRequest request = rentRequestRepository.getById(id);

        if (request.getStatus() != RentRequest.STATUS_PENDING) {
            throw new IllegalArgumentException("Bad rent request ID");
        }

        request.setStatus(RentRequest.STATUS_ACTIVE);
        request = rentRequestRepository.save(request);

        Car car = request.getCar();
        User user = userRepository.getById(request.getUserId());

        rentRequestRepository.deleteAllByCarAndStatus(car, RentRequest.STATUS_PENDING);
        car.setUser(user);
        carRepository.save(car);

        return request;
    }

    public RentRequest deny(int id, String message) {
        RentRequest request = rentRequestRepository.getById(id);

        if (request.getStatus() != RentRequest.STATUS_PENDING) {
            throw new IllegalArgumentException("Bad rent request ID");
        }

        request.setStatus(RentRequest.STATUS_DENIED);
        request.setStatusMessage(message);
        request = rentRequestRepository.save(request);

        //Do refund

        Payment payment = paymentRepository.getFirstByRentRequestIdAndType(id, Payment.TYPE_REVENUE);
        Payment newPayment = new Payment();
        newPayment.setTime(Instant.now());
        newPayment.setRentRequestId(id);
        newPayment.setType(Payment.TYPE_REFUND);
        newPayment.setCar(payment.getCar());
        newPayment.setUahAmount(payment.getUahAmount().negate());
        paymentRepository.save(newPayment);

        return request;
    }

    public RentRequest endSuccessfully(int id, BigDecimal maintenanceCostUah) {
        RentRequest request = rentRequestRepository.getById(id);

        if (request.getStatus() != RentRequest.STATUS_ACTIVE) {
            throw new IllegalArgumentException("Bad rent request ID");
        }

        request.setStatus(RentRequest.STATUS_ENDED);
        request = rentRequestRepository.save(request);

        Car car = request.getCar();
        car.setUser(null);
        car = carRepository.save(car);

        Payment newPayment = new Payment();
        newPayment.setTime(Instant.now());
        newPayment.setRentRequestId(id);
        newPayment.setType(Payment.TYPE_MAINTENANCE);
        newPayment.setCar(car);
        newPayment.setUahAmount(maintenanceCostUah.negate());
        paymentRepository.save(newPayment);

        return request;
    }

    public RentRequest setNeedsRepair(int id, String message, BigDecimal paymentCost) {
        RentRequest request = rentRequestRepository.getById(id);

        if (request.getStatus() != RentRequest.STATUS_ACTIVE) {
            throw new IllegalArgumentException("Bad rent request ID");
        }

        request.setStatus(RentRequest.STATUS_REPAIR_NEEDED);
        request.setStatusMessage(message);
        request.setRepairCost(paymentCost);
        request = rentRequestRepository.save(request);

        Payment newPayment = new Payment();
        newPayment.setTime(Instant.now());
        newPayment.setRentRequestId(id);
        newPayment.setType(Payment.TYPE_REPAIR_COST);
        newPayment.setCar(request.getCar());
        newPayment.setUahAmount(paymentCost.negate());
        paymentRepository.save(newPayment);

        return request;
    }

    public RentRequest payForRepair(int id, BigDecimal uahAmount) {
        RentRequest request = rentRequestRepository.getById(id);
        if (request.getStatus() != RentRequest.STATUS_REPAIR_NEEDED)
            throw new IllegalArgumentException("Bad rent request ID");

        if (!Objects.equals(uahAmount, request.getRepairCost())
                && uahAmount.compareTo(request.getRepairCost()) < 0)
            throw new IllegalArgumentException("Amount is not high enough");

        request.setStatus(RentRequest.STATUS_ENDED);
        request.setStatusMessage(null);
        request = rentRequestRepository.save(request);

        Car car = request.getCar();

        Payment newPayment = new Payment();
        newPayment.setTime(Instant.now());
        newPayment.setRentRequestId(id);
        newPayment.setType(Payment.TYPE_REPAIR_PAID_BY_CUSTOMER);
        newPayment.setCar(car);
        newPayment.setUahAmount(uahAmount);
        paymentRepository.save(newPayment);

        car.setUser(null);
        carRepository.save(car);

        return request;
    }
}
