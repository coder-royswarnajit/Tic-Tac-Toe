import java.awt.*;
import java.awt.event.*;

public class Board extends MouseAdapter {
    private static final int squareLength = 200;
    private Square[][] board;
    private Square winner;
    private TicTacToe_API api;
    private String currentTurn;
    private int rows, cols, movesMade;
    private boolean winnerFound;
    private String playerChoice;
    private boolean playerMoveDone;
    private boolean multiplayerMode;
    private boolean gameStarted;
    private Rectangle singlePlayerButton;
    private Rectangle multiPlayerButton;

    public Board() {
        board = new Square[3][3];
        api = new TicTacToe_API();
        rows = board.length;
        cols = board[0].length;
        currentTurn = " ";
        winner = null;
        winnerFound = false;
        movesMade = 0;
        playerChoice = "";
        playerMoveDone = true;
        multiplayerMode = false;
        gameStarted = false;
        singlePlayerButton = new Rectangle(200, 300, 400, 80);
        multiPlayerButton = new Rectangle(200, 400, 400, 80);
    }

    public void update() {
        if (!gameStarted) {
            return; // Don't update anything if game hasn't started
        }

        if (currentTurn.equals(" "))
            currentTurn = api.changeTurns(currentTurn);
            
        // Computer's move in single player mode
        if (!multiplayerMode && playerMoveDone) {
            if (board != null) {
                computerMove();
                currentTurn = api.changeTurns(currentTurn);
                playerMoveDone = false;
            }
        }
        
        winner = api.winner(board, rows, cols);
        if (winner != null && !winner.getPiece().getValue().equals(" ")) {
            winnerFound = true;
        }
        if (winner == null && movesMade == 9) {
            winnerFound = true;
        }
    }

    public void draw(Graphics g) {
        if (!gameStarted) {
            drawModeSelection(g);
            return;
        }

        g.setFont(new Font("Roboto", Font.BOLD, 180));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null)
                    board[i][j] = new Square(i + 1, j + 1, new Piece(currentTurn, -1, -1));
                g.setColor(Color.WHITE);
                g.fillRect(i * squareLength + 100, j * squareLength + 90, squareLength, squareLength);
                ((Graphics2D) g).setStroke(new BasicStroke(4));
                g.setColor(Color.BLACK);
                g.drawRect(i * squareLength + 100, j * squareLength + 90, squareLength + 1, squareLength + 1);
                g.drawString(board[i][j].getPiece().getValue(), i * squareLength + 95 + (squareLength / 5),
                        j * squareLength + 160 + (squareLength / 2));
            }
        }
        if (winnerFound) {
            if (winner != null) {
                api.drawWinningPath(g, winner, squareLength);
            }
            if (movesMade == 9 && winner == null) {
                api.gameResult(g, "TIE");
            } else {
                api.gameResult(g, winner.getPiece().getValue());
            }
            askToPlayAgain(g);
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString(getGameModeTitle(), 50, 60);
            
            // Show current turn
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String turnMessage = multiplayerMode ? 
                "Player " + currentTurn + "'s Turn" : 
                (currentTurn.equals("X") ? "Your Turn" : "Computer's Turn");
            g.drawString(turnMessage, 320, 60);
        }
    }
    
    private String getGameModeTitle() {
        return multiplayerMode ? "Multiplayer Mode" : "Single Player Mode";
    }
    
    private void drawModeSelection(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        g.drawString("Tic-Tac-Toe", 250, 150);
        
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Select Game Mode:", 250, 250);
        
        // Single player button
        g.setColor(new Color(17, 122, 101));
        g.fill3DRect(singlePlayerButton.x, singlePlayerButton.y, 
                    singlePlayerButton.width, singlePlayerButton.height, true);
        g.setColor(Color.WHITE);
        g.drawString("Single Player", singlePlayerButton.x + 100, singlePlayerButton.y + 50);
        
        // Multiplayer button
        g.setColor(new Color(41, 128, 185));
        g.fill3DRect(multiPlayerButton.x, multiPlayerButton.y, 
                    multiPlayerButton.width, multiPlayerButton.height, true);
        g.setColor(Color.WHITE);
        g.drawString("Multiplayer", multiPlayerButton.x + 120, multiPlayerButton.y + 50);
    }

    public void askToPlayAgain(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(new Color(247, 220, 111));
        g.drawString("Would you like to play again?", 20, 60);

        g.setColor(new Color(17, 122, 101));
        g.fill3DRect(550, 20, 100, 50, true);
        g.setColor(new Color(231, 76, 60));
        g.fill3DRect(670, 20, 100, 50, true);

        g.setColor(Color.WHITE);
        g.drawString("Yes", 565, 55);
        g.drawString("No", 695, 55);
    }

    public void resetGame() {
        movesMade = 0;
        currentTurn = " ";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Square(i + 1, j + 1, new Piece(currentTurn, -1, -1));
            }
        }
        winnerFound = false;
        winner = null;
        playerChoice = "";
        playerMoveDone = true;
        // Keep game mode and gameStarted values to restart in same mode
    }
    
    public void startNewGame(boolean isMultiplayer) {
        multiplayerMode = isMultiplayer;
        gameStarted = true;
        resetGame();
    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        
        // Handle mode selection if game hasn't started
        if (!gameStarted) {
            if (singlePlayerButton.contains(mx, my)) {
                startNewGame(false); // Start single player game
                return;
            } else if (multiPlayerButton.contains(mx, my)) {
                startNewGame(true); // Start multiplayer game
                return;
            }
            return; // Still in selection screen
        }
        
        // Handle game board clicks
        if (getRowClicked(mx, my) != -1 && getColClicked(mx, my) != -1 && !winnerFound) {
            int squareRow = getRowClicked(mx, my);
            int squareCol = getColClicked(mx, my);
            
            // In multiplayer, both X and O are controlled by players
            if (multiplayerMode || currentTurn.equals("X")) {
                if (board[squareCol - 1][squareRow - 1].getPiece().getValue().equals(" ")) {
                    board[squareCol - 1][squareRow - 1].setPiece(new Piece(currentTurn, squareRow, squareCol));
                    movesMade++;
                    currentTurn = api.changeTurns(currentTurn);
                    playerMoveDone = true;
                }
            }
        }
        
        // Handle play again buttons
        if (winnerFound) {
            if (buttonClicked(e, 550, 20, "Yes")) {
                resetGame();
            } else if (buttonClicked(e, 670, 20, "No")) {
                gameStarted = false; // Go back to mode selection
                resetGame();
            }
        }
    }

    public void computerMove() {
        Square[][] tempBoard = copyBoard(board);
        Square tempWinner = null;
        String comp = "O";
        String playMove = "X";
        /**
         * Take the winning move
         * */
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                if (tempBoard[j][i].getPiece().getValue().equals(" ")) 
                {
                    tempBoard[j][i].setPiece(new Piece(comp, i + 1, j + 1));
                    tempWinner = api.winner(tempBoard, 3, 3);
                    if (tempWinner != null && winner == null) 
                    {
                    	board[j][i].setPiece(new Piece(comp, i + 1, j + 1));
                    	movesMade++;
                        return;
                    }
                    else if(winner == null)
                    {
                    	tempBoard[j][i].setPiece(new Piece(" ",i+1,j+1));
                    	tempWinner = null;
                    }
                }
            }
        }
        /**
         * Stop the opponent from winning
         * */
        for(int i = 0;i<rows;i++)
        {
        	for(int j = 0;j<cols;j++)
        	{
        		if (tempBoard[j][i].getPiece().getValue().equals(" ")) 
        		{
        			tempBoard[j][i].setPiece(new Piece(playMove, i + 1, j + 1));
        			tempWinner = api.winner(tempBoard, rows, cols);
        			if(tempWinner != null && winner == null)
        			{
        				board[j][i].setPiece(new Piece(comp,i+1,j+1));
        				movesMade++;
        				return;
        			}
        			else
        			{
        				tempBoard[j][i].setPiece(new Piece(" ",i+1,j+1));
                    	tempWinner = null;
        			}
        		}
        	}
        }
        /**
         * Select center if open
         * */
        if(board[1][1].getPiece().getValue().equals(" "))
        {
        	board[1][1].setPiece(new Piece(comp, 2,2));
        	movesMade++;
        	return;
        }
        /**
         * Select any corners
         * */
        for(int i = (int)(Math.random()*rows);i<rows;i++)
        {
        	for(int j = (int)(Math.random()*rows);j<cols;j++)
        	{
        		if(board[j][i].getPiece().getValue().equals(" ") && (i != 1 || j != 1))
        		{
        			 board[j][i].setPiece(new Piece(comp, j,i));
        			 movesMade++;
        			 return;
        		}
        	}
        }
        for(int i = 0;i<rows;i++)
        {
        	for(int j = 0;j<cols;j++)
        	{
        		if(board[j][i].getPiece().getValue().equals(" "))
        		{
        			board[j][i].setPiece(new Piece(comp, j,i));
       			 	movesMade++;
       			 	return;
        		}
        	}
        }
    }
    
    public Square[][] copyBoard(Square[][] b) {
        Square[][] toReturn = new Square[rows][cols];
        for (int i = 0; i < rows; i++) 
        {
            for (int j = 0; j < cols; j++) 
            {
                Piece p = b[i][j].getPiece();
                int row = b[i][j].getRow();
                int col = b[i][j].getCol();
                toReturn[i][j] = new Square(row, col, new Piece(p.getValue(),p.getRow(),p.getCol()));
            }
        }
        return toReturn;
    }

    public boolean buttonClicked(MouseEvent e, int bx, int by, String buttonName) {
        int mx = e.getX();
        int my = e.getY();
        if (buttonName.equals("Yes")) {
            if (mx >= bx && mx <= bx + 100) {
                if (my >= by && my <= by + 50) {
                    return true;
                }
            }
            return false;
        } else if (buttonName.equals("No")) {
            if (mx >= bx && mx <= bx + 100) {
                if (my >= by && my <= by + 50) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getRowClicked(int mx, int my) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (my >= (board[i][j].getRow() - 1) * squareLength + 90
                        && my <= (board[i][j].getRow() - 1) * squareLength + 90 + squareLength + 1) {
                    return board[i][j].getRow();
                }
            }
        }
        return -1;
    }

    public int getColClicked(int mx, int my) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mx >= (board[i][j].getCol() - 1) * squareLength + 100
                        && mx <= (board[i][j].getCol() - 1) * squareLength + 95 + squareLength + 1) {
                    return board[i][j].getCol();
                }
            }
        }
        return -1;
    }

    // delete later
    private void boardTester(Square[][] board) 
    {
        System.out.println("\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j].getPiece() + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }
}