package model;

import model.CellModel;

import java.util.ArrayList;
import java.util.List;

public class ShipModel {

    CellModel StartCell;

    CellModel EndCell;

    private int length;
    boolean horizontal;

    public ShipModel(){}

    public void setShipParameter(CellModel StartCell, CellModel endCell, int length, boolean horizontal){
            this.StartCell = StartCell;
            this.length = length;
            this.horizontal = true;
            this.EndCell = endCell;
    }
    
    public void changeDirection(boolean directions){
        this.horizontal = directions;
    }
    public boolean checkShipLength(){
        return this.length > 5 || this.length < 2;
    }
}