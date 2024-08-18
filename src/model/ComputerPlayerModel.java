/**
 * @file ComputerPlayerModel.java
 */

package model;

import utils.CellState;

import java.util.*;

/**
 * @class ComputerPlayerModel
 * @brief Ein spezielles PlayerModel, das die Logik für einen Computergegner implementiert.
 */
public class ComputerPlayerModel extends PlayerModel {

    private List<int[]> computerMove; ///< Eine Liste der verfügbaren Züge, die der Computer machen kann.
    private int lastMoveX; ///< Die X-Koordinate des letzten Zuges des Computers.
    private int lastMoveY; ///< Die Y-Koordinate des letzten Zuges des Computers.

    /**
     * @brief Konstruktor, der einen Computergegner mit einem bestimmten Namen erstellt.
     * @param playerName Der Name des Computergegners.
     */
    public ComputerPlayerModel(String playerName) {
        super(playerName);
        this.initializeAvailableMoves();
    }

    /**
     * @brief Initialisiert die Liste der verfügbaren Züge, die der Computer machen kann.
     */
    private void initializeAvailableMoves() {
        computerMove = new ArrayList<>();
        for (int i = 0; i < BoardModel.WIDTH; i++) {
            for (int j = 0; j < BoardModel.HEIGHT; j++) {
                computerMove.add(new int[]{i, j});
            }
        }
    }

    /**
     * @brief Gibt die X-Koordinate des letzten Zuges des Computers zurück.
     * @return Die X-Koordinate des letzten Zuges.
     */
    public int getLastMoveX() {
        return lastMoveX;
    }

    /**
     * @brief Gibt die Y-Koordinate des letzten Zuges des Computers zurück.
     * @return Die Y-Koordinate des letzten Zuges.
     */
    public int getLastMoveY() {
        return lastMoveY;
    }

    /**
     * @brief Führt einen Zug des Computers gegen den gegnerischen Spieler aus.
     * @param opponent Der gegnerische Spieler.
     * @return true, wenn der Computer ein Schiff getroffen hat; false, wenn er verfehlt hat.
     */
    public boolean makeMove(PlayerModel opponent) {
        boolean result = false;
        if (!computerMove.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(computerMove.size());
            int[] move = computerMove.remove(index);
            lastMoveX = move[0];
            lastMoveY = move[1];

            BoardModel opponentBoard = opponent.getBoard();
            CellModel targetCell = opponentBoard.getCell(move[0], move[1]);

            if (targetCell.getCellState() == CellState.FREE) {
                result = false;
            } else {
                result = targetCell.getCellState() == CellState.SET;
                targetCell.updateCellState(CellState.HIT);
            }
        }
        return result;
    }
}
