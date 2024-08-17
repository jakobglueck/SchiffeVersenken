package model;

import utils.CellState;

import java.util.ArrayList;
import java.util.List;

public class ShipModel {
    private final List<CellModel> shipCells;
    private final int length;
    private final boolean horizontal;
    private boolean sunk;

    public ShipModel(BoardModel boardModel, int startX, int startY, int length, boolean horizontal) {
        this.length = length;
        this.horizontal = horizontal;
        this.sunk = false;
        this.shipCells = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            int currentX = horizontal ? boardModel.getCell(startX,startY).getX() + i : boardModel.getCell(startX,startY).getX() ;
            int currentY = horizontal ? boardModel.getCell(startX,startY).getY() : boardModel.getCell(startX,startY).getY() + i;
            boardModel.getCell(currentX,currentY).updateCellState(CellState.SET);
            this.shipCells.add(boardModel.getCell(currentX,currentY));
        }
    }

    public boolean isHit(int x, int y) {
        for (CellModel cell : this.shipCells) {
            if (cell.getX() == x && cell.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void checkShipStatus() {
        for (CellModel cell : this.shipCells) {
            if (cell.getCellState() != CellState.HIT) {
                return;
            }
        }
        this.sunk = true;
    }

    public boolean isSunk() {
        return this.sunk;
    }

    public List<CellModel> getShipCells() {
        return this.shipCells;
    }
}