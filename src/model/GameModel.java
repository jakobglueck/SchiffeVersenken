package model;

import utils.CellState;

import java.util.Random;
import java.util.Scanner;

public class GameModel {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private GameState gameState;
    private PlayerModel currentPlayer;

    private static final int RANDOM_THRESHOLD = 5;
    private static final String DEFAULT_PLAYER_NAME = "Default Player";

    public enum GameState {
        NORMAL,
        DEBUG,
        COMPUTER
    }

    public GameModel() {
    }

    private PlayerModel createPlayer() {
        return new PlayerModel();
    }

    private String createPlayerName() {
        System.out.print("Enter player name: ");
        String playerName = DEFAULT_PLAYER_NAME;
        try (Scanner sc = new Scanner(System.in)) {
            if (sc.hasNextLine()) {
                String input = sc.nextLine().trim();
                if (!input.isEmpty()) {
                    playerName = input;
                }
            }
        }
        return playerName;
    }

    private BoardModel createPlayerBoard() {
        return new BoardModel();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private void createBasementGame() {
        this.playerOne = createPlayer();
        this.playerTwo = createPlayer();

        this.playerOne.setPlayerName(this.createPlayerName());
        this.playerTwo.setPlayerName(this.createPlayerName());

        this.playerOne.setBoard(this.createPlayerBoard());
        this.playerTwo.setBoard(this.createPlayerBoard());
    }

    private void createRandomBoardWithShip() {
        Random random = new Random();
        if (random.nextInt(10) < RANDOM_THRESHOLD) {
            this.playerOne.placeShip();
            this.playerTwo.placeShip();
            this.currentPlayer = playerOne;
        } else {
            this.playerTwo.placeShip();
            this.playerOne.placeShip();
            this.currentPlayer = playerTwo;
        }
    }

    private void createRandomBoardForComputer() {
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
        this.currentPlayer = (this.currentPlayer == this.playerOne) ? this.playerTwo : this.playerOne;
    }

    private void playGameLoop(PlayerModel opponent) {
        while (true) {
            this.currentPlayer.playerMove(opponent);

            if (opponent.getBoard().allShipsAreHit()) {
                System.out.println(this.currentPlayer.getPlayerName() + " wins!");
                break;
            }
            this.switchPlayer();
        }
    }

    public void startNormalGame() {
        createRandomBoardWithShip();
        playGameLoop(this.currentPlayer == this.playerOne ? this.playerTwo : this.playerOne);
    }

    public void startComputerGame() {
        this.currentPlayer = this.playerOne;
        playGameLoop(this.playerTwo);
    }

    public void playGame() {
        this.createBasementGame();
        switch (this.gameState) {
            case DEBUG:
                this.playerGameMove();
                this.createRandomBoardWithShip();
                this.startComputerGame();
                break;
            case NORMAL:
                this.startNormalGame();
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