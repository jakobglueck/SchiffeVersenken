package model;

import utils.CellState;

public class PlayerModel {
    private String playerName;
    private BoardModel board;

    public PlayerModel(String playerName) {
        this.playerName = playerName;
        this.board = new BoardModel();
    }

    public String getPlayerName() {
        return playerName;
    }

    public BoardModel getBoard() {
        return board;
    }

    public void placeShipsManually() {
        // Assume this method will be called with proper inputs in the GUI or controller
        for (int length : BoardModel.BOAT_SIZES) {
            boolean placed = false;
            while (!placed) {
                // Wait for user to input startX, startY, and orientation (horizontal)
                // Example: placeShip(startX, startY, horizontal, length);
                // placed = true if ship placed successfully
            }
        }
    }

    public boolean makeMove(PlayerModel opponent, int x, int y) {
        if (!isValidMove(x, y)) {
            return false;
        }

        boolean hit = opponent.getBoard().registerHit(x, y);
        if (hit) {
            System.out.println(playerName + " hit a target!");
        } else {
            System.out.println(playerName + " missed.");
        }
        return true;
    }

    public void takeTurn(PlayerModel opponent, int x, int y) {
        if (makeMove(opponent, x, y)) {
            System.out.println(playerName + " made a move.");
        } else {
            System.out.println("Invalid move. Please try again.");
        }
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= BoardModel.WIDTH || y < 0 || y >= BoardModel.HEIGHT) {
            return false;
        }

        CellState currentCellState = board.getCell(x, y).getCellState();
        return currentCellState != CellState.HIT && currentCellState != CellState.FREE;
    }
}
