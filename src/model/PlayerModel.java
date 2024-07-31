package model;

import model.BoardModel;
import model.ShipModel;

import java.util.ArrayList;
import java.util.List;

public class PlayerModel {
    private String playerName;
    private BoardModel playBoard;
    private BoardModel overlayBoard;

    public PlayerModel(){
    }

    public String getPlayerName() {
        return playerName;
    }

    public BoardModel getPlayBoard() {
        return playBoard;
    }

    public BoardModel getOverlayBoard() {
        return overlayBoard;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayBoard(BoardModel playBoard) {
        this.playBoard = playBoard;
    }

    public void setOverlayBoard(BoardModel overlayBoard) {
        this.overlayBoard = overlayBoard;
    }

}