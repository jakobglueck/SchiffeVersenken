/**
 * @file PlayerStatus.java
 */

package model;

/**
 * @class PlayerStatus
 * @brief Verantwortlich für die Aufzeichnung und Berechnung von Spielerstatistiken.
 */
public class PlayerStatus {
    // Gesamtzahl der Klicks
    private int totalClicks;
    // Anzahl der Treffer
    private int hits;
    //Anzahl der versenkten Schiffe
    private int shunkShips;

    /**
     * @brief Konstruktor, der den Spielerstatus initialisiert und die Klassenvariablen auf ihre Standardwerte setzt.
     */
    PlayerStatus() {
        this.totalClicks = 0;
        this.hits = 0;
        this.shunkShips = 0;
    }

    /**
     * @brief Gibt die Gesamtzahl der Klicks zurück, die ein Spieler gemacht hat.
     * @return Die Gesamtzahl der Klicks.
     */
    public int getTotalClicks() {
        return this.totalClicks;
    }

    /**
     * @brief Gibt die Anzahl der Treffer zurück, die ein Spieler gemacht hat.
     * @return Die Anzahl der Treffer.
     */
    public int getHits() {
        return this.hits;
    }

    /**
     * @brief Gibt die Anzahl der Schiffe an, die ein Spieler versenkt hat.
     * @return Die Anzahl der versenkten Schiffe.
     */
    public int getSunkShips() {
        return this.shunkShips;
    }

    /**
     * @brief Erhöht die Gesamtzahl der Klicks um eins.
     */
    public void updateTotalClicks() {
        this.totalClicks++;
    }

    /**
     * @brief Berechnet die Anzahl der Treffer.
     * @param boardModel Das Board, das die aktuellen Schiffe und Zellen enthält.
     */
    public void calculateHits(BoardModel boardModel) {
        int temp = 0;
        for (ShipModel ship : boardModel.getPlayerShips()) {
            for (CellModel cell : ship.getShipCells()) {
                if (cell.isHit()) {
                    temp++;
                }
            }
        }
        this.hits = temp;
    }

    /**
     * @brief Berechnet die Anzahl der versenkten Schiffe.
     * @param boardModel Das Board, das die aktuellen Schiffe und Zellen enthält.
     */
    public void calculateShunkShips(BoardModel boardModel) {
        int temp = 0;
        for (ShipModel ship : boardModel.getPlayerShips()) {
            if (ship.isSunk()) {
                temp++;
            }
        }
        this.shunkShips = temp;
    }

    /**
     * @brief Setzt den Spielerstatus zurück, indem alle Werte auf Null gesetzt werden.
     */
    public void reset() {
        this.totalClicks = 0;
        this.hits = 0;
        this.shunkShips = 0;
    }
}
