package game.othello;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;


public class GameManager {
    private static final int BOARD_SIZE = 8;
    private String[][] board; // "empty", "black", "white"
    private String currentPlayer; // "black" or "white"
    private Stack<GameState> history;

    public GameManager() {
        board = new String[BOARD_SIZE][BOARD_SIZE];
        history = new Stack<>();
        currentPlayer = "black"; // Black starts the game
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = "empty";
            }
        }

        // Set the initial 4 pieces in the center
        board[3][3] = "white";
        board[3][4] = "black";
        board[4][3] = "black";
        board[4][4] = "white";
    }

    public String getCell(int row, int col) {
        return board[row][col];
    }

    public void setCell(int row, int col, String color) {
        board[row][col] = color;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.equals("black") ? "white" : "black";
    }

    public boolean isValidMove(int row, int col) {
        if (!board[row][col].equals("empty")) {
            return false; // Cell is already occupied
        }

        // Check if the move will flip at least one piece
        return canFlip(row, col, currentPlayer);
    }

    private boolean canFlip(int row, int col, String player) {
        String opponent = player.equals("black") ? "white" : "black";

        // Check all 8 directions
        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // Vertical and horizontal
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // Diagonal
        };

        for (int[] dir : directions) {
            int r = row + dir[0], c = col + dir[1];
            boolean hasOpponent = false;

            while (isInBounds(r, c) && board[r][c].equals(opponent)) {
                hasOpponent = true;
                r += dir[0];
                c += dir[1];
            }

            if (hasOpponent && isInBounds(r, c) && board[r][c].equals(player)) {
                return true; // Valid move
            }
        }

        return false;
    }

    public void makeMove(int row, int col) {
        history.push(new GameState(board, currentPlayer));
        if (!isValidMove(row, col)) {
            throw new IllegalArgumentException("Invalid move");
        }

        board[row][col] = currentPlayer;
        flipPieces(row, col, currentPlayer);
        switchPlayer();
    }

    public void undo() {
        if (!history.isEmpty()) {
            // Revert to the previous state
            GameState previousState = history.pop();
            this.board = previousState.getBoard();
            this.currentPlayer = previousState.getCurrentPlayer();
        }
    }

    private void flipPieces(int row, int col, String player) {
        String opponent = player.equals("black") ? "white" : "black";

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int r = row + dir[0], c = col + dir[1];
            boolean hasOpponent = false;

            while (isInBounds(r, c) && board[r][c].equals(opponent)) {
                hasOpponent = true;
                r += dir[0];
                c += dir[1];
            }

            if (hasOpponent && isInBounds(r, c) && board[r][c].equals(player)) {
                // Flip pieces in this direction
                r = row + dir[0];
                c = col + dir[1];
                while (board[r][c].equals(opponent)) {
                    board[r][c] = player;
                    r += dir[0];
                    c += dir[1];
                }
            }
        }
    }

    public boolean hasValidMoves(String player) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (isValidMove(row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        return !hasValidMoves("black") && !hasValidMoves("white");
    }

    public String getWinner() {
        int blackCount = 0, whiteCount = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col].equals("black")) {
                    blackCount++;
                } else if (board[row][col].equals("white")) {
                    whiteCount++;
                }
            }
        }

        if (blackCount > whiteCount) {
            return "Black";
        } else if (whiteCount > blackCount) {
            return "White";
        } else {
            return "Draw";
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    public List<int[]> getValidMoves() {
        List<int[]> validMoves = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (isValidMove(row, col)) {
                    validMoves.add(new int[]{row, col});
                }
            }
        }
        return validMoves;
    }

    // AI Method: Pick the first valid move
    public void aiMove() {
        List<int[]> validMoves = getValidMoves();
        if (!validMoves.isEmpty()) {
            // AI makes the first valid move
            int[] move = validMoves.get(0);
            makeMove(move[0], move[1]);
        }
    }

    public void setAIColor(String black) {
    }
}

