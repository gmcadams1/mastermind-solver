/**
 * File Name: MasterMind.java
 * @author Gregory McAdams
 * E-mail: gmcadams1@comcast.net
 * 
 * Description: Game matrix for a single game
 **/

package mm;

import java.util.ArrayList;
import java.util.Random;

public class GameMatrix {

	private double[][] values;
	private int numVars; // Given from Player
	private ArrayList<String> colors; // Given from Player
	private ArrayList<ResultPegs> resultPegs; // Pegs for each previous turn
	private ArrayList<Code> codes; // Codes for each previous turn
	private double blackScore; 
	private double whiteScore;
	final double stdScore = 1.0;  //Arbitrary
	final double blackCoeff = 2.0;  //Gives greater weight to black scores
	final double MIN_SCORE = Double.NEGATIVE_INFINITY;  //Used to rule out possibilities, could be made to relate to numVars, colors.

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	public GameMatrix()
	{
		// Default Constructor
	}
	
	public GameMatrix(int numVars, ArrayList<String> colors)
	{
		this.numVars = numVars;
		this.colors = colors;
		this.resultPegs = new ArrayList<ResultPegs>();
		this.codes = new ArrayList<Code>();
		
		values = new double[colors.size()][numVars];
		for (int i = 0; i < colors.size(); i++)
		{
			for (int j = 0; j < numVars; j++){
				values[i][j] = 1.0;
			}
		}
	}
	
	// Feedback after a turn
	public void giveTurnInfo(ResultPegs pegs, Code code)
	{
		this.resultPegs.add(pegs);
		this.codes.add(code);
		updateMatrix();  
	}
	
	// Called from CSPPlayer, formerly "doSomething" method
	private void updateMatrix()
	{
		int lastTurnIndex = resultPegs.size() - 1;
		
		// Number of black and white pegs from most recent turn
		int numBlacks = resultPegs.get(lastTurnIndex).getNumBlack();
		int numWhites = resultPegs.get(lastTurnIndex).getNumWhite();
		generateScores(numWhites, numBlacks);
		applyScores(numWhites, numBlacks);
	}
	
	
	
	//Takes result pegs, creates black white scores
	//divide by zero
	private void generateScores(int numBlacks, int numWhites){
		
		this.blackScore = this.stdScore * this.blackCoeff * numBlacks / this.numVars;
		
		int div=((this.numVars - numBlacks)==0?1:(this.numVars - numBlacks));
		this.whiteScore = this.stdScore * ((numWhites / (div)) - (1/2));
		
	}
	
	//First checks for special cases where colors can be ruled out, entirely or at specific position
	//Then, applies scores generated for the intermediate case
	private void applyScores(int numWhites, int numBlacks){
		Code currentCode=codes.get(codes.size()-1);
		int colorNum;
		
		if(numWhites == 0 && numBlacks == 0){ //No correct colors
			boolean[] valid=new boolean[colors.size()];
			for(int i=0;i<colors.size();)
				valid[i++]=false;
			for(int i=0;i<currentCode.numVars();++i){
				colorNum = Integer.parseInt(currentCode.colorAt(i));
				colorNum--;
				valid[colorNum]=true;
			}
			for(int i=0;i<colors.size();++i){ //Rule out colors in last guess
				if(valid[i])
					for(int j=0;j<numVars;++j)
					values[i][j]=MIN_SCORE;
			}
		}  // (No need to check other special cases or apply other generated scores		)
		
		else{
			if (numBlacks == 0){ //No colors in the right spots
				for(int i=0;i<currentCode.numVars();++i){ //Remove color at specific spot
					colorNum = Integer.parseInt(currentCode.colorAt(i));
					colorNum--;
					values[colorNum][i]=MIN_SCORE;
				}
			}
			
			if(numWhites + numBlacks == numVars){  //When result pegs full, 
				boolean[] valid=new boolean[colors.size()];
				for(int i=0;i<colors.size();)
					valid[i++]=false;
				for(int i=0;i<currentCode.numVars();++i){
					colorNum = Integer.parseInt(currentCode.colorAt(i));
					colorNum--;
					valid[colorNum]=true;
					
				}
				for(int i=0;i<colors.size();++i){ // eliminate all colors not in code
					if(!valid[i])
						for(int j=0;j<numVars;++j)
						values[i][j]=MIN_SCORE;
				}
			}
			
			//Apply black scores to each color at its pos, if it's not MIN_SCORE
			for(int i=0;i<currentCode.numVars();++i){ 
				colorNum = Integer.parseInt(currentCode.colorAt(i));
				colorNum--;
				if (values[colorNum][i] != MIN_SCORE)
					values[colorNum][i]+= this.blackScore;
			}
			
			//Apply white scores to every color in code, despite pos'n:
			boolean[] valid=new boolean[colors.size()];
			for(int i=0;i<colors.size();)
				valid[i++]=false;
			for(int i=0;i<currentCode.numVars();++i){
				colorNum = Integer.parseInt(currentCode.colorAt(i));
				colorNum--;
				valid[colorNum]=true;
			}
			//think  j i revers
			for(int i=0;i<colors.size();++i){  		  //for all possible colors
				if(valid[i])						  //if the color was in the code
					for(int j=0;j<numVars;++j){		  //at every position
					if (values[i][j] != MIN_SCORE)	  //if it hasn't been ruled out already
					values[i][j] += this.whiteScore;  //add new score to currentCode score in matrix
					}
			}
		
		}
		
		
	}
	
	
	//
	public Code extractGuess(){
		Code nextGuess = new Code(numVars); //Creates code with size equal to length of the most recent code
		
		double[] Column = new double[colors.size()]; //Used to hold an altered copy of column
		Double lowestValue = 0.0;  
		
		for (int i=0; i < this.numVars; i++){  //Use this process to generate a guess for each position
			int ColumnSum = 0;
			//Generate copy of column:
			for (int j=0; j<this.colors.size(); j++){  
				if (values[j][i] != MIN_SCORE) //Don't copy MIN_SCORE colors
					Column[j] = values[j][i];
				else 
					Column[j] = 0.0; //Set MIN_SCORE colors to 0, so they won't get chosen again
				
				//Keep track of the lowest negative score (other than MIN_SCORE) if one exists:
				if (Column[j] < lowestValue && Column[j] != MIN_SCORE)
					lowestValue = Column[j];  
			}
			//Normalize scores, if there is a negative other than MIN_SCORE
			if (lowestValue < 0.0){  
				for (int j=0; j<this.colors.size(); j++){
					if(values[j][i] != MIN_SCORE)
						Column[j] = Column[j] - lowestValue;
				}
			}
			
			//Used to associate non-eliminated colors with weights
			ArrayList<Integer> indexArray = new ArrayList<Integer>();
			ArrayList<Double> weightArray = new ArrayList<Double>();
			
			for (int j=0; j<this.colors.size(); j++){ 
				
				//Check to make sure there aren't columns that will receive score of zero:
				//(If, for example, no normalizing happened, but there was a zero score, it should still have a chance.)
				if (values[j][i] != MIN_SCORE && Column[j] == 0) 
					Column[j] = 0.5; //Give small weighting, so they will still have a chance of getting picked
				
				ColumnSum += Column[j]; //Adds to sums column
				
				//We only want to assign some chance to non eliminated colors, so we make a new smaller array of weights
				//and an associated array of indices corresponding to the original positions in the matrix
				if (values[j][i] != MIN_SCORE){
					indexArray.add(j+1);
					weightArray.add(Column[j]);
				}
			}
			
			//Generate random number to be used soon
			Random generator = new Random();
			int randomNum = generator.nextInt(ColumnSum);
			
			// Make values cumulative for all those non-eliminated colors: 
			for (int j = 1; j < weightArray.size(); j++ ){  
				weightArray.set(j, weightArray.get(j) + weightArray.get(j-1));
			}
			
			// Chooses color based on random number:
			Integer colorNum = -1;
			for (int j = 0; j < weightArray.size(); j++){
				if (randomNum < weightArray.get(j)){  //Check where random value fell within partitioning of colors
					colorNum = indexArray.get(j); //Gets color number
					j = weightArray.size(); //Terminates for loop
				}
			}
	
			//Too lazy to write exception,
			//but if, for some reason
			//colorNum doesn't get set, we should know, and not generate a guess...
			if (colorNum == -1 ||colorNum==0){
				System.out.println("Something is wrong with sum, or partitioning, or random number range...");
			}
			else{
				nextGuess.addEntry(colorNum.toString());
			}	
		}
		return nextGuess;
	}
}

//Removed code:

//ArrayList<Double> ColumnSum = new ArrayList<Double>(numVars);
//...
//for (int j=0; j<this.colors.size(); j++){
//	for (int i=0; i<this.numVars; i++){
//		if (values[j][i] != Double.NEGATIVE_INFINITY){  //Don't add values of negative infinity
//			ColumnSum.set(j, (ColumnSum.get(j) + values[j][i]));
//		}
//	}
//}

////Not used yet, or ever probably...
//private void updateRow(int row, int score){
//	for(int j=0;j<numVars;j++){
//		if (score == MIN_SCORE) 
//			values[row][j] = MIN_SCORE;
//		else 
//			values[row][j] += score;
//	}
//}

