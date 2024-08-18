/**
 * @file GameModel.java
 * @brief Diese Klasse repräsentiert das Modell des Spiels und enthält die Logik für den Spielzustand, die Spieler und die Schiffsplatzierung.
 */

package model;

import utils.GameState;

import java.util.Objects;
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
     * @param playerName Der Name des Spielers.
     * @return Ein neues PlayerModel-Objekt.
     * @brief Erstellt einen neuen Spieler mit dem gegebenen Namen.
     */
    public PlayerModel createPlayer(String playerName) {
        return new PlayerModel(playerName);
    }

    /**
     * @param gameState Der neue Spielzustand.
     * @brief Setzt den aktuellen Spielzustand.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * @param playerOneName Der Name des ersten Spielers.
     * @param playerTwoName Der Name des zweiten Spielers oder "Computer" für den Computergegner.
     * @brief Erstellt zwei Spieler und setzt deren Namen.
     */
    public void createPlayerWithNames(String playerOneName, String playerTwoName) {
        this.playerOne = createPlayer(!Objects.equals(playerOneName, "") ? playerOneName : DEFAULT_PLAYER_NAME);
        if (this.gameState.equals(GameState.COMPUTER)) {
            this.playerTwo = new ComputerPlayerModel("Computer");
        } else {
            this.playerTwo = createPlayer(!Objects.equals(playerTwoName, "") ? playerTwoName : DEFAULT_PLAYER_NAME);
        }
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
     * @return Der zufällig ausgewählte Spieler.
     * @brief Wählt zufällig einen Spieler aus, der das Spiel beginnt.
     */
    private PlayerModel randomPlayer() {
        Random ra = new Random();
        return (ra.nextInt(10) < RANDOM_NUMBER) ? this.playerOne : this.playerTwo;
    }

    /**
     * @return Das PlayerModel für Spieler 1.
     * @brief Gibt das Modell des ersten Spielers zurück.
     */
    public PlayerModel getPlayerOne() {
        return this.playerOne;
    }

    /**
     * @return Das PlayerModel für Spieler 2 oder den Computergegner.
     * @brief Gibt das Modell des zweiten Spielers zurück.
     */
    public PlayerModel getPlayerTwo() {
        return this.playerTwo;
    }

    /**
     * @return Das PlayerModel des aktuellen Spielers.
     * @brief Gibt das Modell des aktuell am Zug befindlichen Spielers zurück.
     */
    public PlayerModel getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * @return Der aktuelle GameState des Spiels.
     * @brief Gibt den aktuellen Zustand des Spiels zurück.
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
     * @return true, wenn das Spiel vorbei ist; false sonst.
     * @brief Überprüft, ob das Spiel beendet ist, basierend darauf, ob alle Schiffe eines Spielers getroffen wurden.
     */
    public boolean isGameOver() {
        return this.playerOne.getBoard().allShipsAreHit() || this.playerTwo.getBoard().allShipsAreHit();
    }

    /**
     * @return Ein Array der Schiffgrößen.
     * @brief Gibt die Größen der Schiffe zurück, die im Spiel verwendet werden.
     */
    public int[] getShipSizes() {
        return SHIP_SIZES;
    }

    /**
     * @param startX     Die X-Position, an der das Schiff platziert werden soll.
     * @param startY     Die Y-Position, an der das Schiff platziert werden soll.
     * @param horizontal Gibt an, ob das Schiff horizontal platziert wird.
     * @return true, wenn das Schiff erfolgreich platziert wurde; false sonst.
     * @brief Platziert das nächste Schiff auf dem Spielfeld des aktuellen Spielers.
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
        playerOne = null;
        playerTwo = null;
        currentPlayer = null;
        gameState = null;
        currentShipIndex = 0;
    }
}
