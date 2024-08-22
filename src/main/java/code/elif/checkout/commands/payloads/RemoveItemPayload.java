package code.elif.checkout.commands.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RemoveItemPayload implements AbstractPayload {
    private int itemId;
}

