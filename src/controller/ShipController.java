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

        initializeShipLists();
    }

    private void initializeShipLists() {

        this.playerOneShips = new ArrayList<>();
        this.playerTwoShips = new ArrayList<>();

        for (int size : gameModel.getShipSizes()) {
            playerOneShips.add(size);
            playerTwoShips.add(size);
        }
    }

    public void handleManualShipPlacement(Runnable onComplete) {
        int shipTurns = determineShipTurns();
        placeShipsForPlayer(0, shipTurns, onComplete);
    }

    private int determineShipTurns() {
        return (gameModel.getPlayerTwo() instanceof ComputerPlayerModel) ? 1 : 2;
    }

    private void placeShipsForPlayer(int currentTurn, int totalTurns, Runnable onComplete) {
        PlayerModel currentPlayer = gameModel.getCurrentPlayer();
        BoardView currentBoard = getCurrentBoardForPlayer(currentPlayer);
        List<Integer> remainingShips = getRemainingShipsForPlayer(currentPlayer);

        prepareBoardForPlacement(currentBoard);

        placeShipsForCurrentPlayer(currentBoard, remainingShips, () -> {
            if (currentTurn < totalTurns - 1) {
                proceedToNextTurn(currentBoard, currentPlayer);
                placeShipsForPlayer(currentTurn + 1, totalTurns, onComplete);
            } else {
                finalizeShipPlacement(currentBoard, onComplete);
            }
        });
    }

    private BoardView getCurrentBoardForPlayer(PlayerModel player) {
        return (player == gameModel.getPlayerOne()) ? gameView.getPlayerBoardOne() : gameView.getPlayerBoardTwo();
    }

    private List<Integer> getRemainingShipsForPlayer(PlayerModel player) {
        return (player == gameModel.getPlayerOne()) ? playerOneShips : playerTwoShips;
    }

    private void prepareBoardForPlacement(BoardView board) {
        this.currentShipIndex = 0;
        board.setVisible(true);
        board.toggleGridVisibility(true);
        this.gameView.getStatusView().updateStatusMessageLabel(gameModel.getCurrentPlayer().getPlayerName() + " muss seine Schiffe platzieren");
    }

    private void proceedToNextTurn(BoardView currentBoard, PlayerModel currentPlayer) {
        currentBoard.updateBoard(currentPlayer.getBoard());
        currentBoard.removeGraphics();
        this.gameModel.resetShipPlacement();
        gameModel.switchPlayer();
    }

    private void finalizeShipPlacement(BoardView currentBoard, Runnable onComplete) {
        currentBoard.removeGraphics();
        JOptionPane.showMessageDialog(gameView, "Alle Schiffe wurden platziert. Das Spiel beginnt!");
        currentBoard.toggleGridVisibility(false);
        onComplete.run();
    }

    private void placeShipsForCurrentPlayer(BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        currentShipIndex = 0;
        isHorizontal = false;

        MouseAdapter mouseAdapter = createMouseAdapterForPlacement(board, remainingShips, onComplete);

        board.addMouseWheelListener(e -> handleMouseWheelRotation(e, board, remainingShips));
        board.addMouseListener(mouseAdapter);
        board.addMouseMotionListener(mouseAdapter);
    }

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

    private void handleMouseClick(MouseEvent e, BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        if (SwingUtilities.isRightMouseButton(e)) {
            isHorizontal = !isHorizontal;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            attemptShipPlacement(e, board, remainingShips, onComplete);
        }
    }

    private void attemptShipPlacement(MouseEvent e, BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        int x = e.getY() / board.getCellSize();
        int y = e.getX() / board.getCellSize();

        int shipSize = remainingShips.get(currentShipIndex); // Richtiges Schiff holen

        if (currentShipIndex < remainingShips.size() && gameModel.placeNextShip(x, y, shipSize, !isHorizontal)) {
            board.addGraphicsToCells(x, y, shipSize, isHorizontal); // Setze das Schiff mit der richtigen Größe
            remainingShips.remove(currentShipIndex); // Entfernt das platzierte Schiff aus der Liste
            if (remainingShips.isEmpty()) {
                completePlacement(board, onComplete);
            } else {
                adjustShipIndex(remainingShips);
            }
        } else {
            JOptionPane.showMessageDialog(gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
        }
    }

    private void completePlacement(BoardView board, Runnable onComplete) {
        board.removeMouseListener(board.getMouseListeners()[0]);
        board.removeMouseMotionListener(board.getMouseMotionListeners()[0]);
        board.hideShipPreview();
        onComplete.run();
    }

    private void adjustShipIndex(List<Integer> remainingShips) {
        currentShipIndex = Math.min(currentShipIndex, remainingShips.size() - 1);
    }

    private void handleMouseWheelRotation(MouseWheelEvent e, BoardView board, List<Integer> remainingShips) {
        if (e.getWheelRotation() < 0) {
            currentShipIndex = Math.max(0, currentShipIndex - 1);
        } else {
            currentShipIndex = Math.min(remainingShips.size() - 1, currentShipIndex + 1);
        }
        Point mousePosition = board.getMousePosition();
        if (mousePosition != null) {
            updateShipPreview(mousePosition.x, mousePosition.y, board, remainingShips);
        }
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
