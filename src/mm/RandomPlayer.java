/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: Implements a completely random player
 **/

package mm;

import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer extends Player {

	 private ArrayList<Code>  guessHistory;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	* Name: RandomPlayer
	* PreCondition: None
	* PostCondition: Constructor for this Player
	* @param name - Name as a String
	*/
	public RandomPlayer(String name)
	{
		super.setName(name);
		guessHistory=new ArrayList<Code>();
	}

	/**
	* Name: nextGuess
	* PreCondition: None
	* PostCondition: Returns this player's next code for a guess
	* @param none
	*/
	@Override
	public Code nextGuess() {
		// TODO Auto-generated method stub
		Random randGen = new Random();
		Code newCode;
		
		ArrayList<String> values;
		while(true)
		{values = new ArrayList<String>();
		for (int i = 0; i < super.getNumVars(); i++)
		{
			int colorIndex = randGen.nextInt((super.getColorList().size()));
			values.add(super.getColorList().get(colorIndex));
		}
		
		newCode = new Code(super.getNumVars());
		
		for (int i = 0; i < values.size(); i++)
		{
			newCode.addEntry(values.get(i));
		}
		//contains method for code
		
		boolean shouldBreak=true;//(guessHistory.size()==0?true:false);
		for(int i=0,totalequal=0;i<guessHistory.size();++i,totalequal=0)
		{
			Code itrHistory=guessHistory.get(i);
			for(int j=0;j<super.getNumVars();++j)
			if(newCode.colorAt(j).equals(itrHistory.colorAt(j)))
					totalequal++;
				
			if(totalequal==super.getNumVars())
				shouldBreak=false;
			
		}
		if(shouldBreak)
			break;
		
		}
		
		
		guessHistory.add(newCode);
		return newCode;
	}

	/**
	* Name: nextGuessResult
	* PreCondition: None
	* PostCondition: Tells this player resulting pegs from last guess
	* @param pegs - Result Pegs
	*/
	@Override
	public void nextGuessResult(ResultPegs resultPegs) {
		// TODO Auto-generated method stub
		
		// Not used here
	}

	/**
	* Name: reset
	* PreCondition: None
	* PostCondition: Tells this player current game has ended
	* @param none
	*/
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
		this.guessHistory.clear();
	}

	/**
	* Name: tellColorList
	* PreCondition: None
	* PostCondition: Tell this Player all possible colors he can choose from
	* @param colors - List of colors
	*/
	@Override
	public void tellColorList(ArrayList<String> colors) {
		// TODO Auto-generated method stub
		
		super.setColorList(colors);
	}

	/**
	* Name: tellNumVars
	* PreCondition: None
	* PostCondition: Tells this player number of entries for each Code
	* @param numVars - Number of variables (entries)
	*/
	@Override
	public void tellNumVars(int numVars) {
		// TODO Auto-generated method stub
		super.setNumVars(numVars);
	}

	/**
	* Name: tellStarting
	* PreCondition: None
	* PostCondition: Lets Player know game is about to start
	* @param none
	*/
	@Override
	public void tellStarting() {
		// TODO Auto-generated method stub

		// Not used here
	}

}
