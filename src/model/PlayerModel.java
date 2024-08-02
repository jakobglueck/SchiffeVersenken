package model;

import model.BoardModel;
import model.ShipModel;
import utils.CellState;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerModel {
    private String playerName;
    private BoardModel board;


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

    public void placeShip(){
        this.board.placeAllShips();
    }

    public void playerMove(int x, int y, CellState cellState){
        this.board.changeCellOnBoard(x,y,cellState);
    }

    public void playerMove(PlayerModel player){
        System.out.println("Bitte wähle die Koordinaten die du treffen willst");
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        if(player.board.registerHit(x,y)){
            System.out.println("Treffer");
        }else{
            System.out.println("Wasser");
        }
    }
}