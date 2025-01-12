package game.othello;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;


public class GameManager {
    private static final int BOARD_SIZE = 8;
    private static final int [][] DIRECTIONS = {
            {-1,0},{1,0},{0,-1},{0,1},
            {-1,-1},{-1,1},{1,-1},{1,1}
    };

    private CellButton.CellState[][] board; // "empty", "black", "white"
    private CellButton.CellState currentPlayer; // "black" or "white"
    private final Stack<GameState> history;
    private CellButton.CellState aiColor; // Variable to store the AI color

    public GameManager() {
        board = new CellButton.CellState[BOARD_SIZE][BOARD_SIZE];
        history = new Stack<>();
        currentPlayer = CellButton.CellState.BLACK; // Black starts the game
        aiColor = CellButton.CellState.WHITE; //Default AI color, can be changed with setAiColor()
        initializeBoard();
    }
    public void setAiColor (CellButton.CellState aiColor) {
        this.aiColor = aiColor;
    }

    public CellButton.CellState getAiColor () {
        return aiColor;
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = CellButton.CellState.EMPTY;
            }
        }

        // Set the initial 4 pieces in the center
        board[3][3] = CellButton.CellState.WHITE;
        board[3][4] = CellButton.CellState.BLACK;
        board[4][3] = CellButton.CellState.BLACK;
        board[4][4] = CellButton.CellState.WHITE;
    }

    public CellButton.CellState getCell(int row, int col) {
        return board[row][col];
    }

    public CellButton.CellState getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == CellButton.CellState.BLACK)
                        ? CellButton.CellState.WHITE
                        : CellButton.CellState.BLACK;
    }

    public boolean isValidMove(int row, int col) {
        if (board[row][col] != CellButton.CellState.EMPTY) {
            return false; // Cell is already occupied
        }

        // Check if the move will flip at least one piece
        return canFlip(row, col, currentPlayer); //Checks if any pieces can be flipped
    }

    private boolean canFlip(int row, int col, CellButton.CellState player) {
        CellButton.CellState opponent = (player == CellButton.CellState.BLACK)
                                            ? CellButton.CellState.WHITE
                                            : CellButton.CellState.BLACK;

        // Directions array
        for (int [] dir : DIRECTIONS) {
            int r = row + dir[0], c = col + dir[1];
            boolean hasOpponent = false;

            while (isInBounds(r, c) && board[r][c] == opponent) {
                hasOpponent = true;
                r += dir [0];
                c += dir [1];
            }

            if (hasOpponent && isInBounds(r, c) && board[r][c] == player){
                return true; //Valid move
            }
        }
        return false;
    }

    public void makeMove(int row, int col) {
        if (!isValidMove(row, col)) {
            throw new IllegalArgumentException("Invalid move");
        }

        history.push(new GameState(copyBoard(), currentPlayer)); //Save state
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

    public boolean isGameOver() {
        return hasValidMoves(CellButton.CellState.BLACK) && hasValidMoves((CellButton.CellState.WHITE));
    }

    private void flipPieces(int row, int col, CellButton.CellState player) {
        CellButton.CellState opponent = (player == CellButton.CellState.BLACK)
                                        ? CellButton.CellState.WHITE
                                        : CellButton.CellState.BLACK;

        for (int[] dir : DIRECTIONS) {
            int r = row + dir[0], c = col + dir[1];
            boolean hasOpponent = false;

            while (isInBounds(r, c) && board[r][c] == opponent) {
                hasOpponent = true;
                r += dir[0];
                c += dir[1];
            }

            if (hasOpponent && isInBounds(r, c) && board[r][c] == player) {
                // Flip pieces in this direction
                r = row + dir[0];
                c = col + dir[1];
                while (board[r][c] == opponent) {
                    board[r][c] = player;
                    r += dir[0];
                    c += dir[1];
                }
            }
        }
    }

    public boolean hasValidMoves(CellButton.CellState currentPlayer) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (isValidMove(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    private CellButton.CellState[][] copyBoard(){
        CellButton.CellState[][] copy = new CellButton.CellState[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            System.arraycopy(board[row], 0, copy[row], 0,BOARD_SIZE);
        }
        return copy;
    }

    public String getWinner() {
        int blackCount = 0, whiteCount = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == CellButton.CellState.BLACK) {
                    blackCount++;
                } else if (board[row][col] == CellButton.CellState.WHITE) {
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
            int[] move = validMoves.getFirst();
            makeMove(move[0], move[1]);
        }
    }


}

