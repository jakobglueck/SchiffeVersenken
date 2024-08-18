/**
 * @file CellModel.java
 */

package model;

import utils.CellState;

/**
 * @class CellModel
 * @brief Diese Klasse stellt eine einzelne Zelle auf einem Spielfeld dar. Sie enthält Informationen über den Zustand der Zelle,
 *      sowie deren Position auf dem Spielfeld, welche durch X- und Y-Koordinaten angegeben werden.
 *
 *        Diese Klasse ermöglicht es, den Zustand der Zelle abzurufen, zu aktualisieren und zu überprüfen, ob die Zelle getroffen wurde.
 */
public class CellModel {
    private CellState cellState; ///< Der aktuelle Zustand der Zelle, wie durch die CellState-Enumeration definiert.
    private final int x; ///< Die X-Koordinate der Zelle auf dem Spielfeld, repräsentiert die horizontale Position.
    private final int y; ///< Die Y-Koordinate der Zelle auf dem Spielfeld, repräsentiert die vertikale Position.

    /**
     * @brief Konstruktor, der eine Zelle mit einer bestimmten Position und einem anfänglichen Zustand erstellt.
     *        Der Zustand der Zelle wird durch den übergebenen CellState-Wert festgelegt, und die Position
     *        der Zelle wird durch die X- und Y-Koordinaten bestimmt.
     * @param x Die X-Koordinate der Zelle auf dem Spielfeld, die ihre horizontale Position bestimmt.
     * @param y Die Y-Koordinate der Zelle auf dem Spielfeld, die ihre vertikale Position bestimmt.
     * @param cellState Der anfängliche Zustand der Zelle, der bestimmt, ob die Zelle frei, belegt, getroffen usw. ist.
     */
    public CellModel(int x, int y, CellState cellState) {
        this.cellState = cellState;
        this.x = x;
        this.y = y;
    }

    /**
     * @brief Gibt die X-Koordinate der Zelle zurück.
     *        Diese Methode gibt die horizontale Position der Zelle auf dem Spielfeld zurück.
     * @return Die X-Koordinate der Zelle als Ganzzahl.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @brief Gibt die Y-Koordinate der Zelle zurück.
     *        Diese Methode gibt die vertikale Position der Zelle auf dem Spielfeld zurück.
     * @return Die Y-Koordinate der Zelle als Ganzzahl.
     */
    public int getY() {
        return this.y;
    }

    /**
     * @brief Gibt den aktuellen Zustand der Zelle zurück.
     *        Diese Methode liefert den aktuellen Zustand der Zelle, der anzeigt, ob die Zelle frei, belegt, getroffen usw. ist.
     * @return Der aktuelle Zustand der Zelle als CellState-Enumeration.
     */
    public CellState getCellState() {
        return this.cellState;
    }

    /**
     * @brief Aktualisiert den Zustand der Zelle.
     *        Diese Methode erlaubt es, den Zustand der Zelle zu ändern, um z.B. anzugeben, dass die Zelle getroffen wurde.
     * @param cellState Der neue Zustand der Zelle, der durch eine CellState-Enumeration angegeben wird.
     */
    public void updateCellState(CellState cellState) {
        this.cellState = cellState;
    }

    /**
     * @brief Überprüft, ob die Zelle getroffen wurde.
     *        Diese Methode überprüft, ob die Zelle den Zustand "getroffen" hat, was darauf hinweist, dass die Zelle von einem Angriff getroffen wurde.
     * @return true, wenn die Zelle getroffen wurde; false, wenn die Zelle nicht getroffen wurde.
     */
    public boolean isHit() {
        return this.cellState == CellState.HIT;
    }
}
