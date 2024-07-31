package model;

import utils.CellState;
import model.ShipModel;

import java.util.ArrayList;
import java.util.List;

public class BoardModel {
    private CellModel[][] board;
    private ArrayList<ShipModel> ships;

    public BoardModel() {
        this.board = new CellModel[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.board[i][j] = new CellModel();
            }
        }
    }

    public void setBoard(int x, int y, CellState cellState) {
        this.board[x][y].setCellValue(cellState);
    }
    
    public CellModel[][] getCompletBoard() {
        return this.board;
    }

    public CellModel getCell(int x, int y) {
        return this.board[x][y];
    }

    public int getRows() {
        return board.length;
    }

    public int getCols() {
        return board[0].length;
    }

    public void setShip(ShipModel ship) {
        this.ships.add(ship);
    }

    public boolean placeShip(ShipModel ship, int startX, int startY, int shipLength, boolean horizontal) {
        List<CellModel> newShipCells = new ArrayList<>();

        for (int i = 0; i < shipLength; i++) {
            int x = startX + (horizontal ? i : 0);
            int y = startY + (horizontal ? 0 : i);

            if (x < 0 || x >= this.getRows() || y < 0 || y >= this.getCols() || this.getCell(x, y).getCellValue() != CellState.FREE) {
                return false;
            }

            newShipCells.add(getCell(x, y));
        }

        for (CellModel cell : newShipCells) {
            cell.setCellValue(CellState.SET);
            ship.addShipCells(cell);
        }

        return true;
    }
}