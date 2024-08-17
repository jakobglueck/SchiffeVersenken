package model;

import utils.GameState;

import java.util.Random;

public class GameModel {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private GameState gameState;
    private PlayerModel currentPlayer;
    public int currentShipIndex;

    private static final int RANDOM_NUMBER = 5;
    private static final String DEFAULT_PLAYER_NAME = "Default Player";
    private static final int[] SHIP_SIZES = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    public GameModel() {

    }

    public PlayerModel createPlayer(String playerName) {
        return new PlayerModel(playerName);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void createPlayerWithNames(String playerOneName, String playerTwoName) {
        this.playerOne = createPlayer(playerOneName != null ? playerOneName : DEFAULT_PLAYER_NAME);

        this.playerTwo = playerTwoName != null && !playerTwoName.equals("Computer") ? createPlayer(playerTwoName)
                : new ComputerPlayerModel("Computer");
    }

    public void startGame() {
        switch (this.gameState) {
            case NORMAL:
                this.currentPlayer = this.randomPlayer();
                break;
            case DEBUG:
                this.playerOne.getBoard().placeAllShips();
                this.playerTwo.getBoard().placeAllShips();
                this.currentPlayer = this.randomPlayer();
                break;
            case COMPUTER:
                this.playerTwo.getBoard().placeAllShips();
                this.currentPlayer = this.playerOne;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.gameState);
        }

        resetShipPlacement();
    }

    private PlayerModel randomPlayer() {
        Random ra = new Random();
        return (ra.nextInt(10) < RANDOM_NUMBER) ? this.playerOne : this.playerTwo;
    }

    public PlayerModel getPlayerOne() {
        return this.playerOne;
    }

    public PlayerModel getPlayerTwo() {
        return this.playerTwo;
    }

    public PlayerModel getCurrentPlayer() {
        return this.currentPlayer;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.playerOne) ? this.playerTwo : this.playerOne;
    }

    public boolean isGameOver() {
        return this.playerOne.getBoard().allShipsAreHit() || this.playerTwo.getBoard().allShipsAreHit();
    }


    public int[] getShipSizes() {
        return SHIP_SIZES;
    }

    public boolean placeNextShip(int startX, int startY, boolean horizontal) {
        if (currentShipIndex >= SHIP_SIZES.length) {
            return false;
        }

        int shipLength = SHIP_SIZES[currentShipIndex];
        boolean placed = getCurrentPlayer().getBoard().placeShip(startX, startY, horizontal, shipLength);

        if (placed) {
            currentShipIndex++;
        }

        return placed;
    }

    public void resetShipPlacement() {
        currentShipIndex = 0;
    }

    public void resetGame() {
        playerOne.reset();
        playerTwo.reset();
        currentPlayer = playerOne;
        resetShipPlacement();
    }
}
