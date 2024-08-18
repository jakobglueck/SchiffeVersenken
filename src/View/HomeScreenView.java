package View;

import javax.swing.*;
import java.awt.*;

/**
 * @class HomeScreenView
 * @brief Diese Klasse stellt das Startbildschirm-Fenster für das Spiel "Schiffe versenken" bereit.
 * Es enthält Schaltflächen, um verschiedene Spielmodi zu starten oder das Spiel zu beenden.
 */
public class HomeScreenView extends JFrame {

    /**
     * @brief Schaltfläche zum Starten eines lokalen Spiels.
     */
    private JButton localGameButton;

    /**
     * @brief Schaltfläche zum Starten eines Spiels gegen den Computer.
     */
    private JButton computerGameButton;

    /**
     * @brief Schaltfläche zum Starten des Debug-Modus.
     */
    private JButton debugModeButton;

    /**
     * @brief Schaltfläche zum Beenden des Spiels.
     */
    private JButton exitButton;

    /**
     * @brief Konstruktor, der die Benutzeroberfläche des Startbildschirms initialisiert.
     */
    public HomeScreenView() {
        this.setTitle("Schiffe versenken");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        this.setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("Schiffe versenken", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.localGameButton = new JButton("Lokales Spiel starten");
        this.computerGameButton = new JButton("Computer Spiel starten");
        this.debugModeButton = new JButton("Debug Modus starten");
        this.exitButton = new JButton("Spiel beenden");

        buttonPanel.add(this.localGameButton);
        buttonPanel.add(this.computerGameButton);
        buttonPanel.add(this.debugModeButton);
        buttonPanel.add(this.exitButton);

        mainPanel.add(buttonPanel);

        this.setVisible(true);
    }

    /**
     * @brief Gibt die Schaltfläche zum Starten eines lokalen Spiels zurück.
     *
     * @return Die Schaltfläche zum Starten eines lokalen Spiels.
     */
    public JButton getLocalGameButton() {
        return this.localGameButton;
    }

    /**
     * @brief Gibt die Schaltfläche zum Starten eines Spiels gegen den Computer zurück.
     *
     * @return Die Schaltfläche zum Starten eines Spiels gegen den Computer.
     */
    public JButton getComputerGameButton() {
        return this.computerGameButton;
    }

    /**
     * @brief Gibt die Schaltfläche zum Starten des Debug-Modus zurück.
     *
     * @return Die Schaltfläche zum Starten des Debug-Modus.
     */
    public JButton getDebugModeButton() {
        return this.debugModeButton;
    }

    /**
     * @brief Gibt die Schaltfläche zum Beenden des Spiels zurück.
     *
     * @return Die Schaltfläche zum Beenden des Spiels.
     */
    public JButton getExitButton() {
        return this.exitButton;
    }
}
