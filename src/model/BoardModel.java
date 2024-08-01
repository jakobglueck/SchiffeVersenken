package model;

import utils.CellState;

import java.util.ArrayList;
import java.util.List;

public class BoardModel {
    private CellModel[][] board;

    private static int HEIGHT = 10;
    private static int WIDTH = 10;

    public BoardModel() {
        this.board = new CellModel[HEIGHT][WIDTH];
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                this.board[x][y] = new CellModel(x,y,CellState.FREE);
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

    public int getRows() {
        return board.length;
    }

    public int getCols() {
        return board[0].length;
    }

    public boolean placeShip(ShipModel ship, int startX, int startY) {
        List<CellModel> newShipCells = new ArrayList<>();

        for (int i = 0; i < shipLength; i++) {
            int x = startX + (horizontal ? i : 0);
            int y = startY + (horizontal ? 0 : i);

            if (x < 0 || x >= HEIGHT || y < 0 || y >= WIDTH || this.getCell(x, y).getCellState() != CellState.FREE) {
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