package controller;

import model.*;
import view.*;
import utils.*;

import javax.swing.*;

/**
 * @brief Hauptkontrollklasse für das Spiel.
 *
 * Diese Klasse koordiniert die Interaktionen zwischen dem Spielmodell und den Ansichten.
 * Sie steuert den Spielablauf, die Initialisierung und die Übergänge zwischen verschiedenen Spielzuständen.
 */
public class GameController {

    private GameModel gameModel;
    private GameView gameView;
    private HomeScreenView homeScreenView;
    private BoardController boardController;
    private ShipController shipController;

    /**
     * @brief Konstruktor für den GameController, der alle Variablen setzt.
     * @param gameModel Das GameModel-Objekt mit den Daten.
     * @param gameView Das GameView-Objekt mit der Ansicht der Daten.
     * @param homeScreenView Die Ansicht des Startbildschirms.
     */
    public GameController(GameModel gameModel, GameView gameView, HomeScreenView homeScreenView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.homeScreenView = homeScreenView;
        this.boardController = new BoardController(gameModel, gameView, this);
        this.shipController = new ShipController(gameModel, gameView);

        this.startHomeScreenListeners();
    }

    /**
     * @brief Initialisiert die ActionListener für den Startbildschirm und ordnet ihnen Aktionen zu.
     */
    public void startHomeScreenListeners() {
        this.homeScreenView.getLocalGameButton().addActionListener(e -> startGame(GameState.NORMAL));
        this.homeScreenView.getComputerGameButton().addActionListener(e -> startGame(GameState.COMPUTER));
        this.homeScreenView.getDebugModeButton().addActionListener(e -> startGame(GameState.DEBUG));
        this.homeScreenView.getExitButton().addActionListener(e -> System.exit(0));
    }

    /**
     * @brief Startet das Spiel mit dem angegebenen Spielzustand.
     * @param gameState Der Spielzustand, in dem das Spiel gestartet werden soll.
     */
    public void startGame(GameState gameState) {
        this.prepareGameStart(gameState);
        this.startPlayerShipPlacement(gameState);
    }

    /**
     * @brief Bereitet den Spielstart vor. Dabei wird der HomeScreen deaktiviert, Der GameStatus gesetzt
     * @param gameState Der Spielzustand für die Vorbereitung.
     */
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

    /**
     * @brief Initialisiert die Spieler basierend auf dem Spielzustand.
     * @param gameState Der aktuelle Spielzustand.
     */
    private void initializePlayers(GameState gameState) {
        String playerOneName = promptForPlayerName("Bitte Namen für Spieler 1 eingeben:");
        String playerTwoName = (gameState == GameState.NORMAL || gameState == GameState.DEBUG)
                ? promptForPlayerName("Bitte Namen für Spieler 2 eingeben:")
                : "Default Player";
        this.gameModel.createPlayerWithNames(playerOneName, playerTwoName);
    }

    /**
     * @brief Fordert den Spielernamen über ein Dialogfenster an.
     * @param message Die anzuzeigende Nachricht im Dialog.
     * @return Der eingegebene Spielername.
     */
    private String promptForPlayerName(String message) {
        return JOptionPane.showInputDialog(message);
    }

    // Spielablauf und Logik

    /**
     * @brief Startet die Schiffsplatzierung für die Spieler.
     * @param gameState Der aktuelle Spielzustand.
     */
    private void startPlayerShipPlacement(GameState gameState) {
        if (gameState == GameState.NORMAL || gameState == GameState.COMPUTER) {
            JOptionPane.showMessageDialog(this.gameView,"Der Spieler " +
                    this.gameModel.getCurrentPlayer().getPlayerName() + " startet mit dem Platzieren der Schiffe.\n " +
                    "Bei einem Rechtsklick wird das Schiff gedreht.\n " +
                    "Bei dem Scrollen mit der Maus wird die Größe der Schiffe verändert.\n " +
                    "Bei Linksklick wird das Schiff platziert. \n" +
                    "Viel Spaß!"  );
            this.initiateShipPlacement();
        } else {
            this.runGameLoop();
        }
    }

    /**
     * @brief Initiiert den Prozess der Schiffsplatzierung.
     */
    private void initiateShipPlacement() {
        SwingUtilities.invokeLater(() -> {
            this.gameView.getPlayerBoardOne().createPanelForShipPlacement();
            this.gameView.getPlayerBoardTwo().createPanelForShipPlacement();
            this.shipController.handleManualShipPlacement(this::finalizeShipPlacement);
        });
    }

    /**
     * @brief Schließt die Schiffsplatzierung ab.
     */
    private void finalizeShipPlacement() {
        this.removePanelForShipPlacement();
    }

    /**
     * @brief Entfernt die Panels für die Schiffsplatzierung und bereitet das Spiel vor.
     */
    private void removePanelForShipPlacement() {
        this.gameView.getPlayerBoardOne().removePanelForShipPlacement(this.gameModel.getPlayerOne().getBoard());
        this.gameView.getPlayerBoardTwo().removePanelForShipPlacement(this.gameModel.getPlayerTwo().getBoard());
        this.gameView.getPlayerBoardOne().createLabelForBoard();
        this.gameView.getPlayerBoardTwo().createLabelForBoard();
        SwingUtilities.invokeLater(this::runGameLoop);
    }

    /**
     * @brief Erkennt den aktuellen Spielmodus.
     * @return Eine Zeichenkette, die den aktuellen Spielmodus beschreibt.
     */
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

    /**
     * @brief Führt die Hauptspielschleife aus.
     */
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

    /**
     * @brief Zeigt den Game Over Bildschirm an.
     */
    public void showGameOverScreen() {
        String winner = this.gameModel.getCurrentPlayer().getPlayerName();
        this.gameView.showGameOverDialog(winner);
        System.exit(0);
    }

    /**
     * @brief Führt den Zug des Computerspielers aus.
     */
    public void performComputerMove() {
        boolean hit;
        do {
            this.gameModel.getPlayerTwo().getPlayerStatus().updateTotalClicks();
            hit = this.executeComputerMove();
            this.updateGameAfterMove();
        } while (hit && !this.gameModel.isGameOver());
        this.gameModel.switchPlayer();
        this.runGameLoop();
    }

    /**
     * @brief Führt einen einzelnen Computerzug aus.
     * @return true, wenn der Zug ein Treffer war, sonst false.
     */
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

    /**
     * @brief Aktualisiert das Spielerbrett nach einem Zug.
     * @param lastX X-Koordinate des letzten Zuges.
     * @param lastY Y-Koordinate des letzten Zuges.
     */
    private void updatePlayerBoardAfterMove(int lastX, int lastY) {
        BoardView playerBoardView = this.gameView.getPlayerBoardOne();
        CellModel targetCell = this.gameModel.getPlayerOne().getBoard().getCell(lastX, lastY);

        if (targetCell.getCellState() == CellState.FREE) {
            playerBoardView.markAsMiss(playerBoardView.getLabelForCell(lastX, lastY));
        } else if (targetCell.getCellState() == CellState.SET) {
            playerBoardView.updateBoard(this.gameModel.getPlayerOne().getBoard());
        }
    }

    /**
     * @brief Aktualisiert das Spiel nach einem Zug.
     */
    private void updateGameAfterMove() {
        this.gameModel.getPlayerTwo().getPlayerStatus().calculateShunkShips(this.gameModel.getPlayerOne().getBoard());
        this.gameModel.getPlayerTwo().getPlayerStatus().calculateHits(this.gameModel.getPlayerOne().getBoard());
        this.gameView.getInfoPanelViewOne().updateStats(this.gameModel.getPlayerTwo());
        this.gameView.getPlayerBoardOne().updateBoard(this.gameModel.getPlayerOne().getBoard());
        this.gameView.getPlayerBoardTwo().updateBoard(this.gameModel.getPlayerTwo().getBoard());

        if (this.gameModel.isGameOver()) {
            this.showGameOverScreen();
        }
    }
}