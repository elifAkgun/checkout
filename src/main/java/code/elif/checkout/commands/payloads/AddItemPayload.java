package code.elif.checkout.commands.payloads;

import code.elif.checkout.exception.AddItemPayloadException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddItemPayload implements AbstractPayload {

    private Integer itemId;

    private Integer categoryId;

    private Integer sellerId;

    private BigDecimal price;

    private Integer quantity;


    public void validate() {
        if (itemId == null || itemId <= 0) {
            throw new AddItemPayloadException("Invalid item ID");
        }
        if (categoryId == null || categoryId <= 0) {
            throw new AddItemPayloadException("Invalid category ID");
        }
        if (sellerId == null || sellerId <= 0) {
            throw new AddItemPayloadException("Invalid seller ID");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AddItemPayloadException("Invalid price");
        }
        if (quantity == null || quantity <= 0) {
            throw new AddItemPayloadException("Invalid quantity");
        }
    }

}
