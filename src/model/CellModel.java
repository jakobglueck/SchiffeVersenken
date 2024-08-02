package model;

import utils.*;

public class CellModel {
    private CellState cellState;
    private int x;
    private int y;


    public CellModel(int x, int y, CellState cellState) {
        this.cellState = cellState;
        this.x = x;
        this.y = y;
    }

    public void updateCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public void updateCellCordX(int x){
        this.x = x;
    }

    public void updateCellCordY(int y){
        this.y = y;
    }

    public int getCellCoordX(){
        return this.x;
    }

    public int getCellCoordY(){
        return this.y;
    }

    public CellState getCellState() {
        return this.cellState;
    }

    public boolean checkHitStatus(){
        return this.cellState == CellState.HIT;
    }
}