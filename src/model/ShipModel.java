/**
 * @file ShipModel.java
 * @brief Diese Klasse repräsentiert ein Schiff auf dem Spielfeld und enthält die Logik für dessen Platzierung und Zustand.
 */

package model;

import utils.CellState;

import java.util.ArrayList;
import java.util.List;

/**
 * @class ShipModel
 * @brief Verantwortlich für die Verwaltung der Zellen, die ein Schiff auf dem Spielfeld bilden, und den Zustand des Schiffs (z.B. ob es versenkt wurde).
 */
public class ShipModel {
    private final List<CellModel> shipCells; ///< Eine Liste der Zellen, die das Schiff bilden.
    private final int length; ///< Die Länge des Schiffs.
    private final boolean horizontal; ///< Gibt an, ob das Schiff horizontal oder vertikal platziert ist.
    private boolean sunk; ///< Gibt an, ob das Schiff versenkt wurde.

    /**
     * @brief Konstruktor, der ein Schiff auf dem Spielfeld platziert und seine Zellen initialisiert.
     * @param boardModel Das Spielfeldmodell, auf dem das Schiff platziert wird.
     * @param startX Die X-Koordinate des Startpunktes des Schiffs.
     * @param startY Die Y-Koordinate des Startpunktes des Schiffs.
     * @param length Die Länge des Schiffs.
     * @param horizontal Gibt an, ob das Schiff horizontal oder vertikal platziert wird.
     */
    public ShipModel(BoardModel boardModel, int startX, int startY, int length, boolean horizontal) {
        this.length = length;
        this.horizontal = horizontal;
        this.sunk = false;
        this.shipCells = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            int currentX = horizontal ? boardModel.getCell(startX, startY).getX() + i : boardModel.getCell(startX, startY).getX();
            int currentY = horizontal ? boardModel.getCell(startX, startY).getY() : boardModel.getCell(startX, startY).getY() + i;
            boardModel.getCell(currentX, currentY).updateCellState(CellState.SET);
            this.shipCells.add(boardModel.getCell(currentX, currentY));
        }
    }

    /**
     * @brief Überprüft, ob das Schiff an der angegebenen Position getroffen wurde.
     * @param x Die X-Koordinate des Treffers.
     * @param y Die Y-Koordinate des Treffers.
     * @return true, wenn das Schiff an dieser Position getroffen wurde; false sonst.
     */
    public boolean isHit(int x, int y) {
        for (CellModel cell : this.shipCells) {
            if (cell.getX() == x && cell.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * @brief Überprüft, ob alle Zellen des Schiffs getroffen wurden und setzt den Status des Schiffs entsprechend.
     */
    public void checkShipStatus() {
        for (CellModel cell : this.shipCells) {
            if (cell.getCellState() != CellState.HIT) {
                return;
            }
        }
        this.sunk = true;
    }

    /**
     * @brief Gibt an, ob das Schiff versenkt wurde.
     * @return true, wenn das Schiff versenkt wurde; false sonst.
     */
    public boolean isSunk() {
        return this.sunk;
    }

    /**
     * @brief Gibt die Liste der Zellen zurück, die das Schiff bilden.
     * @return Eine Liste der Zellen des Schiffs.
     */
    public List<CellModel> getShipCells() {
        return this.shipCells;
    }
}