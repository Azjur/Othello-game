package game.othello;

import java.util.List;

public class GameState {
    private final String[][] board;
    private final String currentPlayer;

    public GameState(String[][] board, String currentPlayer) {
        this.board = new String[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board[i].length);
        }
        this.currentPlayer = currentPlayer;
    }

    public String[][] getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
