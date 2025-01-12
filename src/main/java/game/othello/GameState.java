package game.othello;

import java.util.Arrays;

public class GameState {
    private final CellButton.CellState[][] board;
    private final CellButton.CellState currentPlayer;

    // Constructor: Creates a deep copy of the board and stores the current player
    public GameState(CellButton.CellState[][] board, CellButton.CellState currentPlayer) {
        this.board = deepcopy(board);
        this.currentPlayer = currentPlayer;
    }

    // Getter for the board
    public CellButton.CellState[][] getBoard() {
        return deepcopy(board); // Return a deep copy to maintain immutability
    }

    // Getter for the current player
    public CellButton.CellState getCurrentPlayer() {
        return currentPlayer;
    }

    // Deep copy method for the board
    private static CellButton.CellState[][] deepcopy(CellButton.CellState[][] original) {
        CellButton.CellState[][] copy = new CellButton.CellState[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

}
