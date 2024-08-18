/**
 * @file PlayerModel.java
 * @brief Diese Klasse repräsentiert einen Spieler und enthält die Logik für das Spielfeld und den Spielerstatus.
 */

package model;

import utils.CellState;

/**
 * @class PlayerModel
 * @brief Verantwortlich für die Verwaltung der Spielfeldaktionen und des Status eines Spielers.
 */
public class PlayerModel {
    private String playerName; ///< Der Name des Spielers.
    private BoardModel board; ///< Das Spielfeldmodell des Spielers.
    private PlayerStatus playerStatus; ///< Der Status des Spielers, einschließlich der Statistiken.

    /**
     * @brief Konstruktor, der einen Spieler mit einem gegebenen Namen erstellt.
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
     * @brief Führt einen Spielzug gegen einen gegnerischen Spieler aus.
     * @param opponent Der gegnerische Spieler.
     * @param x Die X-Koordinate des Angriffs.
     * @param y Die Y-Koordinate des Angriffs.
     * @return true, wenn der Zug gültig ist; false sonst.
     */
    public boolean makeMove(PlayerModel opponent, int x, int y) {
        if (!this.isValidMove(x, y)) {
            return false;
        }
        boolean hit = opponent.getBoard().registerHit(x, y) != null;
        return true;
    }

    /**
     * @brief Führt einen Zug des Spielers aus und gibt das Ergebnis aus.
     * @param opponent Der gegnerische Spieler.
     * @param x Die X-Koordinate des Angriffs.
     * @param y Die Y-Koordinate des Angriffs.
     */
    public void takeTurn(PlayerModel opponent, int x, int y) {
        if (makeMove(opponent, x, y)) {
            System.out.println(playerName + " made a move.");
        } else {
            System.out.println("Invalid move. Please try again.");
        }
    }

    /**
     * @brief Überprüft, ob ein Zug auf den angegebenen Koordinaten gültig ist.
     * @param x Die X-Koordinate des Zugs.
     * @param y Die Y-Koordinate des Zugs.
     * @return true, wenn der Zug gültig ist; false sonst.
     */
    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= BoardModel.WIDTH || y < 0 || y >= BoardModel.HEIGHT) {
            return false;
        }

        CellState currentCellState = board.getCell(x, y).getCellState();
        return currentCellState != CellState.HIT;
    }

    /**
     * @brief Gibt den Status des Spielers zurück.
     * @return Der PlayerStatus des Spielers.
     */
    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    /**
     * @brief Setzt das Spielfeld und den Status des Spielers zurück.
     */
    public void reset() {
        board.reset();
        playerStatus.reset();
    }
}
