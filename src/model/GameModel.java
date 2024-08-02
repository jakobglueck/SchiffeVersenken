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
        OFFLINE,
        DEBUG,
        NORMAL,
        COMPUTER
    }

    public GameModel() {
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

    public void startGame() {
        System.out.println("Starting the game...");
        System.out.println(currentPlayer.getPlayerName() + " goes first.");

        while (true) {
            System.out.println("\n" + currentPlayer.getPlayerName() + "'s turn:");
            PlayerModel opponent = (currentPlayer == playerOne) ? playerTwo : playerOne;
            currentPlayer.playerMove(opponent);

            if (opponent.getBoard().allShipsAreHit()) {
                System.out.println(currentPlayer.getPlayerName() + " wins!");
                break;
            }
            currentPlayer = opponent;
        }
    }

    public void playGame() {
        switch (this.gameState) {
            case DEBUG:
                this.createBasementGame();
                playerGameMove();
                break;
            case NORMAL:
                this.createBasementGame();
                startGame();
                break;
            case COMPUTER:
                this.createBasementGame();
                System.out.println("Computer game mode not implemented yet.");
                break;
            case OFFLINE:
                this.createBasementGame();
            default:
                System.out.println("Game is offline or in an unknown state.");
                break;
        }
    }
}