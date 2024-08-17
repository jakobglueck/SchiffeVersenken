/**
 * @file PlayerStatus.java
 * @brief Diese Klasse verwaltet den Status eines Spielers, einschließlich der Gesamtanzahl von Klicks, Treffern und versenkten Schiffen.
 */

package model;

/**
 * @class PlayerStatus
 * @brief Verantwortlich für die Aufzeichnung und Berechnung von Spielerstatistiken wie Klicks, Treffer und versenkte Schiffe.
 */
public class PlayerStatus {

    private int totalClicks; ///< Die Gesamtzahl der Klicks, die der Spieler gemacht hat.
    private int hits; ///< Die Anzahl der Treffer, die der Spieler gelandet hat.
    private int shunkShips; ///< Die Anzahl der versenkten Schiffe des Spielers.

    /**
     * @brief Konstruktor, der den Spielerstatus initialisiert.
     */
    PlayerStatus() {
        this.totalClicks = 0;
        this.hits = 0;
        this.shunkShips = 0;
    }

    /**
     * @brief Gibt die Gesamtzahl der Klicks zurück, die der Spieler gemacht hat.
     * @return Die Gesamtzahl der Klicks.
     */
    public int getTotalClicks() {
        return this.totalClicks;
    }

    /**
     * @brief Erhöht die Gesamtzahl der Klicks um eins.
     */
    public void updateTotalClicks() {
        this.totalClicks++;
    }

    /**
     * @brief Gibt die Anzahl der Treffer zurück, die der Spieler gemacht hat.
     * @return Die Anzahl der Treffer.
     */
    public int getHits() {
        return this.hits;
    }

    /**
     * @brief Berechnet die Anzahl der Treffer basierend auf dem aktuellen Zustand des Spielfelds.
     * @param boardModel Das Spielfeldmodell, das die aktuellen Schiffe und Zellen enthält.
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
     * @brief Gibt die Anzahl der versenkten Schiffe zurück.
     * @return Die Anzahl der versenkten Schiffe.
     */
    public int getSunkShips() {
        return this.shunkShips;
    }

    /**
     * @brief Berechnet die Anzahl der versenkten Schiffe basierend auf dem aktuellen Zustand des Spielfelds.
     * @param boardModel Das Spielfeldmodell, das die aktuellen Schiffe und Zellen enthält.
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
