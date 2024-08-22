package code.elif.checkout.commands.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddVasItemPayload implements AbstractPayload {
    private Integer itemId;
    private Integer vasItemId;
    private Integer vasCategoryId;
    private Integer vasSellerId;
    private BigDecimal price;
    private Integer quantity;

}
