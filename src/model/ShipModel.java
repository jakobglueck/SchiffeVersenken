package model;

import model.CellModel;
import utils.CellState;

import java.util.ArrayList;
import java.util.List;

public class ShipModel {

    CellModel startCell;

    CellModel endCell;

    private int length;
    boolean horizontal;

    private boolean shipStatus;


    public ShipModel(){}

    public void setShipParameters(CellModel startCell, int length, boolean horizontal) {
        this.startCell = startCell;
        this.length = length;
        this.horizontal = horizontal;
        this.endCell = new CellModel(startCell.getCellCoordX(), startCell.getCellCoordY(), CellState.SET);
        this.updateEndCell();
        this.shipStatus = true;
    }

    public void changeDirection() {
        this.horizontal = !this.horizontal;
        this.updateEndCell();
    }

    private void updateEndCell() {
        int endX = this.horizontal ? this.startCell.getCellCoordX() + length - 1 : this.startCell.getCellCoordX();
        int endY = this.horizontal ? this.startCell.getCellCoordY() : this.startCell.getCellCoordY() + length - 1;
        this.endCell.updateCellCordX(endX);
        this.endCell.updateCellCordY(endY);
    }

    private void changeShipStatus() {
        this.shipStatus = !this.shipStatus;
    }

    public CellModel getStartCell() {
        return this.startCell;
    }

    public CellModel getEndCell() {
        return this.endCell;
    }

    public int getLength() {
        return this.length;
    }

    public boolean getHorizontal() {
        return this.horizontal;
    }

    public boolean getShipStatus() {
        return this.shipStatus;
    }

    public boolean isValidLength() {
        return this.length >= 2 && this.length <= 5;
    }

    public boolean isHit(int x, int y) {
        if (this.horizontal) {
            return y == this.startCell.getCellCoordY() &&
                    x >= this.startCell.getCellCoordX() &&
                    x < this.startCell.getCellCoordX() + this.length;
        } else {
            return x == this.startCell.getCellCoordX() &&
                    y >= this.startCell.getCellCoordY() &&
                    y < this.startCell.getCellCoordY() + this.length;
        }
    }

    public void checkShipStatus(BoardModel board) {
        boolean allCellsHit = true;
        int startX = this.startCell.getCellCoordX();
        int startY = this.startCell.getCellCoordY();

        for (int i = 0; i < this.length; i++) {
            int currentX = this.horizontal ? startX + i : startX;
            int currentY = this.horizontal ? startY : startY + i;

            CellModel currentCell = board.getCell(currentX, currentY);
            if (currentCell.getCellState() != CellState.HIT) {
                allCellsHit = false;
                break;
            }
        }

        this.shipStatus = !allCellsHit;
    }
}