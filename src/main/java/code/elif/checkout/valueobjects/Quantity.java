package code.elif.checkout.valueobjects;

import code.elif.checkout.exception.QuantityException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Quantity {
    private final Integer value;

    public Quantity(int value) {
        if (value <= 0) {
            throw new QuantityException("Quantity must be positive");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
