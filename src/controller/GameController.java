package controller;

import View.BoardView;
import model.CellModel;
import model.ComputerPlayerModel;
import model.GameModel;
import View.GameView;
import View.HomeScreenView;
import utils.CellState;
import utils.GameState;

import javax.swing.*;

public class GameController {

    private GameModel gameModel;
    private GameView gameView;
    private HomeScreenView homeScreenView;
    private BoardController boardController;
    private ShipController shipController;

    public GameController(GameModel gameModel, GameView gameView, HomeScreenView homeScreenView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.homeScreenView = homeScreenView;
        this.boardController = new BoardController(gameModel, gameView, this);
        this.shipController = new ShipController(gameModel, gameView);

        initializeHomeScreenListeners();
    }

    public void initializeHomeScreenListeners() {
        homeScreenView.getLocalGameButton().addActionListener(e -> startGame(GameState.NORMAL));
        homeScreenView.getComputerGameButton().addActionListener(e -> startGame(GameState.COMPUTER));
        homeScreenView.getDebugModeButton().addActionListener(e -> startGame(GameState.DEBUG));
        homeScreenView.getExitButton().addActionListener(e -> System.exit(0));
    }

    public void showHomeScreen() {
        homeScreenView.setVisible(true);
        gameView.setVisible(false);
    }

    public void startGame(GameState gameState) {
        homeScreenView.setVisible(false);
        gameModel.setGameState(gameState);
        String playerOneName = JOptionPane.showInputDialog("Bitte Namen für Spieler 1 eingeben:");
        String playerTwoName = (gameState == GameState.NORMAL || gameState == GameState.DEBUG)
                ? JOptionPane.showInputDialog("Bitte Namen für Spieler 2 eingeben:")
                : "Computer";

        gameModel.createPlayerWithNames(playerOneName, playerTwoName);
        gameView.setVisible(true);
        gameView.createPlayerBase();
        this.gameView.updateGameModePanel(this.detectGameMode());
        gameModel.startGame();
        boardController.initializeGameListeners();

        if (gameState == GameState.NORMAL || gameState == GameState.COMPUTER) {
            SwingUtilities.invokeLater(() -> {
                gameView.getPlayerBoardOne().createPanelForShipPlacement();
                gameView.getPlayerBoardTwo().createPanelForShipPlacement();
                shipController.handleManualShipPlacement(() -> {
                    gameView.getPlayerBoardOne().removePanelForShipPlacement();
                    onShipPlacementComplete();
                });
            });
        } else {
            runGameLoop();
        }
    }

    private void onShipPlacementComplete() {
        this.gameView.getPlayerBoardOne().removePanelForShipPlacement();
        this.gameView.getPlayerBoardTwo().removePanelForShipPlacement();
        SwingUtilities.invokeLater(this::runGameLoop);
    }

    private String detectGameMode(){
        switch(this.gameModel.getGameState()){
            case NORMAL:
                return "Spielmodus: Normal";
            case COMPUTER:
                return "Spielmodus: Computer";
            case DEBUG:
                return "Spielmodus: Debug";
            default:
                return "";
        }
    }

    public void runGameLoop() {
        boardController.updateBoardVisibility();
        boardController.updateGameView();

        if (gameModel.getGameState() == GameState.DEBUG) {
            boardController.enableBothBoards();
        } else {
            boardController.toggleBoardsForCurrentPlayer();
            if (!(gameModel.getCurrentPlayer() instanceof ComputerPlayerModel)) {
                return;  // Player's turn, exit the loop here
            }
            performComputerMove();  // Perform computer's move if it's their turn
        }
    }

    public void showGameOverDialog() {
        String winner = gameModel.getCurrentPlayer().getPlayerName();
        int result = this.gameView.showGameOverDialog(winner);

        if (result == 0) {
            resetGame();
        } else {
            showHomeScreen();
        }
    }

    public void resetGame() {
        gameModel.resetGame();
        gameView.resetView();
        gameModel.startGame();
        runGameLoop();
    }

    public void performComputerMove() {
        if (gameModel.getCurrentPlayer() instanceof ComputerPlayerModel) {
            boolean hit = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).makeMove(gameModel.getPlayerOne());

            int lastX = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).getLastMoveX();
            int lastY = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).getLastMoveY();

            BoardView playerBoardView = gameView.getPlayerBoardOne();
            CellModel targetCell = gameModel.getPlayerOne().getBoard().getCell(lastX, lastY);

            if (targetCell.getCellState() == CellState.FREE) {
                playerBoardView.markAsMiss(playerBoardView.getLabelForCell(lastX, lastY));
            } else if (targetCell.getCellState() == CellState.HIT) {
                playerBoardView.updateBoard();
            }

            this.gameView.getPlayerBoardOne().updateBoard();
            this.gameView.getPlayerBoardTwo().updateBoard();

            if (gameModel.isGameOver()) {
                showGameOverDialog();
                return;
            }

            if (!hit) {
                gameModel.switchPlayer();  // Switch to the human player if it was a miss
            }
        }
        runGameLoop();  // Continue the game loop
    }
}
