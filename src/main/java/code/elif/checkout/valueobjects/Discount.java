package code.elif.checkout.valueobjects;

import code.elif.checkout.exception.DiscountException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
@EqualsAndHashCode
public class Discount implements Comparable<Discount> {
    public static final Discount ZERO_DISCOUNT = new Discount(BigDecimal.ZERO, null);
    private final BigDecimal value;
    private final Integer promotionId;

    public Discount(BigDecimal value, Integer promotionId) {
        this.promotionId = promotionId;
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new DiscountException("Discount cannot be null or negative");
        }
        this.value = value;
    }

    @Override
    public int compareTo(Discount o) {
        return this.value.compareTo(o.value);
    }

    public BigDecimal getValue() {
        return value.setScale(2, RoundingMode.HALF_DOWN);
    }

    public Integer getPromotionId() {
        return promotionId;
    }
}
