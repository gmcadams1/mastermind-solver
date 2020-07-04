/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: All Players extend from this abstract class
 **/

package mm;

import java.util.ArrayList;

public abstract class Player {

        private String name; // Name
        
        // These are constant for an instance of a Player
        // Otherwise it could lead to inconsistent guesses, etc.
        private int numVars; // Number of entries (variables)
        private ArrayList<String> colors; // All colors (domain) 
        
        public abstract Code nextGuess(); // Gives next guess
        // Tells Player number of black and white pegs resulting from last guess
        public abstract void nextGuessResult(ResultPegs resultPegs);
        public abstract void reset(); // Reset for next game
        // Tell this Player the number of entries (variables)
        public abstract void tellNumVars(int numVars);  
        // Tell this Player all possible colors he can choose from
        public abstract void tellColorList(ArrayList<String> colors);
        // Lets this Player know the game is about to start
        public abstract void tellStarting();
        
        /**
         * @param args
         */
        public static void main(String[] args) {
                // TODO Auto-generated method stub

        }
        
    	/**
    	* Name: setName
    	* PreCondition: None
    	* PostCondition: Sets this player's name
    	* @param name - name as a String
    	*/
        public void setName(String name) 
        {
                this.name = name;
        }
        
    	/**
    	* Name: getName
    	* PreCondition: None
    	* PostCondition: Gets this player's name
    	* @param none
    	*/
        public String getName() 
        {
        	return name;
        }
        
    	/**
    	* Name: setName
    	* PreCondition: None
    	* PostCondition: Sets number of entries in a code
    	* @param numVars - Number of variables (entries)
    	*/
        protected void setNumVars(int numVars)
        {
        	this.numVars = numVars;
        }
        
    	/**
    	* Name: setName
    	* PreCondition: None
    	* PostCondition: Sets this player's known list of colors
    	* @param colors - All colors as Strings
    	*/
        protected void setColorList(ArrayList<String> colors)
        {
        	this.colors = colors;
        }
        
    	/**
    	* Name: getColorList
    	* PreCondition: None
    	* PostCondition: Get what this Player knows as the list of colors (for child classes)
    	* @param none
    	*/
        public ArrayList<String> getColorList()
        {
        	return colors;
        }
        
    	/**
    	* Name: getNumVars
    	* PreCondition: None
    	* PostCondition: Get what this Player knows as the number of entries (variables)
    	* @param none
    	*/
        public int getNumVars()
        {
        	return numVars;
        }
}