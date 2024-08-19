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

        this.initializeShipLists();
    }

    private void initializeShipLists() {

        this.playerOneShips = new ArrayList<>();
        this.playerTwoShips = new ArrayList<>();

        for (int size : this.gameModel.getShipSizes()) {
            this.playerOneShips.add(size);
            this.playerTwoShips.add(size);
        }
    }

    public void handleManualShipPlacement(Runnable onComplete) {
        int shipTurns = this.determineShipTurns();
        this.placeShipsForPlayer(0, shipTurns, onComplete);
    }

    private int determineShipTurns() {
        return (this.gameModel.getPlayerTwo() instanceof ComputerPlayerModel) ? 1 : 2;
    }

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

    private BoardView getCurrentBoardForPlayer(PlayerModel player) {
        return (player == this.gameModel.getPlayerOne()) ? this.gameView.getPlayerBoardOne() : this.gameView.getPlayerBoardTwo();
    }

    private List<Integer> getRemainingShipsForPlayer(PlayerModel player) {
        return (player == this.gameModel.getPlayerOne()) ? this.playerOneShips : this.playerTwoShips;
    }

    private void prepareBoardForPlacement(BoardView board) {
        this.currentShipIndex = 0;
        board.setVisible(true);
        board.toggleGridVisibility(true);
        this.gameView.getStatusView().updateStatusMessageLabel(this.gameModel.getCurrentPlayer().getPlayerName() + " muss seine Schiffe platzieren");
    }

    private void proceedToNextTurn(BoardView currentBoard, PlayerModel currentPlayer) {
        currentBoard.updateBoard(currentPlayer.getBoard());
        currentBoard.removeGraphics();
        this.gameModel.resetShipPlacement();
        this.gameModel.switchPlayer();
    }

    private void finalizeShipPlacement(BoardView currentBoard, Runnable onComplete) {
        currentBoard.removeGraphics();
        JOptionPane.showMessageDialog(gameView, "Alle Schiffe wurden platziert. Das Spiel beginnt!");
        currentBoard.toggleGridVisibility(false);
        onComplete.run();
    }

    private void placeShipsForCurrentPlayer(BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        this.currentShipIndex = 0;
        this.isHorizontal = false;

        MouseAdapter mouseAdapter = this.createMouseAdapterForPlacement(board, remainingShips, onComplete);

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
            this.isHorizontal = !this.isHorizontal;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            this.attemptShipPlacement(e, board, remainingShips, onComplete);
        }
    }

    private void attemptShipPlacement(MouseEvent e, BoardView board, List<Integer> remainingShips, Runnable onComplete) {
        int x = e.getY() / board.getCellSize();
        int y = e.getX() / board.getCellSize();

        int shipSize = remainingShips.get(currentShipIndex); // Richtiges Schiff holen

        if (this.currentShipIndex < remainingShips.size() && this.gameModel.placeNextShip(x, y, shipSize, !this.isHorizontal)) {
            board.addGraphicsToCells(x, y, shipSize, this.isHorizontal); // Setze das Schiff mit der richtigen Größe
            remainingShips.remove(this.currentShipIndex); // Entfernt das platzierte Schiff aus der Liste
            if (remainingShips.isEmpty()) {
                completePlacement(board, onComplete);
            } else {
                adjustShipIndex(remainingShips);
            }
        } else {
            JOptionPane.showMessageDialog(this.gameView, "Ungültige Schiffsplatzierung. Versuche es erneut.");
        }
    }

    private void completePlacement(BoardView board, Runnable onComplete) {
        board.removeMouseListener(board.getMouseListeners()[0]);
        board.removeMouseMotionListener(board.getMouseMotionListeners()[0]);
        board.hideShipPreview();
        onComplete.run();
    }

    private void adjustShipIndex(List<Integer> remainingShips) {
        this.currentShipIndex = Math.min(this.currentShipIndex, remainingShips.size() - 1);
    }

    private void handleMouseWheelRotation(MouseWheelEvent e, BoardView board, List<Integer> remainingShips) {
        if (e.getWheelRotation() < 0) {
            this.currentShipIndex = Math.max(0, this.currentShipIndex - 1);
        } else {
            this. currentShipIndex = Math.min(remainingShips.size() - 1, this.currentShipIndex + 1);
        }
        Point mousePosition = board.getMousePosition();
        if (mousePosition != null) {
            this.updateShipPreview(mousePosition.x, mousePosition.y, board, remainingShips);
        }
    }

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
