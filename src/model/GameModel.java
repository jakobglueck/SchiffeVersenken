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

    private String createPlayerName() {
        return DEFAULT_PLAYER_NAME;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void startGame() {
        switch (this.gameState) {
            case NORMAL:
                createPlayersForNormalGame();
                createRandomBoardWithShip();
                break;
            case DEBUG:
                createPlayersForDebugGame();
                createRandomBoardWithShip();
                break;
            case COMPUTER:
                createPlayerAndComputer();
                createRandomBoardWithShip();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.gameState);
        }
    }

    private void createPlayersForNormalGame() {
        String playerOneName = createPlayerName();
        String playerTwoName = createPlayerName();

        this.playerOne = createPlayer(playerOneName);
        this.playerTwo = createPlayer(playerTwoName);
    }

    private void createPlayersForDebugGame() {
        this.createPlayersForNormalGame();
    }

    private void createPlayerAndComputer() {
        String playerName = createPlayerName();
        this.playerOne = createPlayer(playerName);
        this.playerTwo = new ComputerPlayerModel("Computer");

        this.currentPlayer = playerOne;
    }
            this.playerTwo.getBoard().placeAllShips();
            this.playerOne.getBoard().placeAllShips();
            this.currentPlayer = playerTwo;
        }
    }

    public PlayerModel getPlayerOne() {
        return this.playerOne;
    }

    public PlayerModel getPlayerTwo() {
        return this.playerTwo;
    }

    public void playerGameMove(int x, int y) {
        this.currentPlayer.takeTurn(this.currentPlayer == playerOne ? playerTwo : playerOne, x, y);
    }

    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.playerOne) ? this.playerTwo : this.playerOne;
    }

    public boolean isGameOver() {
        return playerOne.getBoard().allShipsAreHit() || playerTwo.getBoard().allShipsAreHit();
    }

    public void playTurn(int x, int y) {
        playerGameMove(x, y);
        if (!isGameOver()) {
            switchPlayer();
        } else {
            System.out.println(currentPlayer.getPlayerName() + " wins!");
        }
    }

    public void playComputerTurn() {
        if (currentPlayer instanceof ComputerPlayerModel) {
            ((ComputerPlayerModel) currentPlayer).makeMove(this.currentPlayer == playerOne ? playerTwo : playerOne);
            if (!isGameOver()) {
                switchPlayer();
            } else {
                System.out.println(currentPlayer.getPlayerName() + " wins!");
            }
        }
    }
}
