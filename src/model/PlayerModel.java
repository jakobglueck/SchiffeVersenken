package model;

import model.BoardModel;
import model.ShipModel;

import java.util.ArrayList;
import java.util.List;

public class PlayerModel {
    private String playerName;
    private BoardModel board;
    private ArrayList<ShipModel>  ships;

    public static final int[] BOAT_SIZES = {5,4,3,3,2};

    public PlayerModel(){
    }

    public String getPlayerName() {
        return playerName;
    }

    public BoardModel getBoard() {
        return board;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setBoard(BoardModel board) {
        this.board = board;
    }

    public ArrayList<ShipModel> getShips() {
        return ships;
    }

    void setShip(ShipModel ship){
        this.ships.add(ship);
    }
}