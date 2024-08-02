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
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Enter x coordinate: ");
            int x = sc.nextInt();
            System.out.print("Enter y coordinate: ");
            int y = sc.nextInt();

            if (player.validMove(x, y, player)) {
                CellState newState;

                if (player.board.getCell(x,y).getCellState() == CellState.FREE) {
                    newState = CellState.HIT;
                } else {
                    newState = CellState.REVEAL;
                }
                player.board.changeCellOnBoard(x, y, newState);
                System.out.println("Move accepted.");
                break;
            } else {
                System.out.println("Invalid move. Please try again.");
            }
        }
    }

    public boolean validMove(int x, int y, PlayerModel player) {
        if (x < 0 || x >= 9 || y < 0 || y >= 9) {
            return false;
        }

        CellState currentCellState = player.board.getCell(x,y).getCellState();
        if(currentCellState == CellState.HIT || currentCellState == CellState.REVEAL){
            return false;
        }
        return true;
    }
}