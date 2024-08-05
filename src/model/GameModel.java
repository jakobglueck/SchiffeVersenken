package model;

import utils.GameState;

public class GameModel {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private GameState gameState;
    private PlayerModel currentPlayer;

    private static final int RANDOM_NUMBER = 5;
    private static final String DEFAULT_PLAYER_NAME = "Default Player";

    public GameModel() {
    }

    private PlayerModel createPlayer(String playerName) {
        return new PlayerModel(playerName);
    }

    private String createPlayerName() {
        return DEFAULT_PLAYER_NAME;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void startGame() {
        switch (this.gameState) {
            case NORMAL:
                this.createPlayersForNormalGame();
                break;
            case DEBUG:
                this.createPlayersForDebugGame();
                break;
            case COMPUTER:
                this.createPlayerAndComputer();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.gameState);
        }
    }

    private void createPlayerNames() {
        String playerOneName = createPlayerName();
        String playerTwoName = createPlayerName();

        this.playerOne = createPlayer(playerOneName);
        this.playerTwo = createPlayer(playerTwoName);
    }

    private void createPlayersForNormalGame() {
        this.createPlayerNames();
        this.currentPlayer = playerOne;
    }

    private void createPlayersForDebugGame() {
        this.createPlayerNames();
        this.playerOne.getBoard().placeAllShips();
        this.playerTwo.getBoard().placeAllShips();
        this.currentPlayer = playerOne;
    }

    private void createPlayerAndComputer() {
        this.playerOne = createPlayer(this.createPlayerName());
        this.playerTwo = new ComputerPlayerModel("Computer");
        this.playerTwo.getBoard().placeAllShips();
        this.currentPlayer = playerOne;

    }

    public PlayerModel getPlayerOne() {
        return this.playerOne;
    }

    public PlayerModel getPlayerTwo() {
        return this.playerTwo;
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

    public boolean placeNextShip(int startX, int startY, boolean horizontal) {
        boolean placed = this.currentPlayer.placeNextShip(startX, startY, horizontal);
        if (placed && this.currentPlayer.allShipsPlaced()) {
            this.switchPlayer();
        }
        return placed;
    }

    public boolean allShipsPlaced() {
        return this.playerOne.allShipsPlaced() && this.playerTwo.allShipsPlaced();
    }
}