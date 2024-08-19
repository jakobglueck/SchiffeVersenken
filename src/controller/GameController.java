package controller;

import model.*;
import view.*;
import utils.*;

import javax.swing.*;

/**
 * @brief Hauptkontrollklasse für das Spiel.
 *        Diese Klasse koordiniert die Interaktionen zwischen dem GameModel mit den Daten und der GameView
 *        der Spielansicht.
 *        Sie koordiniert den Spielablauf, das Setzen und Anzeigen von Spielaktionen und die Übergänge zwischen
 *        verschiedenen Spielzuständen.
 */
public class GameController {
    // Instanz der Spieldaten
    private GameModel gameModel;
    // Instanz der Spielansicht
    private GameView gameView;
    // Instanz des Startbildschirms
    private HomeScreenView homeScreenView;
    // Instanz die das Board steuert
    private BoardController boardController;
    // Instanz die, die Schiffe steuert
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
     * @brief Bereitet den Spielstart vor. Dabei wird der HomeScreen deaktiviert, der GameStatus gesetzt, die Spieler
     *        und deren Board generiert und die Actionlistner des BoardControllers gestartet.
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
     * @brief Initialisiert die Spieler basierend auf dem Spielzustand und setzt sie ins gameModel.
     * @param gameState Der aktuelle Spielzustand.
     */
    private void initializePlayers(GameState gameState) {
        String playerOneName = initializeOptionPanelForPlayerName("Bitte Namen für Spieler 1 eingeben:");
        // bei fehlender Eingabe erhält der Spieler den Namen Default Player
        String playerTwoName = (gameState == GameState.NORMAL || gameState == GameState.DEBUG)
                ? initializeOptionPanelForPlayerName("Bitte Namen für Spieler 2 eingeben:")
                : "Default Player";
        this.gameModel.createPlayerWithNames(playerOneName, playerTwoName);
    }

    /**
     * @brief Fordert den Spielernamen über ein Dialogfenster an.
     * @param message Die anzuzeigende Nachricht im Dialogfenster.
     * @return Der eingegebene Spielername.
     */
    private String initializeOptionPanelForPlayerName(String message) {
        return JOptionPane.showInputDialog(message);
    }

    /**
     * @brief Startet die Schiffsplatzierung für die Spieler und zeigt die Regeln für das Platzieren der Schiffe auf.
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
            this.startShipPlacement();
        } else {
            /* Im Debug-Modus werden die Schiffe automatisch platziert. Dadruch entfällt die manuelle Schiffsplatzierung.
               und der GameLoop startet.*/
            this.runGameLoop();
        }
    }

    /**
     * @brief Initiiert den Prozess der Schiffsplatzierung. Dabei werden die Panels zum setzten der Schiffe auf dem
     *        jeweiligen Board der Spieler platziert und die Platzierung aus dem ShipController aufgerufen.
     */
    private void startShipPlacement() {
        SwingUtilities.invokeLater(() -> {
            this.gameView.getPlayerBoardOne().createPanelForShipPlacement();
            this.gameView.getPlayerBoardTwo().createPanelForShipPlacement();
            this.shipController.handleManualShipPlacement(this::endShipPlacement);
        });
    }

    /**
     * @brief Schließt die Schiffsplatzierung ab und entfernt die Panels der Spieler von der Schiffplatzierung
     */
    private void endShipPlacement() {
        this.removePanelForShipPlacement();
    }

    /**
     * @brief Entfernt die Panels für die Schiffsplatzierung und bereitet das Spiel vor, in dem er Board designt.
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
     * @return Eine String, der den aktuellen Spielmodus beschreibt, um ihn Anzeigen zu lassen.
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
     * @brief Führt die Hauptspielschleife aus. Dabei wird das gegnerische Board abgedeckt und die View update sich.
     *        Wenn sich das Game im Debug-Modus befindet, dann werden beide Boards aufgedeckt.
     *        Wenn der Computer-Modus aktiv ist, dann wird überprüft, welcher Spieler an Zug ist und führt
     *        einen Computerzug, wenn dieser dran ist.

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
            this.makeComputerMove();
        }
    }

    /**
     * @brief Zeigt den Game Over Bildschirm an, wenn ein Spieler gewonnen hat.
     */
    public void showGameOverScreen() {
        String winner = this.gameModel.getCurrentPlayer().getPlayerName();
        this.gameView.showGameOverDialog(winner);
        System.exit(0);
    }

    /**
     * @brief Führt den Zug des Computerspielers aus. Der Computer ist so lange am Zug wie sein Zu ein Schiff getroffen hat.
     *        Wenn er nicht trifft, dann switcht der Spieler und der GameLoop startet.
     */
    public void makeComputerMove() {
        boolean hit;
        do {
            this.gameModel.getPlayerTwo().getPlayerStatus().updateTotalClicks();
            hit = this.showComputerMoveHit();
            this.updateGameAfterMove();
        } while (hit && !this.gameModel.isGameOver());
        this.gameModel.switchPlayer();
        this.runGameLoop();
    }

    /**
     * @brief Überprüft, ob der Zug vom Computer ein Schiff getroffen hat.
     * @return true, wenn der Zug ein Treffer war.
     */
    private boolean showComputerMoveHit() {
        if (!(this.gameModel.getCurrentPlayer() instanceof ComputerPlayerModel)) {
            return false;
        }

        boolean hit = ((ComputerPlayerModel) this.gameModel.getCurrentPlayer()).makeMove(this.gameModel.getPlayerOne());
        int lastX = ((ComputerPlayerModel) this.gameModel.getCurrentPlayer()).getLastMoveX();
        int lastY = ((ComputerPlayerModel) this.gameModel.getCurrentPlayer()).getLastMoveY();

        this.updatePlayerBoardAfterComputerMove(lastX, lastY);
        return hit;
    }

    /**
     * @brief Aktualisiert das Board eines Spielers nach einem Zug eines Computers.
     * @param lastX X-Koordinate des letzten Zuges.
     * @param lastY Y-Koordinate des letzten Zuges.
     */
    private void updatePlayerBoardAfterComputerMove(int lastX, int lastY) {
        BoardView playerBoardView = this.gameView.getPlayerBoardOne();
        CellModel targetCell = this.gameModel.getPlayerOne().getBoard().getCell(lastX, lastY);

        if (targetCell.getCellState() == CellState.FREE) {
            // Wenn das Schiff verfehlt wurde, wird dies auf dem Board angezeigt
            playerBoardView.markAsMiss(playerBoardView.getLabelForCell(lastX, lastY));
        } else if (targetCell.getCellState() == CellState.SET) {
            // Treffer wird auf dem Board angezeigt
            playerBoardView.updateBoard(this.gameModel.getPlayerOne().getBoard());
        }
    }

    /**
     * @brief Aktualisiert das Die Statistiken im gameMOdel für die Spieler und updatet nach einem Zug.
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