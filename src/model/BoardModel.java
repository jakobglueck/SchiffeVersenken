package model;

import utils.CellState;

import java.util.ArrayList;
import java.util.Random;

public class BoardModel {
    public static final int[] BOAT_SIZES = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    private final CellModel[][] board;
    private final ArrayList<ShipModel> playerShips;

    public static final int HEIGHT = 10;
    public static final int WIDTH = 10;

    private final Random random = new Random();

    public BoardModel() {
        this.board = new CellModel[HEIGHT][WIDTH];
        this.playerShips = new ArrayList<>();
        this.initializeBoard();
    }

    private void initializeBoard() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.board[x][y] = new CellModel(x, y, CellState.FREE);
            }
        }
    }

    public void changeCellState(int x, int y, CellState cellState) {
        if (isValidCoordinate(x, y)) {
            this.board[x][y].updateCellState(cellState);
        }
    }

    public CellModel[][] getCompleteBoard() {
        return this.board;
    }

    public CellModel getCell(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return this.board[x][y];
        }
        return null;
    }

    public ArrayList<ShipModel> getPlayerShips() {
        return this.playerShips;
    }

    public boolean placeShip(int startX, int startY, boolean horizontal, int length) {
        if (!isValidShipPlacement(startX, startY, horizontal, length)) {
            return false;
        }

        ShipModel ship = new ShipModel(this,startX, startY, length, horizontal);
        for (int i = 0; i < length; i++) {
            int x = horizontal ? startX + i : startX;
            int y = horizontal ? startY : startY + i;
            this.changeCellState(x, y, CellState.SET);
        }
        this.playerShips.add(ship);
        return true;
    }

    private boolean isValidShipPlacement(int startX, int startY, boolean horizontal, int length) {
        if (!isValidCoordinate(startX, startY)) {
            return false;
        }

        int endX = horizontal ? startX + length - 1 : startX;
        int endY = horizontal ? startY : startY + length - 1;

        if (!isValidCoordinate(endX, endY)) {
            return false;
        }

        for (int x = Math.max(0, startX - 1); x <= Math.min(WIDTH - 1, endX + 1); x++) {
            for (int y = Math.max(0, startY - 1); y <= Math.min(HEIGHT - 1, endY + 1); y++) {
                if (this.getCell(x, y).getCellState() == CellState.SET) {
                    return false;
                }
            }
        }

        return true;
    }

    public void placeAllShips() {
        for (int length : BOAT_SIZES) {
            boolean placed = false;
            while (!placed) {
                int startX = random.nextInt(WIDTH);
                int startY = random.nextInt(HEIGHT);
                boolean horizontal = random.nextBoolean();

                if (this.placeShip(startX, startY, horizontal, length)) {
                    placed = true;
                }
            }
        }
    }

    public ShipModel registerHit(int x, int y) {
        if (!isValidCoordinate(x, y)) {
            return null;
        }

        if (this.getCell(x, y).getCellState() == CellState.SET) {
            this.getCell(x, y).updateCellState(CellState.HIT);
            for (ShipModel ship : this.playerShips) {
                if (ship.isHit(x, y)) {
                    ship.checkShipStatus();
                    return ship;
                }
            }
        } else if (this.getCell(x, y).getCellState() == CellState.FREE) {
            this.getCell(x, y).updateCellState(CellState.FREE);
        }
        return null;
    }

    public boolean allShipsAreHit() {
        for(ShipModel ship : this.playerShips) {
            if(!ship.isSunk()){
                return false;
            }
        }
        return true;
    }

    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public void reset() {
        for (int row = 0; row < WIDTH; row++) {
            for (int col = 0; col < HEIGHT; col++) {
                this.board[row][col] = new CellModel(row, col, CellState.FREE);
            }
        }
        this.playerShips.clear();
    }
}
