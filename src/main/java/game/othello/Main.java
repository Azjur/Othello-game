package game.othello;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application {
    private static final int BOARD_SIZE = 8;
    private GameManager gameManager;
    private Label turnLabel; // Label to show the current player's turn
    private Stage primaryStage; // To switch scenes easily
    private String playerColor = "black"; // Default color for the player


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Othello Game");

        // Show the title page first
        showTitlePage();
    }

    private void showTitlePage() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titleLabel = new Label("Welcome to Othello!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-padding: 10;");

        Label colorLabel = new Label("Select your color:");
        colorLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10;");

        // Radio buttons for selecting color
        ToggleGroup colorGroup = new ToggleGroup();
        RadioButton blackButton = new RadioButton("Black");
        blackButton.setToggleGroup(colorGroup);
        blackButton.setSelected(true); // Default selection

        RadioButton whiteButton = new RadioButton("White");
        whiteButton.setToggleGroup(colorGroup);

        blackButton.setOnAction(event -> playerColor = "black");
        whiteButton.setOnAction(event -> playerColor = "white");

        // Start Game Button
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
        startButton.setOnAction(event -> startGame());

        // Quit Button
        Button quitButton = new Button("Quit");
        quitButton.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
        quitButton.setOnAction(event -> {
            // Close the application and terminate the program
            javafx.application.Platform.exit();
            System.exit(0);
        });

        layout.getChildren().addAll(titleLabel, colorLabel, blackButton, whiteButton, startButton, quitButton);

        Scene titleScene = new Scene(layout, 400, 300);
        primaryStage.setScene(titleScene);
        primaryStage.show();
    }
    private void startGame() {
        // Initialize the GameManager and set AI color
        gameManager = new GameManager();
        if ("white".equals(playerColor)) {
            gameManager.setAIColor("black");
        } else {
            gameManager.setAIColor("white");
        }

        // Show the game board
        showGameBoard();
    }
    private void showGameBoard() {
        GridPane board = createBoard();
        turnLabel = new Label(playerColor.substring(0, 1).toUpperCase() + playerColor.substring(1) + "'s turn");
        turnLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10;");

        Button undoButton = new Button("Undo");
        undoButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        undoButton.setOnAction(event -> {
            gameManager.undo();
            updateBoard(board);
            updateTurnLabel();

            if (gameManager.isGameOver()) {
                showGameOverDialog();
            }
        });
        VBox layout = new VBox(10);
        layout.getChildren().addAll(turnLabel, board, undoButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene gameScene = new Scene(layout, 600, 650);
        primaryStage.setScene(gameScene);
    }

//         // Create the board and the label
//        GridPane board = createBoard();
//        turnLabel = new Label("Black's turn");
//        turnLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
//
//        // Undo button
//        Button undoButton = new Button("Undo");
//        undoButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
//        undoButton.setOnAction(event -> {
//            gameManager.undo();
//            updateBoard(board);  // Update the board with the undone move
//            updateTurnLabel();    // Update the turn label after the undo
//
//            if (gameManager.isGameOver()) {
//                showGameOverDialog();
//            }
//        });
//
//        // Layout to add the label and the board together
//        GridPane layout = new GridPane();
//        layout.add(turnLabel, 0, 0, BOARD_SIZE, 1); // Label at the top
//        layout.add(board, 0, 1); // Board below the label
//        layout.add(undoButton, 0, BOARD_SIZE + 1); // Undo button below the board
//
//        // Set up the scene with the layout
//        Scene scene = new Scene(layout, 600, 650);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }

    private GridPane createBoard() {
        GridPane board = new GridPane();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                CellButton cell = new CellButton(row, col);
                cell.setColor(gameManager.getCell(row, col));

                cell.setOnAction(event -> {
                    if (gameManager.isValidMove(cell.getRow(), cell.getCol())) {
                        gameManager.makeMove(cell.getRow(), cell.getCol());
                        updateBoard(board);
                        updateTurnLabel();    // Update the turn label after the move

                        if (gameManager.isGameOver()) {
                            showGameOverDialog();
                        } else {
                            // If the game is not over, let the AI play after the human
                            gameManager.aiMove();
                            updateBoard(board);
                            updateTurnLabel();
                        }
                    }
                });

                board.add(cell, col, row);
            }
        }

        return board;
    }

    private void updateBoard(GridPane board) {
        // Loop through existing board and update the colors of each button
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // Find the existing CellButton at the given row and column
                for (javafx.scene.Node node : board.getChildren()) {
                    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                        CellButton cell = (CellButton) node;
                        cell.setColor(gameManager.getCell(row, col));  // Update the color
                        break;
                    }
                }
            }
        }
    }

    private void updateTurnLabel() {
        // Update the label to show the current player's turn
        String currentPlayer = gameManager.getCurrentPlayer();
        if ("black".equals(currentPlayer)) {
            turnLabel.setText("Black's turn");
        } else {
            turnLabel.setText("White's turn");
        }
    }

    private void showGameOverDialog() {
        String winner = gameManager.getWinner();
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Game Over");
        alert.setHeaderText("The game is over!");

        if ("draw".equals(winner)) {
            alert.setContentText("It's a draw!");
        } else {
            alert.setContentText("Winner: " + capitalize(winner));
        }

        ButtonType backToTitle = new ButtonType("Back to Title");
        ButtonType playAgain = new ButtonType("Play Again");
        alert.getButtonTypes().addAll(playAgain, backToTitle);

        alert.showAndWait().ifPresent(response -> {
            if (response == backToTitle) {
                showTitlePage();
            } else if (response == playAgain) {
                startGame();
            }
        });
    }

    private void switchScene(Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}