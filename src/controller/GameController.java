/**
 * @file GameController.java
 * @brief Diese Klasse ist für die Steuerung des gesamten Spielablaufs verantwortlich.
 */

package controller;

import model.*;
import View.*;
import utils.*;

import javax.swing.*;

/**
 * @class GameController
 * @brief Verwaltet die Hauptlogik und den Ablauf des Spiels.
 */
public class GameController {

    private GameModel gameModel; ///< Das Modell, das den Zustand des Spiels hält.
    private GameView gameView; ///< Die Ansicht, die die grafische Benutzeroberfläche des Spiels darstellt.
    private HomeScreenView homeScreenView; ///< Die Ansicht des Startbildschirms.
    private BoardController boardController; ///< Controller für die Spielfelder.
    private ShipController shipController; ///< Controller für die Schiffe und deren Platzierung.

    /**
     * @brief Konstruktor, der den GameController initialisiert und die Startbildschirm-Listener aktiviert.
     * @param gameModel Das Modell des Spiels.
     * @param gameView Die Ansicht des Spiels.
     * @param homeScreenView Die Ansicht des Startbildschirms.
     */
    public GameController(GameModel gameModel, GameView gameView, HomeScreenView homeScreenView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.homeScreenView = homeScreenView;
        this.boardController = new BoardController(gameModel, gameView, this);
        this.shipController = new ShipController(gameModel, gameView);

        startHomeScreenListeners();
    }

    /**
     * @brief Aktiviert die Listener für die Schaltflächen auf dem Startbildschirm.
     */
    public void startHomeScreenListeners() {
        homeScreenView.getLocalGameButton().addActionListener(e -> startGame(GameState.NORMAL));
        homeScreenView.getComputerGameButton().addActionListener(e -> startGame(GameState.COMPUTER));
        homeScreenView.getDebugModeButton().addActionListener(e -> startGame(GameState.DEBUG));
        homeScreenView.getExitButton().addActionListener(e -> System.exit(0));
    }

    /**
     * @brief Zeigt den Startbildschirm an und versteckt die Spielansicht.
     */
    public void showHomeScreen() {
        homeScreenView.setVisible(true);
        gameView.setVisible(false);
    }

    /**
     * @brief Startet ein neues Spiel basierend auf dem angegebenen Spielmodus.
     * @param gameState Der Modus, in dem das Spiel gestartet werden soll.
     */
    public void startGame(GameState gameState) {
        // Sicherstellen, dass alles zurückgesetzt ist
        homeScreenView.setVisible(false);
        gameModel.setGameState(gameState);
        String playerOneName = JOptionPane.showInputDialog("Bitte Namen für Spieler 1 eingeben:");
        String playerTwoName = (gameState == GameState.NORMAL || gameState == GameState.DEBUG)
                ? JOptionPane.showInputDialog("Bitte Namen für Spieler 2 eingeben:")
                : "Computer";

        gameModel.createPlayerWithNames(playerOneName, playerTwoName);
        gameView.setVisible(true);
        gameView.createPlayerBase(gameModel.getPlayerOne(), gameModel.getPlayerTwo());
        gameView.updateGameModePanel(detectGameMode());
        gameModel.startGame();
        boardController.startGameListeners();

        if (gameState == GameState.NORMAL || gameState == GameState.COMPUTER) {
            SwingUtilities.invokeLater(() -> {
                gameView.getPlayerBoardOne().createPanelForShipPlacement();
                gameView.getPlayerBoardTwo().createPanelForShipPlacement();
                shipController.handleManualShipPlacement(() -> {
                    gameView.getPlayerBoardOne().removePanelForShipPlacement(gameModel.getPlayerOne().getBoard());
                    removePanelForShipPlacement();
                });
            });
        } else {
            runGameLoop();
        }
    }

    /**
     * @brief Entfernt die Panels zur Schiffsplatzierung und startet den Spielablauf.
     */
    private void removePanelForShipPlacement() {
        gameView.getPlayerBoardOne().removePanelForShipPlacement(gameModel.getPlayerOne().getBoard());
        gameView.getPlayerBoardTwo().removePanelForShipPlacement(gameModel.getPlayerTwo().getBoard());
        gameView.getPlayerBoardOne().createLabelForBoard();
        gameView.getPlayerBoardTwo().createLabelForBoard();
        gameView.getStatusView().updateAdditionalInfo("");
        SwingUtilities.invokeLater(this::runGameLoop);
    }

    /**
     * @brief Erkennt den aktuellen Spielmodus und gibt eine entsprechende Beschreibung zurück.
     * @return Eine Zeichenkette, die den aktuellen Spielmodus beschreibt.
     */
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

    /**
     * @brief Führt die Hauptspielschleife aus und verwaltet die Sichtbarkeit der Spielbretter.
     */
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

    /**
     * @brief Zeigt den Game-Over-Bildschirm an und fragt den Spieler, ob er ein neues Spiel starten möchte.
     */
    public void showGameOverScreen() {
        String winner = gameModel.getCurrentPlayer().getPlayerName();
        int result = gameView.showGameOverDialog(winner);

        if (result == 0) {
            // Neues Spiel im gleichen Modus
            startGame(gameModel.getGameState());
        } else {
            // Zurück zum Hauptmenü
            resetGame();
            showHomeScreen();
        }
    }

    /**
     * @brief Setzt das Spiel zurück.
     */
    public void resetGame() {
        gameView.resetView();
        gameModel.resetGame();
        boardController.reset();
        this.gameView.setVisible(false);
        showHomeScreen();
        startHomeScreenListeners();
    }

    /**
     * @brief Führt den Spielzug des Computergegners aus.
     */
    public void performComputerMove() {
        boolean hit;
        do {
            if (gameModel.getCurrentPlayer() instanceof ComputerPlayerModel) {
                hit = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).makeMove(gameModel.getPlayerOne());

                int lastX = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).getLastMoveX();
                int lastY = ((ComputerPlayerModel) gameModel.getCurrentPlayer()).getLastMoveY();

                BoardView playerBoardView = gameView.getPlayerBoardOne();
                CellModel targetCell = gameModel.getPlayerOne().getBoard().getCell(lastX, lastY);

                if (targetCell.getCellState() == CellState.FREE) {
                    playerBoardView.markAsMiss(playerBoardView.getLabelForCell(lastX, lastY));
                } else if (targetCell.getCellState() == CellState.SET){
                    playerBoardView.updateBoard(gameModel.getPlayerOne().getBoard());
                }

                gameView.getPlayerBoardOne().updateBoard(gameModel.getPlayerOne().getBoard());
                gameView.getPlayerBoardTwo().updateBoard(gameModel.getPlayerTwo().getBoard());

                if (gameModel.isGameOver()) {
                    showGameOverScreen();
                    return;
                }
            } else {
                break;
            }
        } while (hit);
        gameModel.switchPlayer();
        runGameLoop();
    }
}