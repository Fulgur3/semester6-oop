package carrental.lab1.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class RentRequest {
    private final int id;
    private final int status;
    private final String statusMessage;
    private final long userId;
    private final int days;
    private final int carId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final BigDecimal repairCost;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_DENIED = 1;
    public static final int STATUS_ACTIVE = 3;
    public static final int STATUS_REPAIR_NEEDED = 4;
    public static final int STATUS_ENDED = 5;

    public RentRequest(int id, int status, String statusMessage, long userId, int days, int carId, LocalDate startDate, BigDecimal repairCost) {
        this.id = id;
        this.status = status;
        this.statusMessage = statusMessage;
        this.userId = userId;
        this.days = days;
        this.carId = carId;
        this.startDate = startDate;
        this.repairCost = repairCost;
        this.endDate = startDate.plusDays(days);
    }

    public int getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public long getUserId() {
        return userId;
    }

    public int getDays() {
        return days;
    }

    public int getCarId() {
        return carId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getRepairCost() {
        return repairCost;
    }

    @Override
    public String toString() {
        return "RentRequest{" +
                "id=" + id +
                ", status=" + status +
                ", statusMessage='" + statusMessage + '\'' +
                ", userId=" + userId +
                ", days=" + days +
                ", carId=" + carId +
                ", startDate=" + startDate +
                ", paymentCost=" + repairCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentRequest that = (RentRequest) o;
        return id == that.id && status == that.status && userId == that.userId && days == that.days && carId == that.carId && Objects.equals(statusMessage, that.statusMessage) && startDate.equals(that.startDate) && Objects.equals(repairCost, that.repairCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, statusMessage, userId, days, carId, startDate, repairCost);
    }
}
