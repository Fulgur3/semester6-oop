package ua.carrental.lab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.carrental.lab2.model.Car;
import ua.carrental.lab2.model.RentRequest;

import java.util.List;

@Repository
public interface RentRequestRepository extends JpaRepository<RentRequest, Integer> {
    void deleteAllByCarAndStatus(Car car, int status);

    List<RentRequest> findAllByStatus(int status);

    List<RentRequest> findAllByStatusAndUserId(int status, long userId);
}
