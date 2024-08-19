/**
 * @file ShipController.java
 */

package controller;

import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Kontrollklasse für die Schiffsplatzierung im Spiel.
 *
 * Diese Klasse verwaltet die Platzierung von Schiffen auf dem Spielfeld,
 * einschließlich der manuellen Platzierung durch Spieler und der Vorbereitung
 * für den Spielbeginn. Sie koordiniert die Interaktionen zwischen dem GameModel
 * und der GameView während der Schiffsplatzierungsphase.
 */
public class ShipController {
    // Instanz die Daten hält
    private GameModel gameModel;
    // Instanz die Spielansicht hält
    private GameView gameView;
    // Index des aktuell zu platzierenden Schiffs
    private int currentShipIndex;
    // Ausrichtung des aktuellen Schiffs zu platzierenden Schiffs
    private boolean isHorizontal;
    // Liste der Schiffsgrößen für Spieler 1
    private List<Integer> playerOneShips;
    // Liste der Schiffsgrößen für Spieler 2
    private List<Integer> playerTwoShips;

    /**
     * @brief Konstruktor für den ShipController der die Klassenvariablen setzt.
     * @param gameModel Instanz der GameModel die Daten.
     * @param gameView Die Spielansicht.
     */
    public ShipController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.currentShipIndex = 0;
        this.isHorizontal = true;

        this.initializeShipLists();
    }

    /**
     * @brief Initialisiert die Listen der Schiffe für beide Spieler.
     *
     * Erstellt zwei separate Listen mit den Schiffsgrößen für jeden Spieler,
     * basierend auf den im GameModel definierten Schiffsgrößen.
     */
    private void initializeShipLists() {
        this.playerOneShips = new ArrayList<>();
        this.playerTwoShips = new ArrayList<>();

        for (int size : this.gameModel.getShipSizes()) {
            this.playerOneShips.add(size);
            this.playerTwoShips.add(size);
        }
    }

    /**
     * @brief Startet die manuelle Schiffsplatzierung.
     * @param onComplete Runnable, das nach Abschluss der Platzierung ausgeführt wird.
     *
     * Bestimmt die Anzahl der Platzierungsrunden und initiiert den Platzierungsprozess.
     */
    public void handleManualShipPlacement(Runnable onComplete) {
        int shipTurns = this.determineShipTurns();
        this.placeShipsForPlayer(0, shipTurns, onComplete);
    }

    /**
     * @brief Bestimmt die Anzahl der Züge für die Schiffsplatzierung.
     * @return 1, wenn Spieler 2 ein Computer ist, sonst 2, um zwei Runden Schiffen zu platzieren.
     */
    private int determineShipTurns() {
        return (this.gameModel.getPlayerTwo() instanceof ComputerPlayerModel) ? 1 : 2;
    }

    /**
     * @brief Führt die Schiffsplatzierung für den aktuellen Spieler durch.
     * @param currentTurn Aktueller Zug.
     * @param totalTurns Gesamtzahl der Züge.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     *
     * Koordiniert den Platzierungsprozess für jeden Spieler, wechselt zwischen den Spielern
     * und beendet den Prozess, wenn alle Schiffe platziert sind.
     */
    private void placeShipsForPlayer(int currentTurn, int totalTurns, Runnable onComplete) {
        PlayerModel currentPlayer = this.gameModel.getCurrentPlayer();
        BoardView currentBoard = this.getCurrentBoardForPlayer(currentPlayer);
        List<Integer> remainingShips = this.getRemainingShipsForPlayer(currentPlayer);

        // Macht das Board, auf das Schiffe platziert werden, sichtbar.
        this.prepareBoardForPlacement(currentBoard);

        this.placeShipsForCurrentPlayer(currentBoard, remainingShips, () -> {
            // Wechselt die manuelle Schiffsplatzierung bei dem Normal-Modus
            if (currentTurn < totalTurns - 1) {

                this.gameView.updateBoardVisibility(this.gameModel, this.gameModel.getGameState());
                this.proceedToNextTurn(currentBoard, currentPlayer);
                this.placeShipsForPlayer(currentTurn + 1, totalTurns, onComplete);
            } else {
                // beendet die Schiffsplatzierung
                this.finalizeShipPlacement(currentBoard, onComplete);
            }
        });
    }

    /**
     * @brief Bereitet das Board für die Schiffsplatzierung vor.
     * @param board Das Board, welches Sichtbar geamcht werden soll.
     *
     * Setzt den aktuellen Schiffsindex zurück, macht das Brett sichtbar und
     * aktualisiert die Statusanzeige mit dem Namen des aktuellen Spielers.
     */
    private void prepareBoardForPlacement(BoardView board) {
        this.currentShipIndex = 0;
        board.setVisible(true);
        board.toggleGridVisibility(true);
        this.gameView.getGameInfoView().updateStatusMessageLabel(this.gameModel.getCurrentPlayer().getPlayerName() + " muss seine Schiffe platzieren");
    }

    /**
     * @brief Platziert Schiffe für den aktuellen Spieler.
     * @param board Das aktuelle Board.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     *
     * Initialisiert die Maussteuerung für die Schiffsplatzierung und fügt die entsprechenden
     * Listener zum Board hinzu.
     */
    private void placeShipsForCurrentPlayer(BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        this.currentShipIndex = 0;
        this.isHorizontal = false;
        // Erstellt Mouseadapter für den Umgang mit einem Klick oder einer Mausbewegung
        MouseAdapter mouseAdapter = this.createMouseAdapterForPlacement(board, remainingShips, onComplete);
        // Aktiviert die MouseListner wie Mausrad, Links- und Rechtsklick
        board.addMouseWheelListener(e -> handleMouseWheelRotation(e, board, remainingShips));
        board.addMouseListener(mouseAdapter);
        board.addMouseMotionListener(mouseAdapter);
    }

    /**
     * @brief Ermittelt das aktuelle Board für den gegebenen Spieler.
     * @param player Der aktuelle Spieler.
     * @return Das BoardView-Objekt des Spielers.
     */
    private BoardView getCurrentBoardForPlayer(PlayerModel player) {
        return (player == this.gameModel.getPlayerOne()) ? this.gameView.getPlayerBoardOne() : this.gameView.getPlayerBoardTwo();
    }

    /**
     * @brief Ermittelt die Liste der verbleibenden Schiffe für den aktuellen Spieler.
     * @param player Der aktuelle Spieler.
     * @return Liste der verbleibenden Schiffe.
     */
    private List<Integer> getRemainingShipsForPlayer(PlayerModel player) {
        return (player == this.gameModel.getPlayerOne()) ? this.playerOneShips : this.playerTwoShips;
    }

    /**
     * @brief Wechselt zum nächsten Spieler nach der Schiffsplatzierung.
     * @param currentBoard Das aktuelle Baord.
     * @param currentPlayer Der aktuelle Spieler.
     *
     * Aktualisiert das Board, entfernt die Grafiken und wechselt zum nächsten Spieler.
     */
    private void proceedToNextTurn(BoardView currentBoard, PlayerModel currentPlayer) {
        currentBoard.updateBoard(currentPlayer.getBoard());
        // Entfernt die blauen Labels vom Board
        currentBoard.removeGraphics();
        this.gameModel.resetShipPlacement();
        // wechselt den Spieler
        this.gameModel.switchPlayer();
    }

    /**
     * @brief Schließt die Schiffsplatzierung ab.
     * @param currentBoard Das aktuelle Board.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     *
     * Entfernt die Grafiken, zeigt eine Meldung an und führt die Aktion zum Ende aus.
     */
    private void finalizeShipPlacement(BoardView currentBoard, Runnable onComplete) {
        currentBoard.removeGraphics();
        JOptionPane.showMessageDialog(gameView, "Alle Schiffe wurden platziert. Das Spiel beginnt!");
        currentBoard.toggleGridVisibility(false);
        onComplete.run();
    }

    /**
     * @brief Erstellt einen MouseAdapter für die Schiffsplatzierung.
     * @param board Das aktuelle Board.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     * @return Ein MouseAdapter für die Schiffsplatzierung.
     *
     * Definiert das Verhalten für Mausklicks und Mausbewegungen während der Schiffsplatzierung.
     */
    private MouseAdapter createMouseAdapterForPlacement(BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e, board, remainingShips, onComplete);
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                updateShipPreview(e.getX(), e.getY(), board, remainingShips);
            }
        };
    }

    /**
     * @brief Behandelt Mausklicks während der Schiffsplatzierung.
     * @param e Das MouseEvent.
     * @param board Das aktuelle Board.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     *
     * Verarbeitet Rechts- und Linksklicks für die Rotation und Platzierung von Schiffen.
     */
    private void handleMouseClick(MouseEvent e, BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        if (SwingUtilities.isRightMouseButton(e)) {
            // Wechsel der Ausrichtung des Schiffes
            this.isHorizontal = !this.isHorizontal;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            // Versuch das Schiff an dieser Stelle zu platzieren
            this.attemptShipPlacement(e, board, remainingShips, onComplete);
        }
    }

    /**
     * @brief Versucht, ein Schiff zu platzieren.
     * @param e Das MouseEvent.
     * @param board Das aktuelle Board.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     *
     * Überprüft die Gültigkeit der Platzierung, platziert das Schiff wenn möglich,
     * und aktualisiert den Spielzustand entsprechend.
     */
    private void attemptShipPlacement(MouseEvent e, BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        int x = e.getY() / board.getCellSize();
        int y = e.getX() / board.getCellSize();

        int shipSize = remainingShips.get(currentShipIndex);

        // Überprüfe, ob die Platzierung gültig ist und platziere das Schiff
        if (this.currentShipIndex < remainingShips.size() && this.gameModel.placeNextShip(x, y, shipSize, !this.isHorizontal)) {
            board.addGraphicsToCells(x, y, shipSize, this.isHorizontal);
            remainingShips.remove(this.currentShipIndex);

            if (remainingShips.isEmpty()) {
                completePlacement(board, onComplete);
            } else {
                shrinkShipIndex(remainingShips);
                // Überprüfe, ob die verbleibenden Schiffe noch platziert werden können
                if (!canPlaceRemainingShips(remainingShips)) {
                    // Spiel ist vorbei, wenn kein Schiff mehr platziert werden kann.
                    showGameOverDialog();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this.gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
        }
    }

    /**
     * @brief Überprüft, ob alle verbleibenden Schiffe platziert werden können.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @return true, wenn alle Schiffe platziert werden können, sonst false.
     *
     * Durchläuft alle möglichen Positionen und Ausrichtungen für jedes verbleibende Schiff,
     * um zu prüfen, ob eine gültige Platzierung möglich ist.
     */
    private boolean canPlaceRemainingShips(List<Integer> remainingShips) {
        BoardModel currentBoard = this.gameModel.getCurrentPlayer().getBoard();
        // Überprüft ob die Restlichen Schiffgrößen platziert werden können
        for (Integer shipSize : remainingShips) {
            // default wert ist false, wenn platziert werden kann, dann ändert er sich zu true
            boolean canPlace = false;
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    // Überprüft alle möglichen Schiffe platzierungen auf dem Board
                    if (currentBoard.isValidShipPlacement(x, y, true, shipSize) ||
                            currentBoard.isValidShipPlacement(x, y, false, shipSize)) {
                        canPlace = true;
                        break;
                    }
                }
                if (canPlace){
                    break;
                }
            }
            if (!canPlace) {
                return false;
            }
        }
        return true;
    }

    /**
     * @brief Zeigt einen Dialog an, der das Spiel beendet.
     *
     * Wird aufgerufen, wenn keine gültige Platzierung für die verbleibenden Schiffe möglich ist und beendet das Spiel.
     */
    private void showGameOverDialog() {
        JOptionPane.showMessageDialog(this.gameView,
                "Es können nicht alle Schiffe platziert werden. Das Spiel muss neu gestartet werden.",
                "Spiel beendet",
                JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    /**
     * @brief Schließt die Platzierung ab und entfernt Event-Listener.
     * @param board Das aktuelle Board.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     */
    private void completePlacement(BoardView board, Runnable onComplete) {
        board.removeMouseListener(board.getMouseListeners()[0]);
        board.removeMouseMotionListener(board.getMouseMotionListeners()[0]);
        board.hideShipPreview();
        onComplete.run();
    }

    /**
     * @brief Passt den Index des aktuellen Schiffs an.
     * @param remainingShips Liste der verbleibenden Schiffe.
     *
     * Stellt sicher, dass der currentShipIndex nicht größer ist als die Anzahl der verbleibenden Schiffe.
     */
    private void shrinkShipIndex(List<Integer> remainingShips) {
        this.currentShipIndex = Math.min(this.currentShipIndex, remainingShips.size() - 1);
    }

    /**
     * @brief Behandelt die Rotation des Mausrads zur Schiffsauswahl.
     * @param e Das MouseWheelEvent.
     * @param board Das aktuelle Board.
     * @param remainingShips Liste der verbleibenden Schiffe.
     *
     * Ermöglicht dem Spieler, durch die verfügbaren Schiffe zu scrollen und aktualisiert die Vorschau entsprechend.
     */
    private void handleMouseWheelRotation(MouseWheelEvent e, BoardView board, List<Integer> remainingShips) {
        // Scrollen nach Oben geht von hinten die Liste durch, sonst von vorne.
        if (e.getWheelRotation() < 0) {
            this.currentShipIndex = Math.max(0, this.currentShipIndex - 1);
        } else {
            this.currentShipIndex = Math.min(remainingShips.size() - 1, this.currentShipIndex + 1);
        }
        Point mousePosition = board.getMousePosition();
        if (mousePosition != null) {
            // Ändert die View abhängig von Mausposition und die Länge des Schiffes.
            this.updateShipPreview(mousePosition.x, mousePosition.y, board, remainingShips);
        }
    }

    /**
     * @brief Aktualisiert die Vorschau des zu platzierenden Schiffs.
     * @param mouseX X-Koordinate der Maus.
     * @param mouseY Y-Koordinate der Maus.
     * @param board Das aktuelle Board.
     * @param remainingShips Liste der verbleibenden Schiffe.
     *
     * Berechnet die Position und Größe des Vorschau-Rechtecks basierend auf der aktuellen Mausposition
     * und der Ausrichtung des Schiffs. Aktualisiert dann die Vorschau auf dem Board.
     */
    private void updateShipPreview(int mouseX, int mouseY, BoardView board, List<Integer> remainingShips) {
        if (remainingShips.isEmpty()){
            return;
        }

        int shipLength = remainingShips.get(this.currentShipIndex);
        int width = this.isHorizontal ? shipLength * board.getCellSize() : board.getCellSize();
        int height = this.isHorizontal ? board.getCellSize() : shipLength * board.getCellSize();

        int x = (mouseX / board.getCellSize()) * board.getCellSize();
        int y = (mouseY / board.getCellSize()) * board.getCellSize();

        board.updateShipPreview(x, y, width, height);
    }
}