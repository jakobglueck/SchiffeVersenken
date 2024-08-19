/**
 * @file GameModel.java
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

    private PlayerModel playerOne;
    private PlayerModel playerTwo;
    // aktuelle Zustand des Spiels
    private GameState gameState;
    // Spieler, der momentan am Zug ist
    private PlayerModel currentPlayer;
    // Index des aktuellen Schiffs, das platziert wird
    public int currentShipIndex;
    //Standardname für Spieler, falls keiner angegeben wird
    private static final String DEFAULT_PLAYER_NAME = "Default Player";
    // Länge der Schiffe
    private static final int[] SHIP_SIZES = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    /**
     * @brief Konstruktor der Klasse GameModel.
     */
    public GameModel() {

    }

    /**
     * @brief Gibt das PlayerModel des ersten Spielers zurück.
     * @return Das PlayerModel für Spieler 1.
     */
    public PlayerModel getPlayerOne() {
        return this.playerOne;
    }

    /**
     * @brief Gibt das PlayerModel des zweiten Spielers zurück.
     * @return Das PlayerModel für Spieler 2.
     */
    public PlayerModel getPlayerTwo() {
        return this.playerTwo;
    }

    /**
     * @brief Gibt das PlayerModel des aktuellen Spielers zurück.
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
     * @brief Gibt die Länge der Schiffe zurück.
     * @return Ein Array mit der Länge der Schiffe.
     */
    public int[] getShipSizes() {
        return SHIP_SIZES;
    }

    /**
     * @brief Setzt den aktuellen Spielzustand.
     * @param gameState Der neue Spielzustand.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
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
     * @brief Erstellt zwei Spieler und setzt deren Namen.
     * @param playerOneName Der Name des ersten Spielers.
     * @param playerTwoName Der Name des zweiten Spielers oder "Computer" für den Computergegner.
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
     * @brief Wählt zufällig einen Spieler aus, der das Spiel beginnt.
     * @return Der zufällig ausgewählte Spieler.
     */
    private PlayerModel randomPlayer() {
        Random ra = new Random();
        return (ra.nextInt(10) < 5) ? this.playerOne : this.playerTwo;
    }


    /**
     * @brief Wechselt den aktuellen Spieler.
     */
    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.playerOne) ? this.playerTwo : this.playerOne;
    }

    /**
     * @brief Startet das Spiel, abhänigi vom Spielmodus und initialisiert den aktuellen Spieler und die Schiffsplatzierung.
     */
    public void startGame() {
        switch (this.gameState) {
            case NORMAL:
                this.currentPlayer = this.randomPlayer();
                break;
            case DEBUG:
                // Setzt für beide Spieler alle Schiffe automatisch.
                this.playerOne.getBoard().placeAllShips();
                this.playerTwo.getBoard().placeAllShips();
                this.currentPlayer = this.randomPlayer();
                break;
            case COMPUTER:
                // Setzt für den Computer alle Schiffe automatisch.
                this.playerTwo.getBoard().placeAllShips();
                this.currentPlayer = this.playerOne;
                break;
            default:
                break;
        }

        resetShipPlacement();
    }

    /**
     * @brief Platziert das Schiff auf dem Board des aktuellen Spielers.
     * @param startX Die X-Koorinate, an der das Schiff platziert werden soll.
     * @param startY Die Y-Koorinate, an der das Schiff platziert werden soll.
     * @param shipSize Gibt die Länge des Schiffes an.
     * @param horizontal Gibt die Ausrichtung des Schiffes an.
     * @return true, wenn das Schiff erfolgreich platziert wurde.
     */
    public boolean placeNextShip(int startX, int startY,int shipSize, boolean horizontal) {
        if (this.currentShipIndex >= SHIP_SIZES.length) {
            return false;
        }

        boolean placed = getCurrentPlayer().getBoard().placeShip(startX, startY, horizontal, shipSize);

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
     * @brief Überprüft, ob das Spiel beendet ist. Dabei werden der Status aller Schiffe der Spieler geprüft.
     * @return true, wenn das Spiel vorbei ist.
     */
    public boolean isGameOver() {
        return this.playerOne.getBoard().allShipsAreHit() || this.playerTwo.getBoard().allShipsAreHit();
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
