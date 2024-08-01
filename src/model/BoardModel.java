package model;

import utils.CellState;

import java.util.ArrayList;

public class BoardModel {
    public static final int[] BOAT_SIZES = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    private CellModel[][] board;
    private ArrayList<ShipModel> playerShips;

    private static final int HEIGHT = 10;
    private static final int WIDTH = 10;

    public BoardModel() {
        this.board = new CellModel[HEIGHT][WIDTH];
        this.playerShips = new ArrayList<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.board[x][y] = new CellModel(x, y, CellState.FREE);
            }
        }
    }

    public void changeCellOnBoard(int x, int y, CellState cellState) {
        this.board[x][y].updateCellState(cellState);
    }

    public CellModel[][] getCompleteBoard() {
        return this.board;
    }

    public CellModel getCell(int x, int y) {
        return this.board[x][y];
    }

    public void addShip(int startX, int startY, boolean horizontal, int length) {
        ShipModel ship = new ShipModel();
        CellModel startCell = this.getCell(startX, startY);
        ship.setShipParameters(startCell, length, horizontal);
        this.playerShips.add(ship);
    }

    public boolean placeShip(int startX, int startY, boolean horizontal, int length) {
        ShipModel ship = new ShipModel();
        CellModel startCell = this.getCell(startX, startY);
        ship.setShipParameters(startCell, length, horizontal);

        if (!ship.isValidLength()) {
            return false;
        }

        if (!isValidPlacement(ship)) {
            return false;
        }

        CellModel endCell = ship.getEndCell();
        for (int x = startCell.getCellCoordX(); x <= endCell.getCellCoordX(); x++) {
            for (int y = startCell.getCellCoordY(); y <= endCell.getCellCoordY(); y++) {
                this.changeCellOnBoard(x, y, CellState.SET);
            }
        }
        this.addShip(startX, startY, horizontal, length);
        return true;
    }

    private boolean isValidPlacement(ShipModel ship) {
        CellModel startCell = ship.getStartCell();
        CellModel endCell = ship.getEndCell();

        if (endCell.getCellCoordX() >= WIDTH || endCell.getCellCoordY() >= HEIGHT) {
            return false;
        }

        for (int x = startCell.getCellCoordX(); x <= endCell.getCellCoordX(); x++) {
            for (int y = startCell.getCellCoordY(); y <= endCell.getCellCoordY(); y++) {
                if (this.getCell(x, y).getCellState() != CellState.FREE) {
                    return false;
                }
            }
        }

        for (int x = Math.max(0, startCell.getCellCoordX() - 1); x <= Math.min(WIDTH - 1, endCell.getCellCoordX() + 1); x++) {
            for (int y = Math.max(0, startCell.getCellCoordY() - 1); y <= Math.min(HEIGHT - 1, endCell.getCellCoordY() + 1); y++) {
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
                int startX = (int) (Math.random() * WIDTH);
                int startY = (int) (Math.random() * HEIGHT);
                boolean horizontal = Math.random() < 0.5;

                if (this.placeShip(startX, startY, horizontal, length)) {
                    placed = true;
                }
            }
        }
    }
}
