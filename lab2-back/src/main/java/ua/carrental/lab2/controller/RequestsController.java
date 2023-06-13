package ua.carrental.lab2.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ua.carrental.lab2.model.RentRequest;
import ua.carrental.lab2.service.RentRequestService;
import ua.carrental.lab2.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class RequestsController {
    private final RentRequestService rentRequestService;
    private final UserService userService;

    @GetMapping("/requests/my-outdated")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseEntity<List<RentRequest>> getOutdatedRequests(Authentication auth) {
        long userId = userService.getUserByKeycloakId(auth.getName()).getPassportId();
        return ResponseEntity.ok(rentRequestService.getActiveOutdatedRequests(userId));
    }

    @GetMapping("/requests/status/my/{status}")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseEntity<List<RentRequest>> getRequests(@PathVariable("status") int status, Authentication auth) {
        long userId = userService.getUserByKeycloakId(auth.getName()).getPassportId();
        return ResponseEntity.ok(rentRequestService.getRequestsWithStatusForUser(status, userId));
    }

    @GetMapping("/requests/outdated")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<RentRequest>> getOutdatedRequests() {
        return ResponseEntity.ok(rentRequestService.getActiveOutdatedRequests());
    }

    @GetMapping("/requests/status/{status}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<RentRequest>> getRequests(@PathVariable("status") int status) {
        return ResponseEntity.ok(rentRequestService.getRequestsWithStatus(status));
    }

    @PostMapping("/requests/approve/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity approve(@PathVariable("id") int requestId) {
        rentRequestService.approve(requestId);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class DenyRequest {
        private String message;
    }

    @PostMapping("/requests/deny/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity deny(@PathVariable("id") int requestId, @Validated @RequestBody DenyRequest request) {
        rentRequestService.deny(requestId, request.message);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class EndSuccessfullyRequest {
        private BigDecimal maintenanceCostUah;
    }

    @PostMapping("/requests/end/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity end(@PathVariable("id") int requestId, @Validated @RequestBody EndSuccessfullyRequest request) {
        rentRequestService.endSuccessfully(requestId, request.maintenanceCostUah);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class BrokenRequest {
        private String message;
        private BigDecimal repairCostUah;
    }

    @PostMapping("/requests/broken/{id}")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity broken(@PathVariable("id") int requestId, @Validated @RequestBody BrokenRequest request) {
        rentRequestService.setNeedsRepair(requestId, request.message, request.repairCostUah);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class RepairRequest {
        private BigDecimal paymentUah;
    }

    @PostMapping("/requests/repair/{id}")
    public ResponseEntity repair(@PathVariable("id") int requestId, @Validated @RequestBody RepairRequest request) {
        rentRequestService.payForRepair(requestId, request.paymentUah);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class NewRentRequest {
        private BigDecimal paymentUah;
        private int carId;
        private int days;
        private LocalDate startDate;
    }

    @PostMapping("/requests/new")
    @PreAuthorize("hasAnyAuthority('user')")
    public ResponseEntity<Integer> newPending(@Validated @RequestBody NewRentRequest request, Authentication auth) {
        long userId = userService.getUserByKeycloakId(auth.getName()).getPassportId();
        return ResponseEntity.ok(rentRequestService.addNewPending(
                userId,
                request.carId,
                request.days,
                request.startDate,
                request.paymentUah
        ).getId());
    }
}
