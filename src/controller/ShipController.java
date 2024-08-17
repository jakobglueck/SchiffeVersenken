package controller;

import model.ComputerPlayerModel;
import model.GameModel;
import model.PlayerModel;
import View.GameView;
import View.BoardView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShipController {

    private GameModel gameModel;
    private GameView gameView;
    private int currentShipIndex;
    private boolean isHorizontal;

    public ShipController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.currentShipIndex = 0;
        this.isHorizontal = true;
    }

    public void handleManualShipPlacement(Runnable onComplete) {
        int shipTurns = (gameModel.getPlayerTwo() instanceof ComputerPlayerModel) ? 1 : 2;
        placeShipsForPlayer(0, shipTurns, onComplete);
    }

    private void placeShipsForPlayer(int currentTurn, int totalTurns, Runnable onComplete) {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        BoardView currentBoard = (currentPlayer == gameModel.getPlayerOne()) ? gameView.getPlayerBoardOne() : gameView.getPlayerBoardTwo();
        this.currentShipIndex = 0; // Ensure currentShipIndex is reset at the start of placing ships for a player
        currentBoard.setVisible(true);
        currentBoard.toggleGridVisibility(true);

        placeShipsForCurrentPlayer(currentBoard, () -> {
            if (currentTurn < totalTurns - 1) {
                gameModel.switchPlayer();
                this.gameModel.currentShipIndex = 0;
                currentShipIndex = 0;
                placeShipsForPlayer(currentTurn + 1, totalTurns, onComplete);
            } else {
                JOptionPane.showMessageDialog(gameView, "Alle Schiffe platziert. Das Spiel beginnt jetzt!");
                updateStatusLabel("Spiel beginnt!");
                currentBoard.toggleGridVisibility(false);
                onComplete.run();
            }
        });
    }

    private void placeShipsForCurrentPlayer(BoardView board, Runnable onComplete) {
        currentShipIndex = 0; // Ensure the index is reset when starting placement for the current player
        isHorizontal = false;

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    isHorizontal = !isHorizontal;
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    int x = e.getY() / board.getCellSize();
                    int y = e.getX() / board.getCellSize();

                    if (gameModel.placeNextShip(x, y, !isHorizontal)) {

                        System.out.println(x +" und " +y);
                        System.out.println(isHorizontal);

                        board.updateBoard();
                        currentShipIndex++;
                        if (currentShipIndex >= gameModel.getShipSizes().length) {
                            board.removeMouseListener(this);
                            board.removeMouseMotionListener(this);
                            board.hideShipPreview();
                            onComplete.run();
                        }
                    } else {
                        JOptionPane.showMessageDialog(gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateShipPreview(e.getX(), e.getY(), board);
            }
        };

        board.addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            if (rotation < 0) {
                currentShipIndex = Math.max(0, currentShipIndex - 1);
            } else {
                currentShipIndex = Math.min(gameModel.getShipSizes().length - 1, currentShipIndex + 1);
            }
            Point mousePosition = board.getMousePosition();
            if (mousePosition != null) {
                updateShipPreview(mousePosition.x, mousePosition.y, board);
            }
        });

        board.addMouseListener(mouseAdapter);
        board.addMouseWheelListener(mouseAdapter);
        board.addMouseMotionListener(mouseAdapter);
    }

    private void updateShipPreview(int mouseX, int mouseY, BoardView board) {
        // Berechnung der korrekten Position für das Vorschau-Schiff
        int shipLength = gameModel.getShipSizes()[currentShipIndex];
        int width = isHorizontal ? shipLength * board.getCellSize() : board.getCellSize();
        int height = isHorizontal ? board.getCellSize() : shipLength * board.getCellSize();

        int x = (mouseX / board.getCellSize()) * board.getCellSize();
        int y = (mouseY / board.getCellSize()) * board.getCellSize();

        board.updateShipPreview(x, y, width, height);
    }


    private void updateStatusLabel(String message) {
        SwingUtilities.invokeLater(() -> gameView.getStatusView().updatePlayerName(message));
    }
}
