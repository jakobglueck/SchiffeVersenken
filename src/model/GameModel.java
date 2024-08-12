package model;

import utils.GameState;

import java.util.Random;

public class GameModel {

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    private GameState gameState;
    private PlayerModel currentPlayer;

    private static final int RANDOM_NUMBER = 5;
    private static final String DEFAULT_PLAYER_NAME = "Default Player";

    public GameModel() {
        // Leerer Konstruktor
    }

    public PlayerModel createPlayer(String playerName) {
        return new PlayerModel(playerName);
    }

    /**
     * Setzt den aktuellen Spielzustand.
     * @param gameState Der Spielzustand.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Erstellt die Spieler mit den gegebenen Namen.
     * @param playerOneName Name des ersten Spielers.
     * @param playerTwoName Name des zweiten Spielers (oder "Computer").
     */
    public void createPlayerWithNames(String playerOneName, String playerTwoName) {
        this.playerOne = createPlayer(playerOneName != null ? playerOneName : DEFAULT_PLAYER_NAME);
        this.playerTwo = playerTwoName != null && !playerTwoName.equals("Computer")
                ? createPlayer(playerTwoName)
                : new ComputerPlayerModel("Computer");
    }

    /**
     * Startet das Spiel basierend auf dem Spielzustand.
     */
    public void startGame() {
        if (this.playerOne == null || this.playerTwo == null) {
            throw new IllegalStateException("Spieler müssen vor dem Start des Spiels initialisiert werden.");
        }

        switch (this.gameState) {
            case NORMAL:
                this.currentPlayer = this.randomPlayer();
                this.playerOne.getBoard().placeAllShips();
                this.playerTwo.getBoard().placeAllShips();
                break;
            case DEBUG:
                this.playerOne.getBoard().placeAllShips();
                this.playerTwo.getBoard().placeAllShips();
                this.currentPlayer = this.randomPlayer();
                break;
            case COMPUTER:
                this.playerTwo = new ComputerPlayerModel("Computer");
                this.playerTwo.getBoard().placeAllShips();
                this.currentPlayer = this.playerOne;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.gameState);
        }
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

    /**
     * Wechselt den aktuellen Spieler.
     */
    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.playerOne) ? this.playerTwo : this.playerOne;
    }

    /**
     * Überprüft, ob das Spiel vorbei ist.
     * @return true, wenn das Spiel vorbei ist; sonst false.
     */
    public boolean isGameOver() {
        return this.playerOne.getBoard().allShipsAreHit() || this.playerTwo.getBoard().allShipsAreHit();
    }

    /**
     * Führt einen Zug für den aktuellen Spieler aus.
     * @param x Die X-Koordinate des Zugs.
     * @param y Die Y-Koordinate des Zugs.
     */
    public void playerTurn(int x, int y) {
        playerGameMove(x, y);
        if (!this.isGameOver()) {
            this.switchPlayer();
        } else {
            System.out.println(this.currentPlayer.getPlayerName() + " wins!");
        }
    }

    /**
     * Führt einen Zug für den Computer aus, wenn dieser am Zug ist.
     */
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

    /**
     * Platziert das nächste Schiff auf dem Brett des aktuellen Spielers.
     * @param startX Die X-Koordinate des Schiffsanfangs.
     * @param startY Die Y-Koordinate des Schiffsanfangs.
     * @param horizontal Gibt an, ob das Schiff horizontal platziert wird.
     * @return true, wenn das Schiff erfolgreich platziert wurde; sonst false.
     */
    public boolean placeNextShip(int startX, int startY, boolean horizontal) {
        boolean placed = this.currentPlayer.placeNextShip(startX, startY, horizontal);
        if (placed && this.currentPlayer.allShipsPlaced()) {
            this.switchPlayer();
        }
        return placed;
    }

    /**
     * Überprüft, ob alle Schiffe platziert wurden.
     * @return true, wenn alle Schiffe platziert wurden; sonst false.
     */
    public boolean allShipsPlaced() {
        return this.playerOne.allShipsPlaced() && this.playerTwo.allShipsPlaced();
    }
}
