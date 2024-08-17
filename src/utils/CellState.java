/**
 * @file CellState.java
 * @brief Dieses Enum definiert die verschiedenen Zustände, die eine Zelle auf dem Spielfeld haben kann.
 */

package utils;

/**
 * @enum CellState
 * @brief Repräsentiert die verschiedenen Zustände, in denen sich eine Zelle auf dem Spielfeld befinden kann.
 */
public enum CellState {
    FREE,   ///< Die Zelle ist frei und enthält kein Schiff.
    SET,    ///< Die Zelle enthält ein platziertes Schiff.
    HIT,    ///< Die Zelle wurde angegriffen und enthält ein getroffenes Schiff.
}
