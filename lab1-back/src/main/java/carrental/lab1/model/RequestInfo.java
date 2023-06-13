package carrental.lab1.model;

import java.math.BigDecimal;
import java.util.Objects;

// Combo of RentRequest and Car
public final class RequestInfo {
    private final int requestId;
    private final int carId;
    private final long passportId;
    private final int status;
    private final String message;
    private final String carManufacturer;
    private final String carModel;
    private final int days;
    private final BigDecimal hrnPerDay;
    private final String startDate;
    private final BigDecimal repairCost;

    public RequestInfo(int requestId, int status, String message, long passportId, int days, int carId, String startDate, String carManufacturer, String carModel, BigDecimal hrnPerDay, BigDecimal repairCost) {
        this.requestId = requestId;
        this.carId = carId;
        this.passportId = passportId;
        this.status = status;
        this.message = message;
        this.carManufacturer = carManufacturer;
        this.carModel = carModel;
        this.days = days;
        this.startDate = startDate;
        this.hrnPerDay = hrnPerDay;
        this.repairCost = repairCost;
    }

    public BigDecimal getRepairCost() {
        return repairCost;
    }

    public int getStatus() {
        return status;
    }

    public int getDays() {
        return days;
    }

    public int getCarId() {
        return carId;
    }

    public String getStartDate() {
        return startDate;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "requestId=" + requestId +
                ", carId=" + carId +
                ", passportId=" + passportId +
                ", status=" + status +
                ", message=" + message +
                ", carManufacturer='" + carManufacturer + '\'' +
                ", carModel='" + carModel + '\'' +
                ", days=" + days +
                ", startDate=" + startDate +
                ", hrnPerDay=" + hrnPerDay +
                ", repairCost=" + repairCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestInfo that = (RequestInfo) o;
        return requestId == that.requestId && carId == that.carId && passportId == that.passportId && status == that.status && days == that.days && carManufacturer.equals(that.carManufacturer) && carModel.equals(that.carModel) && hrnPerDay.equals(that.hrnPerDay) && startDate.equals(that.startDate) && message.equals(that.message) && repairCost.equals(that.repairCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, carId, passportId, status, carManufacturer, carModel, days, hrnPerDay, startDate, message, repairCost);
    }

    public int getRequestId() {
        return requestId;
    }

    public long getPassportId() {
        return passportId;
    }

    public String getCarManufacturer() {
        return carManufacturer;
    }

    public String getCarModel() {
        return carModel;
    }

    public BigDecimal getHrnPerDay() {
        return hrnPerDay;
    }

    public String getMessage() {
        return message;
    }
}
