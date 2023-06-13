package carrental.lab1.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Payment {
    private final int id;
    private final BigDecimal hrnAmount;
    private final Integer rentRequestId;
    private final int type;
    private final Integer carId;
    private final Instant date;

    public static final int TYPE_REVENUE = 0;
    public static final int TYPE_REPAIR_COST = 1;
    public static final int TYPE_MAINTENANCE = 2;
    public static final int TYPE_REFUND = 3;
    public static final int TYPE_REPAIR_PAID_BY_CUSTOMER = 4;
    public static final int TYPE_PURCHASE_NEW_CAR = 5;

    public Payment(Integer id, BigDecimal hrnAmount, Integer rentRequestId, int type, Integer carId, Instant date) {
        this.id = id;
        this.hrnAmount = hrnAmount;
        this.rentRequestId = rentRequestId;
        this.type = type;
        this.carId = carId;
        this.date = date;
    }

    public BigDecimal getHrnAmount() {
        return hrnAmount;
    }

    public Integer getRentRequestId() {
        return rentRequestId;
    }

    public Integer getCarId() {
        return carId;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return hrnAmount.equals(payment.hrnAmount) && Objects.equals(rentRequestId, payment.rentRequestId) && id == payment.id && type == payment.type && Objects.equals(carId, payment.carId) && Objects.equals(date, payment.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hrnAmount, rentRequestId, id, type);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", hrnAmount=" + hrnAmount +
                ", rentRequestId=" + rentRequestId +
                ", type=" + type +
                ", date=" + date +
                '}';
    }

    public Instant getDate() {
        return date;
    }
}
