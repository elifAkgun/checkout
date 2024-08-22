package code.elif.checkout;

import code.elif.checkout.commands.Command;
import code.elif.checkout.commands.Result;
import code.elif.checkout.entity.cart.Cart;
import code.elif.checkout.entity.cart.service.CartService;
import code.elif.checkout.entity.cart.service.PromotionService;
import code.elif.checkout.entity.promotions.CategoryPromotion;
import code.elif.checkout.entity.promotions.Promotion;
import code.elif.checkout.entity.promotions.SameSellerPromotion;
import code.elif.checkout.entity.promotions.TotalPricePromotion;
import code.elif.checkout.exception.CommandException;
import code.elif.checkout.utils.CommandParser;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class App {

    private static final Logger logger = Logger.getAnonymousLogger();

    private static final String INPUT_FILE = "input";
    private static final String OUTPUT_FILE = "output";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final List<Promotion> promotions;
    private static final CartService cartService;

    static {
        promotions = new ArrayList<>();
        promotions.add(new SameSellerPromotion());
        promotions.add(new CategoryPromotion());
        promotions.add(new TotalPricePromotion());
        cartService = new CartService(new Cart(), new PromotionService(promotions));
    }

    public static void main(String[] args) {
        String inputFileName;
        String outputFileName;

        // Log initial message
        logInitialization();

        // Check if arguments are provided
        if (args.length > 0) {
            inputFileName = args[0];
        } else {
            String input = getInputFromUser(INPUT_FILE);
            inputFileName = input.isBlank() ? INPUT_FILE : input;
        }

        if (args.length > 1) {
            outputFileName = args[1];
        } else {
            String output = getInputFromUser(OUTPUT_FILE);
            outputFileName = output.isBlank() ? OUTPUT_FILE : output;
        }

        CommandParser parser = new CommandParser(cartService);

        List<String> commands = readCommandsFromFile(inputFileName);
        String results = processCommands(commands, parser);
        writeResultsToFile(results, outputFileName);
    }

    private static void logInitialization() {
            System.out.println("Initializing the application with the following settings:");
            System.out.println("Default input file name is 'input' if no argument is provided.");
            System.out.println("Default output file name is 'output' if no argument is provided.");
            System.out.println("To set custom file paths, provide arguments as follows:");
            System.out.println("java App [inputFilePath] [outputFilePath]");
            System.out.println("Example: java App customInput.txt customOutput.txt");
            System.out.println("If no arguments are provided, you will be prompted to enter the file paths.");
            System.out.println("--------------------------------------------------");
    }

    private static String getInputFromUser(String fileType) {
        Scanner scanner = new Scanner(System.in);
        String inputMessage = "Please enter the " + fileType +
                " file path: (default file name \"" +
                fileType +
                "\"";
        System.out.println(inputMessage);
        return scanner.nextLine().trim();
    }

    private static List<String> readCommandsFromFile(String fileName) {
        try {
            return Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            logger.warning("Error reading input file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private static String processCommands(List<String> commandLines, CommandParser parser) {
        StringBuilder output = new StringBuilder();
        for (String commandLine : commandLines) {
            try {
                Command command = parser.parseCommand(commandLine);
                Result result = command.execute();

                if (result != null) {
                    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                    String jsonString = objectMapper.writeValueAsString(result);
                    output.append(jsonString).append("\n");
                }
            } catch (CommandException | JsonProcessingException e) {
                logger.warning("Commandline is not valid: \"" + commandLine + "\"");
            }
        }
        return output.toString();
    }

    private static void writeResultsToFile(String output, String outputFile) {
        try {
            Files.write(Paths.get(outputFile), output.getBytes());
        } catch (IOException e) {
            logger.warning("Error writing output file: " + e.getMessage());
        }
    }
}
