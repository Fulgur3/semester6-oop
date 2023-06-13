package carrental.lab1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Objects;

public final class Car {
    private final int id;
    private final String model;
    private final String manufacturer;
    private final BigDecimal hrnPerDay;
    @JsonIgnore
    private final Long currentUserId;
    private final String thumbnailUrl;
    private final String description;
    @JsonIgnore
    private final BigDecimal hrnPurchase;

    public Car(int id, String model, String manufacturer, BigDecimal hrnPerDay, Long currentUserId, String thumbnailUrl, String description, BigDecimal hrnPurchase) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.hrnPerDay = hrnPerDay;
        this.currentUserId = currentUserId;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.hrnPurchase = hrnPurchase;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public BigDecimal getHrnPerDay() {
        return hrnPerDay;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public int getId() {
        return id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getHrnPurchase() {
        return hrnPurchase;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", hrnPerDay=" + hrnPerDay +
                ", currentUserId=" + currentUserId +
                ", thumnbailUrl=" + thumbnailUrl +
                ", description=" + description +
                ", hrnPurchase=" + hrnPurchase +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && model.equals(car.model) && manufacturer.equals(car.manufacturer) && hrnPerDay.equals(car.hrnPerDay) && Objects.equals(currentUserId, car.currentUserId) && Objects.equals(thumbnailUrl, car.thumbnailUrl) && description.equals(car.description) && hrnPurchase.equals(car.hrnPurchase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model, manufacturer, hrnPerDay, currentUserId, thumbnailUrl, description, hrnPurchase);
    }
}
