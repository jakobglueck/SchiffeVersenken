/**
 * @file CellState.java
 */

package utils;

/**
 * @enum CellState
 * @brief Zeigt die verschiedenen Zustände einer Zelle auf dem Board.
 */
public enum CellState {
    // Zelle enthält kein Schiff und ist somit nicht besetzt.
    FREE,
    // Die Zelle besitzt ein Teil eines Schiffes.
    SET,
    // Die Zelle besitzt ein Teil eines Schiffes, welches durch einen Spieler getroffen wurde
    HIT,
}
