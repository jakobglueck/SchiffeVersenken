/**
 * @file BoardModel.java
 * @brief Diese Klasse repräsentiert das Spielfeld und die Logik für die Platzierung und Verwaltung von Schiffen.
 */

package model;

import utils.CellState;

import java.util.*;

/**
 * @class BoardModel
 * @brief Verantwortlich für die Verwaltung des Spielfeldes und der Schiffe eines Spielers.
 */
public class BoardModel {
    public static final int[] BOAT_SIZES = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2}; ///< Die Größen der Boote, die auf dem Spielfeld platziert werden.

    private final CellModel[][] board; ///< Das zweidimensionale Array, das das Spielfeld repräsentiert.
    private final ArrayList<ShipModel> playerShips; ///< Eine Liste der Schiffe, die auf dem Spielfeld platziert sind.

    public static final int HEIGHT = 10; ///< Die Höhe des Spielfelds.
    public static final int WIDTH = 10; ///< Die Breite des Spielfelds.

    private final Random random = new Random(); ///< Zufallsgenerator für die Platzierung von Schiffen.

    /**
     * @brief Konstruktor, der das Spielfeld initialisiert und die Zellen erstellt.
     */
    public BoardModel() {
        this.board = new CellModel[HEIGHT][WIDTH];
        this.playerShips = new ArrayList<>();
        this.createBoard();
    }

    /**
     * @brief Erstellt das Spielfeld, indem alle Zellen auf den Zustand `FREE` gesetzt werden.
     */
    private void createBoard() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.board[x][y] = new CellModel(x, y, CellState.FREE);
            }
        }
    }

    /**
     * @brief Ändert den Zustand einer Zelle auf dem Spielfeld.
     * @param x Die X-Koordinate der Zelle.
     * @param y Die Y-Koordinate der Zelle.
     * @param cellState Der neue Zustand der Zelle.
     */
    public void changeCellInBoard(int x, int y, CellState cellState) {
        if (isValidCoordinate(x, y)) {
            this.board[x][y].updateCellState(cellState);
        }
    }

    /**
     * @brief Gibt eine Zelle des Spielfeldes zurück.
     * @param x Die X-Koordinate der Zelle.
     * @param y Die Y-Koordinate der Zelle.
     * @return Die Zelle an der angegebenen Position, oder null, wenn die Koordinate ungültig ist.
     */
    public CellModel getCell(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return this.board[x][y];
        }
        return null;
    }

    /**
     * @brief Gibt die Liste der auf dem Spielfeld platzierten Schiffe zurück.
     * @return Eine Liste der Schiffe auf dem Spielfeld.
     */
    public ArrayList<ShipModel> getPlayerShips() {
        return this.playerShips;
    }

    /**
     * @brief Platziert ein Schiff auf dem Spielfeld, wenn die Position gültig ist.
     * @param startX Die X-Koordinate des Startpunktes des Schiffs.
     * @param startY Die Y-Koordinate des Startpunktes des Schiffs.
     * @param horizontal Gibt an, ob das Schiff horizontal oder vertikal platziert wird.
     * @param length Die Länge des Schiffs.
     * @return true, wenn das Schiff erfolgreich platziert wurde; false sonst.
     */
    public boolean placeShip(int startX, int startY, boolean horizontal, int length) {
        if (!isValidShipPlacement(startX, startY, horizontal, length)) {
            return false;
        }

        ShipModel ship = new ShipModel(this, startX, startY, length, horizontal);
        for (int i = 0; i < length; i++) {
            int x = horizontal ? startX + i : startX;
            int y = horizontal ? startY : startY + i;
            this.changeCellInBoard(x, y, CellState.SET);
        }
        this.playerShips.add(ship);
        return true;
    }

    /**
     * @brief Überprüft, ob eine Schiffsplatzierung gültig ist.
     * @param startX Die X-Koordinate des Startpunktes des Schiffs.
     * @param startY Die Y-Koordinate des Startpunktes des Schiffs.
     * @param horizontal Gibt an, ob das Schiff horizontal oder vertikal platziert wird.
     * @param length Die Länge des Schiffs.
     * @return true, wenn die Platzierung gültig ist; false sonst.
     */
    private boolean isValidShipPlacement(int startX, int startY, boolean horizontal, int length) {
        if (!isValidCoordinate(startX, startY)) {
            return false;
        }

        int endX = horizontal ? startX + length - 1 : startX;
        int endY = horizontal ? startY : startY + length - 1;

        if (!isValidCoordinate(endX, endY)) {
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

    /**
     * @brief Platziert alle Schiffe zufällig auf dem Spielfeld.
     */
    public void placeAllShips() {
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
     * @brief Registriert einen Treffer auf dem Spielfeld.
     * @param x Die X-Koordinate des Treffers.
     * @param y Die Y-Koordinate des Treffers.
     * @return Das getroffene Schiff, falls eines getroffen wurde, ansonsten null.
     */
    public ShipModel registerHit(int x, int y) {
        if (!isValidCoordinate(x, y)) {
            return null;
        }

        if (this.getCell(x, y).getCellState() == CellState.SET) {
            this.getCell(x, y).updateCellState(CellState.HIT);
            for (ShipModel ship : this.playerShips) {
                if (ship.isHit(x, y)) {
                    ship.checkShipStatus();
                    return ship;
                }
            }
        } else if (this.getCell(x, y).getCellState() == CellState.FREE) {
            this.getCell(x, y).updateCellState(CellState.FREE);
        }
        return null;
    }

    /**
     * @brief Überprüft, ob alle Schiffe auf dem Spielfeld versenkt wurden.
     * @return true, wenn alle Schiffe versenkt wurden; false sonst.
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
     * @param x Die X-Koordinate.
     * @param y Die Y-Koordinate.
     * @return true, wenn die Koordinaten gültig sind; false sonst.
     */
    private boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    /**
     * @brief Setzt das Spielfeld zurück und entfernt alle Schiffe.
     */
    public void reset() {
        for (int row = 0; row < WIDTH; row++) {
            for (int col = 0; col < HEIGHT; col++) {
                this.board[row][col] = new CellModel(row, col, CellState.FREE);
            }
        }
        this.playerShips.clear();
    }
}