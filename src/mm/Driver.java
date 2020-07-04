/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: Instantiates a new game, controls how many games are played,
 * and responsible for retaining info between games. *MENU BASED DRIVER*
 */

package mm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Driver {

        /**
         * @param args
         */
        public static void main(String[] args) {
                // TODO Auto-generated method stub
                
                MasterMind game; // Instance of Mastermind
                ArrayList<Code> masterCodes = null; // List of master codes user enters
                Player player = null; // Player type
                int numVars = -1; // To be specified; number of entries (variables)
                ArrayList<String> colors = new ArrayList<String>(); // All colors (domain)
                
                printWelcome();
                
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                
                /* Main Menu interface */
                
                String selection = "";
               
                // Required exception handling by BufferedReader
                try {
                        // Main menu loop; loop until user enters menu selection to quit
                        while (true)
                        {
                        		printMenu();
                                System.out.print("Enter Selection: ");
                                selection = in.readLine(); // Enter main menu selection
                                
                                // Main menu selection 1
                                if (selection.equalsIgnoreCase("1"))
                                {
                                        System.out.print("Enter Player's name: ");
                                        String name = in.readLine();
                                        printPlayerMenu();
                                        System.out.print("Enter Player type: ");
                                        String type = in.readLine();
                                        
                                        // Wants human player
                                        if (type.equalsIgnoreCase("HUMAN"))
                                                player = new HumanPlayer(name);
                                        else if (type.equalsIgnoreCase("RANDOM"))
                                        		player = new RandomPlayer(name);
                                        else if (type.equalsIgnoreCase("GA"))
                                        		player = new GAPlayer(name);
                                        	
                                        // Add more options later here if needed
                                        
                                        // Player type not defined
                                        else {
                                                System.out.println("Error: Player type not recognized.");
                                                continue;
                                        }
                                        
                                        System.out.println("Player " + name + " entered.");
                                }
                                
                                // Main menu selection 2
                                else if (selection.equalsIgnoreCase("2"))
                                {
                                        System.out.print("Enter number of entries (variables): ");
                                        
                                        // Loop until user enters a valid integer
                                        while (true)
                                        {
                                                try {
                                                	numVars = Integer.parseInt(in.readLine()); // Enter number of entries (variables) as integer
                                                    masterCodes = new ArrayList<Code>(numVars); // Instantiate master codes for use later
                                                    break;
                                                }
                                                catch (NumberFormatException e) {
                                                        System.out.println("Error: Not an integer, retrying.");
                                                }
                                        }
                                }
                                
                                // Main menu selection 3
                                else if (selection.equalsIgnoreCase("3"))
                                {
                                        printColorsMenu();
                                        System.out.print("Enter Selection: ");
                                        String choice = in.readLine();
                                        
                                        // Enter color(s)
                                        if (choice.equalsIgnoreCase("1"))
                                        {
                                                // Loop until user enters QUIT
                                                while (true)
                                                {
                                                        System.out.print("Enter new color, or QUIT to stop: ");
                                                        String newColor = in.readLine();
                                                        if (newColor.equalsIgnoreCase("QUIT"))
                                                                break;
                                                        colors.add(newColor); // Add new color
                                                }
                                        }
                                        
                                        // Delete color(s)
                                        if (choice.equalsIgnoreCase("2"))
                                        {
                                                // Loop until user enters QUIT
                                                while (true)
                                                {
                                                        System.out.print("Enter color to delete, or QUIT to stop: ");
                                                        String color = in.readLine();
                                                        if (color.equalsIgnoreCase("QUIT"))
                                                                break;
                                                        // For each color already defined
                                                        for (int i = 0; i < colors.size(); i++)
                                                        {
                                                                // If color found, remove it from color list and break
                                                                if (colors.get(i).equalsIgnoreCase(color)) {
                                                                        System.out.println("Removed Color: " + color);
                                                                        colors.remove(i);
                                                                        break;
                                                                }
                                                                // Color not found
                                                                if (i == colors.size() - 1)
                                                                        System.out.println("Color not found, did not delete.");
                                                        }       
                                                }
                                        }
                                        
                                        // Show user all current colors
                                        System.out.print("Current Colors: ");
                                        for (int i = 0; i < colors.size(); i++)
                                                System.out.print(colors.get(i) + " ");
                                        System.out.println(); // Newline
                                }
                                
                                // Main menu selection 4
                                else if (selection.equalsIgnoreCase("4"))
                                {
                                        // Specify a specific master code to use in a game
                                        // NOTE: Master code currently generated at random
                                }
                                
                                // Main menu selection 5 (Start game)
                                else if (selection.equalsIgnoreCase("5"))
                                {
                                        /* Checks before game can start */
                                        
                                        // Check to make sure player has been defined
                                        if (player == null) {
                                                System.out.println("Error: Player type not specified, cannot start.");
                                                continue;
                                        }

                                        // Check to make sure number of entries (variables) has been defined
                                        if (numVars == -1) {
                                                System.out.println("Error: Number of variables has not been specified, cannot start");
                                                continue;
                                        }
                                        
                                        // Check to make sure there are some colors
                                        if (colors.size() == 0) {
                                                System.out.println("Error: No colors specified, cannot start.");
                                                continue;
                                        }
                                        
                                        // Do not need to tell Player the number of entries (variables), and do not
                                        // need to tell Player all colors(domain), since it is all told during game time
                                        
                                        /* End checks */
                                        
                                        System.out.print("Number of games to play: ");
                                        int numGames = Integer.parseInt(in.readLine());
                                        
                                        
                                        // Number of turns or all games below
                                        int totalTurns = 0;
                                        
                                        /* Start playing n games */
                                        for (int games = 0; games < numGames; games++) {
                                   
                                        // Generate random master code
                                        Random generator = new Random();
                                        ArrayList<String> aMasterCodeString = new ArrayList<String>();
                                        
                                        // For each entry, give it a random color
                                        for (int i = 0; i < numVars; i++) {
                                                aMasterCodeString.add(colors.get(generator.nextInt(colors.size())));
                                        }
                                        
                                        // Make master code and add it to master code list
                                        Code aMasterCode = new Code(aMasterCodeString);
                                        masterCodes.add(aMasterCode);
                                        
                                        // Instantiate new game
                                        game = new MasterMind(numVars, colors, player);
                                        
                                        // Play a game; control goes to playGame method until the game is completed
                                        try {
                                                game.playGame(aMasterCode);
                                                // If game is completed successfully, give player that game state
                                                totalTurns += game.getNumTurns();
                                        }
                                        // Exception caught if a Player gives an invalid Code during the game
                                        // Terminate game and return to main menu
                                        catch (VariableMismatchException e) {
                                                System.out.println(e.getMessage());
                                                System.out.println("Quitting game and returning to menu.");
                                        }
                                        
                                        player.reset();
                                        
                                        } /* End playing n games */
                                        
                                        float averageTurns = (float)totalTurns / (float)numGames;
                                        
                                        System.out.println("\nNumber of games played: " + numGames);
                                        System.out.println("Average number of turns: " + averageTurns);
                                }
                                
                                // Menu selection 6
                                else if (selection.equalsIgnoreCase("6"))
                                        break;
                                
                                // Invalid menu selection
                                else {
                                        System.out.println("Error: Invalid Option");
                                }
                        }
                }
                
                // Required by BufferedReader
                catch (IOException e) {
                        System.out.println(e.getMessage());
                }
                System.out.println("Program ending...");
        }
        
        // Welcome screen
        public static void printWelcome()
        {
                System.out.println("Welcome to Mastermind version 1.0!");
                System.out.println("Please wait for the main menu to load.");
        }
        
        // Main menu
        public static void printMenu()
        {
                System.out.println("\nGoing to main menu...");
                wait(5); // Wait 5 seconds before printing menu
                System.out.println("Mastermind 1.0 Menu:");
                System.out.println("1. Specify player type");
                System.out.println("2. Set number of entries (variables)");
                System.out.println("3. Add/delete colors");
                System.out.println("4. Add Master Code (NOT IMPLEMENTED)");
                System.out.println("5. Play game(s)");
                System.out.println("6. Exit Program");
        }
        
        // Player submenu
        public static void printPlayerMenu()
        {
                System.out.println("\tFor Human Player, type 'HUMAN'");
                System.out.println("\tFor Random Player, type 'RANDOM'");
                System.out.println("\tFor a GA Player, type 'GA'");
        }
        
        // Colors submenu
        public static void printColorsMenu()
        {
                System.out.println("1. Enter colors");
                System.out.println("2. Delete colors");
                System.out.println("3. Display current colors");
        }
        
        // Wait n seconds
        public static void wait (int n)
        {
        	long t0, t1;
        
        	t0 = System.currentTimeMillis();
        
        	do {
        		t1 = System.currentTimeMillis();
        	}
        	while (t1 - t0 < 1000);
        }
}