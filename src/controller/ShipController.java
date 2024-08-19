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
 * für den Spielbeginn.
 */
public class ShipController {

    private GameModel gameModel;
    private GameView gameView;
    private int currentShipIndex;
    private boolean isHorizontal;
    private List<Integer> playerOneShips;
    private List<Integer> playerTwoShips;

    /**
     * @brief Konstruktor für den ShipController.
     * @param gameModel Das Spielmodell.
     * @param gameView Die Spielansicht.
     */
    public ShipController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.currentShipIndex = 0;
        this.isHorizontal = true;

        this.initializeShipLists();
    }

    // Initialisierung und Setup

    /**
     * @brief Initialisiert die Listen der Schiffe für beide Spieler.
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
     */
    public void handleManualShipPlacement(Runnable onComplete) {
        int shipTurns = this.determineShipTurns();
        this.placeShipsForPlayer(0, shipTurns, onComplete);
    }

    /**
     * @brief Bestimmt die Anzahl der Züge für die Schiffsplatzierung.
     * @return 1, wenn Spieler 2 ein Computer ist, sonst 2.
     */
    private int determineShipTurns() {
        return (this.gameModel.getPlayerTwo() instanceof ComputerPlayerModel) ? 1 : 2;
    }

    // Hauptlogik für Schiffsplatzierung

    /**
     * @brief Führt die Schiffsplatzierung für den aktuellen Spieler durch.
     * @param currentTurn Aktueller Zug.
     * @param totalTurns Gesamtzahl der Züge.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     */
    private void placeShipsForPlayer(int currentTurn, int totalTurns, Runnable onComplete) {
        PlayerModel currentPlayer = this.gameModel.getCurrentPlayer();
        BoardView currentBoard = this.getCurrentBoardForPlayer(currentPlayer);
        List<Integer> remainingShips = this.getRemainingShipsForPlayer(currentPlayer);

        this.prepareBoardForPlacement(currentBoard);

        this.placeShipsForCurrentPlayer(currentBoard, remainingShips, () -> {
            if (currentTurn < totalTurns - 1) {
                this.proceedToNextTurn(currentBoard, currentPlayer);
                this.placeShipsForPlayer(currentTurn + 1, totalTurns, onComplete);
            } else {
                this.finalizeShipPlacement(currentBoard, onComplete);
            }
        });
    }

    /**
     * @brief Bereitet das Spielbrett für die Schiffsplatzierung vor.
     * @param board Das vorzubereitende Spielbrett.
     */
    private void prepareBoardForPlacement(BoardView board) {
        this.currentShipIndex = 0;
        board.setVisible(true);
        board.toggleGridVisibility(true);
        this.gameView.getStatusView().updateStatusMessageLabel(this.gameModel.getCurrentPlayer().getPlayerName() + " muss seine Schiffe platzieren");
    }

    /**
     * @brief Platziert Schiffe für den aktuellen Spieler.
     * @param board Das aktuelle Spielbrett.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     */
    private void placeShipsForCurrentPlayer(BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        this.currentShipIndex = 0;
        this.isHorizontal = false;

        MouseAdapter mouseAdapter = this.createMouseAdapterForPlacement(board, remainingShips, onComplete);

        board.addMouseWheelListener(e -> handleMouseWheelRotation(e, board, remainingShips));
        board.addMouseListener(mouseAdapter);
        board.addMouseMotionListener(mouseAdapter);
    }

    // Hilfsmethoden für die Schiffsplatzierung

    /**
     * @brief Ermittelt das aktuelle Spielbrett für den gegebenen Spieler.
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
     * @param currentBoard Das aktuelle Spielbrett.
     * @param currentPlayer Der aktuelle Spieler.
     */
    private void proceedToNextTurn(BoardView currentBoard, PlayerModel currentPlayer) {
        currentBoard.updateBoard(currentPlayer.getBoard());
        currentBoard.removeGraphics();
        this.gameModel.resetShipPlacement();
        this.gameModel.switchPlayer();
    }

    /**
     * @brief Schließt die Schiffsplatzierung ab.
     * @param currentBoard Das aktuelle Spielbrett.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     */
    private void finalizeShipPlacement(BoardView currentBoard, Runnable onComplete) {
        currentBoard.removeGraphics();
        JOptionPane.showMessageDialog(gameView, "Alle Schiffe wurden platziert. Das Spiel beginnt!");
        currentBoard.toggleGridVisibility(false);
        onComplete.run();
    }

    // Event-Handler und UI-Interaktionen

    /**
     * @brief Erstellt einen MouseAdapter für die Schiffsplatzierung.
     * @param board Das aktuelle Spielbrett.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     * @return Ein MouseAdapter für die Schiffsplatzierung.
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
     * @param board Das aktuelle Spielbrett.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     */
    private void handleMouseClick(MouseEvent e, BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        if (SwingUtilities.isRightMouseButton(e)) {
            this.isHorizontal = !this.isHorizontal;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            this.attemptShipPlacement(e, board, remainingShips, onComplete);
        }
    }

    /**
     * @brief Versucht, ein Schiff zu platzieren.
     * @param e Das MouseEvent.
     * @param board Das aktuelle Spielbrett.
     * @param remainingShips Liste der verbleibenden Schiffe.
     * @param onComplete Runnable, das nach Abschluss ausgeführt wird.
     */
    private void attemptShipPlacement(MouseEvent e, BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        int x = e.getY() / board.getCellSize();
        int y = e.getX() / board.getCellSize();

        int shipSize = remainingShips.get(currentShipIndex);

        if (this.currentShipIndex < remainingShips.size() && this.gameModel.placeNextShip(x, y, shipSize, !this.isHorizontal)) {
            board.addGraphicsToCells(x, y, shipSize, this.isHorizontal);
            remainingShips.remove(this.currentShipIndex);
            if (remainingShips.isEmpty()) {
                completePlacement(board, onComplete);
            } else {
                adjustShipIndex(remainingShips);
            }
        } else {
            JOptionPane.showMessageDialog(this.gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
        }
    }

    /**
     * @brief Schließt die Platzierung ab und entfernt Event-Listener.
     * @param board Das aktuelle Spielbrett.
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
     */
    private void adjustShipIndex(List<Integer> remainingShips) {
        this.currentShipIndex = Math.min(this.currentShipIndex, remainingShips.size() - 1);
    }

    /**
     * @brief Behandelt die Rotation des Mausrads zur Schiffsauswahl.
     * @param e Das MouseWheelEvent.
     * @param board Das aktuelle Spielbrett.
     * @param remainingShips Liste der verbleibenden Schiffe.
     */
    private void handleMouseWheelRotation(MouseWheelEvent e, BoardView board, List<Integer> remainingShips) {
        if (e.getWheelRotation() < 0) {
            this.currentShipIndex = Math.max(0, this.currentShipIndex - 1);
        } else {
            this.currentShipIndex = Math.min(remainingShips.size() - 1, this.currentShipIndex + 1);
        }
        Point mousePosition = board.getMousePosition();
        if (mousePosition != null) {
            this.updateShipPreview(mousePosition.x, mousePosition.y, board, remainingShips);
        }
    }

    /**
     * @brief Aktualisiert die Vorschau des zu platzierenden Schiffs.
     * @param mouseX X-Koordinate der Maus.
     * @param mouseY Y-Koordinate der Maus.
     * @param board Das aktuelle Spielbrett.
     * @param remainingShips Liste der verbleibenden Schiffe.
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