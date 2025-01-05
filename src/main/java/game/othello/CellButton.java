package game.othello;

import javafx.scene.control.Button;


public class CellButton extends Button {
    private int row;
    private int col;
    private String color; // "empty", "black", or "white"

    public CellButton(int row, int col) {
        this.row = row;
        this.col = col;
        this.color = "empty"; // Default state is empty
        setPrefSize(50, 50); // Set button size for the board

        // Set initial appearance
        setStyle("-fx-background-color: lightgray; -fx-pref-width: 50; -fx-pref-height: 50;");

        // Add a click listener
        setOnAction(event -> handleClick());
    }

    public void setHighlight(boolean isValidMove) {
        if (isValidMove) {
            setStyle("-fx-background-color: yellow; -fx-pref-width: 50; -fx-pref-height: 50;");
        } else {
            setStyle("-fx-background-color: lightgray; -fx-pref-width: 50; -fx-pref-height: 50;");
        }
    }

    private void handleClick() {
        // For now, toggle between black and white
        if ("empty".equals(color)) {
            color = "black";
        } else if ("black".equals(color)) {
            color = "white";
        } else {
            color = "empty";
        }

        // Update appearance
        updateStyle();
    }

    private void updateStyle() {
        // Change the button's appearance based on its state
        switch (color) {
            case "black":
                setStyle("-fx-background-color: black; -fx-border-color: gray;");
                break;
            case "white":
                setStyle("-fx-background-color: white; -fx-border-color: gray;");
                break;
            default:
                setStyle("-fx-background-color: lightgray; -fx-border-color: gray;");
                break;
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        updateStyle();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}