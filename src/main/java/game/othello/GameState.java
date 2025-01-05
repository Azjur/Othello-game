package game.othello;

public record GameState(String[][] board, String currentPlayer) {
    public GameState(String[][] board, String currentPlayer) {
        this.board = new String[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, board[i].length);
        }
        this.currentPlayer = currentPlayer;
    }
}
