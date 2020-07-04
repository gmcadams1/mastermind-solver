/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: Interface for a human player
 */

package mm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HumanPlayer extends Player {

        /**
         * @param args
         */
        public static void main(String[] args) {
                // TODO Auto-generated method stub

        }
        
    	/**
    	* Name: HumanPlayer
    	* PreCondition: None
    	* PostCondition: Constructor for this Player
    	* @param name - Name as a String
    	*/
        public HumanPlayer(String name)
        {
                super.setName(name);
        }
        
    	/**
    	* Name: nextGuess
    	* PreCondition: None
    	* PostCondition: Returns this player's next code for a guess
    	* @param none
    	*/
        public Code nextGuess()
        {
                ArrayList<String> codeString = new ArrayList<String>(); // Store user input
                ArrayList<String> colors = super.getColorList(); // All possible colors (domain)
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                
                System.out.print("Possible colors: ");
                
                // Remind user of all possible colors to choose from
                for (int i = 0; i < colors.size(); i++)
                        System.out.print(colors.get(i)+ " ");
                
                System.out.println(); // Newline
                System.out.print("Enter code, separated with spaces: ");
                String input = null;
                
                // Get Code from user, hopefully separated with spaces
                try {
                        input = in.readLine();
                } catch (IOException e) {
                        System.out.println(e.getMessage());
                }
                
                String code = new String();
                code = "";
                
                // For each code (ie. each String separated with spaces) in input
                for (int i = 0; i < input.length(); i++)
                {
                        // If space found, add the entry to codeString list,
                        // reset 'code' and continue with rest
                        if (input.charAt(i) == ' ') {
                                codeString.add(code);
                                code = new String();
                                continue;
                        }
                        // Add next character to current code
                        code += input.charAt(i);
                }
                
                // Special case for last entry
                codeString.add(code);
                
                return new Code(codeString);
        }
        
    	/**
    	* Name: nextGuessResult
    	* PreCondition: None
    	* PostCondition: Tells this player resulting pegs from last guess
    	* @param pegs - Result Pegs
    	*/
        public void nextGuessResult(ResultPegs pegs)
        {
        	// Not used in Human Player
        }
        
    	/**
    	* Name: reset
    	* PreCondition: None
    	* PostCondition: Tells this player current game has ended
    	* @param none
    	*/
        public void reset()
        {
        	// Not used in Human Player
        }
        
    	/**
    	* Name: tellStarting
    	* PreCondition: None
    	* PostCondition: Lets Player know game is about to start
    	* @param none
    	*/
        public void tellStarting()
        {
        	// Not used in Human Player
        }
        
    	/**
    	* Name: tellNumVars
    	* PreCondition: None
    	* PostCondition: Tells this player number of entries for each Code
    	* @param numVars - Number of variables (entries)
    	*/
        public void tellNumVars(int numVars)
        {
                super.setNumVars(numVars);
        }
        
    	/**
    	* Name: tellColorList
    	* PreCondition: None
    	* PostCondition: Tell this Player all possible colors he can choose from
    	* @param colors - List of colors
    	*/
        public void tellColorList(ArrayList<String> colors)
        {
                super.setColorList(colors);
        }
}