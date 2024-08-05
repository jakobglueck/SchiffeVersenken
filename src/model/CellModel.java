package model;

import utils.CellState;

public class CellModel {
    private CellState cellState;
    private final int x;
    private final int y;

    public CellModel(int x, int y, CellState cellState) {
        this.cellState = cellState;
        this.x = x;
        this.y = y;
    }

    public void updateCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public CellState getCellState() {
        return this.cellState;
    }

    public boolean isHit() {
        return this.cellState == CellState.HIT;
    }
}