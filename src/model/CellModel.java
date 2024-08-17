/**
 * @file CellModel.java
 * @brief Diese Klasse repräsentiert eine Zelle auf dem Spielfeld und enthält Informationen über deren Zustand und Position.
 */

package model;

import utils.CellState;

/**
 * @class CellModel
 * @brief Verantwortlich für die Verwaltung des Zustands und der Position einer einzelnen Zelle auf dem Spielfeld.
 */
public class CellModel {
    private CellState cellState; ///< Der aktuelle Zustand der Zelle (frei, belegt, getroffen, etc.).
    private final int x; ///< Die X-Koordinate der Zelle auf dem Spielfeld.
    private final int y; ///< Die Y-Koordinate der Zelle auf dem Spielfeld.

    /**
     * @brief Konstruktor, der eine Zelle mit einer bestimmten Position und einem Zustand erstellt.
     * @param x Die X-Koordinate der Zelle.
     * @param y Die Y-Koordinate der Zelle.
     * @param cellState Der anfängliche Zustand der Zelle.
     */
    public CellModel(int x, int y, CellState cellState) {
        this.cellState = cellState;
        this.x = x;
        this.y = y;
    }

    /**
     * @brief Aktualisiert den Zustand der Zelle.
     * @param cellState Der neue Zustand der Zelle.
     */
    public void updateCellState(CellState cellState) {
        this.cellState = cellState;
    }

    /**
     * @brief Gibt die X-Koordinate der Zelle zurück.
     * @return Die X-Koordinate der Zelle.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @brief Gibt die Y-Koordinate der Zelle zurück.
     * @return Die Y-Koordinate der Zelle.
     */
    public int getY() {
        return this.y;
    }

    /**
     * @brief Gibt den aktuellen Zustand der Zelle zurück.
     * @return Der aktuelle Zustand der Zelle.
     */
    public CellState getCellState() {
        return this.cellState;
    }

    /**
     * @brief Überprüft, ob die Zelle getroffen wurde.
     * @return true, wenn die Zelle getroffen wurde; false sonst.
     */
    public boolean isHit() {
        return this.cellState == CellState.HIT;
    }
}
