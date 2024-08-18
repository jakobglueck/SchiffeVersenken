/**
 * @file ShipModel.java
 */

package model;

import utils.CellState;

import java.util.*;

/**
 * @class ShipModel
 * @brief Verantwortlich für die Verwaltung der Zellen, die ein Schiff auf dem Spielfeld bilden.
 * Weiterhin wird der den Zustand des Schiffes geprüft.
 */
public class ShipModel {
    private final List<CellModel> shipCells;
    private final int length;
    private final boolean horizontal;
    private boolean sunk;

    /**
     * @brief Der Konstruktor belegt alle Klassenvariablen mit Werten.
     *        Mittels der Koordinaten der ersten Zelle, sowie die Länge und Ausrichtung dieser, werden die restlichen
     *        Zellen des Schiffes berechnet und auf das Board gesetzt.
     * @param boardModel Das Board, auf dem das Schiff platziert wird.
     * @param startX Die X-Koordinate der ersten Schiffzelle.
     * @param startY Die Y-Koordinate der ersten Schiffzelle.
     * @param length Die Länge des Schiffs.
     * @param horizontal Gibt die Ausrichtung (horizontal (true) oder vertikal(false)) des Schiffes an.
     */
    public ShipModel(BoardModel boardModel, int startX, int startY, int length, boolean horizontal) {
        this.length = length;
        this.horizontal = horizontal;
        this.sunk = false;
        this.shipCells = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            // Ermittlung der X-Koordinate abhängig von der Ausrichtung des Schiffes.
            int currentX = horizontal ? boardModel.getCell(startX, startY).getX() + i : boardModel.getCell(startX, startY).getX();
            // Ermittlung der Y-Koordinate abhängig von der Ausrichtung des Schiffes.
            int currentY = horizontal ? boardModel.getCell(startX, startY).getY() : boardModel.getCell(startX, startY).getY() + i;
            // Der Zellstatus wird auf SET gestellt.
            boardModel.getCell(currentX, currentY).updateCellState(CellState.SET);
            this.shipCells.add(boardModel.getCell(currentX, currentY));
        }
    }

    /**
     * @brief Gibt alle Zellen des Schiffes zurück.
     * @return Liste der Zellen des Schiffs
     */
    public List<CellModel> getShipCells() {
        return this.shipCells;
    }

    /**
     * @brief Gibt den Wert der Klassenvariable sunk zurück.
     * @return den boolean Wert der Klassenvariable sunk
     */
    public boolean isSunk() {
        return this.sunk;
    }

    /**
     * @brief Überprüft, ob ein Angriff ein Teil des Schiffes getroffen hat.
     * @param cordX Die X-Koordinate des Angriffs.
     * @param cordY Die Y-Koordinate des Angriffs.
     * @return true, wenn das Schiff an dieser Position getroffen wurde.
     */
    public boolean isHit(int cordX, int cordY) {
        for (CellModel cell : this.shipCells) {
            if (cell.getX() == cordX && cell.getY() == cordY) {
                return true;
            }
        }
        return false;
    }

    /**
     * @brief Überprüft, ob alle Zellen des Schiffs getroffen wurden und setzt, wenn alle Zellen des Schiffes getroffen worden,
     * die Klassenvariable sunk auf true.
     */
    public void checkShipStatus() {
        for (CellModel cell : this.shipCells) {
            if (cell.getCellState() != CellState.HIT) {
                return;
            }
        }
        this.sunk = true;
    }
}