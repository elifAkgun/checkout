package code.elif.checkout.commands;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@ToString
@Getter
public class Result implements Serializable {
    private final boolean success;
    private final String message;
}
