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
        // Leerer Konstruktor
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
        if (this.playerOne == null || this.playerTwo == null) {
            throw new IllegalStateException("Spieler müssen vor dem Start des Spiels initialisiert werden.");
        }

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
                this.currentPlayer = this.playerOne;  // Der menschliche Spieler beginnt
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

    public void playerGameMove(int x, int y) {
        this.currentPlayer.takeTurn(this.currentPlayer == this.playerOne ? this.playerTwo : this.playerOne, x, y);
    }

    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.playerOne) ? this.playerTwo : this.playerOne;
    }

    public boolean isGameOver() {
        return this.playerOne.getBoard().allShipsAreHit() || this.playerTwo.getBoard().allShipsAreHit();
    }

    public void playerTurn(int x, int y) {
        playerGameMove(x, y);
        if (!this.isGameOver()) {
            this.switchPlayer();
        } else {
            System.out.println(this.currentPlayer.getPlayerName() + " wins!");
        }
    }

    public void computerPlayTurn() {
        if (this.currentPlayer instanceof ComputerPlayerModel) {
            ((ComputerPlayerModel) this.currentPlayer).makeMove(this.currentPlayer == this.playerOne ? this.playerTwo : this.playerOne);
            if (!isGameOver()) {
                this.switchPlayer();
            } else {
                System.out.println(this.currentPlayer.getPlayerName() + " wins!");
            }
        }
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

    public int getCurrentShipLength() {
        return BoardModel.BOAT_SIZES[currentShipIndex];
    }

    public void resetShipPlacement() {
        currentShipIndex = 0;
    }

    public boolean allShipsPlaced() {
        return this.playerOne.allShipsPlaced() && this.playerTwo.allShipsPlaced();
    }

    public void resetGame() {
        playerOne.reset();
        playerTwo.reset();
        currentPlayer = playerOne;
        resetShipPlacement();
    }
}
