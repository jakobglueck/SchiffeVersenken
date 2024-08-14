package controller;

import model.ComputerPlayerModel;
import model.GameModel;
import View.GameView;
import View.HomeScreenView;
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
        this.shipController = new ShipController(gameModel, gameView, this);

        initializeHomeScreenListeners();
    }

    private void initializeHomeScreenListeners() {
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
        String playerOneName = JOptionPane.showInputDialog("Bitte Namen für Spieler 1 eingeben:");
        String playerTwoName = (gameState == GameState.NORMAL || gameState == GameState.DEBUG)
                ? JOptionPane.showInputDialog("Bitte Namen für Spieler 2 eingeben:")
                : "Computer";

        gameModel.createPlayerWithNames(playerOneName, playerTwoName);
        gameModel.setGameState(gameState);
        gameView.setVisible(true);
        gameView.createPlayerBase();
        boardController.initializeGameListeners();
        gameModel.startGame();
        if (gameState == GameState.NORMAL || gameState == GameState.COMPUTER) {
            shipController.handleManualShipPlacement();
        } else {
            runGameLoop();
        }
    }

    public void runGameLoop() {
        boardController.updateBoardVisibility();
        boardController.updateGameView();

        if (gameModel.getGameState() == GameState.DEBUG) {
            boardController.enableBothBoards();
        } else {
            boardController.toggleBoardsForCurrentPlayer();
            if (gameModel.getCurrentPlayer() instanceof ComputerPlayerModel) {
                performComputerMove(); // Wenn der aktuelle Spieler der Computer ist, führe automatisch den Zug aus
            }
        }
    }

    public void showGameOverDialog() {
        String winner = gameModel.getCurrentPlayer().getPlayerName();
        int result = JOptionPane.showOptionDialog(
                gameView,
                "Spiel vorbei! " + winner + " gewinnt!\nMöchtest du ein neues Spiel starten oder zum Hauptmenü zurückkehren?",
                "Spiel beendet",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Neues Spiel", "Hauptmenü"},
                "Neues Spiel"
        );

        if (result == JOptionPane.YES_OPTION) {
            resetGame();  // Neustart des Spiels mit denselben Spielern
        } else {
            showHomeScreen();
        }
    }

    private void resetGame() {
        gameModel.resetGame();  // Zurücksetzen des GameModel
        gameView.resetView();   // Zurücksetzen der Ansicht
        gameModel.startGame();  // Spiel neu starten
        runGameLoop();         // Spielschleife neu starten
    }

    public void performComputerMove() {
        gameModel.computerPlayTurn();  // Führt den Zug des Computers aus
        if (!gameModel.isGameOver()) {
            gameModel.switchPlayer();  // Wenn das Spiel nicht vorbei ist, wechsle den Spieler
            runGameLoop();  // Starte die nächste Runde
        } else {
            showGameOverDialog();  // Zeige das Dialogfenster "Spiel vorbei" an
        }
    }
}
