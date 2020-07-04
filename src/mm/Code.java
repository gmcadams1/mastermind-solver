/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: A valid code (ie. each variable with a domain) for each turn,
 * and for the master code.
 */

package mm;

import java.util.ArrayList;

public class Code {
        
        private ArrayList<String> values; // Color (value) for each variable, as a String
        private int numEntries; // Number of entries (variables) in a code

        /**
         * @param args
         */
        public static void main(String[] args) {
                // TODO Auto-generated method stub

        }
        
    	/**
    	* Name: Code
    	* PreCondition: None
    	* PostCondition: Copy constructor
    	* @param aCode - Code to copy
    	*/
        public Code(Code aCode)
        {
        	this.values = aCode.values;
        	this.numEntries = aCode.numEntries;
        }
        
    	/**
    	* Name: Code
    	* PreCondition: None
    	* PostCondition: Construct Code with specified number of entries; basically default constructor
    	* @param numEntries - Number of entries
    	*/
        public Code(int numEntries)
        {
                this.numEntries = numEntries;
                this.values = new ArrayList<String>(numEntries);
        }
        
    	/**
    	* Name: Code
    	* PreCondition: None
    	* PostCondition: Construct an exact given code
    	* @param values - Values to use
    	*/
        public Code(ArrayList<String> values)
        {
                this.values = values;
        }
        
    	/**
    	* Name: addEntry
    	* PreCondition: None
    	* PostCondition: Adds an entry to this Code
    	* @param entry - String color
    	*/
        public void addEntry(String entry)
        {
                if (values.size() == numEntries) {
                        System.out.println("Error: Cannot add another entry to Code");
                        return;
                }
                values.add(entry);
        }
        
    	/**
    	* Name: colorAt
    	* PreCondition: None
    	* PostCondition: Returns the color at the specified index of the Code
    	* @param index - Index at
    	*/
        public String colorAt(int index)
        {
                return values.get(index);
        }
        
    	/**
    	* Name: setColorAt
    	* PreCondition: color given is valid
    	* PostCondition: Can change a specific entry of Code
    	* 				 Used primarily by GAPlayer for mutation
    	* @param index - Index at
    	* @param color - New color
    	*/
        public void setColorAt(int index, String color)
        {
        	values.set(index, color);
        }
        
    	/**
    	* Name: numVars
    	* PreCondition: None
    	* PostCondition: Number of entries (variables)
    	* @param none
    	*/
        public int numVars()
        {
                return values.size();
        }
        
        // Prints Code
        @Override
        public String toString()
        {
                String temp = "";
                for (int i = 0; i < values.size(); i++)
                        temp += values.get(i) + " ";
                return temp;
        }
        
    	/**
    	* Name: getValues
    	* PreCondition: None
    	* PostCondition: Return all colors of Code
    	* @param none
    	*/
        public ArrayList<String> getValues()
        {
        	return this.values;
        }
        
        // For comparison
        @Override
        public boolean equals(Object arg)
        {
        	if (arg.toString().equals(this.toString()))
        		return true;
        	else
        		return false;
        }
}
