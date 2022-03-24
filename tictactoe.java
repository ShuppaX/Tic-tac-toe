import java.util.Scanner;

class Tictactoe {
    static Scanner userInput = new Scanner(System.in);
    
    //Strings that are part of the UI
    static String cross = " x ";
    static String circle = " o ";
    static String line = " | "; 
    static String dash = "---";
    static String empty = "   ";

    //Strings used in win determination
    static String winX = "";
    static String winO = "";
    static String checkLine = "";
    
    //Integers
    static int size = 0;
    static int turns = 0;
    static int playerInputs = 0;
    static int number = 1;
    static int playerX = 0;
    static int playerY = 0;

    //Booleans
    static Boolean gameOver = false;
    static Boolean turn = true;
    static Boolean slotEmpty = true;
    static Boolean xWon = false;
    static Boolean oWon = false;

    //Messages to the player
    static String errorMessage = "Incorrect input, please try again.";
    static String slotFull = "This slot is already occupied, please try another one.";
    static String cpuTurn = "It's the computers turn, please wait.";
    static String draw = "The game has ended in a draw.";
    static String compWon = "The computer has won!";
    static String playerWon = "The player has won!";

    //This is the gameboard array, which is twice the size that is entered by the user.
    //It is multiplied here because the size integer is used in other parts as it is
    //and it was simply easiest to make the size bigger in here.
    static String[][] gameBoard = new String[gameStart() * 2][gameStart() * 2];
    public static void main(String[] args) throws InterruptedException {
        initializeBoard();
        System.out.println();

        while (!gameOver) {
            printGame();

            //A loop to print an error message about an incorrect input
            //which is detected if the player has entered a number
            //already on the same turn.
            if (playerInputs >= 1) {
                System.out.println(errorMessage);
            }
            System.out.println();
            checkWin();
            if (gameOver) {
                break;
            } else if (!gameOver) {
                checkTurn();
            }
        }
        printWinner();
    }

    //A method to ask the size of the gameboard from the user, and to give
    //the user basic information about the game.
    public static int gameStart() {
        while (size < 3 || size > 12) {
            System.out.print("\033[H\033[2J");
            System.out.println("Welcome to play a game of Tic-Tac-Toe!");
            System.out.println();
            System.out.println("The number of marks in a row required for a win is:");
            System.out.println("3 for 3x3");
            System.out.println("4 for 4x4, 6x6, 7x7 and 8x8");
            System.out.println("5 for 5x5, 9x9, 10x10, 11x11 and 12x12");
            System.out.println();
            System.out.println("Enter the size for your Tic-Tac-Toe (3 - 12)");
            try {
                size = Integer.parseInt(userInput.nextLine());
            } catch (Exception e) {
            }
            
        }
        return size;
    }

    //A method for the initialization of the gameboard.
    public static void initializeBoard() {
        //A string that is used for the numbers on the left and top side of the gameboard.
        String[] numbers = new String[size * 2];

        //A for-loop to fill the "numbers" stringarray.
        for (int i = 0; i < size * 2; i++) {
            if (i % 2 == 0) {
                numbers[i] = "   ";
            } else {
                if (number < 10) {
                    numbers[i] = " " + number + " ";
                    number++;
                } else {
                    numbers[i] = number + " ";
                    number++;
                }
            }
            
        }

        //A for-loop to fill the baseboard
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                if (x == 0 && y == 0) {
                    gameBoard[x][y] = empty;
                } else if (x % 2 == 0) {
                    gameBoard[x][y] = dash;
                } else if (y % 2 == 0) {
                    gameBoard[x][y] = line;
                } else {
                    gameBoard[x][y] = empty;
                }
            }
        }

        //This part is used to set the line numbers in every other row and column where x = 0 and y = 0
        for (int i = 0; i < gameBoard[0].length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                if (j % 2 != 0 && j != 0) {
                    gameBoard[0][j] = numbers[j];
                    gameBoard[j][0] = numbers[j];
                } else {
                    gameBoard[0][j] = empty;
                    gameBoard[j][0] = empty;
                }
            }
        }
    }

    //A method to print the gameboard and empty out the rest of the commandline.
    public static void printGame() {
        System.out.print("\033[H\033[2J");
        
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                System.out.print(gameBoard[x][y]);
            }
            System.out.println();
        }
    }

    //A method for the users moves.
    public static void playerMove() {
        if (!slotEmpty) {
            System.out.println(slotFull);
        }
        
        System.out.println();

        String playerFirstCoord = "It's your turn, enter a number from the left row (1 - " + size + "):";
        String playerSecondCoord = "It's your turn, enter a number from the top row (1 - " + size + "):";

        //Prints of the instructions the player gets, there is a simple line which tells the player
        //which number they have to pick first.
        System.out.println(playerFirstCoord);
        playerX = Integer.parseInt(userInput.nextLine()) * 2 - 1;

        System.out.println();

        System.out.println(playerSecondCoord);
        playerY = Integer.parseInt(userInput.nextLine()) * 2 - 1;

        System.out.println();

        //The first part of this if-loop checks if the slot entered is not empty, if it isn't the error message
        //is printed and the player is asked to enter other numbers.
        //If the slot is empty, the players mark is printed on the slot and then the turn calculator has
        //it's value added by one and the boolean turn is made false, to determine that the players
        //turn is over.
        if (gameBoard[playerX][playerY] != empty) {
            slotEmpty = false;
        } else if (gameBoard[playerX][playerY] == empty) {
            gameBoard[playerX][playerY] = cross;
            turns++;
            playerInputs = 0;
            slotEmpty = true;
            turn = false;
        }
    }

    //A method for the computers moves.
    public static void computerMove() {

        //The computers moves are simply counted with Math.random by using the size entered earlier times two
        //because the gameboards array is twice the size that was entered by the user.
        int computerX = (int)(Math.random() * size * 2);
        
        //A simple if-loop to check if the computers randomnumber is 0 and then it is forced to be 1 instead
        //because the computer cannot enter it's mark on a 0 slot and then on the other part the computers
        //integer is forced to be an odd number, because the gameboards gameslots are only on odd numbers.
        if (computerX == 0) {
            computerX = 1;
        } else if (computerX % 2 == 0) {
            computerX--;
        }

        int computerY = (int)(Math.random() * size * 2);
        if (computerY == 0) {
            computerY = 1;
        } else if (computerY % 2 == 0) {
            computerY--;
        }

        //A print to inform the player that it's the computers turn.
        System.out.println(cpuTurn);

        //A if-loop to check if the slot for the computer is empty and that the computers input doesn't contain a 0,
        //and then if those are true then the circle string is entered into the slot, the turn counters value goes up
        //by one and then the boolean turn is turned true to let the program know that it's the players turn.
        if (gameBoard[computerX][computerY] == empty && computerX != 0 && computerY != 0) {
            gameBoard[computerX][computerY] = circle;
            turns++;
            turn = true;
        }
    }

    //A method to check whose turn it is.
    public static void checkTurn() {
        //An if-loop to check whose turn it is. If the boolean turn is false, it is the computers turn
        //and if it is true then it is the players turn.

        //There is also a try-catch for the players moves so that the program doesn't crash if the player
        //accidentally or on purpose enters a wrong number.
        if (!turn) {
            computerMove();
        } else {
            try {
                playerMove();
            } catch (Exception e) {
                playerInputs++;
            }
        }
    }

    //A method to check if there is a win on the board.
    public static Boolean checkWin() {
        //If-loop to set the strings used for comparison
        //on checking if there is a win on the board.
        if (size == 3) {
            winX = " x  x  x ";
            winO = " o  o  o ";
        } else if (size == 4 || size == 6 || size == 7 || size == 8) {
            winX = " x  x  x  x ";
            winO = " o  o  o  o ";
        } else if (size == 5 || size == 9 || size == 10 || size == 11 || size == 12) {
            winX = " x  x  x  x  x ";
            winO = " o  o  o  o  o ";
        }

        //These loops check if there is a win on any left to right
        //diagonal line.

        //This first loop checks the top half of the diagonals
        //meaning the ones on the first row.
        for (int i = gameBoard.length - 1; i > 0; i--) {
            for (int y = 0, x = i; x <= gameBoard.length - 1; y++, x++) {
                if (x % 2 != 0 && y % 2 != 0) {
                    checkLine += gameBoard[x][y];
                }
            }
            if (checkLine.contains(winX)) {
                xWon = true;
                return gameOver = true;
            } else if (checkLine.contains(winO)) {
                oWon = true;
                return gameOver = true;
            }
            checkLine = "";
        }

        //This second loop checks the diagonals from the left row, where
        //at the startpoint y is always 1.
        for (int i = 0; i <= gameBoard.length - 1; i++) {
            for (int x = 0, y = i; y <= gameBoard.length - 1; x++, y++) {
                if (y % 2 != 0 && x % 2 != 0) {
                    checkLine += gameBoard[x][y];
                }
            }
            if (checkLine.contains(winX)) {
                xWon = true;
                return gameOver = true;
            } else if (checkLine.contains(winO)) {
                oWon = true;
                return gameOver = true;
            }
            checkLine = "";
        }

        //This loop checks if there is a win on any row.
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                if (x % 2 != 0 && y % 2 != 0) {
                    checkLine += gameBoard[x][y];
                    }
            }
            if (checkLine.contains(winX)) {
                xWon = true;
                return gameOver = true;
            } else if (checkLine.contains(winO)) {
                oWon = true;
                return gameOver = true;
            }
            checkLine = "";
        }

        //This loop checks if there is a win on any column.
        for (int x = 0; x < gameBoard.length; x++) {
            for (int y = 0; y < gameBoard[x].length; y++) {
                if (x % 2 != 0 && y % 2 != 0) {
                    checkLine += gameBoard[y][x];
                    }
            }
            if (checkLine.contains(winX)) {
                xWon = true;
                return gameOver = true;
            } else if (checkLine.contains(winO)) {
                oWon = true;
                return gameOver = true;
            }
            checkLine = "";
        }

        //This loop checks if there is a win on any the right to left
        //diagonal lines.
        for(int i = 0 ; i < gameBoard.length * 2 ; i++) {
            for(int y = 0 ; y <= i ; y++) {
                int x = i - y;
                if(x < gameBoard.length && y < gameBoard.length && x % 2 != 0 && y % 2 != 0) {
                    checkLine += gameBoard[x][y];
                }
            }
            if (checkLine.contains(winX)) {
                xWon = true;
                return gameOver = true;
            } else if (checkLine.contains(winO)) {
                oWon = true;
                return gameOver = true;
            }
            checkLine = "";
        }

        //This if loop checks that if the amount of turns equals
        //the size of the gameboard times itself (which is the gameareas area)
        //and if it does then the game is over and a draw.
        if (turns == size * size) {
            return gameOver = true;
        }
        return gameOver = false;
    }

    //A method to determine the winner and to print out a draw announcement
    public static void printWinner() {
        printGame();

        System.out.println();

        //This if-loop is used to check who won or if the game was a draw.
        //If the boolean gameOver is true but xWon and oWon are false, then
        //the loop will print out that the game was a draw.
        if (gameOver = true && xWon != true && oWon != true) {
            System.out.println(draw);
        
        //If gameOver is true and oWon is true, then the loop will print out
        //that the computer has won the game.
        } else if (gameOver = true && oWon == true) {
            System.out.println(compWon);

        //If gameOver is true and xWon is true, then the loop will print out
        //that the player has won the game.
        } else if (gameOver = true && xWon == true){
            System.out.println(playerWon);
        }
    }
}