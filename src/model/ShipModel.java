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

    public ShipModel(){}

    public void setShipParameters(CellModel startCell, int length, boolean horizontal) {
        this.startCell = startCell;
        this.length = length;
        this.horizontal = horizontal;
        this.endCell = new CellModel(startCell.getCellCoordX(), startCell.getCellCoordY(), CellState.FREE);
        this.updateEndCell();
    }

    private void updateEndCell() {
        int endX = this.horizontal ? this.startCell.getCellCoordX() + length - 1 : this.startCell.getCellCoordX();
        int endY = this.horizontal ? this.startCell.getCellCoordY() : this.startCell.getCellCoordY() + length - 1;
        this.endCell.updateCellCordX(endX);
        this.endCell.updateCellCordY(endY);
    }

    public void changeDirection() {
        this.horizontal = !this.horizontal;
        this.updateEndCell();
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

    public boolean isValidLength() {
        return this.length >= 2 && this.length <= 5;
    }
}