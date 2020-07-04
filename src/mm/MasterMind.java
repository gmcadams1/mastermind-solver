/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: An instance of the Mastermind game; game driver  
 **/

package mm;

import java.util.ArrayList;

public class MasterMind {
        
        private Code masterCode; // Code to crack
        private Player player; // Some player, a base class of Player
        private ArrayList<String> colors; // All colors (domain)
        private int numVariables; // Number of entries (variables)
        
        // Each index 'i' in these refers to the same turn
        private ArrayList<Code> codes; // Set of codes for each turn
        private ArrayList<ResultPegs> pegs; // Result pegs for each turn
        
        private int numTurns; // Number of turns in this game

        /**
         * @param args
         */
        public static void main(String[] args) {
                // TODO Auto-generated method stub

        }
        
    	/**
    	* Name: MasterMind
    	* PreCondition: None
    	* PostCondition: Default constructor for game driver
    	* @param none
    	*/
        public MasterMind()
        {
                // Default constructor
        }
        
    	/**
    	* Name: MasterMind
    	* PreCondition: None
    	* PostCondition: Constructor for game driver
    	* @param numVars - Number of entries in each Code
    	* @param colors - Color list
    	* @param player - A Player to play the game
    	*/
        public MasterMind(int numVars, ArrayList<String> colors, Player player)
        {
                this.numVariables = numVars;
                this.codes = new ArrayList<Code>(numVars);
                this.colors = colors;
                this.player = player;
                this.pegs = new ArrayList<ResultPegs>();
                this.numTurns = 1;
        }
        
    	/**
    	* Name: playGame
    	* PreCondition: None
    	* PostCondition: Play a Mastermind game
    	* @param masterCode - Given secret code to play with
    	* @throws VariableMismatchException - If number of entries is inconsistent, quit game
    	*/
        public void playGame(Code masterCode) throws VariableMismatchException
        {
                // Check to make sure the master code is consistent (ie. # of variables is valid)
                if (masterCode.numVars() == numVariables)
                        this.masterCode = masterCode;
                else {
                        throw new VariableMismatchException("Error: Specified number of variables does not match master code.");
                }
                
                // If master code is valid, tell player entries(variables)/color specifications for this game
                player.tellNumVars(this.numVariables);
                player.tellColorList(colors);
                
                // Tells Player the game has been initialized and is about to start
                player.tellStarting();
                
                boolean codeCracked = false;
                boolean forfeit = false; // Ends after 1000 turns
                //int numTurns = 1; // Number of turns counter
                
                // While code is not solved
                while (codeCracked == false)
                {
                        System.out.println("\nTurn #" + numTurns);
                        Code nextCode = this.player.nextGuess(); // Next guess from a Player
                        if (nextCode.numVars() != this.numVariables) {
                                throw new VariableMismatchException("Error: Player's # of variables is inconsistent.");
                        }
                        System.out.println("Player " + player.getName() + " guessed: " + nextCode.toString());
                        this.codes.add(nextCode); // Store guess of Player
                        codeCracked = checkCode(nextCode); // Check to see if code
                        
                        // If code is cracked, don't increment number of turns again
                        if (codeCracked == false)
                                numTurns++;
                        //if (numTurns > 10000) {
                        		//forfeit = true;
                        		//break;
                       // }
                }
                
                if (forfeit == true) {
                	System.out.println("Too many turns taken...game has been forfeited");
                	System.out.println("The Master Code is: " + this.masterCode.toString());
                }
                else {
                	System.out.println("This game took " + numTurns + " turns.");
                }
        }
        
    	/**
    	* Name: checkCode
    	* PreCondition: None
    	* PostCondition: Check a code with master code, and return true if code is cracked
    	* @param aCode - Code to check
    	*/
        private boolean checkCode(Code aCode)
        {               
            int numBlack = 0; // Number of black pegs
            int numWhite = 0; // Number of white pegs
            
            ArrayList<Integer> usedIndicesMaster = new ArrayList<Integer>();
            ArrayList<Integer> usedIndicesCode = new ArrayList<Integer>();
            
            // For each entry in the master code, check for blacks first
            for (int i = 0; i < masterCode.numVars(); i++)
            {
                    // If master code's color is the same as Player's guess, inc. black pegs
                    if (masterCode.colorAt(i).equalsIgnoreCase((aCode.colorAt(i))))
                    {
                    		if (!usedIndicesMaster.contains(i)) {
                    			numBlack++;
                    			usedIndicesMaster.add(i);
                    			usedIndicesCode.add(i);
                    		}
                    }
            }
            
            // At here, the master code indices were mapped one-to-one to the same given code indices
            
            // Now check for whites
            for (int i = 0; i < masterCode.numVars(); i++)
            {      	
            	if (usedIndicesMaster.contains(i))
            			continue;
                
            	for (int j = 0; j < aCode.numVars(); j++)
            	{
            		if (usedIndicesCode.contains(j))
            			continue;
            		
                    if (masterCode.colorAt(i).equalsIgnoreCase(aCode.colorAt(j)))
                    {
                    	if (!usedIndicesMaster.contains(i) && !usedIndicesCode.contains(j)) {
                            numWhite++;
                            usedIndicesMaster.add(i);
                            usedIndicesCode.add(j);
                            break;
                    	}
                    }
            	}
            }
                
            pegs.add(new ResultPegs(numBlack, numWhite));
                
            // If number of black pegs = number of variables, code cracked
            if (numBlack == masterCode.numVars())
            {
            	System.out.println("\nCode cracked! Winner!");
            	player.nextGuessResult(new ResultPegs(numBlack, numWhite)); // Give # pegs for guess to Player
            	System.out.println("Master code is: " + masterCode.toString());
            	return true;
            }
                
            // Else, not cracked; continue on with game
            else
            {
            	System.out.println("Code not solved!");
            	System.out.println("Black Pegs: " + numBlack);
            	System.out.println("White Pegs: " + numWhite);
            	player.nextGuessResult(new ResultPegs(numBlack, numWhite)); // Give # pegs for guess to Player
            	return false;
            }
        }
        
        /* Use these methods below to retrieve info about this game after the game has been completed */
        
    	/**
    	* Name: getNumTurns
    	* PreCondition: None
    	* PostCondition: Current turn number
    	* @param none
    	*/
        public int getNumTurns()
        {
        	return this.numTurns;
        }
}