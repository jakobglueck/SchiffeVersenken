/**
 * @file CellModel.java
 */

package model;

import utils.CellState;

/**
 * @class CellModel
 * @brief Diese Klasse stellt eine einzelne Zelle auf einem Spielfeld dar. Sie enthält Informationen über den Zustand der Zelle,
 *        sowie deren Position auf dem Spielfeld, welche durch X- und Y-Koordinaten angegeben werden.
 *        Diese Klasse ermöglicht es, die einzelnen Zellkomponenten zu erhalten, zu aktualisieren und zu überprüfen, ob die Zelle getroffen wurde.
 */
public class CellModel {
    private CellState cellState;
    private final int cordX;
    private final int cordY;

    /**
     * @brief Konstruktor, legt die Variablen der Klasse fest.
     * @param cordX Die X-Koordinate der Zelle auf dem Spielfeld, die ihre horizontale Position bestimmt.
     * @param cordY Die Y-Koordinate der Zelle auf dem Spielfeld, die ihre vertikale Position bestimmt.
     * @param cellState Der anfängliche Zustand der Zelle, der bestimmt, ob die Zelle frei, belegt, getroffen usw. ist.
     */
    public CellModel(int cordX, int cordY, CellState cellState) {
        this.cellState = cellState;
        this.cordX = cordX;
        this.cordY = cordY;
    }

    /**
     * @brief Gibt die X-Koordinate der Zelle zurück.
     * @return Die X-Koordinate der Zelle als Ganzzahl.
     */
    public int getX() {
        return this.cordX;
    }

    /**
     * @brief Gibt die Y-Koordinate der Zelle zurück.
     * @return Die Y-Koordinate der Zelle als Ganzzahl.
     */
    public int getY() {
        return this.cordY;
    }

    /**
     * @brief Gibt den aktuellen Zustand der Zelle zurück.
     * @return Gibt den aktuellen Zustand der Zelle als Enum-Wert des CellState-Enums wieder.
     */
    public CellState getCellState() {
        return this.cellState;
    }

    /**
     * @brief Aktualisiert den Zustand der Zelle.
     *        Diese Methode ändert den Zustand der Zelle.
     * @param cellState Der neue Zustand der Zelle, der ein Wert des CellState-Enums ist.
     */
    public void updateCellState(CellState cellState) {
        this.cellState = cellState;
    }

    /**
     * @brief Diese Methode überprüft, den Zustand der Zelle mit den CellState.HIT, um zu überprüfen, ob eine Zelle durch einen
     *        Spieler
     * @return Wenn die Zelle getroffen wurde, erfolgt als Ausgabe true;
     */
    public boolean isHit() {
        return this.cellState == CellState.HIT;
    }
}
