package code.elif.checkout.dto;

import code.elif.checkout.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ItemDto {
    private final Integer itemId;
    private final Integer categoryId;
    private final Integer sellerId;
    private final BigDecimal price;
    private final Integer quantity;
    private final Integer parentItemId;
    private final ItemType itemType;
}