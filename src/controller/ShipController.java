package controller;

import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ShipController {

    private GameModel gameModel;
    private GameView gameView;
    private int currentShipIndex;
    private boolean isHorizontal;
    private List<Integer> playerOneShips;
    private List<Integer> playerTwoShips;

    public ShipController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.currentShipIndex = 0;
        this.isHorizontal = true;

        // Initialisiere die Schiffe für beide Spieler
        this.playerOneShips = new ArrayList<>();
        this.playerTwoShips = new ArrayList<>();

        // Lade die Schiffsgrößen einmalig aus dem GameModel
        for (int size : gameModel.getShipSizes()) {
            playerOneShips.add(size);
            playerTwoShips.add(size);
        }
    }

    public void handleManualShipPlacement(Runnable onComplete) {
        int shipTurns = (gameModel.getPlayerTwo() instanceof ComputerPlayerModel) ? 1 : 2;
        placeShipsForPlayer(0, shipTurns, onComplete);
    }

    private void placeShipsForPlayer(int currentTurn, int totalTurns, Runnable onComplete) {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        BoardView currentBoard = (currentPlayer == gameModel.getPlayerOne()) ? gameView.getPlayerBoardOne() : gameView.getPlayerBoardTwo();
        List<Integer> remainingShips = (currentPlayer == gameModel.getPlayerOne()) ? playerOneShips : playerTwoShips;

        this.currentShipIndex = 0;
        currentBoard.setVisible(true);
        currentBoard.toggleGridVisibility(true);
        this.gameView.getStatusView().updateStatusMessageLabel(currentPlayer.getPlayerName() + " muss seine Schiffe platzieren");

        placeShipsForCurrentPlayer(currentBoard, remainingShips, () -> {
            if (currentTurn < totalTurns - 1) {
                currentBoard.updateBoard(currentPlayer.getBoard());
                currentBoard.removeGraphics();
                this.gameModel.resetShipPlacement();
                gameModel.switchPlayer();
                placeShipsForPlayer(currentTurn + 1, totalTurns, onComplete);
            } else {
                currentBoard.removeGraphics();
                JOptionPane.showMessageDialog(gameView, "Alle Schiffe wurden platziert. Das Spiel beginnt!");
                currentBoard.toggleGridVisibility(false);
                onComplete.run();
            }
        });
    }

    private void placeShipsForCurrentPlayer(BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        currentShipIndex = 0;
        isHorizontal = false;

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    isHorizontal = !isHorizontal;
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    int x = e.getY() / board.getCellSize();
                    int y = e.getX() / board.getCellSize();

                    int shipSize = remainingShips.get(currentShipIndex); // Richtiges Schiff holen

                    if (currentShipIndex < remainingShips.size() && gameModel.placeNextShip(x, y, shipSize, !isHorizontal)) {
                        board.addGraphicsToCells(x, y, shipSize, isHorizontal); // Setze das Schiff mit der richtigen Größe
                        remainingShips.remove(currentShipIndex); // Entfernt das platzierte Schiff aus der Liste
                        if (remainingShips.isEmpty()) {
                            board.removeMouseListener(this);
                            board.removeMouseMotionListener(this);
                            board.hideShipPreview();
                            onComplete.run();
                        } else {
                            currentShipIndex = Math.min(currentShipIndex, remainingShips.size() - 1); // Index anpassen
                        }
                    } else {
                        JOptionPane.showMessageDialog(gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateShipPreview(e.getX(), e.getY(), board, remainingShips);
            }
        };

        board.addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            if (rotation < 0) {
                currentShipIndex = Math.max(0, currentShipIndex - 1);
            } else {
                currentShipIndex = Math.min(remainingShips.size() - 1, currentShipIndex + 1);
            }
            Point mousePosition = board.getMousePosition();
            if (mousePosition != null) {
                updateShipPreview(mousePosition.x, mousePosition.y, board, remainingShips);
            }
        });

        board.addMouseListener(mouseAdapter);
        board.addMouseMotionListener(mouseAdapter);
    }

    private void updateShipPreview(int mouseX, int mouseY, BoardView board, List<Integer> remainingShips) {
        if (remainingShips.isEmpty()) return;
        int shipLength = remainingShips.get(currentShipIndex);
        int width = isHorizontal ? shipLength * board.getCellSize() : board.getCellSize();
        int height = isHorizontal ? board.getCellSize() : shipLength * board.getCellSize();

        int x = (mouseX / board.getCellSize()) * board.getCellSize();
        int y = (mouseY / board.getCellSize()) * board.getCellSize();

        board.updateShipPreview(x, y, width, height);
    }
}