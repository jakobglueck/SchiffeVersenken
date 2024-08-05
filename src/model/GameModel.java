package model;

import utils.CellState;

import java.util.Random;
import java.util.Scanner;

public class GameModel {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private GameState gameState;
    private PlayerModel currentPlayer;

    public enum GameState {
        NORMAL,
        DEBUG,
        COMPUTER
    }

    public GameModel() {
        this.createBasementGame();
    }

    public PlayerModel createPlayer() {
        return new PlayerModel();
    }

    public String createPlayerName() {
        System.out.print("Enter player name: ");
        Scanner sc = new Scanner(System.in);
        String playerName = "Default Player";
        if (sc.hasNextLine()) {
            playerName = sc.nextLine();
        }
        return playerName;
    }

    public BoardModel createPlayerBoard() {
        return new BoardModel();
    }

    public void addGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void createBasementGame() {
        this.playerOne = createPlayer();
        this.playerTwo = createPlayer();

        this.playerOne.setPlayerName(this.createPlayerName());
        this.playerTwo.setPlayerName(this.createPlayerName());

        this.playerOne.setBoard(this.createPlayerBoard());
        this.playerTwo.setBoard(this.createPlayerBoard());

    }

    public void createRandomBoardWithShip(){
        int randomNumber = 1 + (int)(Math.random() * 10);
        if (randomNumber <= 5) {
            this.playerOne.placeShip();
            this.playerTwo.placeShip();
            this.currentPlayer = playerOne;
        } else {
            this.playerTwo.placeShip();
            this.playerOne.placeShip();
            this.currentPlayer = playerTwo;
        }
    }

    public void createRandomBoardForComputer(){
        this.playerTwo.placeShip();
    }

    public PlayerModel getPlayerOne() {
        return this.playerOne;
    }

    public PlayerModel getPlayerTwo() {
        return this.playerTwo;
    }

    public void playerGameMove() {
        this.playerOne.playerMove(this.playerTwo);
        this.playerTwo.playerMove(this.playerOne);
    }

    private void switchPlayer() {
        this.currentPlayer = ( this.currentPlayer ==  this.playerOne) ?  this.playerTwo :  this.playerOne;
    }

    public void startNormalGame() {

        while (true) {
            PlayerModel opponent = ( this.currentPlayer ==  this.playerOne) ?  this.playerTwo :  this.playerOne;
            this.currentPlayer.playerMove(opponent);

            if (opponent.getBoard().allShipsAreHit()) {
                System.out.println( this.currentPlayer.getPlayerName() + " wins!");
                break;
            }
            this.switchPlayer();
        }
    }

    public void startComputerGame() {
        while (true) {
            PlayerModel opponent =   this.playerTwo;
            this.currentPlayer.playerMove(opponent);

            if (opponent.getBoard().allShipsAreHit()) {
                System.out.println( this.currentPlayer.getPlayerName() + " wins!");
                break;
            }
            this.switchPlayer();
        }
    }
    
    public void playGame() {
        switch (this.gameState) {
            case DEBUG:
                this.playerGameMove();
                this.createRandomBoardWithShip();

                break;
            case NORMAL:
                this.startGame();
                break;
            case COMPUTER:
                this.playerOne.placeShip();
                this.createRandomBoardForComputer();
                this.startComputerGame();
                break;
            default:
                System.out.println("Game is offline or in an unknown state.");
                break;
        }
    }
}