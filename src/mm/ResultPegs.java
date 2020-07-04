/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: Black and white result pegs used in game
 **/

package mm;

/**
 * @author mcadams1
 *
 */
public class ResultPegs {
	
	private int numBlack;
	private int numWhite;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	* Name: ResultPegs
	* PreCondition: None
	* PostCondition: Default constructor
	* @param none
	*/
	public ResultPegs()
	{
		// Default Constructor
	}
	
	/**
	* Name: ResultPegs
	* PreCondition: None
	* PostCondition: Constructor for result pegs
	* @param numBlack - Number of black pegs
	* @param numWhite - Number of white pegs
	*/
	public ResultPegs(int numBlack, int numWhite)
	{
		this.numBlack = numBlack;
		this.numWhite = numWhite;
	}
	
	/**
	* Name: addBlack
	* PreCondition: None
	* PostCondition: Adds a black peg
	* @param none
	*/
	public void addBlack()
	{
		this.numBlack++;
	}
	
	/**
	* Name: addWhite
	* PreCondition: None
	* PostCondition: Adds a white peg
	* @param none
	*/
	public void addWhite()
	{
		this.numWhite++;
	}
	
	/**
	* Name: getNumBlack
	* PreCondition: None
	* PostCondition: Returns number of black pegs
	* @param none
	*/
	public int getNumBlack()
	{
		return this.numBlack;
	}
	
	/**
	* Name: getNumWhite
	* PreCondition: None
	* PostCondition: Returns number of white pegs
	* @param none
	*/
	public int getNumWhite()
	{
		return this.numWhite;
	}

}
