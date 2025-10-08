import swiftbot.*; // Import the SwiftBot library for robot functionality

import javax.imageio.ImageIO; // Import for handling image input/output operations
import java.awt.image.BufferedImage; // Import for managing buffered images
import java.io.File; // Import for file handling
import java.util.Scanner; // Import for user input handling
import java.util.Random; // Import for random number generation
import java.util.ArrayList; // Import for using ArrayList data structure
import java.util.Collections; // Import for sorting and shuffling lists
import java.util.List; // Import for list operations

public class MasterMind2 {
    // Store the generated color sequence as a class-level variable
    private static String generatedColors;

    // Maximum number of attempts a player gets to guess the code
    private static int maxAttempts = 6; // Deafult Mode , Deafult Values

    // Length of the code sequence to be guessed
    private static int codeLength = 4; // Default Mode , Deafult Values

    // Minimum allowed code length in custom mode
    private static final int MIN_CODE_LENGTH = 3; // Custom Mode

    // Maximum allowed code length in custom mode
    private static final int MAX_CODE_LENGTH = 6; // Custom Mode

    // Instance of SwiftBotAPI for robot interactions
    private static SwiftBotAPI swiftBot;

    // Scoreboard variables to track player and computer scores
    private static int playerScore = 0;
    private static int computerScore = 0;

    // Boolean flag to indicate if a game is currently in progress
    private static boolean gameInProgress = false;

    // RGB color values for loss celebration (red color)
    private static final int[] RED_COLOR = { 255, 0, 0 };

    // RGB values to turn off color (black)
    private static final int[] OFF = { 0, 0, 0 };

    public static void main(String[] args) throws InterruptedException {
        swiftBot = new SwiftBotAPI(); // Initialize the SwiftBot API instance

        displayWelcomeScreen(); // Display the welcome screen to the user , access the displayWelcomeScreen
                                // method

        try {
            // If Button A is pressed, Default Mode is chosen
            swiftBot.enableButton(Button.A, () -> {
                System.out.println("\nMASTERMIND SwiftBot");
                System.out.println("*********************");
                System.out.println("Default Mode:"); // Inform user that Default Mode is selected
                swiftBot.disableButton(Button.A); // Disable Button A after selection to prevent re-triggering

                try {
                    gameInProgress = true; // Set game state to active
                    maxAttempts = 6; // Set max attempts to default value
                    codeLength = 4; // Set code length to default value
                    defaultMode(); // Start Default Mode
                } catch (InterruptedException e) {
                    System.out.println("Game was interrupted: " + e.getMessage()); // Handle interruption error
                }
            });

            // If Button B is pressed, Custom Mode is chosen
            swiftBot.enableButton(Button.B, () -> {
                System.out.println("\nMASTERMIND SwiftBot");
                System.out.println("*********************");
                System.out.println("Custom Mode:"); // Inform user that Custom Mode is selected
                swiftBot.disableButton(Button.B); // Disable Button B after selection to prevent re-triggering

                try {
                    gameInProgress = true; // Set game state to active
                    customMode(); // Start Custom Mode
                } catch (InterruptedException e) {
                    System.out.println("Game was interrupted: " + e.getMessage()); // Handle interruption error
                }
            });

            // If Button X is pressed, exit the game
            swiftBot.enableButton(Button.X, () -> {
                
                if (gameInProgress) {
                    displayFinalScores(); // Show final scores if the game is active
                } else {
                    displayFinalScores(); // Show final scores if no game is in progress
                }
                try {
                    Thread.sleep(2000); // Pause for 2 seconds before exiting
                } catch (InterruptedException e) {
                    // Ignore exception since it's non-critical
                }
                System.exit(0); // Exit the program
                swiftBot.disableButton(Button.X); // Disable Button X to prevent re-triggering
            });

            // If Button Y is pressed, restart the game only if a game has finished
            swiftBot.enableButton(Button.Y, () -> {
                if (!gameInProgress) { // Only restart if a game has finished
                    System.out.println("\nStarting a new game...");
                    displayScores(); // Display scores before restarting
                    try {
                        gameInProgress = true; // Set game state to active
                        defaultMode(); // Restart in Default Mode
                    } catch (InterruptedException e) {
                        System.out.println("Game was interrupted: " + e.getMessage()); // Handle interruption error
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage()); // Print error message if an exception occurs
        }
    }

    private static boolean AlbanianLanguage = false; // Default to English
private static final String[][] languageStrings = {
    // English strings
    {
        "MASTERMIND SwiftBot",
        "Welcome to MasterMind Game",
        "Press A --> Default Mode",
        "Press B --> Custom Mode",
        "Type E --> English",
        "Type A --> Albanian",
        "Default Mode:",
        "Custom Mode:",
        "Game over! You've used all",
        "Attempts.", //9
        "The secret code was:",
        "Exit Game --> Press X",
        "Play Again --> Press Y",
        "Final Scores:",
        "Player Score:",
        "Computer Score:",
        "Congratulations! You won!",
        "The computer won. Better luck next time! :(",
        "Tie!",
        "Current Scores:",
        "Starting a new game...",
        "Enter the maximum number of attempts (1 or more):",
        "Please enter a positive number (1 or more).",
        "Please enter a valid number.",
        "Enter the code length (between 3-6):",
        "Please enter a number between",
        "and",
        "Starting game with",
        "attempts and code length of",
        "Using default settings:",
        "Generated a secret color code with",
        "colors.",
        "Try to guess the code! (The colors are: R, G, B, Y, P, O)", // Text line 31
        "You have", // 32
        "attempts.", // 33
        "Attempt",
        "Capturing colors with camera. Please show", // 35
        "colors...",
        "Captured colors:",
        "Please enter exactly",
        "colors!",
        "Invalid colors! Use only R, G, B, Y, P, O.",
        "Your guess contains duplicate colors! Each color must be used only once.",
        "Result:",
        "Congratulations! You've cracked the code in",
        "Attempts!",
        "ERROR: Image is null",
        "Error saving image:",
        "Couldn't identify color. Please try again.",
        "Duplicate color detected",
        "Please show a different color.",
        "ERROR:",
        "ERROR!!"
    },
    // Albanian strings
    {
        "MASTERMIND SwiftBot",
        "Miresevini ne lojen MasterMind", 
        "Shtyp A --> Modo i Default", 
        "Shtyp B --> Modo i Custom",
        "Shkruani E --> Anglisht", 
        "Shkruani F --> Shqip", 
         "Modo i Default:", 
         "Modo i Custom:", 
         "Loje e mbaruar! Keni perdorur te gjitha", 
         "perpjekjet.", 
         "Kodi sekret ishte:", 
         "Dil nga loja --> Shtyp X", 
         "Luaj perseri --> Shtyp Y", 
         "Rezultatet perfundimtare:", 
         "Rezultati i lojtarit:", 
         "Rezultati i kompjuterit:", 
         "Pergezime! Keni fituar!", 
         "Kompjuteri fitoi. Fat me te mire heren tjeter! :(",
         "Barazim!", 
         "Rezultatet aktuale:", 
         "Pergatitja e nje loje te re...", 
         "Shkruani numrin maksimal te perpjekjeve (1 ose me shume):", 
         "Ju lutem shkruani nje numer pozitiv (1 ose me shume).", 
         "Ju lutem shkruani nje numer te vlefshem.", 
         "Shkruani gjatesine e kodit (mes 3-6):", 
         "Ju lutem shkruani nje numer mes", 
         "dhe", 
         "Po fillojme lojen me", 
         "perpjekje dhe gjatesine e kodit prej", 
         "Po perdorim cilesimet e default:", 
         "U krijua nje kod sekret me ngjyrat", 
         "ngjyrave.", 
         "Provo te gjesh kodin! (Ngjyrat jane: R, G, B, Y, P, O)", 
         "Ju keni",
          "perpjekje.", 
          "Perpjekje", 
          "Po kapim ngjyrat me kamere. Ju lutem tregoni", 
          "ngjyrat...", 
          "Ngjyrat e kapura:", 
          "Ju lutem shkruani saktesisht",
          "ngjyra!",
         "Ngjyra te pavlefshme! Perdorni vetem R, G, B, Y, P, O.", 
            "Guess-i juaj permban ngjyra te perseritura! Çdo ngjyre duhet te perdoret vetem nje here.", 
            "Rezultati:", 
            "Pergezime! Keni zbuluar kodin ne", 
            "perpjekje!",
            "GABIM: Imazhi eshte null",
        "Gabim gjate ruajtjes se imazhit:",
        "Nuk mund te identifikoja ngjyren. Ju lutem provoni perseri.",
        "U zbulua ngjyre e perseritur",
        "Ju lutem tregoni nje ngjyre tjeter.",
        "GABIM:",
        "GABIM!!"
    }
};
/**
 * Returns text in the current language
 * 
 * @param stringIndex The index of the string in the language array
 * @return The text in the current language
 */
private static String getText(int stringIndex) {
    return languageStrings[AlbanianLanguage ? 1 : 0][stringIndex];
}
    /*
     * Displays the welcome screen at the start of the program
     */
    private static void displayWelcomeScreen() {
        System.out.println("\n" + getText(0));
        System.out.println("*********************");
        System.out.println(getText(1)); // Welcome to MasterMind Game
        System.out.println("\n" + getText(4)); // Press E for English
        System.out.println("\n" + getText(5)); // Press A for French
        
        // Wait for language selection
        Scanner scanner = new Scanner(System.in);
        boolean languageSelected = false;
        
        while (!languageSelected) {
            System.out.print("\nSelect language (E/A): ");
            String input = scanner.nextLine().trim().toUpperCase();
            
            if (input.equals("E")) {
                AlbanianLanguage = false;
                languageSelected = true;
                System.out.println("Language set to English");
            } else if (input.equals("A")) {
                AlbanianLanguage = true;
                languageSelected = true;
                System.out.println("Gjuha u vendos ne Shqip");
            } else {
                System.out.println("Invalid input. Please enter E or A.");
            }
        }
        
        // Now display the game options in the selected language
        System.out.println("\n" + getText(0));
        System.out.println("*********************");
        System.out.println(getText(1)); // Welcome to MasterMind Game
        System.out.println("\n" + getText(2)); // Press A for Default Mode
        System.out.println("\n" + getText(3)); // Press B for Custom Mode
    }

    /**
     * Displays the current scores of the player and computer
     */
    private static void displayScores() { // Method gets called
        System.out.println("\nCurrent Scores:");
        System.out.println("Player Score: " + playerScore); // Display player's score
        System.out.println("Computer Score: " + computerScore); // Display computer's score
        System.out.println("*********************");
    }

    /**
     * Pauses execution for a given number of milliseconds
     * 
     * @param milliseconds The duration to pause the program , Parameter of this
     *                     method is SleepFor
     *                     SleepFor Method is for pausing
     */
    private static void sleepFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds); // Pause execution for the specified time
        } catch (InterruptedException e) {
            // Ignore / add minimal handling to prevent crash
        }
    }

    /**
     * Displays the final scores when exiting the game and provides visual feedback
     */
    private static void displayFinalScores() { // calls displayFinalScores method
        System.out.println("\n" + getText(13)); // \n is for new Line
        System.out.println(getText(14) + playerScore); // Display final player's score
        System.out.println(getText(15) + computerScore); // Display final computer's score

        if (playerScore > computerScore) {
            System.out.println(getText(16)); // Player wins
            swiftBot.move(100, -100, 3000); // SwiftBot moves to celebrate, does a spin for 3 seconds , 100 is the
                                            // velocity left wheel forward right wheel backwards
        } else if (computerScore > playerScore) {
            System.out.println(getText(17)); // Computer wins
            swiftBot.fillUnderlights(RED_COLOR); // Show red light , calls private static variable RED-COLOR which is
                                                 // the RED Colour
            sleepFor(1000); // Wait 1 second , calls SleepFor Method
            swiftBot.fillUnderlights(OFF); // Turn off lights , Lights are turned off calls priavte integer variable OFF
                                           // which is no color assigned to
            sleepFor(1000); // Wait 1 second
            swiftBot.fillUnderlights(RED_COLOR); // Show red light again
            sleepFor(1000); // Wait 1 second
            swiftBot.fillUnderlights(OFF); // Turn off lights
            sleepFor(1000); // Wait 1 second
            swiftBot.fillUnderlights(RED_COLOR); // Show red light again
        } else {
            System.out.println(getText(18)); // If scores are equal, it's a tie
        }
        System.out.println("*********************");
    }

    /**
     * Generates a random sequence of colors based on the input length. This is for
     * the Computer genrating the guessing code
     * 
     * @param input the parameter input is the number of colors to be selected from
     *              the list of available colors (R, G, B, Y, P, O)
     * @return A random sequence of colors as a String, or null if the input is
     *         invalid
     */
    public static String generateColors(int input) {
        // Define the valid color characters: Red (R), Green (G), Blue (B), Yellow (Y),
        // Pink (P), Orange (O)
        char[] validColors = { 'R', 'G', 'B', 'Y', 'P', 'O' };

        // Check if the input length is greater than the number of available unique
        // colors
        if (input > validColors.length) {
            System.out.println(getText(52)); // Inform the user that the input is invalid
            return null; // Cannot generate a sequence longer than the number of available colors
        }

        // Creating a copy of the valid colors array to shuffle without modifying the
        // original
        char[] colors = validColors.clone();

        // Shuffle the colors array using the Fisher-Yates algorithm for randomness
        // ,using For Loop
        for (int i = colors.length - 1; i > 0; i--) {
            // Generate a random index between 0 and i (inclusive)
            int index = (int) (Math.random() * (i + 1));

            // Swap elements at index 'i' and 'index'
            char temp = colors[i];
            colors[i] = colors[index];
            colors[index] = temp;
        }

        // Build a string by appending the first 'input' number of shuffled colors
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input; i++) {
            result.append(colors[i]); // Append each selected color to the result
        }

        // Return the generated random sequence of colors as a string
        return result.toString();
    }

    /**
     * Custom mode that allows users to set maximum attempts and code length.
     * This mode asks the user for their desired settings and then starts the game
     * with those settings. The Changes are mad within the Max Attempts and Code length
     */
    private static void customMode() throws InterruptedException {
        Scanner scanner = new Scanner(System.in); // User input scanner for the Max Attempts

        // Print custom mode title
        System.out.println("\nMASTERMIND SwiftBot - " + getText(7)); // Custom Mode
        System.out.println("*********************");

        // Get maximum number of attempts from the user
        boolean validAttempts = false; // Boolean flag to check if the input is valid
        while (!validAttempts) { // While loop to keep asking for input until its valid
            System.out.print(getText(21) + " ");
            try {
                // Attempt to read an integer value for max attempts from the user
                int input = Integer.parseInt(scanner.nextLine().trim()); // Trim method is used to remove any trailing of white space from the input
                if (input >= 1) {
                    maxAttempts = input; // Set maxAttempts to user input
                    validAttempts = true; // Valid input, exit loop
                } else {
                    System.out.println(getText(22)); // Prompt for valid input
                }
            } catch (NumberFormatException e) {
                System.out.println(getText(23)); // Catch invalid number input
            }
        }

        // Get code length from the user
        boolean validLength = false; // boolena flaf to check if the input is valid
        while (!validLength) { // while loop to keep asking for input until its valid
            System.out.print(getText(24) + " ");
            try {
                // Attempt to read an integer value for code length
                int input = Integer.parseInt(scanner.nextLine().trim()); // Trim is used to remove any trailing of white space from the input
                if (input >= MIN_CODE_LENGTH && input <= MAX_CODE_LENGTH) { // Checking input is within acceptable range 3 - 6
                    codeLength = input; // Set codeLength to user input if valid
                    validLength = true; // Valid input, exit loop
                } else {
                    // Prompt for a valid code length within the acceptable range
                    System.out.println(getText(25) + MIN_CODE_LENGTH + " " + getText(26) + " " + MAX_CODE_LENGTH + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(getText(23)); // Catch invalid number input
            }
        }

        // Output the custom settings and inform the user the game is starting
        System.out.println("\n " + getText(27) + " " + maxAttempts + " " + getText(28) + " " + codeLength);
        Thread.sleep(1500); // Short pause to simulate game preparation

        // Start the game with the custom settings
        playGame(); // This method starts the game using the custom mode settings
    }

    /**
     * Default mode for the MasterMind game.
     * This mode uses default settings: maxAttempts = 6, codeLength = 4.
     */
    private static void defaultMode() throws InterruptedException {
        // Print default mode title
        System.out.println("\nMASTERMIND SwiftBot - " + getText(6));
        System.out.println("*********************");

        // Inform the user of the default settings being used
        System.out.println(getText(29) + " " + maxAttempts + " " + getText(28) + " " + codeLength);
        Thread.sleep(1500); // Short pause to simulate game preparation

        // Start the game with the default settings
        playGame(); // This method starts the game using default mode settings
    }

/**
 * Capture a sequence of colors using the camera. (R , G , B , Y , P , O)
 * The method takes a specified number of colors as input(Code Length Amount), captures images, 
 * and determines the dominant color in each image.
 * 
 * @param numColors The Paramater Number of Colours The number of colors to capture
 * @return A string containing the sequence of color codes
 */
private static String captureColorSequence(int numColors) { // Paraneter is Number Of Colors 
    StringBuilder colorSequence = new StringBuilder(); // Stores the identified color sequence

    System.out.println(getText(36) + " " + numColors + " " + getText(37)); // Inform the user about the process

    for (int i = 0; i < numColors; i++) { // For Loop to check if the number of colours is less than the input
        try { // Try block to handle exceptions
            System.out.println(getText(36) + " " + (i + 1) + " of " + numColors + "...");
            Thread.sleep(3000); // Wait for user to prepare the color

            // Capture an image using the SwiftBot camera (API Usage)
            BufferedImage image = swiftBot.takeStill(ImageSize.SQUARE_720x720);

            if (image == null) {
                System.out.println(getText(46)); // Inform the user if the image is null
                i--; // Retry the current color
                continue;
            } else {
                // Save the captured image to a file
                try {
                    ImageIO.write(image, "png", new File("/data/home/pi/colourImage" + i + ".png")); // Save the image in a file
                } catch (Exception e) {
                    System.out.println(getText(47) + " " + e.getMessage());// Inform the user if the image cannot be saved
                }

                // Identify the dominant color in the captured image this is for the color identification
                char colorCode = identifyDominantColor(image);

                // If color identification fails, ask the user to try again
                if (colorCode == 'X') {
                    System.out.println(getText(48)); // Inform the user if the color cannot be identified
                    i--; // Retry the current color
                    continue;
                }

                // Prevent duplicate colors in the sequence
                if (colorSequence.indexOf(String.valueOf(colorCode)) != -1) {
                    System.out.println(getText(49) + " " + colorCode + ". " + getText(50));
                    i--; // Retry the current color
                    continue;
                }

                // Append/Add the identified color to the sequence
                colorSequence.append(colorCode);
                System.out.println(getText(38) + " " + colorCode); // Display the identified color to the user

                Thread.sleep(2000); // Pause before capturing the next color
            } 
        } catch (Exception e) {
            System.out.println(getText(51) + " " + e.getMessage());
            i--; // Retry in case of an exception
        }
    }

    return colorSequence.toString(); // Return the final sequence of colour codes
    
}

/**
 * Identifies the dominant color in an image by analyzing pixel values.
 * The method scans the image at intervals and determines which color appears most frequently.
 * 
 * @param image The BufferedImage to analyze
 * @return A character representing the dominant color (R, G, B, Y, P, O) or 'X' if unknown
 */
private static char identifyDominantColor(BufferedImage image) {
    // Initialize counters for different colors
    int redCount = 0, greenCount = 0, blueCount = 0;
    int yellowCount = 0, pinkCount = 0, orangeCount = 0;

    // Process every 15th pixel to improve performance while maintaining accuracy
    for (int y = 0; y < image.getHeight(); y += 15) { // Loop through the image height
        for (int x = 0; x < image.getWidth(); x += 15) { // Loop through the image width
            int rgb = image.getRGB(x, y); // Get the color of the current pixel

            // Extract red, green, and blue components from the RGB value
            int red = (rgb >> 16) & 0xFF;
            int green = (rgb >> 8) & 0xFF;
            int blue = rgb & 0xFF;

            // Determine the dominant color based on intensity thresholds
            if (red > 200 && green < 100 && blue < 100) {
                redCount++; // Red: high red, low green and blue = red
            } else if (green > 200 && red < 100 && blue < 100) {
                greenCount++; // Green: high green, low red and blue = green
            } else if (blue > 200 && red < 100 && green < 100) {
                blueCount++; // Blue: high blue, low red and green = blue
            } else if (red > 200 && green > 200 && blue < 100) {
                yellowCount++; // Yellow: high red and green, low blue = yellow
            } else if (red > 200 && blue > 150 && green > 100 && green < 180) {
                pinkCount++; // Pink: high red and blue, medium green = yellow
            } else if (red > 200 && green > 100 && green < 180 && blue < 100) {
                orangeCount++; // Orange: high red, medium green, low blue = orange
            }
        }
    }

    // Determine which color has the highest count
    int maxCount = Math.max(redCount, Math.max(greenCount, Math.max(blueCount, // Finding the maxiuim count of the colors
            Math.max(yellowCount, Math.max(pinkCount, orangeCount))))); // Using Math.max to find the maximum count

    // Return the corresponding color code based on the highest count
    if (maxCount == redCount && redCount > 0) { // If the max count is red and red count is greater than 0
        return 'R'; //The greater than zero ''> 0' is used to check if the color is present as maxCount is 0 if the color is not present (with just MaxCOunt == redCount it would only return R even if no red pixels were found)
    } else if (maxCount == greenCount && greenCount > 0) {
        return 'G';
    } else if (maxCount == blueCount && blueCount > 0) {
        return 'B';
    } else if (maxCount == yellowCount && yellowCount > 0) {
        return 'Y';
    } else if (maxCount == pinkCount && pinkCount > 0) {
        return 'P';
    } else if (maxCount == orangeCount && orangeCount > 0) {
        return 'O';
    } else {
        return 'X'; // If no dominant color is found, return 'X' (unknown)
    }
}


  /**
 * Main game logic - used by both default and custom mode
 * Handles user input, color validation, and determines win/loss conditions.
 */
private static void playGame() throws InterruptedException {
    // Generate a random sequence of colors based on the current code length
    generatedColors = generateColors(codeLength); // Takes the codelength input and generates the colors
    Scanner scanner = new Scanner(System.in); // User input

    // Display game introduction
    System.out.println("\nMASTERMIND SwiftBot");
    System.out.println("*********************");
    System.out.println(getText(30)+ " " + codeLength + " " + getText(31));
    System.out.println(getText(32) + " " + maxAttempts + " " + getText(33));

    // Testing purposes reveals the secret code
     System.out.println("(Secret code: " + generatedColors + ")");

    boolean hasWon = false; // Tracks whether the player has won

    // Loop through maximum allowed attempts
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
        System.out.println("\nMASTERMIND SwiftBot");
        System.out.println("*********************");
        System.out.println("\n " + getText(34) + " " + attempt + "/" + maxAttempts + ":"); // Checking each attempt and max attempts

        // Capture the color sequence using the camera of the SwiftBot
        System.out.println(getText(36) + " " + codeLength + " " + getText(37));
        String userGuess = captureColorSequence(codeLength); // Capture the color sequence
        System.out.println(getText(38) + " " + userGuess); // Display the captured colors to the user

        // Validate input length
        if (userGuess.length() != codeLength) {// Checks If the user input length is not equal to the code length
            System.out.println(getText(39) + " " + codeLength + " " + getText(40));
            attempt--; // Don't count invalid attempts
            continue; // Restart loop
        }

        // Check if input contains only valid colors (R, G, B, Y, P, O)
        boolean validInput = true; // Assume input is valid using boolean flag
        for (char c : userGuess.toCharArray()) {
            if ("RGBYPO".indexOf(c) == -1) { // If character is not in the valid color set
                validInput = false; // ValidInput is false if the character is not in the valid color set
                break; // Exit loop early
            }
        }

        if (!validInput) { // If the input is not valid
            System.out.println(getText(41));
            attempt--; // Don't count invalid attempts
            continue;
        }

        // Check for duplicate colors in the input (each color must be unique)
        boolean hasDuplicates = false;
        for (int i = 0; i < userGuess.length(); i++) { // Nested Loop to check for Duplicates within the User Guess
            for (int j = i + 1; j < userGuess.length(); j++) {
                if (userGuess.charAt(i) == userGuess.charAt(j)) { // This shows that the user guess has duplicates
                    hasDuplicates = true;
                    break; // Exit inner loop early if duplicate found
                }
            }
            if (hasDuplicates) {
                break; // Exit outer loop if duplicate detected
            }
        }

        if (hasDuplicates) {
            System.out.println(getText(42)); // Inform the user about duplicate colors
            attempt--; // Don't count invalid attempts
            continue;
        }

        // Compare the guess with the generated colors and provide feedback
        String result = compareWithGeneratedColors(userGuess);
        System.out.println(getText(43) + " " + result); // Display the result of the comparison

        // Check if the user has won (all correct positions are marked with '+')
        String allCorrect = "";
        for (int i = 0; i < codeLength; i++) {
           allCorrect += "+";
}
 

        if (result.equals(allCorrect)) {
            System.out.println(getText(44) + " " + attempt + " " + getText(45));
            playerScore++; // Increase player score
            hasWon = true;
            break; // Exit the game loop
        }
    }

    // If user didn't win within the allowed attempts
    if (!hasWon) {
        System.out.println("\n" + getText(8) + maxAttempts + getText(9));
        System.out.println(getText(10) + generatedColors);
        computerScore++; // Increase computer's score
    }

    // Mark game as finished
    gameInProgress = false; // Game is no longer in progress, this is here becuase the game is finished

    // Display the current scores
    displayScores();

    // Prompt user for next action
    System.out.println("\n" + getText(11));
    System.out.println("\n" + getText(12));
}


    /**
     * Compares the user's guess with the generated colors
     * 
     * @param userGuess The user's guess
     * @return A string with '+' for correct position and '-' for correct color in
     *         wrong position
     */
    private static String compareWithGeneratedColors(String userGuess) {
        // Create copies of the generated colors and user guess for manipulation
        char[] secretCode = generatedColors.toCharArray(); // Convert the generated colors to a character array because it is easier to manipilate the characters
        char[] guessCode = userGuess.toCharArray(); // Convert the user guess to a character array because it is easier to manipilate the characters
        StringBuilder result = new StringBuilder(); // Creating a stringbulider to store result of the comparison

        // Array to track which positions have been checked
        boolean[] secretUsed = new boolean[codeLength];// Boolean array to track which positions have been checked
        boolean[] guessUsed = new boolean[codeLength];

        // First loop: Find exact matches (correct position)
        for (int i = 0; i < codeLength; i++) { // Loop checks for exact matches adds '+'' to the result and sets the secretUsed and guessUsed to true
            if (guessCode[i] == secretCode[i]) {
                result.append('+');
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Second loop: Find color matches in wrong positions
        for (int i = 0; i < codeLength; i++) { // Loop checks for color matches in wrong positions adds '-' to the result and sets the secretused and guessused to true becacuse the color is in the wrong position 
            if (!guessUsed[i]) {
                for (int j = 0; j < codeLength; j++) {
                    if (!secretUsed[j] && guessCode[i] == secretCode[j]) {
                        result.append('-');
                        secretUsed[j] = true;
                        guessUsed[i] = true;
                        break;
                    }
                }
            }
        }

        // Fill remaining positions with '-' if there's no more correct colors
        while (result.length() < codeLength) {
            result.append('-'); // Changed to - to indicate no match
        }

        return result.toString();
    }
}
