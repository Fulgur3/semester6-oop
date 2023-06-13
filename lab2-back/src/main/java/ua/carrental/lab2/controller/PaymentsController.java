package ua.carrental.lab2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.carrental.lab2.model.Payment;
import ua.carrental.lab2.service.PaymentService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentService paymentService;

    @GetMapping("/payments")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/payments/car/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<Payment>> getPaymentsForCar(@PathVariable("id") int carId) {
        return ResponseEntity.ok(paymentService.getPaymentsForCar(carId));
    }
}
