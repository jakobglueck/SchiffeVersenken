/**
 * @file PlayerModel.java
 */

package model;

import utils.CellState;

/**
 * @class PlayerModel
 * @brief Verantwortlich für den Namen, sowie das Board und des Status eines Spielers.
 */
public class PlayerModel {
    private String playerName;
    private BoardModel board;
    private PlayerStatus playerStatus;

    /**
     * @brief Konstruktor, der einen Spieler mit einem gegebenen Namen erstellt, ein Board, sowie die Spielerstatistiken
     *        für in erstellt.
     * @param playerName Der Name des Spielers.
     */
    public PlayerModel(String playerName) {
        this.playerName = playerName;
        this.board = new BoardModel();
        this.playerStatus = new PlayerStatus();
    }

    /**
     * @brief Gibt den Namen des Spielers zurück.
     * @return Der Name des Spielers.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @brief Gibt das Spielfeld des Spielers zurück.
     * @return Das BoardModel des Spielers.
     */
    public BoardModel getBoard() {
        return board;
    }

    /**
     * @brief Gibt den Status des Spielers zurück.
     * @return Der PlayerStatus des Spielers.
     */
    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }
}
