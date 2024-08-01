package model;

import utils.*;

public class CellModel {
    private CellState cellState;
    private int x;
    private int y;


    public CellModel(int x, int y, CellState cellState) {
        this.cellState = CellState.FREE;
        this.x = x;
        this.y = y;
    }

    public CellState getCellState() {
        return this.cellState;
    }

    public void updateCellState(CellState cellState) {
        this.cellState = cellState;
    }
    
    public int getCellCordX(){
        return this.x;
    }

    public int getCellCordY(){
        return this.y;
    }
}