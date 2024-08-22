package code.elif.checkout.commands;

/**
 * An abstract base class representing a generic command.
 * <p>
 * Subclasses of this class are expected to provide concrete implementations
 * of the {@link #execute()} method to perform specific actions.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * public class MyCommand extends Command {
 *     {@literal @}Override
 *     public Result execute() {
 *         // Command execution logic
 *         return new Result(true, "Command executed successfully.");
 *     }
 * }
 * </pre>
 *
 * @see Result
 * @since 1.0
 */
public abstract class Command {

    /**
     * Executes the command and returns the result.
     * <p>
     * Subclasses should override this method to provide the specific
     * execution logic for the command.
     * </p>
     *
     * @return the result of the command execution
     */
    public abstract Result execute();
}
