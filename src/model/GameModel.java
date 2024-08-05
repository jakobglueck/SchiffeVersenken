package model;

import java.util.Random;

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

    private PlayerModel createPlayer(String playerName) {
        return new PlayerModel(playerName);
    }

    // TODO name holen von VIEW
    private String createPlayerName() {
        System.out.print("Enter player name: ");
        String playerName = DEFAULT_PLAYER_NAME;

        return playerName;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    // BASE game mit zwei Boards
    private void createBasementGame() {
        String playerOneName = createPlayerName();
        String playerTwoName = createPlayerName();

        this.playerOne = createPlayer(playerOneName);
        this.playerTwo = createPlayer(playerTwoName);

        this.playerOne.placeShips();
        this.playerTwo.placeShips();
    }
    // create RandomBoards mit Schiffen
    private void createRandomBoardWithShip() {
        Random random = new Random();
        if (random.nextInt(10) < RANDOM_THRESHOLD) {
            this.playerOne.getBoard().placeAllShips();
            this.playerTwo.getBoard().placeAllShips();
            this.currentPlayer = playerOne;
        } else {
            this.playerTwo.getBoard().placeAllShips();
            this.playerOne.getBoard().placeAllShips();
            this.currentPlayer = playerTwo;
        }
    }
    // create RandomBoard für das Computer spiel mit Schiffen
    private void createRandomBoardForComputer() {
        this.playerTwo.getBoard().placeAllShips();
    }

    public PlayerModel getPlayerOne() {
        return this.playerOne;
    }

    public PlayerModel getPlayerTwo() {
        return this.playerTwo;
    }
    // mouse Event müssen hier getriggert werden
    public void playerGameMove(int x, int y) {
        this.currentPlayer.takeTurn(this.currentPlayer == playerOne ? playerTwo : playerOne, x, y);
    }

    private void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.playerOne) ? this.playerTwo : this.playerOne;
    }

    private void playGameLoop() {

        int x = 0;
        int y = 0;

        while (true) {
            //
            this.playerGameMove(x,y);

            if (currentPlayer == playerOne ? playerTwo.getBoard().allShipsAreHit() : playerOne.getBoard().allShipsAreHit()) {
                System.out.println(this.currentPlayer.getPlayerName() + " wins!");
                break;
            }
            this.switchPlayer();
        }
    }

    public void startNormalGame() {
        createRandomBoardWithShip();
        playGameLoop();
    }

    public void startComputerGame() {
        this.currentPlayer = this.playerOne;
        playGameLoop();
    }

    public void playGame() {
        this.createBasementGame();
        switch (this.gameState) {
            case DEBUG:
                this.playerGameMove(2, 3);
                this.createRandomBoardWithShip();
                this.startComputerGame();
                break;
            case NORMAL:
                this.startNormalGame();
                break;
            case COMPUTER:
                this.playerOne.getBoard().placeAllShips();
                this.createRandomBoardForComputer();
                this.startComputerGame();
                break;
            default:
                System.out.println("Game is offline or in an unknown state.");
                break;
        }
    }
}
