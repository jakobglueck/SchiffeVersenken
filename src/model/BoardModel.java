package model;

import utils.CellState;

import java.util.ArrayList;
import java.util.List;

public class BoardModel {
    private CellModel[][] board;
    private ArrayList<ShipModel> playerShips;


    private static final int HEIGHT = 10;
    private static final int WIDTH = 10;

    public BoardModel() {
        this.board = new CellModel[HEIGHT][WIDTH];
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

    public void addShip(ShipModel ship , int startX, int startY, boolean horizontal){
        if(this.placeShip(ship , startX, startY, horizontal)){
            playerShips.add(ship);
        }
    }

    public boolean placeShip(ShipModel ship, int startX, int startY, boolean horizontal) {
        if (startX < 0 || startY < 0 || startX >= WIDTH || startY >= HEIGHT) {
            return false;
        }

        CellModel startCell = this.getCell(startX, startY);
        ship.setShipParameters(startCell, ship.getLength(), horizontal);

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

        return true;
    }

    private boolean isValidPlacement(ShipModel ship) {
        CellModel startCell = ship.getStartCell();
        CellModel endCell = ship.getEndCell();

        for (int x = startCell.getCellCoordX(); x <= endCell.getCellCoordX(); x++) {
            for (int y = startCell.getCellCoordY(); y <= endCell.getCellCoordY(); y++) {
                if (x >= WIDTH || y >= HEIGHT || this.getCell(x, y).getCellState() != CellState.FREE) {
                    return false;
                }
            }
        }

        return true;
    }
}