package model;

import utils.CellState;

import java.util.Random;
import java.util.Scanner;

public class GameModel {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;

    public GameModel(){
        this.createBasementGame();

    };

    public PlayerModel createPlayer() {
       PlayerModel player = new PlayerModel();
       return player;
    }

    public String createPlayerName(){
        System.out.print("Enter player name: ");
        Scanner sc = new Scanner(System.in);
        String playerName = "Default Player";
        if (sc.hasNextLine()) {
            playerName = sc.nextLine();
        }
        return playerName;
    }

    public BoardModel createPlayerBoard(){
        BoardModel board = new BoardModel();
        return board;
    }

    public void createBasementGame(){
        this.playerOne = createPlayer();
        this.playerTwo = createPlayer();

        this.playerOne.setPlayerName(this.createPlayerName());
        this.playerTwo.setPlayerName(this.createPlayerName());

        this.playerOne.setBoard(this.createPlayerBoard());
        this.playerTwo.setBoard(this.createPlayerBoard());

        int randomNumber = 1 + (int)(Math.random() * 10);
        if (randomNumber <= 5) {
            this.playerOne.placeShip();
            this.playerTwo.placeShip();
        } else {
            this.playerTwo.placeShip();
            this.playerOne.placeShip();
        }
    }

    public PlayerModel getPlayerOne(){
        return this.playerOne;
    }

    public PlayerModel getPlayerTwo(){
        return this.playerTwo;
    }
}
