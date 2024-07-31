package model;

import model.CellModel;

import java.util.ArrayList;
import java.util.List;

public class ShipModel {

    private ArrayList<CellModel> shipCells;

    public ShipModel(){
    }

    public List<CellModel> getShip(){
        return shipCells;
    }
    public void setShip(ArrayList<CellModel> ship){
        this.shipCells = ship;
    }

    public void  addShipCells(CellModel cell){
        this.shipCells.add(cell);
    }

    public void removeShipCells(CellModel cell){
        this.shipCells.remove(cell);
    }

    public void removeAllShipCells(){
        this.shipCells.clear();
    }

    public boolean checkShipLength(){
        if(this.shipCells.size()>4 && this.shipCells.size() < 2){
            return false;
        }
        return true;
    }
}