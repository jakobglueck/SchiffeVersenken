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

        startHomeScreenListeners();
    }

    public void startHomeScreenListeners() {
        homeScreenView.getLocalGameButton().addActionListener(e -> startGame(GameState.NORMAL));
        homeScreenView.getComputerGameButton().addActionListener(e -> startGame(GameState.COMPUTER));
        homeScreenView.getDebugModeButton().addActionListener(e -> startGame(GameState.DEBUG));
        homeScreenView.getExitButton().addActionListener(e -> System.exit(0));
    }

    public void startGame(GameState gameState) {
        prepareGameStart(gameState);
        startPlayerShipPlacement(gameState);
    }

    private void prepareGameStart(GameState gameState) {
        homeScreenView.setVisible(false);
        gameModel.setGameState(gameState);
        initializePlayers(gameState);
        gameView.setVisible(true);
        gameView.setupGameInterface(gameModel.getPlayerOne(), gameModel.getPlayerTwo());
        gameView.updateGameModePanel(detectGameMode());
        gameModel.startGame();
        boardController.startGameListeners();
    }

    private void initializePlayers(GameState gameState) {
        String playerOneName = promptForPlayerName("Bitte Namen für Spieler 1 eingeben:");
        String playerTwoName = (gameState == GameState.NORMAL || gameState == GameState.DEBUG)
                ? promptForPlayerName("Bitte Namen für Spieler 2 eingeben:")
                : "Default Player";
        gameModel.createPlayerWithNames(playerOneName, playerTwoName);
    }

    private String promptForPlayerName(String message) {
        return JOptionPane.showInputDialog(message);
    }

    private void startPlayerShipPlacement(GameState gameState) {
        if (gameState == GameState.NORMAL || gameState == GameState.COMPUTER) {
            initiateShipPlacement();
        } else {
            runGameLoop();
        }
    }

    private void initiateShipPlacement() {
        SwingUtilities.invokeLater(() -> {
            gameView.getPlayerBoardOne().createPanelForShipPlacement();
            gameView.getPlayerBoardTwo().createPanelForShipPlacement();
            shipController.handleManualShipPlacement(this::finalizeShipPlacement);
        });
    }

    private void finalizeShipPlacement() {
        removePanelForShipPlacement();
    }

    private void removePanelForShipPlacement() {
        gameView.getPlayerBoardOne().removePanelForShipPlacement(gameModel.getPlayerOne().getBoard());
        gameView.getPlayerBoardTwo().removePanelForShipPlacement(gameModel.getPlayerTwo().getBoard());
        gameView.getPlayerBoardOne().createLabelForBoard();
        gameView.getPlayerBoardTwo().createLabelForBoard();
        SwingUtilities.invokeLater(this::runGameLoop);
    }

    private String detectGameMode() {
        switch (gameModel.getGameState()) {
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
                return;
            }
            performComputerMove();
        }
    }

    public void showGameOverScreen() {
        String winner = gameModel.getCurrentPlayer().getPlayerName();
        this.gameView.showGameOverDialog(winner);
        System.exit(0);
    }

    public void performComputerMove() {
        boolean hit;
        do {
            hit = executeComputerMove();
            updateGameAfterMove();
        } while (hit && !gameModel.isGameOver());
        gameModel.switchPlayer();
        runGameLoop();
    }

    private boolean executeComputerMove() {
        if (!(gameModel.getCurrentPlayer() instanceof ComputerPlayerModel)) {
            return false;
        }

        boolean hit = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).makeMove(gameModel.getPlayerOne());
        int lastX = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).getLastMoveX();
        int lastY = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).getLastMoveY();

        updatePlayerBoardAfterMove(lastX, lastY);
        return hit;
    }

    private void updatePlayerBoardAfterMove(int lastX, int lastY) {
        BoardView playerBoardView = gameView.getPlayerBoardOne();
        CellModel targetCell = gameModel.getPlayerOne().getBoard().getCell(lastX, lastY);

        if (targetCell.getCellState() == CellState.FREE) {
            playerBoardView.markAsMiss(playerBoardView.getLabelForCell(lastX, lastY));
        } else if (targetCell.getCellState() == CellState.SET) {
            playerBoardView.updateBoard(gameModel.getPlayerOne().getBoard());
        }
    }

    private void updateGameAfterMove() {
        gameView.getPlayerBoardOne().updateBoard(gameModel.getPlayerOne().getBoard());
        gameView.getPlayerBoardTwo().updateBoard(gameModel.getPlayerTwo().getBoard());

        if (gameModel.isGameOver()) {
            showGameOverScreen();
        }
    }
}
