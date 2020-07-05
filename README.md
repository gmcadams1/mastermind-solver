# Mastermind Solver
Efficiently solves any Mastermind puzzle using a genetic algorithm described in the reference literature pdf.

Also includes a completely random player for comparison, and an interactive gameplay mode so the user can play the game themselves.

## Running
Run the main method in class Driver to begin the program.  You will first be presented with the following menu:
```
Welcome to Mastermind version 1.0!
Please wait for the main menu to load.

Going to main menu...
Mastermind 1.0 Menu:
1. Specify player type
2. Set number of entries (variables)
3. Add/delete colors
4. Add Master Code (NOT IMPLEMENTED)
5. Play game(s)
6. Exit Program
Enter Selection:
```
First, enter selection 1 to specify the player name and type:
```
Enter Selection: 1
Enter Player's name: Greg
	For Human Player, type 'HUMAN'
	For Random Player, type 'RANDOM'
	For a GA Player, type 'GA'
Enter Player type: 
```
If type HUMAN entered, the game will be interactive.  Otherwise, a number of games will be played by an AI player of the specified type.

Second, enter selection 2 to specify the number of "codes" will exist in each game (a standard Mastermind game has 4 codes):
```
Enter Selection: 2
Enter number of entries (variables): 4
```
Third, enter selection 3 to specify the domain of colors for each code (a standard Mastermind game has 6 unique colors):
```
Enter Selection: 3
1. Enter colors
2. Delete colors
3. Display current colors
Enter Selection: 1
Enter new color, or QUIT to stop: R
Enter new color, or QUIT to stop: G
Enter new color, or QUIT to stop: B
Enter new color, or QUIT to stop: Y
Enter new color, or QUIT to stop: T
Enter new color, or QUIT to stop: P
Enter new color, or QUIT to stop: QUIT
Current Colors: R G B Y T P
```
Finally, enter selection 5 to start playing some games!
```
Enter Selection: 5
Number of games to play: 1

Turn #1
Player Greg guessed: R R B Y 
Code not solved!
Black Pegs: 1
White Pegs: 1

Turn #2
Player Greg guessed: G R Y P 
Code not solved!
Black Pegs: 2
White Pegs: 0

Turn #3
Player Greg guessed: B R P P 
Code not solved!
Black Pegs: 2
White Pegs: 0

Turn #4
Player Greg guessed: T R R P 

Code cracked! Winner!
Master code is: T R R P 
This game took 4 turns.

Number of games played: 1
Average number of turns: 4.0
```
