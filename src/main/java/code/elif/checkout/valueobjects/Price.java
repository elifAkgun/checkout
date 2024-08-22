package code.elif.checkout.valueobjects;

import code.elif.checkout.exception.PriceException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
public class Price {
    private final BigDecimal value;

    public Price(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceException("Price cannot be null or negative");
        }
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
