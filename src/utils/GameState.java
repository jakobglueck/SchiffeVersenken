/**
 * @file GameState.java
 * @brief Dieses Enum definiert die verschiedenen Zustände, in denen sich das Spiel befinden kann.
 */

package utils;

/**
 * @enum GameState
 * @brief Repräsentiert die verschiedenen Spielmodi und Zustände, in denen sich das Spiel befinden kann.
 */
public enum GameState {
    NORMAL,  ///< Der normale Spielmodus, in dem zwei Spieler gegeneinander antreten.
    DEBUG,   ///< Ein Debug-Modus, der für Testzwecke verwendet wird und zusätzliche Informationen oder Funktionen bietet.
    COMPUTER ///< Ein Spielmodus, in dem ein Spieler gegen den Computer antritt.
}
