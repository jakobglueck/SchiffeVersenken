/**
 * @file GameModel.java
 * @brief Diese Klasse repräsentiert das Modell des Spiels und enthält die Logik für den Spielzustand, die Spieler und die Schiffsplatzierung.
 */

package model;

import utils.GameState;

import java.util.Random;

/**
 * @class GameModel
 * @brief Verantwortlich für die Verwaltung der Spielzustände, Spieler und Schiffsplatzierungen.
 */
public class GameModel {

    private PlayerModel playerOne; ///< Das Modell für Spieler 1.
    private PlayerModel playerTwo; ///< Das Modell für Spieler 2 oder den Computergegner.
    private GameState gameState; ///< Der aktuelle Zustand des Spiels.
    private PlayerModel currentPlayer; ///< Der Spieler, der momentan am Zug ist.
    public int currentShipIndex; ///< Der Index des aktuellen Schiffs, das platziert wird.

    private static final int RANDOM_NUMBER = 5; ///< Eine Konstante zur Bestimmung des zufälligen Spielstarts.
    private static final String DEFAULT_PLAYER_NAME = "Default Player"; ///< Der Standardname für Spieler, falls keiner angegeben wird.
    private static final int[] SHIP_SIZES = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2}; ///< Die Größen der Schiffe, die im Spiel platziert werden.

    /**
     * @brief Konstruktor der Klasse GameModel.
     */
    public GameModel() {

    }

    /**
     * @brief Erstellt einen neuen Spieler mit dem gegebenen Namen.
     * @param playerName Der Name des Spielers.
     * @return Ein neues PlayerModel-Objekt.
     */
    public PlayerModel createPlayer(String playerName) {
        return new PlayerModel(playerName);
    }

    /**
     * @brief Setzt den aktuellen Spielzustand.
     * @param gameState Der neue Spielzustand.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * @brief Erstellt zwei Spieler und setzt deren Namen.
     * @param playerOneName Der Name des ersten Spielers.
     * @param playerTwoName Der Name des zweiten Spielers oder "Computer" für den Computergegner.
     */
    public void createPlayerWithNames(String playerOneName, String playerTwoName) {
        this.playerOne = createPlayer(playerOneName != null ? playerOneName : DEFAULT_PLAYER_NAME);

        this.playerTwo = playerTwoName != null && !playerTwoName.equals("Computer") ? createPlayer(playerTwoName)
                : new ComputerPlayerModel("Computer");
    }

    /**
     * @brief Startet das Spiel und initialisiert den aktuellen Spieler und die Schiffsplatzierung.
     */
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

    /**
     * @brief Wählt zufällig einen Spieler aus, der das Spiel beginnt.
     * @return Der zufällig ausgewählte Spieler.
     */
    private PlayerModel randomPlayer() {
        Random ra = new Random();
        return (ra.nextInt(10) < RANDOM_NUMBER) ? this.playerOne : this.playerTwo;
    }

    /**
     * @brief Gibt das Modell des ersten Spielers zurück.
     * @return Das PlayerModel für Spieler 1.
     */
    public PlayerModel getPlayerOne() {
        return this.playerOne;
    }

    /**
     * @brief Gibt das Modell des zweiten Spielers zurück.
     * @return Das PlayerModel für Spieler 2 oder den Computergegner.
     */
    public PlayerModel getPlayerTwo() {
        return this.playerTwo;
    }

    /**
     * @brief Gibt das Modell des aktuell am Zug befindlichen Spielers zurück.
     * @return Das PlayerModel des aktuellen Spielers.
     */
    public PlayerModel getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * @brief Gibt den aktuellen Zustand des Spiels zurück.
     * @return Der aktuelle GameState des Spiels.
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * @brief Wechselt den aktuellen Spieler.
     */
    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.playerOne) ? this.playerTwo : this.playerOne;
    }

    /**
     * @brief Überprüft, ob das Spiel beendet ist, basierend darauf, ob alle Schiffe eines Spielers getroffen wurden.
     * @return true, wenn das Spiel vorbei ist; false sonst.
     */
    public boolean isGameOver() {
        return this.playerOne.getBoard().allShipsAreHit() || this.playerTwo.getBoard().allShipsAreHit();
    }

    /**
     * @brief Gibt die Größen der Schiffe zurück, die im Spiel verwendet werden.
     * @return Ein Array der Schiffgrößen.
     */
    public int[] getShipSizes() {
        return SHIP_SIZES;
    }

    /**
     * @brief Platziert das nächste Schiff auf dem Spielfeld des aktuellen Spielers.
     * @param startX Die X-Position, an der das Schiff platziert werden soll.
     * @param startY Die Y-Position, an der das Schiff platziert werden soll.
     * @param horizontal Gibt an, ob das Schiff horizontal platziert wird.
     * @return true, wenn das Schiff erfolgreich platziert wurde; false sonst.
     */
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

    /**
     * @brief Setzt den Index für die Schiffsplatzierung zurück.
     */
    public void resetShipPlacement() {
        currentShipIndex = 0;
    }

    /**
     * @brief Setzt das Spiel zurück und initialisiert die Spieler und die Schiffsplatzierung neu.
     */
    public void resetGame() {
        playerOne.reset();
        playerTwo.reset();
        currentPlayer = playerOne;
        resetShipPlacement();
    }
}
