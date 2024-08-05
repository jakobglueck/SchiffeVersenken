package model;

import utils.CellState;

import java.util.Scanner;

public class PlayerModel {
    private String playerName;
    private BoardModel board;

    public PlayerModel(String playerName) {
        this.playerName = playerName;
        this.board = new BoardModel();
    }

    public String getPlayerName() {
        return playerName;
    }

    public BoardModel getBoard() {
        return board;
    }

    public void placeShips() {
        this.board.placeAllShips();
    }

    public boolean makeMove(PlayerModel opponent, int x, int y) {
        if (!isValidMove(x, y)) {
            return false;
        }

        boolean hit = opponent.getBoard().registerHit(x, y);
        if (hit) {
            System.out.println(playerName + " hit a target!");
        } else {
            System.out.println(playerName + " missed.");
        }
        return true;
    }

    public void takeTurn(PlayerModel opponent, int x , int y) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                if (this.makeMove(opponent, x, y)) {
                    break;
                } else {
                    System.out.println("Invalid move. Please try again.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter numbers only.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= BoardModel.WIDTH || y < 0 || y >= BoardModel.HEIGHT) {
            return false;
        }

        CellState currentCellState = board.getCell(x, y).getCellState();
        return currentCellState != CellState.HIT && currentCellState != CellState.FREE;
    }
}