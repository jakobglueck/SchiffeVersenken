/**
 * @file BoardModel.java
 */

package model;

import utils.CellState;

import java.util.*;

/**
 * @class BoardModel
 * @brief Verantwortlich für die Verwaltung des Boards und der Schiffe eines Spielers.
 */
public class BoardModel {
    // Die Länge der Boote, die auf dem Board platziert werden.
    public static final int[] BOAT_SIZES = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};

    private final CellModel[][] board;
    private final ArrayList<ShipModel> playerShips;

    // Höhe des Spielfelds.
    public static final int HEIGHT = 10;
    //Breite des Spielfelds
    public static final int WIDTH = 10; ///

    /**
     * @brief Konstruktor, der das Board initialisiert und die Zellen erstellt.
     */
    public BoardModel() {
        this.board = new CellModel[HEIGHT][WIDTH];
        this.playerShips = new ArrayList<>();
        this.createBoard();
    }

    /**
     * @brief Gibt eine Zelle des Spielfeldes zurück.
     * @param cordX Die X-Koordinate der Zelle.
     * @param cordY Die Y-Koordinate der Zelle.
     * @return Die Zelle an der angegebenen Position und wenn die Koordinate nicht existiert null.
     */
    public CellModel getCell(int cordX, int cordY) {
        if (this.isValidCoordinate(cordX, cordY)) {
            return this.board[cordX][cordY];
        }
        return null;
    }

    /**
     * @brief Gibt die Liste aller Schiffe, die auf dem Board platzierten wurden, zurück.
     * @return Eine Liste von Schiffen.
     */
    public ArrayList<ShipModel> getPlayerShips() {
        return this.playerShips;
    }

    /**
     * @brief Erstellt ein Board, indem alle Zellen auf den Zustand CellState.FREE gesetzt werden.
     */
    private void createBoard() {
        for (int cordX = 0; cordX < WIDTH; cordX++) {
            for (int cordY = 0; cordY < HEIGHT; cordY++) {
                this.board[cordX][cordY] = new CellModel(cordX, cordY, CellState.FREE);
            }
        }
    }

    /**
     * @brief Ändert den Zustand einer Zelle auf dem Board.
     * @param cordX Die X-Koordinate der Zelle.
     * @param cordY Die Y-Koordinate der Zelle.
     * @param cellState Der neue Zustand der Zelle.
     */
    public void changeCellInBoard(int cordX, int cordY, CellState cellState) {
        if (this.isValidCoordinate(cordX, cordY)) {
            this.board[cordX][cordY].updateCellState(cellState);
        }
    }

    /**
     * @brief Platziert ein Schiff auf dem Board, wenn die Position gültig ist.
     * @param startX Die X-Koordinate des Startpunktes des Schiffs.
     * @param startY Die Y-Koordinate des Startpunktes des Schiffs.
     * @param horizontal Gibt an, ob das Schiff horizontal oder vertikal platziert wird.
     * @param length Die Länge des Schiffes.
     * @return true, wenn das Schiff erfolgreich platziert wurde.
     */
    public boolean placeShip(int startX, int startY, boolean horizontal, int length) {
        if (!this.isValidShipPlacement(startX, startY, horizontal, length)) {
            return false;
        }

        ShipModel ship = new ShipModel(this, startX, startY, length, horizontal);
        this.playerShips.add(ship);
        return true;
    }

    /**
     * @brief Platziert alle Schiffe zufällig auf dem Board.
     */
    public void placeAllShips() {
        Random random = new Random();
        for (int length : BOAT_SIZES) {
            boolean placed = false;
            while (!placed) {
                int startX = random.nextInt(WIDTH);
                int startY = random.nextInt(HEIGHT);
                boolean horizontal = random.nextBoolean();

                if (this.placeShip(startX, startY, horizontal, length)) {
                    placed = true;
                }
            }
        }
    }

    /**
     * @brief Stellt fest, ob ein Angriff einen Teil eines Schiffes auf dem Board getroffen hat.
     * @param cordX Die X-Koordinate des Angriffes.
     * @param cordY Die Y-Koordinate des Angriffes.
     * @return Das getroffene Schiff, falls eines getroffen wurde.
     */
    public ShipModel registerHit(int cordX, int cordY) {
        if (!this.isValidCoordinate(cordX, cordY)) {
            return null;
        }

        if (this.getCell(cordX, cordY).getCellState() == CellState.SET) {
            this.getCell(cordX, cordY).updateCellState(CellState.HIT);
            for (ShipModel ship : this.playerShips) {
                if (ship.isHit(cordX, cordY)) {
                    ship.checkShipStatus();
                    return ship;
                }
            }
        } else if (this.getCell(cordX, cordY).getCellState() == CellState.FREE) {
            this.getCell(cordX, cordY).updateCellState(CellState.FREE);
        }
        return null;
    }

    /**
     * @brief Überprüft, ob alle Schiffe auf dem Board versenkt wurden.
     * @return true, wenn alle Schiffe versenkt wurden.
     */
    public boolean allShipsAreHit() {
        for (ShipModel ship : this.playerShips) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @brief Überprüft, ob die angegebenen Koordinaten innerhalb des Spielfeldes liegen.
     * @param cordX Die X-Koordinate.
     * @param cordY Die Y-Koordinate.
     * @return true, wenn die Koordinaten gültig sind.
     */
    private boolean isValidCoordinate(int cordX, int cordY) {
        return cordX >= 0 && cordX < WIDTH && cordY >= 0 && cordY < HEIGHT;
    }

    /**
     * @brief Überprüft, ob die Platzierung eines Schiffes den Regeln entsprechen.
     * @param startX Die X-Koordinate des Startpunktes des Schiffs.
     * @param startY Die Y-Koordinate des Startpunktes des Schiffs.
     * @param horizontal Gibt an, ob das Schiff horizontal oder vertikal platziert wird.
     * @param length Die Länge des Schiffs.
     * @return true, wenn die Platzierung gültig ist; false sonst.
     */
    public boolean isValidShipPlacement(int startX, int startY, boolean horizontal, int length) {
        if (!this.isValidCoordinate(startX, startY)) {
            return false;
        }

        int endX = horizontal ? startX + length - 1 : startX;
        int endY = horizontal ? startY : startY + length - 1;

        if (!this.isValidCoordinate(endX, endY)) {
            return false;
        }

        for (int x = Math.max(0, startX - 1); x <= Math.min(WIDTH - 1, endX + 1); x++) {
            for (int y = Math.max(0, startY - 1); y <= Math.min(HEIGHT - 1, endY + 1); y++) {
                if (this.getCell(x, y).getCellState() == CellState.SET) {
                    return false;
                }
            }
        }
        return true;
    }
}