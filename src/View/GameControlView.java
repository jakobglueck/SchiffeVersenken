package View;

import javax.swing.*;
import java.awt.*;

/**
 * @class GameControlView
 * @brief Diese Klasse stellt eine Benutzeroberfläche zur Steuerung des Spiels bereit.
 * Sie enthält Schaltflächen, um zum Hauptmenü zurückzukehren, das Spiel zu pausieren oder das Spiel zu verlassen.
 */
public class GameControlView extends JPanel {

    /**
     * @brief Schaltfläche zum Zurückkehren zum Hauptmenü.
     */
    private JButton mainMenuButton;

    /**
     * @brief Schaltfläche zum Pausieren des Spiels.
     */
    private JButton pauseGameButton;

    /**
     * @brief Schaltfläche zum Verlassen des Spiels.
     */
    private JButton endGameButton;

    /**
     * @brief Konstruktor, der die Steuerelemente für das Spiel initialisiert.
     */
    public GameControlView() {
        this.setLayout(new FlowLayout());

        this.mainMenuButton = new JButton("Zurück zum Hauptmenü");
        this.pauseGameButton = new JButton("Spiel pausieren");
        this.endGameButton = new JButton("Spiel verlassen");

        this.add(this.mainMenuButton);
        this.add(this.pauseGameButton);
        this.add(this.endGameButton);

        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * @brief Gibt die Schaltfläche zum Zurückkehren zum Hauptmenü zurück.
     *
     * @return Die Schaltfläche zum Zurückkehren zum Hauptmenü.
     */
    public JButton getMainMenuButton() {
        return this.mainMenuButton;
    }

    /**
     * @brief Gibt die Schaltfläche zum Pausieren des Spiels zurück.
     *
     * @return Die Schaltfläche zum Pausieren des Spiels.
     */
    public JButton getPauseGameButton() {
        return this.pauseGameButton;
    }

    /**
     * @brief Gibt die Schaltfläche zum Verlassen des Spiels zurück.
     *
     * @return Die Schaltfläche zum Verlassen des Spiels.
     */
    public JButton getEndGameButton() {
        return this.endGameButton;
    }
}