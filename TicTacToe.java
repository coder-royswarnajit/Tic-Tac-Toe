import java.util.Scanner;

public class TicTacToe 
{
    private Scanner sc;
    private String[][] board;
    private String player1, player2;
    private int movesMade;
    private int row, column;

    TicTacToe() 
    {
        sc = new Scanner(System.in);
        board = new String[3][3];
        player1 = "O";
        player2 = "X";
        movesMade = 0;
        row = board.length;
        column = board[0].length;
    }

    public void run() 
    {
        System.out.print("\n\n\n");
        String currentPlayer = player1;
        boolean endGame = false;
        SetUpBoard();

        do 
        {
            UpdateBoard();
            makeMove(currentPlayer);

            // Change Turns
            if (currentPlayer.equals(player1)) 
                currentPlayer = player2;
            else 
                currentPlayer = player1;

            movesMade++;

            if (movesMade == 9 && winner().equals(" "))
            {
                UpdateBoard();
                System.out.println("TIE!!");
                if (PlayAgain().equalsIgnoreCase("NO")) 
                    endGame = true;
                else 
                {
                    movesMade=0;
                    SetUpBoard();
                    currentPlayer = player1;
                }
            } 
            else if (movesMade <= 9 && !winner().equals(" ")) 
            {
                UpdateBoard();
                System.out.println("THE WINNER OF THIS GAME IS '" + winner() + "'");
                if (PlayAgain().equalsIgnoreCase("NO")) 
                    endGame = true;
                else
                { 
                    movesMade = 0;
                    SetUpBoard();
                    currentPlayer = player1;
                }
                
            }

        } while (!endGame);
        System.out.print("\n\n THANKS FOR PLAYING\n\n\n");
    }

    public void SetUpBoard() 
    {
        System.out.println("\nWELCOME TO TIC TAC TOE");
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board[i][j] = " ";
            }
        }
    }

    public void UpdateBoard() 
    {
        System.out.println("\nCurrent Board:");
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                System.out.print("[" + board[i][j] + "]");
            }
            System.out.println();
        }
    }

    public void makeMove(String move) 
    {
        boolean moveMade = false;
        int playerRow = 0, playerColumn = 0;
        do {
            System.out.println("Player with '" + move + "', enter a row number (1,2,3):");
            playerRow = sc.nextInt();
            System.out.println("Player with '" + move + "', enter a column number (1,2,3):");
            playerColumn = sc.nextInt();

            if ((playerRow >= 1 && playerRow <= row) && 
                (playerColumn >= 1 && playerColumn <= column) &&
                board[playerRow - 1][playerColumn - 1].equals(" ")) 
                {
                    board[playerRow - 1][playerColumn - 1] = move;
                    moveMade = true;
            } else {
                System.out.println("\nINVALID INPUT, TRY AGAIN");
            }
        } while (!moveMade);
    }

    public String winner() 
    {
        // Check Rows
        for (int i = 0; i < row; i++) {
            if (board[i][0].equals(board[i][1]) && 
                board[i][1].equals(board[i][2]) && 
                !board[i][0].equals(" ")) 
            {
                    return board[i][0];
            }
        }
        // Check Columns
        for (int i = 0; i < column; i++) {
            if (board[0][i].equals(board[1][i]) && 
                board[1][i].equals(board[2][i]) && 
                !board[0][i].equals(" "))
                {
                    return board[0][i];
                }
        }
        // Check Diagonals
        if (board[0][0].equals(board[1][1]) && 
            board[1][1].equals(board[2][2]) && 
            !board[0][0].equals(" ")) 
            {
                return board[0][0];
            }
        if (board[0][2].equals(board[1][1]) && 
            board[1][1].equals(board[2][0]) && 
            !board[0][2].equals(" ")) 
            {
                return board[0][2];
            }
        return " ";
    }

    public String PlayAgain() 
    {
        String statusCall;
        do 
        {
            System.out.println("\nWOULD YOU LIKE TO PLAY AGAIN? (YES/NO)");
            statusCall = sc.next();
        } while (!(statusCall.equalsIgnoreCase("YES") || statusCall.equalsIgnoreCase("NO")));

        return statusCall;
    }


    public static void main(String[] args) 
    {
        TicTacToe ob = new TicTacToe();
        ob.run();
    }

}
