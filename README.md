# Tic-Tac-Toe 

## Overview
This is a Java-based Tic-Tac-Toe game with both single-player and multiplayer modes. The game is built using Java Swing and AWT for graphical rendering and user interaction.

## Features
- Single-player mode with an AI opponent.
- Multiplayer mode for two players to play on the same machine.
- Graphical user interface (GUI) using Java AWT and Swing.
- Intelligent AI that makes strategic moves.
- Automatic game result detection (Win/Tie).
- Interactive buttons for selecting game modes and restarting games.

## Project Structure
```
📂 TicTacToe
 ├── Board.java        # Handles the game board logic
 ├── Frame.java        # Creates the game window
 ├── Game.java         # Main game loop and rendering
 ├── Piece.java        # Represents a player's move
 ├── Square.java       # Represents a board cell
 ├── TicTacToe_API.java # Game logic, winner detection, and AI logic
```

## How to Run
1. Ensure you have Java installed (Java 8+ recommended).
2. Compile all Java files:
   ```sh
   javac *.java
   ```
3. Run the game:
   ```sh
   java Game
   ```

## Controls
- Click on the buttons to select either **Single Player** or **Multiplayer** mode.
- Click on an empty square to make a move.
- If playing against AI, the computer will automatically take its turn.
- Once the game ends, you can choose to play again.

## AI Strategy
- The AI prioritizes winning moves.
- If no winning move is available, it blocks the opponent's winning move.
- It prefers selecting the center first.
- It then picks corners before choosing other positions.

## Future Enhancements
- Add support for online multiplayer.
- Implement difficulty levels for AI.
- Improve UI/UX with animations and sounds.

## Author
Swarnajit Roy

## License
This project is licensed under the MIT License.

