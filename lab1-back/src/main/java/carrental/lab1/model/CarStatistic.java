package carrental.lab1.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class CarStatistic {
    public static final class Profit {
        private final String model;
        private final String manufacturer;
        private final int id;
        private final BigDecimal hrnProfit;

        public Profit(int id, String manufacturer, String model, BigDecimal hrnProfit) {
            this.model = model;
            this.manufacturer = manufacturer;
            this.id = id;
            this.hrnProfit = hrnProfit;
        }

        public String getModel() {
            return model;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public int getId() {
            return id;
        }

        public BigDecimal getHrnProfit() {
            return hrnProfit;
        }

        @Override
        public String toString() {
            return "CarAndPayment{" +
                    "model='" + model + '\'' +
                    ", manufacturer='" + manufacturer + '\'' +
                    ", id=" + id +
                    ", hrnProfit=" + hrnProfit +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Profit that = (Profit) o;
            return id == that.id && model.equals(that.model) && manufacturer.equals(that.manufacturer) && hrnProfit.equals(that.hrnProfit);
        }

        @Override
        public int hashCode() {
            return Objects.hash(model, manufacturer, id, hrnProfit);
        }
    }

    public static final class RequestCount {
        private final String model;
        private final String manufacturer;
        private final int id;
        private final int requestCount;

        public RequestCount(int id, String manufacturer, String model, int requestCount) {
            this.model = model;
            this.manufacturer = manufacturer;
            this.id = id;
            this.requestCount = requestCount;
        }

        public String getModel() {
            return model;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public int getId() {
            return id;
        }

        public int getRequestCount() {
            return requestCount;
        }

        @Override
        public String toString() {
            return "CarAndPayment{" +
                    "model='" + model + '\'' +
                    ", manufacturer='" + manufacturer + '\'' +
                    ", id=" + id +
                    ", requestCount=" + requestCount +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RequestCount that = (RequestCount) o;
            return id == that.id && model.equals(that.model) && manufacturer.equals(that.manufacturer) && requestCount == that.requestCount;
        }

        @Override
        public int hashCode() {
            return Objects.hash(model, manufacturer, id, requestCount);
        }
    }
}
