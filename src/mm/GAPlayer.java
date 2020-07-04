/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: Implements advanced genetic algorithm player
 **/

package mm;

import java.util.ArrayList; 
import java.util.Collections;
import java.util.Random;

public class GAPlayer extends Player {
	
	// Allows sorting of fitness function on each Code in random population
	// but keeps track of index to which Code it refers to in randomPop
	public class FitnessForPop implements Comparable<FitnessForPop> {
		int indexOfCode; // Keeps index of Code in randomPop
		float fitnessVal; // Fitness value for this Code
		float normalizedFitness; // Between [0,1]
		
		public FitnessForPop(int indexOfCode, float fitnessVal) {
			this.indexOfCode = indexOfCode;
			this.fitnessVal = fitnessVal;
		}
		
		// This is the index in randomPop in which Code lies
		public int getIndex() {
			return this.indexOfCode;
		}
		
		public float getFitness() {
			return this.fitnessVal;
		}
		
		public float getNormalizedFitness() {
			return this.normalizedFitness;
		}
		
		public void setNormalizedFitness(float normalizedFitness) {
			this.normalizedFitness = normalizedFitness;
		}

		public int compareTo(FitnessForPop arg) 
		{	
			if (arg.normalizedFitness > this.normalizedFitness)
				return 1;
			else if (arg.normalizedFitness < this.normalizedFitness)
				return -1;
			else
				return 0;
		}
	}
	
	/*
	 * Use these three constants to vary speed/performance
	 * 	MAX_GEN, MAX_POP, and MAX_SIZE	
	 * 		Lower values = faster but worse performance
	 *  	Higher values = slower but better performance
	 *  REMOVE_DUPLICATES
	 *  	If true removes all duplicates when randomly generating population.
	 *  	When turned on this drastically hurts run time but has better guessing.
	 *  	Turn off when scaling huge # of pegs/colors.
	 */
	public static final int MAX_GEN = 150; // Maximum number of times to generate a new random population per turn
	public static final int MAX_POP = 150; // Maximum number of Codes to generate per new random population
	public static final int MAX_SIZE = 70; // Maximum number of eligible codes (children) to choose from
	public static final boolean REMOVE_DUPLICATES = false; // Removes duplicates from random population
	
	public static final boolean DO_BIAS = false; // Checks for biases
	
	private ArrayList<Code> eligibleChildren; // Eligible Codes to be chosen for the guess
	private ArrayList<Code> randomPop; // Generated random population
	private ArrayList<FitnessForPop> fitnessValsForPop; // Fitness values for the random population
	private int turnNum; // Turn number
	private Random rand; // Pseudo-random number generator
	private ArrayList<Code> lastGuesses; // All previous guesses for a game
	private ArrayList<ResultPegs> lastGuessesResults; // Result pegs for each previous guess
	private float totalFitness; // Total fitness for each random population
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	/**
	* Name: GAPlayer
	* PreCondition: None
	* PostCondition: Constructor for GAPlayer
	* @param name - Name given to player
	*/
	public GAPlayer(String name)
	{
		this.setName(name);
		this.turnNum = 1;
		this.rand = new Random();
		this.eligibleChildren = new ArrayList<Code>(MAX_SIZE * 2);
		this.randomPop = new ArrayList<Code>(MAX_POP * 2);
		this.lastGuesses = new ArrayList<Code>(100);
		this.lastGuessesResults = new ArrayList<ResultPegs>(100);
		this.fitnessValsForPop = new ArrayList<FitnessForPop>(MAX_POP * 2);
		this.totalFitness = 0;
	}

	/**
	* Name: nextGuess
	* PreCondition: None
	* PostCondition: Returns this player's next code for a guess
	* @param none
	*/
	public Code nextGuess() 
	{
		// First turn
		if (turnNum == 1)
		{
			Code firstCode  = new Code(super.getNumVars());
			
			// If number of entries exceeds number of colors, choose randomly
			if (super.getColorList().size() < super.getNumVars())
			{
				// Random code
				for (int i = 0; i < super.getNumVars(); i++)
				{
					firstCode.addEntry(super.getColorList().get(rand.nextInt(super.getColorList().size())));
				}
			}
			
			// First half (floor) of entries have same first color; after that is subsequent
			// (ie. (1,1,2,3)
			else 
			{
				for (int i = 0; i < super.getNumVars(); i++)
					if (i < super.getNumVars() / 2)
						firstCode.addEntry(super.getColorList().get(0));
					else
						firstCode.addEntry(super.getColorList().get(i));
			}
			
			this.lastGuesses.add(firstCode); // Store last guess before returning
			return firstCode;
		}
		
		// Use new random population for each turn
		this.eligibleChildren.clear();
		
		// This while loop ensures at least 1 guess will be good
		while (eligibleChildren.size() < 1)
		{
			for (int h = 0; h <= MAX_GEN && eligibleChildren.size() <= MAX_SIZE; h++)
			{
				// Generate new random population each iteration
				// All eligible parents from previous random pop. will be in eligibleParents
				this.randomPop.clear();
				this.fitnessValsForPop.clear();
				this.totalFitness = 0;
				generateRandomPop();
				// Generate new population using crossover, mutation, inversion and permutation
				generateEligibleChildren(); // These codes are offspring of the eligible parents
			}
		}
		
		// Choose Code at random from eligible children
		int newCodeIndex = 0;
		boolean validGuess = false; // Use to make sure no duplicate guesses
		
		while (validGuess == false)
		{
			// Randomly get new code from eligible children
			newCodeIndex = rand.nextInt(eligibleChildren.size());
			
			for (int i = 0; i < this.lastGuesses.size(); i++)
			{
				// Chosen guess is same as a previous guess; restart loop
				if (eligibleChildren.get(newCodeIndex).toString().equals(lastGuesses.get(i).toString()))
					break;
				// At this point code is unique to previous guesses, since no break occurred above
				if (i == this.lastGuesses.size() - 1)
					validGuess = true;
			}
		}
		
		this.lastGuesses.add(eligibleChildren.get(newCodeIndex));
		return eligibleChildren.get(newCodeIndex);
	}
	
	/**
	* Name: doMutation
	* PreCondition: None
	* PostCondition: 3% chance of mutation on child
	* @param aChild - Child Code
	*/
	private Code doMutation(Code aChild)
	{
		// Number is in the range 1-100%
		int doMutation = rand.nextInt(100) + 1;
		
		// 3% chance mutation
		if (doMutation <= 3)
		{
			int randIndex = rand.nextInt(super.getNumVars()); // Random mutation spot
			String randColor = super.getColorList().get(rand.nextInt(super.getColorList().size())); // Get random color
			
			aChild.setColorAt(randIndex, randColor); // Set color at index to new random color
		}
		
		return aChild;
	}
	
	/**
	* Name: doPermutation
	* PreCondition: None
	* PostCondition: 3% chance of permutation on child
	* @param aChild - Child Code
	*/
	private Code doPermutation(Code aChild)
	{
		int doPermutation = rand.nextInt(100) + 1;
		
		// 3% chance permutation
		// (ie. choose two entries and exchange colors)
		if (doPermutation <= 3)
		{
			int xSwitch = 0;
			int ySwitch = 0;
			
			while (xSwitch == ySwitch)
			{
				xSwitch = rand.nextInt(aChild.numVars());
				ySwitch = rand.nextInt(aChild.numVars());
			}
			
			String tempColor = aChild.colorAt(xSwitch);
			aChild.setColorAt(xSwitch, aChild.colorAt(ySwitch));
			aChild.setColorAt(ySwitch, tempColor);
		}
		
		return aChild;
	}
	
	/**
	* Name: doInversion
	* PreCondition: None
	* PostCondition: 2% chance of inversion on child
	* @param aChild - Child Code
	*/
	private Code doInversion(Code aChild)
	{
		int doInversion = rand.nextInt(100) + 1;
		
		// 2% chance of inversion
		// (ie. choose two entries; reverse each entry between them)
		if (doInversion <= 2)
		{
			int inversionPoint1 = rand.nextInt(aChild.numVars());
			int inversionPoint2 = rand.nextInt(aChild.numVars());
			
			if (inversionPoint1 > inversionPoint2) {
				int temp = inversionPoint1;
				inversionPoint1 = inversionPoint2;
				inversionPoint2 = temp;
			}
			
			for (int i = inversionPoint1; i < inversionPoint2 / 2; i++)
			{
				String temp = aChild.colorAt(i+1);
				aChild.setColorAt(i+1, aChild.colorAt(inversionPoint2-1));
				aChild.setColorAt(inversionPoint2-1, temp);
				inversionPoint2--;
			}
		}		
		
		return aChild;
	}
	
	/**
	* Name: generateRandomPop
	* PreCondition: None
	* PostCondition: Make a random population with all colors (maybe duplicates)
	* @param none
	*/
	private void generateRandomPop()
	{
		//System.out.println("In generateRandomPop()");
		// Generate MAX_POP new random individuals
		for (int i = 0; i < MAX_POP; i++)
		{
			Code newCode = new Code(super.getNumVars());
			// For each entry, add a random color
			for (int j = 0; j < super.getNumVars(); j++)
			{
				int nextRand = rand.nextInt(super.getColorList().size());
				newCode.addEntry(super.getColorList().get(nextRand));
			}
			
			if (REMOVE_DUPLICATES == true)
			{
				if (this.randomPop.contains(newCode)) {
					i--;
					continue;
				}
				else {
					this.randomPop.add(newCode);
					getFitness(randomPop.size() - 1);
				}
			}
			else {
				this.randomPop.add(newCode);
				getFitness(randomPop.size() - 1);
			}
		}
	}
	
	/**
	* Name: getFitness
	* PreCondition: None
	* PostCondition: Find fitness value of each new random code generated
	*				 Index refers to the index in randomPop List
	* @param index - Index of list
	*/
	private void getFitness(int index)
	{
		//System.out.println("In generateFitness()");
		
		// Calculate fitness for each Code in random population
		float fitness = fitnessFunc(randomPop.get(index), turnNum);
		//System.out.println("Fitness for: " + randomPop.get(index).toString() + " is "+ fitness);
		this.totalFitness += fitness;
		this.fitnessValsForPop.add(new FitnessForPop(index, fitness));
	}
	
	/**
	* Name: generateEligibleChildren
	* PreCondition: None
	* PostCondition: Offspring from eligible parents; these will be chosen as next guess
	*				 Check to make sure an eligible child was not previously guessed
	* @param none
	*/
	private void generateEligibleChildren()
	{		
		//System.out.println("In generateEligibleChildren()");
		
		//System.out.println("Total fitness: " + totalFitness);
		
		// Normalize every fitness value in random population to be in [0,1]
		// NOTE: Do this in compareTo to avoid excess computation
		//  	 Can't, doesn't sort properly
		for (int j = 0; j < this.randomPop.size(); j++)
		{
			float normalizedFitness = fitnessValsForPop.get(j).getFitness() / totalFitness;
			this.fitnessValsForPop.get(j).setNormalizedFitness(normalizedFitness);
		}
		
		// Choose eligible parents using fitness
		Collections.sort(this.fitnessValsForPop); // Sort on fitness first
		
		//System.out.println("Size of randomPop: " + randomPop.size());
		
		// For each eligible parent; must be at least 2 parents
		// POSSIBLY allow asexual reproduction by requiring only one parent - YES
		for (int i = 0; i < this.randomPop.size(); i++)
		{
			
			float choose = rand.nextFloat();
			//System.out.println();
			//System.out.println("Choose random number is: " + choose);
			
			// Two parents to mate
			ArrayList<Code> twoParents = new ArrayList<Code>(2);
			
			for (int j = 0; j < fitnessValsForPop.size(); j++)
			{
				//System.out.println("Fitness value at " + j + " is " + fitnessValsForPop.get(j).getNormalizedFitness() 
						//+ " for Code: " + this.randomPop.get(fitnessValsForPop.get(j).getIndex()) );
				int floatCmp = Float.compare(choose, fitnessValsForPop.get(j).getNormalizedFitness());
				if (floatCmp <= 0)
				{
					//System.out.println("Adding fit parent: " + randomPop.get(fitnessValsForPop.get(j).getIndex()));
					//System.out.println(choose + " <= " + fitnessValsForPop.get(j).getNormalizedFitness());
					twoParents.add(this.randomPop.get(fitnessValsForPop.get(j).getIndex()));
					
					if (twoParents.size() == 2)
						break;
					choose = rand.nextFloat();
					//System.out.println("New choose after adding fit parent: " + choose);
					j = 0; // Reset loop
					continue;
				}

				choose = choose - fitnessValsForPop.get(j).getNormalizedFitness();
				//System.out.println("New choose random number: " + choose);
			}
			
			//for (int t = 0; t < twoParents.size(); t++)
				//System.out.println("TwoParents at " + t + " is " + twoParents.get(t).toString());
			
			Code xParent = null;
			Code yParent = null;
			
			if (twoParents.size() == 2) {
				xParent = twoParents.get(0);
				yParent = twoParents.get(1);
			}
			else if (twoParents.size() == 1){
				xParent = twoParents.get(0);
				yParent = twoParents.get(0);
			}
			else {
				// Get 2 random Codes from random population
				int chooseParent1 = 0;
				int chooseParent2 = 0;
				
				while (chooseParent1 == chooseParent2) {
					chooseParent1 = rand.nextInt(randomPop.size());
					chooseParent2 = rand.nextInt(randomPop.size());
				}
				
				xParent = this.randomPop.get(chooseParent1);
				yParent = this.randomPop.get(chooseParent2);
			}
			
			/*// Prevent parent mating with itself
			while (xParent.toString().equals(yParent.toString()))
			{
				xIndex = rand.nextInt(this.eligibleParents.size());
				xParent = this.eligibleParents.get(xIndex);
				yIndex = rand.nextInt(this.eligibleParents.size());
				yParent = this.eligibleParents.get(yIndex);
			}*/
				
			
			// 2-point crossover if true, 1-point otherwise
			int crossOverType = rand.nextInt(2);
			
			//System.out.println("xParent: " + xParent.toString());
			//System.out.println("yParent: " + yParent.toString());
			
			/*
			 *  Reproduce
			 */
			Code child = new Code(super.getNumVars());
			
			// 1-point crossover
			if (crossOverType == 0)
			{
				//System.out.println("1-point crossover");
				int spliceIndex = 0;
				
				// Prevent child being exactly like parent
				while (spliceIndex == 0)
					spliceIndex = rand.nextInt(super.getNumVars());
				
				//System.out.println("Splice at: " + spliceIndex);
				// Get first half of Code after splicing
				int useParentFirst = rand.nextInt(2); // If 0 use xParent, if 1 use yParent
				for (int j = 0; j < spliceIndex; j++)
				{
					if (useParentFirst == 0)
						child.addEntry(xParent.colorAt(j));
					else
						child.addEntry(yParent.colorAt(j));
				}
				
				// Get second half of Code after splicing
				int useParentSecond = Math.abs(useParentFirst - 1); // Use the other parent
				for (int j = spliceIndex; j < super.getNumVars(); j++)
				{
					if (useParentSecond == 0)
						child.addEntry(xParent.colorAt(j));
					else
						child.addEntry(yParent.colorAt(j));
				}
				
			}
			// 2-point crossover
			else
			{
				//System.out.println("2-point crossover");
				int index1 = 0;
				int index2 = 0;
				// Ensure crossover point is not the same
				while (index1 == index2) {
					index1 = rand.nextInt(super.getNumVars());
					index2 = rand.nextInt(super.getNumVars());
				}

				//System.out.println("Splice from " + index1 + " to " + index2);
				
				// Make index1 refer to first crossover point
				if (index2 < index1) 
				{
					int temp = index1;
					index1 = index2;
					index2 = temp;
				}
				
				// First third of code
				int useFirstAndThirdParent = rand.nextInt(2);
				for (int j = 0; j < index1; j++)
				{
					if (useFirstAndThirdParent == 0)
						child.addEntry(xParent.colorAt(j));
					else
						child.addEntry(yParent.colorAt(j));
				}
				
				// Crossed over middle code; use other parent not used for first and third
				int useSecondParent = Math.abs(useFirstAndThirdParent - 1);
				for (int j = index1; j < index2; j++)
				{
					if (useSecondParent == 0)
						child.addEntry(xParent.colorAt(j));
					else
						child.addEntry(yParent.colorAt(j));
				}
				
				// Last third of code; use same parent as first third
				for (int j = index2; j < super.getNumVars(); j++)
				{
					if (useFirstAndThirdParent == 0)
						child.addEntry(xParent.colorAt(j));
					else
						child.addEntry(yParent.colorAt(j));
				}
			}
			//System.out.println("Child produced: " + child.toString());
			child = doMutation(child);
			child = doPermutation(child);
			child = doInversion(child);
			
			// Check eligiblity of a child
			if (checkEligibility(child) == 0)
				this.eligibleChildren.add(child); // Add child to be considered for next guess
		}
	}
	
	/**
	* Name: checkEligibility
	* PreCondition: None
	* PostCondition: Difference b/t black and white pegs for eligibility
	* @param aChild - Child Code
	*/
	private int checkEligibility(Code aChild)
	{
		int a = 1; // Constant of first term
		int firstTerm = 0;
		int secondTerm = 0;
		
		// For each previous guess
		for (int i = 0; i < this.lastGuesses.size(); i++)
		{
			// Calculate difference of black/white pins b/t treating 
			// each previous guess as if it was the secret code and
			// the actual black/white result pegs from that guess
			Code masterCode = new Code(lastGuesses.get(i));			
			ResultPegs result = getResultPegs(aChild, masterCode);
			ResultPegs pastGuess = this.lastGuessesResults.get(i);
			
			firstTerm += Math.abs(result.getNumBlack() - pastGuess.getNumBlack());
			secondTerm += Math.abs(result.getNumWhite() - pastGuess.getNumWhite());
		}
		return a*firstTerm + secondTerm;
	}
	
	/**
	* Name: checkEligibility
	* PreCondition: None
	* PostCondition: Implements fitness function as described in Berghman (2009)
	* @param individual - Individual Code
	* @param turnNum - Current turn number
	*/
	private int fitnessFunc(Code individual, int turnNum)
	{
		int a = 1; // Constant of first term
		int b = 2; // Constant of last term
		int firstTerm = 0;
		int secondTerm = 0;
		int thirdTerm = super.getNumVars() * (turnNum - 1);
		
		// For each previous guess
		for (int i = 0; i < this.lastGuesses.size(); i++)
		{
			// Calculate difference of black/white pins b/t treating 
			// each previous guess as if it was the secret code and
			// the actual black/white result pegs from that guess
			Code masterCode = new Code(lastGuesses.get(i));			
			ResultPegs result = getResultPegs(individual, masterCode);
			ResultPegs pastGuess = this.lastGuessesResults.get(i);
			
			firstTerm += Math.abs(result.getNumBlack() - pastGuess.getNumBlack());
			secondTerm += Math.abs(result.getNumWhite() - pastGuess.getNumWhite());
		}
		
		// In case bias number is invalid
		// Same as when biasNumber == 0
		return a*firstTerm + secondTerm + b*thirdTerm;
	}
	
	/**
	* Name: getResultPegs
	* PreCondition: None
	* PostCondition: Returns number of black/white pegs from comparison of two Codes
	* @param aCode - Code to check
	* @param masterCode - A secret (master) code
	*/
	private ResultPegs getResultPegs(Code aCode, Code masterCode)
	{
        int numBlack = 0; // Number of black pegs
        int numWhite = 0; // Number of white pegs
        
        ArrayList<Integer> usedIndicesMaster = new ArrayList<Integer>(super.getNumVars());
        ArrayList<Integer> usedIndicesCode = new ArrayList<Integer>(super.getNumVars());
        
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
        
        //System.out.println();
        
        // System.out.println("NumVars masterCode: " + masterCode.numVars() + " NumVars aCode: " + aCode.numVars());
        
        // Now check for whites
        for (int i = 0; i < masterCode.numVars(); i++)
        {
            /*System.out.print("Used master indices: ");
            for (int p = 0; p < usedIndicesMaster.size(); p++)
            	System.out.print(usedIndicesMaster.get(p) + " ");
            System.out.println();
            System.out.print("Used aCode indices: ");
            for (int p = 0; p < usedIndicesMaster.size(); p++)
            	System.out.print(usedIndicesCode.get(p) + " ");
            System.out.println();*/
        	
        	if (usedIndicesMaster.contains(i))
        			continue;
            
        	for (int j = 0; j < aCode.numVars(); j++)
        	{
        		if (usedIndicesCode.contains(j))
        			continue;
        		
        		//System.out.println("Comparing masterCode: " + masterCode.colorAt(i) + " with aCode: " + aCode.colorAt(j));
                if (masterCode.colorAt(i).equalsIgnoreCase(aCode.colorAt(j)))
                {
                	//System.out.println("masterCode at: " + i + " = " + "aCode at: " + j);
                	if (!usedIndicesMaster.contains(i) && !usedIndicesCode.contains(j)) {
                        numWhite++;
                        usedIndicesMaster.add(i);
                        usedIndicesCode.add(j);
                        break;
                	}
                }
        	}
        }
        
        ResultPegs out = new ResultPegs(numBlack, numWhite);
        
        /*System.out.println("mCode: " + masterCode.toString());
        System.out.println("aCode: " + aCode.toString()); 
        System.out.println("NumBlack: " + numBlack + " numWhite: " + numWhite);*/
        
        return out;
	}

	/**
	* Name: nextGuessResult
	* PreCondition: None
	* PostCondition: Tells this player resulting pegs from last guess
	* @param pegs - Result Pegs
	*/
	public void nextGuessResult(ResultPegs resultPegs) 
	{
		this.lastGuessesResults.add(resultPegs);
		this.turnNum++;
	}

	/**
	* Name: reset
	* PreCondition: None
	* PostCondition: Tells this player current game has ended
	* @param none
	*/
	public void reset() 
	{
		this.turnNum = 1;
		this.lastGuessesResults.clear();
		this.lastGuesses.clear();
		this.eligibleChildren.clear();
		this.randomPop.clear();
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
	* Name: tellStarting
	* PreCondition: None
	* PostCondition: Lets Player know game is about to start
	* @param none
	*/
	public void tellStarting() 
	{
		
	}
}