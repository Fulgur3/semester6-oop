package carrental.lab1.model;

import java.math.BigDecimal;

// Combo of Payment and Car
public class PaymentInfo {
    public final String model;
    public final String manufacturer;
    public final int type;
    public final BigDecimal hrnAmount;
    public final String date;

    public PaymentInfo(String date, String model, String manufacturer, int type, BigDecimal hrnAmount) {
        this.date = date;
        this.model = model;
        this.manufacturer = manufacturer;
        this.type = type;
        this.hrnAmount = hrnAmount;
    }
}
