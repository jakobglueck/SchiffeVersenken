package controller;

import model.*;
import view.*;
import utils.*;

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

        this.startHomeScreenListeners();
    }

    public void startHomeScreenListeners() {
        this.homeScreenView.getLocalGameButton().addActionListener(e -> startGame(GameState.NORMAL));
        this.homeScreenView.getComputerGameButton().addActionListener(e -> startGame(GameState.COMPUTER));
        this.homeScreenView.getDebugModeButton().addActionListener(e -> startGame(GameState.DEBUG));
        this.homeScreenView.getExitButton().addActionListener(e -> System.exit(0));
    }

    public void startGame(GameState gameState) {
        this.prepareGameStart(gameState);
        this.startPlayerShipPlacement(gameState);
    }

    private void prepareGameStart(GameState gameState) {
        this.homeScreenView.setVisible(false);
        this.gameModel.setGameState(gameState);
        this.initializePlayers(gameState);
        this.gameView.setVisible(true);
        this.gameView.setupGameInterface(this.gameModel.getPlayerOne(), this.gameModel.getPlayerTwo());
        this.gameView.updateGameModePanel(detectGameMode());
        this.gameModel.startGame();
        this.boardController.startGameListeners();
    }

    private void initializePlayers(GameState gameState) {
        String playerOneName = promptForPlayerName("Bitte Namen für Spieler 1 eingeben:");
        String playerTwoName = (gameState == GameState.NORMAL || gameState == GameState.DEBUG)
                ? promptForPlayerName("Bitte Namen für Spieler 2 eingeben:")
                : "Default Player";
        this.gameModel.createPlayerWithNames(playerOneName, playerTwoName);
    }

    private String promptForPlayerName(String message) {
        return JOptionPane.showInputDialog(message);
    }

    private void startPlayerShipPlacement(GameState gameState) {
        if (gameState == GameState.NORMAL || gameState == GameState.COMPUTER) {
            this.initiateShipPlacement();
        } else {
            this.runGameLoop();
        }
    }

    private void initiateShipPlacement() {
        SwingUtilities.invokeLater(() -> {
            this.gameView.getPlayerBoardOne().createPanelForShipPlacement();
            this.gameView.getPlayerBoardTwo().createPanelForShipPlacement();
            this.shipController.handleManualShipPlacement(this::finalizeShipPlacement);
        });
    }

    private void finalizeShipPlacement() {
        this.removePanelForShipPlacement();
    }

    private void removePanelForShipPlacement() {
        this.gameView.getPlayerBoardOne().removePanelForShipPlacement(this.gameModel.getPlayerOne().getBoard());
        this.gameView.getPlayerBoardTwo().removePanelForShipPlacement(this.gameModel.getPlayerTwo().getBoard());
        this.gameView.getPlayerBoardOne().createLabelForBoard();
        this.gameView.getPlayerBoardTwo().createLabelForBoard();
        SwingUtilities.invokeLater(this::runGameLoop);
    }

    private String detectGameMode() {
        switch (this.gameModel.getGameState()) {
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
        this.boardController.updateBoardVisibility();
        this.boardController.updateGameView();

        if (this.gameModel.getGameState() == GameState.DEBUG) {
            this.boardController.enableBothBoards();
        } else {
            this.boardController.toggleBoardsForCurrentPlayer();
            if (!(this.gameModel.getCurrentPlayer() instanceof ComputerPlayerModel)) {
                return;
            }
            this.performComputerMove();
        }
    }

    public void showGameOverScreen() {
        String winner = this.gameModel.getCurrentPlayer().getPlayerName();
        this.gameView.showGameOverDialog(winner);
        System.exit(0);
    }

    public void performComputerMove() {
        boolean hit;
        do {
            hit = this.executeComputerMove();
            this.updateGameAfterMove();
        } while (hit && !this.gameModel.isGameOver());
        this.gameModel.switchPlayer();
        this.runGameLoop();
    }

    private boolean executeComputerMove() {
        if (!(this.gameModel.getCurrentPlayer() instanceof ComputerPlayerModel)) {
            return false;
        }

        boolean hit = ((ComputerPlayerModel) this.gameModel.getCurrentPlayer()).makeMove(this.gameModel.getPlayerOne());
        int lastX = ((ComputerPlayerModel) this.gameModel.getCurrentPlayer()).getLastMoveX();
        int lastY = ((ComputerPlayerModel) this.gameModel.getCurrentPlayer()).getLastMoveY();

        this.updatePlayerBoardAfterMove(lastX, lastY);
        return hit;
    }

    private void updatePlayerBoardAfterMove(int lastX, int lastY) {
        BoardView playerBoardView = this.gameView.getPlayerBoardOne();
        CellModel targetCell = this.gameModel.getPlayerOne().getBoard().getCell(lastX, lastY);

        if (targetCell.getCellState() == CellState.FREE) {
            playerBoardView.markAsMiss(playerBoardView.getLabelForCell(lastX, lastY));
        } else if (targetCell.getCellState() == CellState.SET) {
            playerBoardView.updateBoard(this.gameModel.getPlayerOne().getBoard());
        }
    }

    private void updateGameAfterMove() {
        this.gameView.getPlayerBoardOne().updateBoard(this.gameModel.getPlayerOne().getBoard());
        this.gameView.getPlayerBoardTwo().updateBoard(this.gameModel.getPlayerTwo().getBoard());

        if (this.gameModel.isGameOver()) {
            this.showGameOverScreen();
        }
    }
}
