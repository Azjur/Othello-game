package game.othello;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;


public class CellButton extends Button {

    private final int row;
    private final int col;
    private CellState color; // "empty", "black", or "white"
    private final String originalStyle = "-fx-background-color: green; -fx-border-color: gray; -fx-pref-width: 50; -fx-pref-height: 50;";

    public enum CellState{
        EMPTY, BLACK, WHITE
    }

    public CellButton(int row, int col) {
        this.row = row;
        this.col = col;
        this.color = CellState.EMPTY; // Default state is empty

        // Set initial appearance
        setDefaultStyle();

        // Add action to the button to handle clicks
        setOnAction(event -> handleClick());
    }

    private void setDefaultStyle(){
        setPrefSize(50, 50); // Set button size for the board
        setStyle(originalStyle); // Apply the default style
    }

    public void setHighlight(boolean isValidMove) {
        if (isValidMove) {
            setStyle("-fx-background-color: lightgreen; -fx-border-color:gray;");
        } else {
            setStyle(originalStyle); //Reset to the original style
        }
    }

    private void handleClick() {
        // For now, toggle between black and white
        if (color == CellState.EMPTY) {
            setColor(CellState.BLACK);
        } else if (color == CellState.BLACK) {
            setColor(CellState.WHITE);
        } else {
            setColor(CellState.EMPTY);
        }
    }

    public CellState getColor(){
        return this.color;
    }

    public void setColor(CellState newColor) {
        this.color = newColor;
        Color cssColor = switch (newColor) {
            case EMPTY -> Color.GREEN;
            case BLACK -> Color.BLACK;
            case WHITE -> Color.WHITE;
        };
        setStyle("-fx-background-color: " + toWebColor(cssColor) + "; -fx-border-color: gray; -fx-pref-width: 50; -fx-height: 50;");
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private String toWebColor(Color color) {
        return String.format("#%02x%02x%02x",
                (int) (color.getRed()* 255),
                (int) (color.getGreen()* 255),
                (int) (color.getBlue())* 255);
    }
}